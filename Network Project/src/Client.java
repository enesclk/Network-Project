

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

    public InetAddress Ip;
    public int Port = 2000;
    public int id;
    public String kullaniciAdi;

    Client(InetAddress Ip, String kullaniciAdi) {
        this.Ip = Ip;
        this.kullaniciAdi = kullaniciAdi;

    }

}

