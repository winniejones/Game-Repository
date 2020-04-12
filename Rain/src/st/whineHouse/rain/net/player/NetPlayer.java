package st.whineHouse.rain.net.player;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.mob.player.Player;
import st.whineHouse.rain.gx.Screen;
import st.whineHouse.rain.input.Keyboard;

import java.net.InetAddress;

public class NetPlayer extends Player {
    public InetAddress address;
    public int port;

    public NetPlayer(String username,int x, int y, Keyboard input, InetAddress address, int port) {
        super(username,x,y,input);
        this.address = address;
        this.port = port;
    }

    public NetPlayer(String username, int x, int y, InetAddress address, int port) {
        super(username, x,y, null);
        this.address = address;
        this.port = port;
    }

    @Override
    public void update() {
        super.update();
    }

    /*public void render(Screen screen) {
        screen.fillRect(x, y, 32, 32, 0x2030cc, true);
    }*/

}