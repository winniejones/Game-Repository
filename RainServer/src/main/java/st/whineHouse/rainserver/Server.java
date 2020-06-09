package st.whineHouse.rainserver;

import st.whineHouse.raincloud.net.host.Host;
import st.whineHouse.raincloud.net.packet.*;
import st.whineHouse.raincloud.serialization.RCDatabase;
import st.whineHouse.raincloud.serialization.RCField;
import st.whineHouse.raincloud.serialization.RCObject;
import st.whineHouse.raincloud.serialization.Type;
import st.whineHouse.raincloud.shared.ProjectileType;
import st.whineHouse.rainserver.entity.ServerEntity;
import st.whineHouse.rainserver.entity.ServerMob;
import st.whineHouse.rainserver.projectiles.ServerProjectile;
import st.whineHouse.rainserver.user.ServerPlayer;
import st.whineHouse.rainserver.world.ServerLevel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server extends Host {
    private int port;
    private Thread listeningThread;
    private boolean listening = false;
    private DatagramSocket socket;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
    ServerLevel level;
    private List<ServerClient> clients = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public synchronized void start(ServerLevel level) {
        this.level = level;
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

    public synchronized void stop() {
        try {
            listeningThread.join();
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
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



    public synchronized List<ServerClient> getPlayers(ServerEntity e, int radius){
        List<ServerClient> result = new ArrayList<>();
        int ex = e.getX();
        int ey = e.getY();
        for(int i =0; i< clients.size(); i++){
            ServerClient player = clients.get(i);
            int x = player.x;
            int y = player.y;
            int dx = Math.abs(x - ex);
            int dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx*dx)+(dy*dy));
            if(distance <= radius) result.add(player);
        }
        return result;
    }

    private void process(RCDatabase database) {
        dump(database);
    }


    private void process(DatagramPacket dataPacket) {
        byte[] data = dataPacket.getData();
        InetAddress address = dataPacket.getAddress();
        int port = dataPacket.getPort();
        //dump(dataPacket);
        if(new String(data, 0, 4).equals("RCDB")) {
            RCDatabase database = RCDatabase.Deserialize(data);
            process(database);
        } else if (data[0] == 0x40 && data[1] == 0x40) {
            byte[] filteredData = Arrays.copyOfRange(data, 3, data.length -3);
            switch (data[2]) {
                case 0: // Connection
                    System.out.println("Client connected");
                    break;
                case 1: //Login
                    LoginPacket loginPacket = new LoginPacket(filteredData);
                    System.out.println("Client "
                                    + loginPacket.getUsername()
                                    +" has logged in with position("
                                    +loginPacket.getX()+","+loginPacket.getY()+")"
                    );
                    ServerClient client = new ServerClient(loginPacket.getUsername(), loginPacket.getX(), loginPacket.getY(), address, port);
                    addConnection(client, loginPacket);
                    break;
                case 2: // Disconnect
                    LogoutPacket logoutPacket = new LogoutPacket(filteredData);
                    System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                            + (logoutPacket).getUsername() + " has left...");
                    removeConnection(logoutPacket);
                    break;
                case 3: // move packet
                    MovePacket movePacket = new MovePacket(filteredData);
                    //System.out.println("Handle move packet from " + movePacket.getUsername());
                    handleMove(movePacket);
                    break;
                case 4: // error
                    break;
                case 5: // projectiles
                    ProjectilePacket projectilePacket = new ProjectilePacket(filteredData);
                    handleProjectiles(projectilePacket);
                    projectilePacket.broadcastData(this);
                    break;
                case 6: // projectiles
                    MobPacket mobPacket = new MobPacket(filteredData);
                    mobPacket.broadcastData(this);
                    break;
                default: System.out.println("Recieved packet but cannot handle response");
            }
        }
    }

    private void handleProjectiles(ProjectilePacket projectilePacket) {
        try {
            ServerProjectile projectile = new ServerProjectile(
                    projectilePacket.getX(),
                    projectilePacket.getY(),
                    projectilePacket.getDir(),
                    ProjectileType.getProjectileType(projectilePacket.getProjectileType())
            );
            // logProjectile(projectilePacket);
            Rainserver.rainserver.level.addProjectile(projectile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logProjectile(ProjectilePacket projectilePacket) {
        System.out.println("adding projectile: "
                + projectilePacket.getProjectileType() + ", at: ("
                + projectilePacket.getX() + ","
                + projectilePacket.getY() + "): (x,y)");
    }

    private void sendMobs(InetAddress address, int port) {
        List<ServerMob> mobs = Rainserver.rainserver.level.getMobs();
        for(int i = 0; i < mobs.size(); i++) {
            ServerMob mob = mobs.get(i);
            MobPacket mobPacket = new MobPacket(mob.getType().getId(),mob.getId(),mob.x,mob.y);
            send(mobPacket.getData(), address, port);
        }
    }

    private void addConnection(ServerClient addedClient, LoginPacket loginPacket) {
        boolean alreadyConnected = false;
        for(ServerClient client: clients) {
            if(addedClient.username.equalsIgnoreCase(client.username)) {
                if(client.address == null) {
                    client.address = addedClient.address;
                }
                if(client.port == -1) {
                    client.port = addedClient.port;
                }
                System.out.println(client.username + " is already connected");
                alreadyConnected = true;
            } else {
                // send packet to other playas so they know whats up
                send(loginPacket.getData(), client.address, client.port);

                loginPacket = new LoginPacket(client.username, client.x, client.y);
                send(loginPacket.getData(), addedClient.address, addedClient.port);
            }
        }
        if(!alreadyConnected) {
            clients.add(addedClient);
            ServerPlayer player = new ServerPlayer(loginPacket.getUsername(), loginPacket.getX(), loginPacket.getY(), addedClient.address, addedClient.port);
            level.addPlayer(player);
            sendMobs(addedClient.address, addedClient.port);
        }
    }

    private void removeConnection(LogoutPacket logoutPacket){
        clients.removeIf(player -> player.username.equalsIgnoreCase(logoutPacket.getUsername()));
        logoutPacket.broadcastData(this);
    }

    @Override
    public void send(byte[] data) throws Exception {
        throw new Exception("Not implemented in server");
    }

    @Override
    public void broadcastToClients(byte[] data) {
        for (ServerClient p : clients) {
            send(data, p.address, p.port);
        }
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

    public ServerClient getPlayerMP(String username) {
        for (ServerClient client : this.clients) {
            if (client.username.equals(username)) {
                return client;
            }
        }
        return null;
    }


    private void handleMove(MovePacket packet) {
        ServerClient client = getPlayerMP(packet.getUsername());
        if ( client != null) {
            // Save client state
            ServerClient player = getPlayerMP(client.username);
            player.x = packet.getX();
            player.y = packet.getY();
            player.speed = packet.getSpeed();
            player.walking = packet.isWalking();
            player.movingDir = packet.getMovingDir();
            packet.broadcastData(this);
            level.movePlayer(packet.getUsername(),packet.getX(),packet.getY(),packet.getSpeed(),packet.isWalking());
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
