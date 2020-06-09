package st.whineHouse.raincloud.net.packet;

import st.whineHouse.raincloud.net.host.Host;
import st.whineHouse.raincloud.utility.BinaryWriter;

import java.util.Arrays;

import static st.whineHouse.raincloud.net.packet.PacketType.PROJECTILE;

public class ProjectilePacket extends Packet {

    private int projectileType;
    private double x, y, dir;

    public ProjectilePacket(byte[] data) {
        super(PROJECTILE);
        String[] dataArray = readData(data).split(",");
        this.projectileType = Integer.parseInt(dataArray[0]);
        this.x = Double.parseDouble(dataArray[1]);
        this.y = Double.parseDouble(dataArray[2]);
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
    public void writeData(Host client) {
        try {
            client.send(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcastData(Host server) {
        try {
            //System.out.println("projectilepacket: "+ toString());
            server.broadcastToClients(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getData() {
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(PROJECTILE.getId());
        writer.write((
            this.projectileType + "," +
            this.x + "," +
            this.y + "," +
            this.dir + ","
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

    public String toString(){
        return
                "type: " + getProjectileType()
                + ", dir: " + getDir() + ", "+
                + getX() + ","+ getY()+ ": (x,y)";
    }
}
