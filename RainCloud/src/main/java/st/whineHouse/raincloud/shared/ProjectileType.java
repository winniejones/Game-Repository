package st.whineHouse.raincloud.shared;

public enum ProjectileType {
    NINJABLADE(1),
    WIZARDPROJECTILE(2),
    WIZARDARROW(3);

    private int id;

    ProjectileType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ProjectileType getProjectileType(int id) throws Exception {
        for (ProjectileType p : ProjectileType.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new Exception("No ProjectileType with that id");
    }
}
