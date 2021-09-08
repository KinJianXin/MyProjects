package game.item;

import edu.monash.fit2099.engine.*;

import java.util.Random;

/**
 * A class that represents a laser gun.
 */
public class LaserGun extends WeaponItem {
    /**
     * Constructor.
     */
    public LaserGun() {
        super("laser gun", '/', new Random().nextInt(51) + 50, "shoots");
    }

}
