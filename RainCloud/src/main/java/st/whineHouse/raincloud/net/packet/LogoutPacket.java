package st.whineHouse.raincloud.net.packet;


import st.whineHouse.raincloud.net.host.Host;
import st.whineHouse.raincloud.utility.BinaryWriter;

import static st.whineHouse.raincloud.net.packet.PacketType.DISCONNECT;
import static st.whineHouse.raincloud.net.packet.PacketType.LOGIN;

public class LogoutPacket extends Packet {

    private String username;

    public LogoutPacket(byte[] data) {
        super(DISCONNECT);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
    }

    public LogoutPacket(String username, int x, int y) {
        super(DISCONNECT);
        this.username = username;
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
            server.broadcastToClients(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public byte[] getData() {
        BinaryWriter writer = new BinaryWriter();
        writer.write(PACKET_HEADER);
        writer.write(DISCONNECT.getId());
        writer.write((this.username).getBytes());
        return writer.getBuffer();
    }

    public String getUsername() {
        return username;
    }
}
