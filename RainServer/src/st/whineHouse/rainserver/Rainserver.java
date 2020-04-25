package st.whineHouse.rainserver;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.mob.npc.*;
import st.whineHouse.rain.level.Level;
import st.whineHouse.raincloud.net.packet.MobPacket;
import st.whineHouse.rainserver.entity.MobSpawner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Rainserver implements Runnable {
    public static Rainserver rainserver;
    private Server server;
    private volatile boolean stop, stopped = true;
    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private Thread serverThread;

    private MobSpawner spawner;
    public Level level;

    public Rainserver() {
        spawner = new MobSpawner();
        level = Level.spawn;

        int port = 8192;
        server = new Server(port);
        server.start(level);
    }

    public synchronized void start() {
        if(!stopped) {
            throw new RuntimeException("Server is already running");
        }
        stopped = stop = false;
        serverThread = new Thread(this);
        serverThread.setName(this + ":ServerThread");
        serverThread.start();
        updateSpawn(spawner.getMobs());
    }

    public synchronized void stop() {
        if (stop)
            return;
        stop = true;
        server.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        return !stop;
    }

    public synchronized void updateSpawn(List<Mob> spawnMobs){
        List<Mob> mobs = level.getMobs();
        /*spawnMobs.forEach(mob -> {
            if(!level.getMobs().contains(mob)) {
                mobs.add(mob);
            }
        });*/
        for(int i = 0; i < spawnMobs.size(); i++) {
            Mob mob = spawnMobs.get(i);
            MobPacket mobPacket = null;
            if(mob instanceof DeidaraMob) {
                mobPacket = new MobPacket(1,mob.getId(),mob.x,mob.y);
            } else if (mob instanceof HirukoMob) {
                mobPacket = new MobPacket(2,mob.getId(),mob.x,mob.y);
            }else if (mob instanceof ItachiMob) {
                mobPacket = new MobPacket(3,mob.getId(),mob.x,mob.y);
            }else if (mob instanceof OrochimaruMob) {
                mobPacket = new MobPacket(4,mob.getId(),mob.x,mob.y);
            }else if (mob instanceof Shooter) {
                mobPacket = new MobPacket(5,mob.getId(),mob.x,mob.y);
            }
            if(mobPacket != null){
                mobPacket.writeData(server);
                level.addMob(mob);
            }
        }
    }

    public static void main(String[] args) {
        rainserver = new Rainserver();
        rainserver.start();

    }

    private void update() {
        List<Mob> mobs = level.getMobs();
        for (int i = 0; i < mobs.size(); i++) {
            mobs.get(i).update();
        }
        List<Mob> players = level.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).update();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        while (isRunning()) {
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime = now;
            while(delta >= 1){
                update();
                delta--;
            }
//            if(System.currentTimeMillis()-timer > 600000) {
//                timer += 600000;
//                updateSpawn(spawner.getMobs());
//            }
        }
        stop();
    }

    public Server getServer(){
        return server;
    }
}
