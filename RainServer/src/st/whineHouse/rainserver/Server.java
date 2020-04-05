package st.whineHouse.rainserver;

import st.whineHouse.raincloud.serialization.RCDatabase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    private int port;
    private Thread listeningThread;
    private boolean listening = false;
    private DatagramSocket socket;

    public Server(int port) {
        this.port = port;
    }

    private final int MAX_PACKET_SIZE = 1024;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];

     public void start() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        listening = true;

        listeningThread = new Thread(this::listen, "RainServer-ListenThread");
        listeningThread.start();
     }

     private void listen() {
        while (listening) {
            DatagramPacket packet = new DatagramPacket(receivedDataBuffer, MAX_PACKET_SIZE);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            process(packet);
        }
     }

     private void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        if(new String(data, 0, 4).equals("RCDB")) {
            RCDatabase database = RCDatabase.Deserialize(data);
            String username = database.findObject("root").findString("username").getString();
            process(database);
        } else {
            switch (data[0]) {
                case 1:
                    // connection packet
                    break;
                case 2:
                    // ping packet
                    break;
                case 3:
                    // login attempt packet
                    break;
            }
        }
     }

    private void process(RCDatabase database) {

    }

     public void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
}
