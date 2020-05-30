package st.whineHouse.rainserver.entity;

import java.util.ArrayList;
import java.util.List;

import static st.whineHouse.raincloud.shared.MobType.*;

public class MobSpawner {
    private List<ServerMob> mobs = new ArrayList<>();
    private final int TILE_SIZE = 16;
    private int incId = 1;
    public MobSpawner() {
        mobs.add(new ServerMob(15*TILE_SIZE,60*TILE_SIZE, incId++, DEIDARA));
        mobs.add(new ServerMob(17*TILE_SIZE,35*TILE_SIZE, incId++, HIROKU));
        //mobs.add(new ServerMob(10*TILE_SIZE,40*TILE_SIZE, incId++));
    mobs.add(new ServerMob(20*TILE_SIZE,48*TILE_SIZE, incId++, ITACHI));
        mobs.add(new ServerMob(15*TILE_SIZE,53*TILE_SIZE, incId++, OROCHIMARU));
    }

    public List<ServerMob> getMobs() {
        return mobs;
    }
}
