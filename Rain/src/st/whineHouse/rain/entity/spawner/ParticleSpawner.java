package st.whineHouse.rain.entity.spawner;

import st.whineHouse.rain.entity.particle.FlameParticle;
import st.whineHouse.rain.entity.particle.Particle;
import st.whineHouse.rain.gx.Sprite;
import st.whineHouse.rain.level.Level;

/**
 * ParticleSpawner-klassen är en Spawner-klass.
 * Används för att skapa partiklar. 
 * Används för tillfället när man skjuter projektiler på väggar, skapar particklar av kollisionen.
 * 
 * @author winston_8
 *
 */
public class ParticleSpawner extends Spawner{
	private int life;

	public ParticleSpawner(int x, int y,int life, int amount, Level level, Sprite sprite) {
		super(x, y, Type.PARTICLE, amount, level);
		this.life=life;
		for(int i =0; i < amount; i++){
			
				level.add(new Particle(x,y, life, sprite));
		}
	}
	
	public ParticleSpawner(int x, int y,int life, int amount, Level level, Sprite sprite, int type) {
		super(x, y, Type.PARTICLE, amount, level);
		this.life=life;
		
		if(type==0){
			for(int i =0; i < amount; i++){	
				level.add(new Particle(x,y, life, sprite));
			}
		} else if(type == 1){
			for(int i =0; i < amount; i++){	
				level.add(new FlameParticle(x,y, life, sprite));
			}
		}
	}

}
