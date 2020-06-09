package st.whineHouse.rain.net;

import st.whineHouse.rain.Game;
import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.mob.npc.*;
import st.whineHouse.rain.entity.projectile.NinjaBlade;
import st.whineHouse.rain.entity.projectile.Projectile;
import st.whineHouse.rain.entity.projectile.WizardProjectile;
import st.whineHouse.rain.entity.projectile.WizzardArrow;
import st.whineHouse.rain.net.player.NetPlayer;
import st.whineHouse.raincloud.utility.BinaryWriter;
import st.whineHouse.raincloud.net.packet.*;
import st.whineHouse.raincloud.serialization.RCDatabase;
import st.whineHouse.raincloud.serialization.RCField;
import st.whineHouse.raincloud.serialization.RCObject;
import st.whineHouse.raincloud.serialization.Type;
import st.whineHouse.raincloud.net.host.Host;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Client extends Host {
    private final static byte[] PACKET_HEADER = new byte[] { 0x40, 0x40 };
    private final static byte PACKET_TYPE_CONNECT = 0x00;
    private final static byte PACKET_TYPE_LOGIN = 0x01;
    private final static byte PACKET_TYPE_DISCONNECT = 0x02;
    private final static byte PACKET_TYPE_MOVE = 0x03;
    private final static byte PACKET_TYPE_INVALID = 0x04;
    private Game game;


    public enum Error {
        NONE, INVALID_HOST, SOCKET_EXCEPTION
    }

    private String ipAddress;
    private int port;
    private boolean listening = false;
    private Thread listeningThread;
    private Error errorCode = Error.NONE;

    private InetAddress serverAddress;
    private DatagramSocket socket;

    /**
     *
     * @param host
     *            Eg. 192.168.1.1:5000
     */
    public Client(String host) {
        String[] parts = host.split(":");
        if (parts.length != 2) {
            errorCode = Error.INVALID_HOST;
            return;
        }
        ipAddress = parts[0];
        try {
            port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            errorCode = Error.INVALID_HOST;
            return;
        }
    }

    /**
     *
     * @param host
     *            Eg. 192.168.1.1
     * @param port
     *            Eg. 5000
     */
    public Client(Game game, String host, int port) {
        this.game = game;
        this.ipAddress = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            serverAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            errorCode = Error.INVALID_HOST;
            return false;
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            errorCode = Error.SOCKET_EXCEPTION;
            return false;
        }
        System.out.println("Connecting to server..");
        listening = true;

        listeningThread = new Thread(this::listen, "RainServer-ListenThread");
        listeningThread.start();
        sendConnectionPacket();
        // Wait for server to reply
        return true;
    }

    public void listen() {
        while (listening) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            process(packet);
        }
    }

    private void process(DatagramPacket datagramPacket) {
        byte[] data = datagramPacket.getData();
        InetAddress address = datagramPacket.getAddress();
        int port = datagramPacket.getPort();
        // dump(datagramPacket);
        if (data[0] == 0x40 && data[1] == 0x40) {
            byte[] filteredData = Arrays.copyOfRange(data, 3, data.length -3);
            switch (data[2]) {
                case 0:
                    break;
                case 1:
                    LoginPacket loginPacket = new LoginPacket(filteredData);
                    System.out.println("Recieving connection packet with data length..: " + data.length );
                    handleLogin(loginPacket, address, port);
                    // connection packet
                    break;
                case 2: // disconnect packet
                    LogoutPacket logoutPacket = new LogoutPacket(filteredData);
                    System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                            + (logoutPacket).getUsername() + " has left...");
                    game.level.removePlayer(logoutPacket.getUsername());
                    break;
                case 3: // move packet
                    MovePacket movePacket = new MovePacket(filteredData);
                    //System.out.println("Handle move packet from user " + movePacket.getUsername());
                    handleMove(movePacket);
                    break;
                case 4:
                    // error
                    break;
                case 5: // projectiles
                    ProjectilePacket projectilePacket = new ProjectilePacket(filteredData);
                    handleProjectiles(projectilePacket);
                    break;
                case 6: // projectiles
                    MobPacket mobPacket = new MobPacket(filteredData);
                    handleMob(mobPacket);
                    break;
            }
        } else System.out.println("Something went left from recieving data");
    }

    private void handleProjectiles(ProjectilePacket projectilePacket) {
        Projectile projectile = null;
        switch (projectilePacket.getProjectileType()){
            case 2:
                projectile = new WizzardArrow(
                        projectilePacket.getX(),
                        projectilePacket.getY(),
                        projectilePacket.getDir()
                        );
                break;
            case 1:
                projectile = new NinjaBlade(
                        projectilePacket.getX(),
                        projectilePacket.getY(),
                        projectilePacket.getDir()
                );
                break;
            case 3:
                projectile = new WizardProjectile(
                        projectilePacket.getX(),
                        projectilePacket.getY(),
                        projectilePacket.getDir()
                );
                break;
        }
        if(projectile != null) {
            Game.game.level.add(projectile);
        }
    }

    private void handleMob(MobPacket mobPacket) {
        Mob mob = null;
        switch (mobPacket.getMobType()){
            case 1:
                mob= new DeidaraMob(
                        mobPacket.getX(),
                        mobPacket.getY(),
                        mobPacket.getId()
                        );
                break;
            case 2:
                mob= new HirukoMob(
                        mobPacket.getX(),
                        mobPacket.getY(),
                        mobPacket.getId()
                );
                break;
            case 3:
                mob= new ItachiMob(
                        mobPacket.getX(),
                        mobPacket.getY(),
                        mobPacket.getId()
                );
                break;
            case 5:
                mob= new OrochimaruMob(
                        mobPacket.getX(),
                        mobPacket.getY(),
                        mobPacket.getId()
                );
                break;
            case 4:
                mob= new KisuneMob(
                        mobPacket.getX(),
                        mobPacket.getY(),
                        mobPacket.getId()
                );
                break;
        }
        if(mob != null) {
            Game.game.level.add(mob);
        }
    }

    private void handleLogin(LoginPacket packet, InetAddress address, int port) {
        System.out.println(
                "[" + address.getHostAddress() + ":" + port + "] "
                + packet.getUsername()
                + " has joined the game..."
        );
        NetPlayer player = new NetPlayer(
            packet.getUsername(),
            packet.getX(),
            packet.getY(),
            address,
            port
        );
        this.game.level.addPlayer(player);
    }
    private void handleMove(MovePacket packet) {
        if(packet.isMob()){
            //System.out.println("[" + packet.getUsername() + " has now moved to " + packet.getX() + "," + packet.getY() + "] ");
            game.level.moveMob(
                    Integer.parseInt(packet.getUsername()),
                    packet.getX(),
                    packet.getY(),
                    packet.isWalking()
            );
        } else {
            //System.out.println("[" + packet.getUsername() + " has now moved to " + packet.getX() +"," + packet.getY() + "] ");
            game.level.movePlayer(
                    packet.getUsername(),
                    packet.getX(),
                    packet.getY(),
                    packet.getSpeed(),
                    packet.isWalking()
            );
        }
    }

    private void sendConnectionPacket() {
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(PACKET_TYPE_CONNECT);
        send(writer.getBuffer());
    }

    @Override
    public void send(byte[] data) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcastToClients(byte[] data) throws Exception {
        throw new Exception("Not implemented in client");
    }

    public void send(RCDatabase database) {
        byte[] data = new byte[database.getSize()];
        database.getBytes(data, 0);
        send(data);
    }

    public Error getErrorCode() {
        return errorCode;
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
