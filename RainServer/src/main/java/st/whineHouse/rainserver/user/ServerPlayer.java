package whineHouse.rainserver.user;

import st.whineHouse.rainserver.entity.ServerMob;
import st.whineHouse.rainserver.projectiles.ServerProjectile;

import java.net.InetAddress;

import static st.whineHouse.raincloud.shared.MobType.PLAYER;
import static st.whineHouse.raincloud.shared.ProjectileType.WIZARDPROJECTILE;

public class ServerPlayer extends ServerMob {
    public InetAddress address;
    public int port, xBound, yBound;
    public String username;
    public ServerProjectile p;
    private boolean shooting = false;
    public double speed;

    public ServerPlayer(String username, int x, int y, InetAddress address, int port) {
        super(x,y, 0,PLAYER);
        this.address = address;
        this.port = port;
        this.username = username;
        this.isColliding = false;
        this.xBound = 1;
        this.yBound = 1;
        this.p = new ServerProjectile(x,y,1d, WIZARDPROJECTILE);
        this.health = 100;
        this.speed = 2.0;
        System.out.println("is created at position.. x: " +x+" y: "+y);
    }

    @Override
    public void update() {

    }

}