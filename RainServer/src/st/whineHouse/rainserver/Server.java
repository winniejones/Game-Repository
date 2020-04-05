package st.whineHouse.rainserver;

import st.whineHouse.raincloud.serialization.RCDatabase;
import st.whineHouse.raincloud.serialization.RCField;
import st.whineHouse.raincloud.serialization.RCObject;
import st.whineHouse.raincloud.serialization.Type;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

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

    private Set<ServerClient> clients = new HashSet<>();

     public void start() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        System.out.println((String.format("Started server on port %s...", port)));

        listening = true;

        listeningThread = new Thread(this::listen, "RainServer-ListenThread");
        listeningThread.start();
        System.out.println("Server is listening...");
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
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        dump(packet);
        if(new String(data, 0, 4).equals("RCDB")) {
            RCDatabase database = RCDatabase.Deserialize(data);
//            String username = database.findObject("root").findString("username").getString();
            process(database);
        } else if (data[0] == 0x40 && data[1] == 0x40) {
            switch (data[2]) {
                case 0x01:
                    clients.add(new ServerClient(packet.getAddress(), packet.getPort()));
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

    private void dump(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        System.out.println("----------------------------------------");
        System.out.println("PACKET:");
        System.out.println("\t" + address.getHostAddress() + ":" + port);
        System.out.println();
        System.out.println("\tContents:");
        System.out.print("\t\t");

        for (int i = 0; i < packet.getLength(); i++) {
            System.out.printf("%x ", data[i]);
            if ((i + 1) % 16 == 0)
                System.out.print("\n\t\t");
        }

        System.out.println();
        System.out.println("----------------------------------------");
    }

    private void dump(RCDatabase database) {
        System.out.println("----------------------------------------");
        System.out.println("               RCDatabase               ");
        System.out.println("----------------------------------------");
        System.out.println("Name: " + database.getName());
        System.out.println("Size: " + database.getSize());
        System.out.println("Object Count: " + database.objects.size());
        System.out.println();
        for (RCObject object : database.objects) {
            System.out.println("\tObject:");
            System.out.println("\tName: " + object.getName());
            System.out.println("\tSize: " + object.getSize());
            System.out.println("\tField Count: " + object.fields.size());
            for (RCField field : object.fields) {
                System.out.println("\t\tField:");
                System.out.println("\t\tName: " + field.getName());
                System.out.println("\t\tSize: " + field.getSize());
                String data = "";
                switch (field.type) {
                    case Type.BYTE:
                        data += field.getByte();
                        break;
                    case Type.SHORT:
                        data += field.getShort();
                        break;
                    case Type.CHAR:
                        data += field.getChar();
                        break;
                    case Type.INTEGER:
                        data += field.getInt();
                        break;
                    case Type.LONG:
                        data += field.getLong();
                        break;
                    case Type.FLOAT:
                        data += field.getFloat();
                        break;
                    case Type.DOUBLE:
                        data += field.getDouble();
                        break;
                    case Type.BOOLEAN:
                        data += field.getBoolean();
                        break;
                }
                System.out.println("\t\tData: " + data);
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
    }
}
