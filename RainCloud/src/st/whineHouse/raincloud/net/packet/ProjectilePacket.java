package st.whineHouse.raincloud.net.packet;

import st.whineHouse.rain.net.Client;
import st.whineHouse.rain.utilities.BinaryWriter;
import st.whineHouse.rainserver.Server;

import static st.whineHouse.raincloud.net.packet.PacketType.PROJECTILE;

public class ProjectilePacket extends Packet {

    private int projectileType;
    private double x, y, dir;

    public ProjectilePacket(byte[] data) {
        super(PROJECTILE);
        String[] dataArray = readData(data).split(",");
        this.projectileType = Integer.parseInt(dataArray[0]);
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.dir = Float.parseFloat(dataArray[3]);
    }

    public ProjectilePacket(int projectileType, double x, double y, double dir) {
        super(PROJECTILE);
        this.projectileType = projectileType;
        this.x = x;
        this.y = y;
        this.dir = dir;
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
        writer.write(PROJECTILE.getId());
        writer.write((
            this.projectileType + "," +
            x + "," +
            y + "," +
            dir + ","
        ).getBytes());
        return writer.getBuffer();
    }


    public int getProjectileType() {
        return projectileType;
    }

    public double getDir() {
        return dir;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
