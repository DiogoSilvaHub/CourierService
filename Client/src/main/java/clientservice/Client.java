package clientservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try {
            if (args.length < 2) throw new RuntimeException("Not enough arguments (rabbitMQIP, rabbitMQPort)");
            String rabbitMQIP = args[0];
            int rabbitMQPort = Integer.parseInt(args[1]);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQIP); factory.setPort(rabbitMQPort);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Primeiro pergunta ao utilizador qual o nome do seu exchange,
            //a sua região, a morada de destino e morada de recolha
            String exchangeName = readLine("Exchange Name?");
            String regionName = readLine("Region Name? (Lisboa or Porto)");
            if (regionName.equalsIgnoreCase("lisboa")){
                regionName ="Lisboa";
            } else if (regionName.equalsIgnoreCase("porto")){
                regionName = "Porto";
            }else {
                System.out.println("Choose region between Lisboa or Porto!");
                System.exit(0);
            }
            String pickUpAddress = readLine("Pick Up Address?");
            String deliveryAddress = readLine("Delivery Address?");

            // cria o objeto clientMessage com a informação obtida anteriormente
            ClientMessage clientMessage = new ClientMessage(exchangeName, regionName, pickUpAddress, deliveryAddress);

            // cria a exchange do tipo FANOUT com o nome declarado pelo cliente e a queue associada à mesma
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(exchangeName+"Queue", true, false, false, null);
            channel.queueBind(exchangeName+"Queue", exchangeName, "");

            // Consumer handler para receber mensagens
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String recMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Message Received: " + recMessage);
            };
            // Consumer handler para receber mensagens de cancelamento
            CancelCallback cancelCallback=(consumerTag)->{
                System.out.println("CANCEL Received! "+consumerTag);
            };
            //fica à escuta de mensagens publicadas neste exchange.
            channel.basicConsume(exchangeName+"Queue", true, deliverCallback, cancelCallback);

            // converte o objeto clientMessage em string JSON
            Gson js = new GsonBuilder().create();
            String jsonString=js.toJson(clientMessage);

            // converte essa string em byte[]
            byte[] binData = jsonString.getBytes(StandardCharsets.UTF_8);

            // publica no exchange global a mensagem com a informação obtida do
            //cliente para mais tarde ser consumida por um dos workers e guardada na aplicação
            //logger
            channel.basicPublish("Global", "", true, null, binData);

            System.out.println("Waiting for messages or Press any key to finish");
            Scanner scan = new Scanner(System.in);
            scan.nextLine();

            channel.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static String readLine(String msg) {
        Scanner scanInput = new Scanner(System.in);
        System.out.println(msg);
        return scanInput.nextLine();
    }
}
