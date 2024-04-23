package clientservice;

// Formato das mensagens enviadas pelo cliente
public class ClientMessage {
    private String exchangeName;
    private String regionName;
    private String pickUpAddress;
    private String deliveryAddress;

    public ClientMessage(String exchangeName, String regionName, String pickUpAddress, String deliveryAddress){
        this.exchangeName = exchangeName;
        this.regionName = regionName;
        this.pickUpAddress = pickUpAddress;
        this.deliveryAddress = deliveryAddress;
    }
}
