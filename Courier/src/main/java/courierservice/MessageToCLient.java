package courierservice;

// Formato das mensagens enviadas ao Client
public class MessageToCLient {
    private String key;
    private String ID;
    public MessageToCLient(String key, String ID) {
        this.key = key;
        this.ID = ID;
    }
}
