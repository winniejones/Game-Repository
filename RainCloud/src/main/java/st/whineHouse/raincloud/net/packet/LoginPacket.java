package st.whineHouse.raincloud.net.packet;

import st.whineHouse.raincloud.net.host.Host;
import st.whineHouse.raincloud.utility.BinaryWriter;

import static st.whineHouse.raincloud.net.packet.PacketType.LOGIN;

public class LoginPacket extends Packet {

    private String username;
    private int x, y, usernameLength;

    public LoginPacket(byte[] data) {
        super(LOGIN);
        String[] dataArray = readData(data).split(",");
        this.x = Integer.parseInt(dataArray[0]);
        this.y = Integer.parseInt(dataArray[1]);
        this.username = dataArray[2];
    }

    public LoginPacket(String username, int x, int y) {
        super(LOGIN);
        this.username = username;
        this.usernameLength = username.length();
        this.x = x;
        this.y = y;
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
        writer.write(LOGIN.getId());
        writer.write(
            (
                getX() + "," +
                getY() + "," +
                this.username
            ).getBytes()
        );
        return writer.getBuffer();
    }

    /*public byte[] getData_test() {
        byte[] bytes = new byte[15 + usernameLength];
        int i = 0, j;
        bytes[i++] = PACKET_HEADER[0];
        bytes[i++] = PACKET_HEADER[1];
        bytes[i++] = LOGIN.getId();
        j = usernameLength;
        bytes[i++] = (byte) ((j >> 24) & 0xFF);
        bytes[i++] = (byte) ((j >> 16) & 0xFF);
        bytes[i++] = (byte) ((j >> 8) & 0xFF);
        bytes[i++] = (byte) (j & 0xFF);
        j = x;
        bytes[i++] = (byte) ((j >> 24) & 0xFF);
        bytes[i++] = (byte) ((j >> 16) & 0xFF);
        bytes[i++] = (byte) ((j >> 8) & 0xFF);
        bytes[i++] = (byte) (j & 0xFF);
        j = y;
        bytes[i++] = (byte) ((j >> 24) & 0xFF);
        bytes[i++] = (byte) ((j >> 16) & 0xFF);
        bytes[i++] = (byte) ((j >> 8) & 0xFF);
        bytes[i++] = (byte) (j & 0xFF);
        for (char c : username.toCharArray()) {
            bytes[i++] = (byte) c;
        }
        return bytes;
    }*/

    /*public void construct(byte[] bytes){
        int i = 3;
        System.out.println("username: "+username);
        System.out.println("usernameLenth: "+usernameLength);
        System.out.println("x: "+x);
        System.out.println("y: "+y);
        usernameLength = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
        //x
        x = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
        //y
        y = ((bytes[i++] & 0xFF) << 24) | ((bytes[i++] & 0xFF) << 16) | ((bytes[i++] & 0xFF) << 8) | (bytes[i++] & 0xFF);
        StringBuilder builder = new StringBuilder(usernameLength - 10);
        for (x = 0; x < usernameLength; x++) {
            builder.append((char) bytes[i++]);
        }
        username = builder.toString();
        System.out.println("username: "+username);
        System.out.println("usernameLenth: "+usernameLength);
        System.out.println("x: "+x);
        System.out.println("y: "+y);
    }*/

    public String getUsername() {
        return username;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
