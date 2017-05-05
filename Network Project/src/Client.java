
import java.net.InetAddress;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author My Computer
 */
public class Client {

    public static int uniqueId = 0;
    public InetAddress Ip;
    public int Port;
    public int id;

    Client(InetAddress Ip, int Port) {
        this.Ip = Ip;
        this.Port = Port;
        this.id = Client.uniqueId;
        Client.uniqueId++;
    }

}
