package game.ambient;

import edu.monash.fit2099.engine.*;
import game.item.Fruit;
import game.dinosaur.Brachiosaur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that represents a bush.
 */
public class Bush extends Ground {

    /**
     * Constructor.
     */
    public Bush() {
        super(':');
    }

    /**
     * Bush can experience the passage of time.
     *
     * @param location the location of this ground
     */
    public void tick(Location location) {
        super.tick(location);
        if (location.getActor() instanceof Brachiosaur) {
            // Set the bound for use in generating a random integer within the range [0-bound)
            // There will be 2 possible integers
            int bound = 2;
            // Kill bush by chance when Brachiosaur steps on it
            // To yield 1 when there are 2 possible integers = 50% chance
            Random random = new Random();
            if (random.nextInt(bound) == 1) {
                location.setGround(new Dirt());
                // Destroy all fruits after bush is killed
                List<Item> fruits = new ArrayList<Item>();
                for (Item item : location.getItems()) {
                    if (item.toString().equals("bush fruit")) {
                        fruits.add(item);
                    }
                }
                for (Item fruit : fruits) {
                    location.removeItem(fruit);
                }
            }
        } else {
            // Set the bound for use in generating a random integer within the range [0-bound)
            // There will be 10 possible integers
            int bound = 10;
            // Produce fruit in bush by chance
            // To yield 1 when there are 10 possible integers = 10% chance
            Random random = new Random();
            if (random.nextInt(bound) == 1) {
                location.addItem(new Fruit("bush fruit", displayChar));
            }
        }
    }
}
