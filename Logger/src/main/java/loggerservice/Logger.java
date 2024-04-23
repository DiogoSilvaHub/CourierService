package loggerservice;

import com.rabbitmq.client.*;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Logger {
    private static final org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(Logger.class);
    public static void main(String[] args) {

        try {
            if (args.length < 2) throw new RuntimeException("Not enough arguments (rabbitMQIP, rabbitMQPort)");
            String rabbitMQIP = args[0];
            int rabbitMQPort = Integer.parseInt(args[1]);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQIP);
            factory.setPort(rabbitMQPort);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Consumer handler para receber mensagens
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                //Quando é publicada uma mensagem de um Cliente, a aplicação Logger irá guardar
                //essa mensagem no ficheiro mylogs.log com a data e hora da sua publicação
                String recMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                String routingKey = delivery.getEnvelope().getRoutingKey();
                //Foram alteradas as propriedades do slf4jLogger para que não mostre só as mensagens no terminal, mas
                //também as guarde no ficheiro mylogs.log
                slf4jLogger.info("Message Received:" + consumerTag + ":" + routingKey + ":" + recMessage);

            };

            // Consumer handler para receber mensagens de cancelamento
            CancelCallback cancelCallback = (consumerTag) -> {
                slf4jLogger.info("CANCEL Received! " + consumerTag);
            };

            // A aplicação Logger escuta a LoggerQueue
            channel.basicConsume("LoggerQueue", true, deliverCallback, cancelCallback);

            System.out.println("Waiting for messages or Press any key to finish");
            Scanner scan = new Scanner(System.in);
            scan.nextLine();

            channel.close();
            connection.close();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
