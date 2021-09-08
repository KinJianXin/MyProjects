package game.item;

import edu.monash.fit2099.engine.*;
import game.action.EatItemsAction;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a corpse.
 */
public class Corpse extends PortableItem {

    private int age;
    private int maxGroundTurns; // Maximum number of turns allowed for being on ground before rotting away

    /**
     * Constructor.
     *
     * @param name the name of this corpse
     */
    public Corpse(String name) {
        super(name, '%');
        age = 0;
        switch (name) {
            case "stegosaur corpse":
            case "allosaur corpse":
                setEatPoints(50);
                setFeedPoints(50);
                maxGroundTurns = 20;
                break;
            case "brachiosaur corpse":
                // Brachiosaur corpse can fill up Allosaur's food level
                setEatPoints(100);
                setFeedPoints(100);
                maxGroundTurns = 40;
                break;
            case "pterodactyl corpse":
                setEatPoints(30);
                setFeedPoints(30);
                maxGroundTurns = 20;
                break;
        }
        List<Item> items = new ArrayList<Item>();
        items.add(this);
        allowableActions.add(new EatItemsAction(items));
    }

    /**
     * Corpse on the ground can experience the passage of time.
     *
     * @param currentLocation the location of the ground on which this item lies
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
        if (age == maxGroundTurns) {
            currentLocation.removeItem(this);
        }
    }
}
