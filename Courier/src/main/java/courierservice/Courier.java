package courierservice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.grpc.ServerBuilder;

public class Courier {
    public static void main(String[] args) {

        try {
            if (args.length < 7) throw new RuntimeException("Not enough arguments (rabbitMQIP, rabbitMQPort, daemonIP, daemonPort, courierID, regionName, svcPort)");
            String rabbitMQIP = args[0];
            int rabbitMQPort = Integer.parseInt(args[1]);
            String daemonIP = args[2];
            int daemonPort = Integer.parseInt(args[3]);
            String courierID = args[4];
            String regionName = args[5];
            String svcPort = args[6];

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQIP);
            factory.setPort(rabbitMQPort);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Criação do objeto GroupMember que guarda as propriedades do servidor Courier
            GroupMember member = new GroupMember(courierID, daemonIP, daemonPort, regionName, channel);

            // Inicialização do Servidor com o serviço CourierToCourierAppService
            io.grpc.Server svc = ServerBuilder
                    .forPort(Integer.parseInt(svcPort))
                    .addService(new CourierToCourierAppService(member))
                    .build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);

            svc.awaitTermination();
            svc.shutdown();
            channel.close();
            connection.close();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
