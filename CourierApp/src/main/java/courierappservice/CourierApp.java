package courierappservice;

import couriertocourierappservice.Address;
import couriertocourierappservice.Availability;
import couriertocourierappservice.CourierToCourierAppServiceGrpc;
import couriertocourierappservice.Void;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class CourierApp {
    static int Menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("######## MENU ##########");
            System.out.println("Options for Server Operations:");
            System.out.println(" 0: Available to receive addresses");
            System.out.println(" 1: Disconnect from Server");
            System.out.print("Enter an Option:");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 1)));
        return option;
    }

    public static void main(String[] args) {
        try {
            if (args.length < 2) throw new RuntimeException("Not enough arguments (courierServerIP, courierServerPort)");
            String courierServerIP = args[0];
            int courierServerPort = Integer.parseInt(args[1]);

            //Conexão entre o CourierApp e o servidor Courier
            ManagedChannel channelCourierServer = ManagedChannelBuilder.forAddress(courierServerIP, courierServerPort)
                    .usePlaintext()
                    .build();
            CourierToCourierAppServiceGrpc.CourierToCourierAppServiceBlockingStub blockingStubCourierServer = CourierToCourierAppServiceGrpc.newBlockingStub(channelCourierServer);

            while(true){
                // Começa por apresentar ao estafeta o menu
                int option = Menu();
                switch (option){
                    case 0:
                        //Caso o estafeta escolher a opção “0”, a aplicação avisa o servidor Courier a que está
                        //associada, que o estafeta se encontra disponível para receber a próxima encomenda.
                        //De seguida a aplicação fica em modo de espera até que o servidor seja escolhido e
                        //lhe envie a morada de recolha e a de destino. Por último, mostra ao estafeta as
                        //respetivas moradas.
                        System.out.println("Waiting for addresses...");

                        Address addresses = blockingStubCourierServer.connect(Availability.newBuilder().setAvailability("free").build());

                        System.out.println("PickUp Address: "+ addresses.getPickUpAddress() +", Delivery Address: "+ addresses.getDeliveryAddress());
                        break;
                    case 1:
                        //Caso o estafeta escolher a opção “1”, a aplicação avisa o servidor Courier que o
                        //estafeta não quer realizar mais transportes, para que este se possa retirar do grupo
                        //da região onde atuava.

                        blockingStubCourierServer.disconnect(Void.newBuilder().build());
                        System.out.println("Disconnected from server");
                        System.exit(0);
                }
            }

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
