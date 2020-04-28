package st.whineHouse.rainserver.entity;

import st.whineHouse.rain.entity.mob.Mob;
import st.whineHouse.rain.entity.mob.npc.*;

import java.util.ArrayList;
import java.util.List;

public class MobSpawner {
    private List<Mob> mobs = new ArrayList<>();
    private final int TILE_SIZE = 16;
    private int incId = 1;
    public MobSpawner() {
        mobs.add(new DeidaraMob(15*TILE_SIZE,60*TILE_SIZE, incId++));
        mobs.add(new HirukoMob(17*TILE_SIZE,35*TILE_SIZE, incId++));
        //mobs.add(new Shooter(10*TILE_SIZE,40*TILE_SIZE, incId++));
        mobs.add(new ItachiMob(20*TILE_SIZE,48*TILE_SIZE, incId++));
        mobs.add(new OrochimaruMob(15*TILE_SIZE,53*TILE_SIZE, incId++));
    }

    public List<Mob> getMobs() {
        return mobs;
    }
}
