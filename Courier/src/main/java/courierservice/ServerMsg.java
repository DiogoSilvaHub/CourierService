package courierservice;

// Formato das mensagens enviadas entre os Couriers do tipo ELECTION ou RESULT
public class ServerMsg {

    private final String msgType;
    private final int courierID;
    private final int numberProcessedRequests;
    private final String exchangeName;
    private final String availability;
    public ServerMsg(String msgType, int courierID, String exchangeName,int numberProcessedRequests, String availability) {
        this.msgType = msgType;
        this.courierID = courierID;
        this.numberProcessedRequests = numberProcessedRequests;
        this.exchangeName = exchangeName;
        this.availability = availability;
    }
    public String getMsgType() {
        return msgType;
    }
    public int getCourierID() {
        return courierID;
    }
    public int getNumberProcessedRequests() {
        return numberProcessedRequests;
    }
    public String getExchangeName() {
        return exchangeName;
    }
    public String getAvailability() { return availability; }
}
