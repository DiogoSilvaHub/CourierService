package globalconfigurator;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class GlobalConfigurator {

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

            //Criação do Exchange Global e todas as partes relacionadas
            //com o modelo Publish/Subscribe com padrão Work-Queue usado pelos clientes, pelos
            //workers e pelo logger

            channel.exchangeDeclare("Global", BuiltinExchangeType.FANOUT);
            channel.exchangeDeclare("Workers", BuiltinExchangeType.FANOUT);
            channel.queueDeclare("WorkerQueue", true, false, false, null);
            channel.queueDeclare("LoggerQueue", true, false, false, null);
            channel.exchangeBind("Workers","Global", "");
            channel.queueBind("WorkerQueue", "Workers", "");
            channel.queueBind("LoggerQueue", "Global", "");

            channel.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

}
