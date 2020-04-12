package st.whineHouse.raincloud.net.packet;

import st.whineHouse.rain.net.Client;
import st.whineHouse.rain.utilities.BinaryWriter;
import st.whineHouse.rainserver.Server;

import static st.whineHouse.raincloud.net.packet.PacketType.MOVE;

public class MovePacket extends Packet {

    private String username;
    private int x, y;

    private int speed = 0;
    private boolean walking;
    private int movingDir = 1;

    public MovePacket(byte[] data) {
        super(MOVE);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.speed = Integer.parseInt(dataArray[3]);
        this.walking = Boolean.parseBoolean(dataArray[4]);
        this.movingDir = Integer.parseInt(dataArray[5]);
    }

    public MovePacket(String username, int x, int y, int speed, boolean walking) {
        super(MOVE);
        this.username = username;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.walking = walking;
    }

    @Override
    public void writeData(Client client) {
        client.send(getData());
    }

    @Override
    public void writeData(Server server) {
        server.broadcastToClients(getData());
    }

    @Override
    public byte[] getData() {
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(MOVE.getId());
        writer.write((
            this.username + "," +
            x + "," +
            y + "," +
            speed + "," +
            walking + "," +
            movingDir
        ).getBytes());
        return writer.getBuffer();
    }

    public boolean isWalking() {
        return walking;
    }

    public int getMovingDir() {
        return movingDir;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUsername() {
        return username;
    }
}
