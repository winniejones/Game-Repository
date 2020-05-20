package st.whineHouse.raincloud.shared;

public enum MobType {
    PLAYER(0),
    DEIDARA(1),
    HIROKU(2),
    ITACHI(3),
    KISUNE(4),
    OROCHIMARU(5);

    private int id;

    MobType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MobType getMobType(int id) throws Exception {
        for (MobType p : MobType.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new Exception("No MobType with that id");
    }
}
