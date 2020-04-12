package st.whineHouse.rain.entity.spawner;

import st.whineHouse.rain.entity.mob.npc.KisuneMob;
import st.whineHouse.rain.entity.mob.Mob; 
import st.whineHouse.rain.level.Level;

public class MobSpawner extends Spawner{

	public MobSpawner(int x, int y,Mob mobType, int amount, Level level) {
		super(x, y, Type.MOB, amount, level);
		
		if(mobType instanceof KisuneMob){
			for(int i =0; i < amount; i++){
				
					level.add(new KisuneMob(x,y));
			}
		}
	}


}
