package courierservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import couriertocourierappservice.Address;
import spread.BasicMessageListener;
import spread.SpreadMessage;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class MessageHandling implements BasicMessageListener {
    public int numberCourierServers;
    private final GroupMember courierServer;
    private Map<String, ClientMessage> clientMsgMap = new HashMap<>();
    private Map<String, List<ServerMsg>> serverMsgElectionMap = new HashMap<>();
    private Map<String, List<ServerMsg>> serverMsgResultMap = new HashMap<>();

    public MessageHandling(GroupMember courierServer) {
        this.courierServer = courierServer;
    }

    @Override
    public void messageReceived(SpreadMessage spreadMessage) {
        try {
            System.out.println("Message Received ThreadID="+Thread.currentThread().getId()+":");
            PrintMessages.MessageDetails(spreadMessage);
            if (spreadMessage.isMembership()){
                numberCourierServers = spreadMessage.getMembershipInfo().getMembers().length;
            }


            // verifica se a mensagem recebida é do tipo regular, ou seja não é do tipo membership ou de outro tipo
            if (spreadMessage.isRegular()) {
                // verifica se a mensagem recebida é do tipo 1, ou seja ou é do tipo ELECTION ou do tipo RESULT
                if (spreadMessage.getType() == 1) {
                    String recMessage = new String(spreadMessage.getData(), StandardCharsets.UTF_8);

                    Gson js = new GsonBuilder().create();
                    ServerMsg newServerMessage = js.fromJson(recMessage, ServerMsg.class);

                    // Election
                    // Primeiro guarda as mensagens recebidas do tipo ELECTION
                    // Depois de receber a mensagem do tipo ELECTION de todos os outros
                    // membros da região incluindo a sua mensagem, inicia o processo de escolha. Primeiro verifica qual o Courier
                    // livre com menor número de mensagens processadas, caso existam vários Couriers
                    // com o mesmo número de mensagens processadas é escolhido o com menor ID.
                    // De seguida envia uma mensagem do tipo RESULT para os membros da sua região com o resultado da escolha
                    if (newServerMessage.getMsgType().equalsIgnoreCase("ELECTION")) {
                        if (serverMsgElectionMap.containsKey(newServerMessage.getExchangeName())) {
                            List<ServerMsg> serverMsgList = serverMsgElectionMap.get(newServerMessage.getExchangeName());
                            serverMsgList.add(newServerMessage);
                            serverMsgElectionMap.replace(newServerMessage.getExchangeName(), serverMsgList);
                        } else {
                            List<ServerMsg> serverMsgList = new ArrayList<>();
                            serverMsgList.add(newServerMessage);
                            serverMsgElectionMap.put(newServerMessage.getExchangeName(), serverMsgList);
                        }

                        List<ServerMsg> serverMsgList = serverMsgElectionMap.get(newServerMessage.getExchangeName());
                        if (serverMsgList.size() == numberCourierServers) {
                            int lowestCourierID = serverMsgList.stream().max(Comparator.comparing(ServerMsg::getCourierID)).get().getCourierID();
                            int lowestNumberProcessedRequests = serverMsgList.stream().max(Comparator.comparing(ServerMsg::getNumberProcessedRequests)).get().getNumberProcessedRequests();
                            for (ServerMsg serverMsg : serverMsgList) {
                                if (!serverMsg.getAvailability().equalsIgnoreCase("occupied")) {
                                    if (serverMsg.getNumberProcessedRequests() == lowestNumberProcessedRequests) {
                                        if (serverMsg.getCourierID() < lowestCourierID) {
                                            lowestCourierID = serverMsg.getCourierID();
                                        }
                                    } else if (serverMsg.getNumberProcessedRequests() < lowestNumberProcessedRequests) {
                                        lowestNumberProcessedRequests = serverMsg.getNumberProcessedRequests();
                                        lowestCourierID = serverMsg.getCourierID();
                                    }
                                }
                            }

                            ServerMsg newServerMsg = new ServerMsg(
                                    "RESULT",
                                    lowestCourierID,
                                    newServerMessage.getExchangeName(),
                                    lowestNumberProcessedRequests,
                                    courierServer.getAvailability()
                            );

                            String newServerMsgString = js.toJson(newServerMsg);

                            courierServer.SendMessage(courierServer.getGroup().toString(), newServerMsgString);

                        }

                    }

                    // Result
                    // Primeiro guarda as mensagens recebidas do tipo RESULT
                    // Depois de receber a mensagem do tipo RESULT de todos os outros membros da região incluindo a sua mensagem,
                    // o Courier escolhido verifica se todos os membros se encontram ocupados,
                    // caso aconteça publica no exchange associado ao Cliente que todos os Couriers da região se encontram ocupados.
                    // No caso do Courier escolhido não se encontrar ocupado,
                    // publica no exchange associado ao Cliente uma chave e o identificador que permitirá a este colocar a encomenda
                    // e encerrar a box de forma segura.A chave e o identificador são strings com caracteres aleatórios.
                    // Depois envia ao Courier App o objeto Address com a morada de recolha e a
                    // morada de destino e altera a sua disponibilidade para occupied.
                    if (newServerMessage.getMsgType().equalsIgnoreCase("RESULT")) {
                        if (serverMsgResultMap.containsKey(newServerMessage.getExchangeName())) {
                            List<ServerMsg> serverMsgList = serverMsgResultMap.get(newServerMessage.getExchangeName());
                            serverMsgList.add(newServerMessage);
                            serverMsgResultMap.replace(newServerMessage.getExchangeName(), serverMsgList);
                        } else {
                            List<ServerMsg> serverMsgList = new ArrayList<>();
                            serverMsgList.add(newServerMessage);
                            serverMsgResultMap.put(newServerMessage.getExchangeName(), serverMsgList);
                        }

                        List<ServerMsg> serverMsgList = serverMsgResultMap.get(newServerMessage.getExchangeName());
                        if (serverMsgList.size() == numberCourierServers) {
                            int chosenCourierID = serverMsgList.get(0).getCourierID();
                            if (chosenCourierID == Integer.parseInt(courierServer.getCourierID())) {
                                int numberOfOccupiedCouriers = 0;
                                for (ServerMsg serverMsg : serverMsgList) {
                                    if (serverMsg.getCourierID() != chosenCourierID) {
                                        System.out.println("Couriers picked different leaders!");
                                    }
                                    if (serverMsg.getAvailability().equalsIgnoreCase("occupied")) {
                                        numberOfOccupiedCouriers++;
                                    }
                                }

                                if (numberOfOccupiedCouriers == numberCourierServers) {
                                    String jsonString = "Todos os couriers ocupados. Tente mais tarde. Desculpe o incómodo.";
                                    byte[] binData = jsonString.getBytes(StandardCharsets.UTF_8);

                                    courierServer.getChannel().basicPublish(newServerMessage.getExchangeName(), "", true, null, binData);
                                } else {

                                    String pickUpAddress = clientMsgMap.get(newServerMessage.getExchangeName()).getPickUpAddress();
                                    String deliveryAddress = clientMsgMap.get(newServerMessage.getExchangeName()).getDeliveryAddress();

                                    courierServer.getResponseObserver().onNext(Address.newBuilder().setPickUpAddress(pickUpAddress).setDeliveryAddress(deliveryAddress).build());
                                    courierServer.getResponseObserver().onCompleted();

                                    courierServer.setAvailability("occupied");

                                    String key = getAlphaNumericString(20);
                                    String ID = getAlphaNumericString(20);
                                    MessageToCLient msgToClient = new MessageToCLient(key, ID);

                                    String jsonString = js.toJson(msgToClient);

                                    byte[] binData = jsonString.getBytes(StandardCharsets.UTF_8);

                                    courierServer.getChannel().basicPublish(newServerMessage.getExchangeName(), "", true, null, binData);

                                    courierServer.setNumberProcessedRequests(courierServer.getNumberProcessedRequests()+1);
                                }
                            }
                            serverMsgElectionMap.remove(newServerMessage.getExchangeName());
                            serverMsgResultMap.remove(newServerMessage.getExchangeName());
                            clientMsgMap.remove(newServerMessage.getExchangeName());
                        }
                    }


                } else {
                    //Após o envio da mensagem do cliente pelo Worker, em multicast, para o grupo de
                    //uma região, cada Courier pertencente a essa mesma região enviará para os outros
                    //Couriers membros, uma mensagem do tipo ELECTION para eleger quem vai processar a respetiva
                    //mensagem do Worker.
                    String recMessage = new String(spreadMessage.getData(), StandardCharsets.UTF_8);

                    Gson js = new GsonBuilder().create();
                    ClientMessage newClientMessage = js.fromJson(recMessage, ClientMessage.class);

                    clientMsgMap.put(newClientMessage.getExchangeName(), newClientMessage);

                    ServerMsg newServerMsg = new ServerMsg(
                            "ELECTION",
                            Integer.parseInt(courierServer.getCourierID()),
                            newClientMessage.getExchangeName(),
                            courierServer.getNumberProcessedRequests(),
                            courierServer.getAvailability()
                    );

                    String newServerMsgString = js.toJson(newServerMsg);

                    courierServer.SendMessage(courierServer.getGroup().toString(), newServerMsgString);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // metodo para gerar uma string aleatória com tamanho n
    private static String getAlphaNumericString(int n) {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}

