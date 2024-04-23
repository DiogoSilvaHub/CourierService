package courierservice;

import couriertocourierappservice.Address;
import couriertocourierappservice.Availability;
import couriertocourierappservice.CourierToCourierAppServiceGrpc;
import couriertocourierappservice.Void;
import io.grpc.stub.StreamObserver;
import spread.SpreadException;

public class CourierToCourierAppService extends CourierToCourierAppServiceGrpc.CourierToCourierAppServiceImplBase{

    private GroupMember courierServer;

    public CourierToCourierAppService(GroupMember courierServer) {
        this.courierServer = courierServer;
    }

    //Ao ser chamado o método connect, o servidor Courier irá alterar a sua
    //disponibilidade para free, de modo a quando existir uma eleição entre os Couriers
    //de uma região, este poder ser o servidor escolhido. Caso seja o escolhido, o
    //Courier envia ao Courier App o objeto Address com a morada de recolha e a
    //morada de destino e altera a sua disponibilidade para occupied.
    @Override
    public void connect(Availability availability, StreamObserver<Address> responseObserver) {

        courierServer.setAvailability(availability.getAvailability());
        courierServer.setResponseObserver(responseObserver);


    }

    //Ao ser chamado o método disconnect, o servidor Courier irá se retirar do grupo
    //da região onde atuava.
    @Override
    public void disconnect(Void empty, StreamObserver<Void> responseObserver) {
        responseObserver.onNext(Void.newBuilder().build());
        responseObserver.onCompleted();
        try {
            courierServer.close();
            System.exit(0);
        } catch (SpreadException e) {
            throw new RuntimeException(e);
        }
    }
}
