package st.whineHouse.raincloud.net.packet;

import st.whineHouse.rain.net.Client;
import st.whineHouse.rain.utilities.BinaryWriter;
import st.whineHouse.rainserver.Server;

import static st.whineHouse.raincloud.net.packet.PacketType.EVENT;
import static st.whineHouse.raincloud.net.packet.PacketType.MOVE;

public class EventPacket extends Packet {

    private String username;
    private int eventType; // 1: died
    private boolean isMob;
    private int hpChange;

    private int speed = 0;
    private boolean walking;
    private int movingDir = 1;


    public EventPacket(byte[] data) {
        super(EVENT);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.eventType = Integer.parseInt(dataArray[1]);
        this.isMob = Boolean.parseBoolean(dataArray[2]);
        this.hpChange = Integer.parseInt(dataArray[3]);
    }

    public EventPacket(String username, int eventType, boolean isMob, int hpChange) {
        super(EVENT);
        this.username = username;
        this.eventType = eventType;
        this.isMob = isMob;
        this.hpChange = hpChange;
    }

    public EventPacket(int id, int eventType, int hpChange) {
        super(EVENT);
        this.username = Integer.toString(id);
        this.isMob = true;
        this.eventType = eventType;
        this.hpChange = hpChange;
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
            eventType + "," +
            isMob
        ).getBytes());
        return writer.getBuffer();
    }

    public boolean isMob() {
        return isMob;
    }
    public int getEventType() {
        return this.eventType;
    }
    public String getUsername() {
        return username;
    }
}
