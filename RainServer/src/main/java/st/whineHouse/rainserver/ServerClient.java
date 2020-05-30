package st.whineHouse.rainserver;

import java.net.InetAddress;

public class ServerClient {
    public int userID;
    public String username;
    public int x;
    public int y;
    public int speed = 0;
    public boolean walking;
    public int movingDir = 1;

    public InetAddress address;
    public int port;
    public boolean status = false;

    private static int userIDCounter = 1;

    public ServerClient(String username, int x, int y, InetAddress address, int port) {
        userID = userIDCounter ++;
        this.username = username;
        this.address = address;
        this.port = port;
        status = true;
    }

    public int hashCode() {
        return userID;
    }
}
