package st.whineHouse.rainserver;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Rainserver {
    public static void main(String[] args) {
        int port = 8192;
        Server server = new Server(port);
        server.start();

        InetAddress address = null;
        try {
            address = InetAddress.getByName("192.168.1.10");
        } catch (UnknownHostException uhe){
            uhe.printStackTrace();
        }
        server.send(new byte[] {0, 1, 2}, address, port);
    }
}
