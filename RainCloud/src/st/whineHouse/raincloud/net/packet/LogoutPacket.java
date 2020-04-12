package st.whineHouse.raincloud.net.packet;

import st.whineHouse.rain.net.Client;
import st.whineHouse.rain.utilities.BinaryWriter;
import st.whineHouse.rainserver.Server;

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
        writer.write(DISCONNECT.getId());
        writer.write((this.username).getBytes());
        return writer.getBuffer();
    }

    public String getUsername() {
        return username;
    }
}
