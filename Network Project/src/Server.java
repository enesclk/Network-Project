
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.JList;
import javax.swing.event.EventListenerList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author My Computer
 */
public class Server {

    private static DatagramSocket socketThis;
    private static Thread listen;
    public static ArrayList<Client> connectedClients = new ArrayList();
    public static ArrayList<Integer> connectedClientsID;
    private static EventListenerList messageReceivedListenerList = new EventListenerList();

    public static void create(int port) throws SocketException {
        Server.socketThis = new DatagramSocket(port);
        Server.connectedClients = new ArrayList();
        Server.listen = new Thread() {
            public void run() {
                System.out.println("Server ready");
                while (true) {
                    try {
                        byte[] rData = new byte[1024];
                        DatagramPacket rPack = new DatagramPacket(rData, rData.length);
                        Server.socketThis.receive(rPack);
                        String text = new String(rPack.getData());
                        Server.fireMessageReceivedEvent(text);
                        Client newclient = new Client(rPack.getAddress(), rPack.getPort());
                        Server.connectedClients.add(newclient);

                        rData = null;
                        rPack = null;
                        Client c = new Client(InetAddress.getByName("192.168.1.38"), 2000);

                        //sendMessage(UdpClient.message);
                    } catch (IOException ex) {
                        System.out.println("Hata!");
                    }
                }
            }
        };

    }

    public static void start() {
        Server.listen.start();
    }

    public static void stop() {
        Server.listen.interrupt();
        Server.connectedClients.clear();
        Server.socketThis.close();
    }

    public static void addClient(Client newclient) throws UnknownHostException {
        boolean isexist = false;
        Client c = new Client(InetAddress.getByName("172.16.16.200"), 3000);
        connectedClients.add(newclient);
        for (Client connectedClient : connectedClients) {

            if (connectedClient.Ip.toString().equals(newclient.Ip.toString()) && connectedClient.Port == newclient.Port) {
                isexist = true;
            }
        }
        if (!isexist) {
            Server.connectedClients.add(newclient);
        }

    }

    public static void sendMessage(String message) throws IOException {
        byte[] sData = new byte[1024];
        sData = message.getBytes();
        for (Client T : connectedClients) {
            System.out.println("msg to: " + T.Ip + " client: ");
            DatagramPacket sPack = new DatagramPacket(sData, sData.length, T.Ip, T.Port);
            Server.socketThis.send(sPack);
            sPack = null;
            sData = null;

        }
    }

    public static void sendFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] sData = new byte[(int) file.length()];
        fis.read(sData, 0, sData.length);
        for (Client T : connectedClients) {
            System.out.println("msg to: " + T.Ip + " client: ");
            DatagramPacket sPack = new DatagramPacket(sData, sData.length, T.Ip, T.Port);
            Server.socketThis.send(sPack);
            sPack = null;
            sData = null;
        }
        fis.close();
    }

    public class ReceivedMessage extends EventObject {

        public ReceivedMessage(Object source) {
            super(source);
        }
    }

    public interface ReceivedMessageListener extends EventListener {

        public void messageReceived(ReceivedMessage evt);

        public void messageReceived(String evt);

    }

    public static void addMyEventListener(ReceivedMessageListener listener) {
        Server.messageReceivedListenerList.add(ReceivedMessageListener.class, listener);

    }

    private static void fireMessageReceivedEvent(ReceivedMessage evt) {
        Object[] listeners = messageReceivedListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == ReceivedMessageListener.class) {
                ((ReceivedMessageListener) listeners[i + 1]).messageReceived(evt);
            }
        }
    }

    private static void fireMessageReceivedEvent(String evt) {
        Object[] listeners = messageReceivedListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == ReceivedMessageListener.class) {
                ((ReceivedMessageListener) listeners[i + 1]).messageReceived(evt);
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < connectedClientsID.size(); i++) {
            System.out.println(connectedClientsID.get(i));
        }
    }

}
