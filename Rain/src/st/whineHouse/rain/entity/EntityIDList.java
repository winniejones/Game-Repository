package st.whineHouse.rain.entity;

/**
 * Denna klass används inte än.
 * EntityIDList-klass
 * Tänkt att vara till för att hålla koll på varje etentitet som id.
 * 
 * @author winston jones
 *
 */
public enum EntityIDList {
	PLAYER,
	MOB;
	
	
	private String name;
	private int id;
	
	private EntityIDList(){
		
	}
	private EntityIDList(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
	}
	

}
