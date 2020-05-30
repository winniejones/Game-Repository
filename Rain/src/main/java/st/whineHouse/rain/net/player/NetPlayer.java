package st.whineHouse.rain.net.player;

import st.whineHouse.rain.Game;
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
        System.out.println("is created at position.. x: " +x+" y: "+y);
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

    public void render(Screen screen) {
        super.render(screen);
        int positionOffset = Game.mobPixelSize/2;
        if(health > 0)
            screen.drawRect(x - positionOffset,y - (positionOffset+2),(int)(0.32*health),1,0x6add6a, true);
    }

}