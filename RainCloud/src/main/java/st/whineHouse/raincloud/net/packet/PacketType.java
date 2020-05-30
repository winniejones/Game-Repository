package st.whineHouse.raincloud.net.packet;

public enum PacketType {
    INVALID((byte)0x00),
    LOGIN((byte)0x01),
    DISCONNECT((byte)0x02),
    MOVE((byte)0x03),
    PROJECTILE((byte)0x05),
    MOBSPAWN((byte)0x06),
    EVENT((byte)0x07);

    private byte id;

    PacketType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public static PacketType lookupPacket(byte id) {
        for (PacketType p : PacketType.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return PacketType.INVALID;
    }
}
