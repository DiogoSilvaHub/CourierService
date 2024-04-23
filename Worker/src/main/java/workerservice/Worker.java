package workerservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Worker {
    public static void main(String[] args) {

        try {
            if (args.length < 5) throw new RuntimeException("Not enough arguments (rabbitMQIP, rabbitMQPort, daemonIP, daemonPort, workerName)");
            String rabbitMQIP = args[0];
            int rabbitMQPort = Integer.parseInt(args[1]);
            String daemonIP = args[2];
            int daemonPort = Integer.parseInt(args[3]);
            String workerName = args[4];

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQIP);
            factory.setPort(rabbitMQPort);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Primeiro cria uma conexão ao servidor Spread
            SpreadConnection spreadConnection = new SpreadConnection();
            spreadConnection.connect(InetAddress.getByName(daemonIP), daemonPort, workerName, false, true);

            // Conecta-se ao grupo Workers
            SpreadGroup newGroup=new SpreadGroup();
            newGroup.join(spreadConnection, "Workers");

            // Consumer handler para receber mensagens
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                //Quando é publicada uma mensagem de um cliente, um dos workers irá receber essa
                //mensagem e enviará em multicast para o grupo de couriers associado à região
                //indicada na mensagem.
                String recMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println("Message Received:" + consumerTag + ":" + routingKey + ":" + recMessage);

                Gson js = new GsonBuilder().create();
                ClientMessage newClientMessage = js.fromJson(recMessage,ClientMessage.class);

                SpreadMessage msg = new SpreadMessage();
                msg.setSafe();
                msg.addGroup(newClientMessage.getRegionName());
                msg.setData(recMessage.getBytes());
                try {
                    spreadConnection.multicast(msg);
                } catch (SpreadException e) {
                    throw new RuntimeException(e);
                }
            };

            // Consumer handler para receber mensagens de cancelamento
            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println("CANCEL Received! " + consumerTag);
            };

            // escuta a WorkerQueue conectada ao exchange global no formato worker-queue
            channel.basicConsume("WorkerQueue", true, deliverCallback, cancelCallback);

            System.out.println("Waiting for messages or Press any key to finish");
            Scanner scan = new Scanner(System.in);
            scan.nextLine();

            channel.close();
            connection.close();
            spreadConnection.disconnect();

        }
        catch(SpreadException e)  {
            System.err.println("There was an error connecting to the daemon.");
            e.printStackTrace();
            System.exit(1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
