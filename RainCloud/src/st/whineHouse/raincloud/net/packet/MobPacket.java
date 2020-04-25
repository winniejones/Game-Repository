package st.whineHouse.raincloud.net.packet;

import st.whineHouse.rain.net.Client;
import st.whineHouse.rain.utilities.BinaryWriter;
import st.whineHouse.rainserver.Server;

import static st.whineHouse.raincloud.net.packet.PacketType.MOBSPAWN;

public class MobPacket extends Packet {

    private int mobType, id, x, y;

    public MobPacket(byte[] data) {
        super(MOBSPAWN);
        String[] dataArray = readData(data).split(",");
        this.mobType = Integer.parseInt(dataArray[0]);
        this.id = Integer.parseInt(dataArray[1]);
        this.x = Integer.parseInt(dataArray[2]);
        this.y = Integer.parseInt(dataArray[3]);
    }

    public MobPacket(int mobType, int id, int x, int y) {
        super(MOBSPAWN);
        this.mobType = mobType;
        this.id = id;
        this.x = x;
        this.y = y;
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
        writer.write(MOBSPAWN.getId());
        writer.write((
            mobType + "," +
            id + "," +
            x + "," +
            y
        ).getBytes());
        return writer.getBuffer();
    }


    public int getMobType() {
        return mobType;
    }

    public int getId() {
        return id;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

}
