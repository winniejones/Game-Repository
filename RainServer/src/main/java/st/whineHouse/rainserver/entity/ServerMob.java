package st.whineHouse.rainserver.entity;

import st.whineHouse.raincloud.graphics.Sprite;
import st.whineHouse.raincloud.net.packet.MovePacket;
import st.whineHouse.raincloud.net.packet.ProjectilePacket;
import st.whineHouse.raincloud.shared.MobType;
import st.whineHouse.raincloud.shared.ProjectileType;
import st.whineHouse.raincloud.utility.Node;
import st.whineHouse.raincloud.utility.RayCastingResult;
import st.whineHouse.raincloud.utility.Vector2i;
import st.whineHouse.rainserver.Rainserver;
import st.whineHouse.rainserver.projectiles.ServerProjectile;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mob-klassen som är en Entity-klass
 * Används som en entitet som har en attribut som en gubbe eller spelare m.m.
 *
 * @author Winston Jones
 */
public class ServerMob extends ServerEntity {

    private int id;
    private int time = 0;
    private AtomicInteger aint = new AtomicInteger();
    public boolean walking = false;
    private int numOfSteps = 0;
    public int health;
    private ServerProjectile projectile;
    private int weaponID;
    protected Boolean isColliding = false;
    private MobType type;
    private double speed;
    private int xa = 0, ya = 0;
    private List<Node> path = null;

    public MobType getType() {
        return type;
    }

    protected enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    protected Direction dir;

    public ServerMob(int x, int y, int id, MobType type) {
        super(x, y);
        this.id = id;
        this.type = type;
        setProperties(type);
        setWeaponId();
    }

    private void setProperties(MobType type) {
        switch (type) {
            case HIROKU:
                this.speed = 0.85;
                this.health = 70;
                break;
            case ITACHI:
                this.speed = 1;
                this.health = 500;
                break;
            case KISUNE:
                this.speed = 1.25;
                this.health = 300;
                break;
            case DEIDARA:
                this.speed = 2;
                this.health = 300;
                break;
            case OROCHIMARU:
                this.speed = 1;
                this.health = 100;
                break;
            default:
                this.speed = 1;
                break;
        }
    }

    private void setWeaponId() {
        switch (type) {
            default:
                weaponID = 1;
                break;
        }
    }

    private void move(double xa, double ya) {
        if (xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            numOfSteps--;
            return;
        }
        numOfSteps++;
        if (xa > 0) dir = Direction.RIGHT;
        if (xa < 0) dir = Direction.LEFT;
        if (ya > 0) dir = Direction.DOWN;
        if (ya < 0) dir = Direction.UP;

        while (xa != 0) {
            if (Math.abs(xa) > 1) {
                if (!collision(abs(xa), ya)) {
                    this.x += abs(xa);
                }
                xa -= abs(xa);
            } else {
                if (!collision(abs(xa), ya)) {
                    this.x += xa;
                }
                xa = 0;
            }
        }
        while (ya != 0) {
            if (Math.abs(ya) > 1) {
                if (!collision(xa, abs(ya))) {
                    this.y += abs(ya);
                }
                ya -= abs(ya);
            } else {
                if (!collision(xa, abs(ya))) {
                    this.y += ya;
                }
                ya = 0;
            }
        }

    }

    private void moveRandom(int time) {
        if (time % (random.nextInt(50) + 30) == 0) {
            xa = random.nextInt(3) - 1;
            ya = random.nextInt(3) - 1;
            if (random.nextInt(3) == 0) {
                xa = 0;
                ya = 0;
            }
        }

        if (xa != 0 || ya != 0) {
            MovePacket movePacket = new MovePacket(id, x, y, 1, walking, 1, true);
            movePacket.broadcastData(Rainserver.rainserver.getServer());
            //logIsMoving();
            move(xa, ya);
            walking = true;
        } else {
            walking = false;
        }
    }

