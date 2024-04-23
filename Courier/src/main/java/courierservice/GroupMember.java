package courierservice;

import com.rabbitmq.client.Channel;
import couriertocourierappservice.Address;
import io.grpc.stub.StreamObserver;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GroupMember {

    private SpreadConnection connection;
    private SpreadGroup group;
    private MessageHandling msgHandling;
    private String courierID;
    private int numberProcessedRequests;
    private String availability;
    private Channel channel;
    private StreamObserver<Address> responseObserver;
    public GroupMember(String courierID, String address, int port, String regionName, Channel channel) {
        this.courierID = courierID;
        this.numberProcessedRequests = 0;
        this.channel = channel;
        this.availability = "occupied";
        // Estabelece a conexão ao servidor Spread com objeto MessageHandling especifico
        try  {
            connection = new SpreadConnection();
            connection.connect(InetAddress.getByName(address), port, regionName+courierID, false, true);
            msgHandling = new MessageHandling(this); connection.add(msgHandling);
            JoinGroup(regionName);
        }
        catch(SpreadException e)  {
            System.err.println("There was an error connecting to the daemon.");
            e.printStackTrace();
            System.exit(1);
        }
        catch(UnknownHostException e) {
            System.err.println("Can't find the daemon " + address);
            System.exit(1);
        }
    }

    //método para juntar se a um grupo de Spread
    private void JoinGroup(String groupName) throws SpreadException {
        SpreadGroup newGroup=new SpreadGroup();
        newGroup.join(connection, groupName);
        group = newGroup;
    }
    public SpreadGroup getGroup() {
        return group;
    }
    public String getCourierID() {
        return courierID;
    }
    public int getNumberProcessedRequests() {
        return numberProcessedRequests;
    }
    public void setNumberProcessedRequests(int numberProcessedRequests) {
        this.numberProcessedRequests = numberProcessedRequests;
    }
    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    public StreamObserver<Address> getResponseObserver() {
        return responseObserver;
    }
    public void setResponseObserver(StreamObserver<Address> responseObserver) {
        this.responseObserver = responseObserver;
    }
    public Channel getChannel() {
        return channel;
    }

    //método para enviar uma mensagem entre o grupo do tipo ELECTION ou RESULT
    public void SendMessage(String groupToSend, String txtMessage) throws SpreadException {
        SpreadMessage msg = new SpreadMessage();
        msg.setSafe();
        msg.addGroup(groupToSend);
        msg.setData(txtMessage.getBytes());
        msg.setType((short) 1);
        connection.multicast(msg);
    }

    //método para desconectar do servidor Spread
    public void close() throws SpreadException {
        // remove listener
        connection.remove(msgHandling);
        // Disconnect.
        connection.disconnect();
    }
}
