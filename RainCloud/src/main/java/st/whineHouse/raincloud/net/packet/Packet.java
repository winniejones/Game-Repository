package st.whineHouse.raincloud.net.packet;


import st.whineHouse.raincloud.net.host.Host;

import java.util.Arrays;

public abstract class Packet {
    protected final static byte[] PACKET_HEADER = new byte[] { 0x40, 0x40 };
    public PacketType packetType;
    public Packet(PacketType packetType) {
        this.packetType = packetType;
    }

    public abstract void writeData(Host client);

    public abstract void broadcastData(Host server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message;
    }

    public abstract byte[] getData();
}
