package st.whineHouse.rainserver;

import java.net.InetAddress;

public class ServerProjectile {
    public int id;
    public int projectilType;
    public double x;
    public double y;
    public double dir;

    public InetAddress address;
    public int port;

    private static int idCounter = 1;

    public ServerProjectile(int projectilType, double x, double y, double dir, InetAddress address, int port) {
        id = idCounter ++;
        this.projectilType = projectilType;
        this.x=x;
        this.y=y;
        this.dir=dir;
        this.address = address;
        this.port = port;
    }

    public int hashCode() {
        return id;
    }
}
