package st.whineHouse.raincloud.net.packet;

import st.whineHouse.rain.net.Client;
import st.whineHouse.rainserver.Server;

import java.util.Arrays;

public abstract class Packet {
    protected final static byte[] PACKET_HEADER = new byte[] { 0x40, 0x40 };
    public PacketType packetType;
    public Packet(PacketType packetType) {
        this.packetType = packetType;
    }

    public abstract void writeData(Client client);

    public abstract void writeData(Server server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message;
    }

    public abstract byte[] getData();
}