    private void logIsMoving() {
        System.out.println(
                type + " is moving from "
                        + (x) + ","
                        + (y) + ": (x,y))"
        );
    }

    private void followWithRadius(int time, int radius) {
        xa = 0;
        ya = 0;
        List<ServerMob> players = level.getPlayers(this, radius);
        if (players.size() > 0) {
            int px = players.get(0).getX();
            int py = players.get(0).getY();
            Vector2i start = new Vector2i(getX() >> 4, getY() >> 4);
            Vector2i destination = new Vector2i(px >> 4, py >> 4);
            if (time % 3 == 0) path = level.findPath(start, destination);
            if (path != null) {
                if (path.size() > 0) {
                    Vector2i vec = path.get(path.size() - 1).tile;
                    if ((int) x < vec.getX() << 4) xa += speed;
                    if ((int) x > vec.getX() << 4) xa -= speed;
                    if ((int) y < vec.getY() << 4) ya += speed;
                    if ((int) y > vec.getY() << 4) ya -= speed;
                }
            }
        } else if (players.size() == 0) {
            if (time % (random.nextInt(50) + 30) == 0) {
                xa = random.nextInt(3) - 1;
                ya = random.nextInt(3) - 1;
                if (random.nextInt(3) == 0) {
                    xa = 0;
                    ya = 0;
                }
            }
        }
        if (xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        } else {
            walking = false;
        }
    }

    private int abs(double value) {
        if (value < 0) return -1;
        return 1;
    }

    public void update() {
        time++;
        super.update();
        //moveRandom(time);
        move();
        shootClosest();
        if(health<=0){
            //System.out.println(type + " is ded");
            remove();
        }
    }

    private void move() {
        switch (type) {
            case ITACHI:
            case OROCHIMARU:
                moveRandom(time);
                break;
            case HIROKU:
                followWithRadius(time, 70);
                break;
            case KISUNE:
                followWithRadius(time, 30);
                break;
        }
    }

    private void shootClosest() {
        ServerMob closest = null;
        double min = 0;
        if (!level.getPlayers().isEmpty()) {
            List<ServerMob> players = level.getPlayers(this, 100);

            for (int i = 0; i < players.size(); i++) {
                ServerMob p = players.get(i);
                double distance = Vector2i.getDistance(new Vector2i(x, y), new Vector2i(p.getX(), p.getY()));
                if (i == 0 || distance < min) {
                    min = distance;
                    closest = p;
                }
            }
        }
        if (closest != null) {
            double dx = closest.getX() - x;
            double dy = closest.getY() - y;
            double dir = Math.atan2(dy, dx);
            RayCastingResult raycast = level.RayCast(new Vector2i(x, y), dir, (int) min);
            if (!raycast.hasCollided()) {
                if (time % 60 == 0) {
                    try {
                        // System.out.println(type + " is shooting from point (" + x + "," + y + ")");
                        shoot(x, y, dir, weaponID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void shoot(double x, double y, double dir, int weaponID) throws Exception {

        projectile = new ServerProjectile(x, y, dir, ProjectileType.getProjectileType(weaponID));

        ProjectilePacket projectilePacket = new ProjectilePacket(weaponID, x, y, dir);
        projectilePacket.broadcastData(Rainserver.rainserver.getServer());
        level.addProjectile(projectile);
    }


    private boolean collision(double xa, double ya) {
        boolean solid = false;
        for (int c = 0; c < 4; c++) {
            double xt = ((x + xa) - c % 2 * 16) / 16;
            double yt = ((y + ya) - c / 2 * 16 + 12) / 16;
            int ix = (int) Math.ceil(xt);
            int iy = (int) Math.ceil(yt);
            if (c % 2 == 0) ix = (int) Math.floor(xt);
            if (c / 2 == 0) iy = (int) Math.floor(yt);
            if (level.getTile(ix, iy).solid()) solid = true;

        }
        return solid;
    }

    public int getId() {
        return id;
    }
}
