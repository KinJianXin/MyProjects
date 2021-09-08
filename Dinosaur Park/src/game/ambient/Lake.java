package game.ambient;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import game.dinosaur.Pterodactyl;
import game.item.Fish;

import java.util.Random;

public class Lake extends Ground {

    private boolean isInitialised;
    private int sip;

    /**
     * Constructor.
     */
    public Lake() {
        super('~');
        isInitialised = false;
        sip = 25;
    }

    /**
     * Get the sip capacity of this lake.
     *
     * @return an integer representing the sip capacity of this lake
     */
    public int getSip() {
        return sip;
    }

    /**
     * Set the sip capacity of this lake with the specified value.
     *
     * @param sip an integer representing the sip capacity to set for this lake
     */
    public void setSip(int sip) {
        // Sip capacity range of lake = [0-25]
        this.sip = Math.max(0, Math.min(sip, 25));
    }

    /**
     * Check if this lake is passable.
     *
     * @return true only if actor is a flying Pterodactyl
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return actor instanceof Pterodactyl && ((Pterodactyl) actor).getCondition().equals("flying ");
    }

    /**
     * Lake can experience the passage of time.
     *
     * @param location the location of this ground
     */
    public void tick(Location location) {
        super.tick(location);
        // Lake starts with 5 fish
        if (!isInitialised) {
            for (int i = 0; i < 5; i++) {
                location.addItem(new Fish());
            }
            isInitialised = true;
        }
        // Set the bound for use in generating a random integer within the range [0-bound)
        // There will be 5 possible integers
        int bound = 5;
        // Spawn fish in lake by chance
        // To yield 0 or 1 or 2 when there are 5 possible integers = 60% chance
        Random random = new Random();
        if (random.nextInt(bound) < 3) {
            int fishCounter = 0;
            for (Item item : location.getItems()) {
                if (item instanceof Fish) {
                    fishCounter += 1;
                }
            }
            // Lake can hold up to 25 fish
            if (fishCounter <= 25) {
                location.addItem(new Fish());
            }
        }
    }
}
