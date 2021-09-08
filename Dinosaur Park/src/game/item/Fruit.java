package game.item;

import edu.monash.fit2099.engine.*;
import game.action.DropFruitAction;
import game.action.EatItemsAction;
import game.action.PickFruitAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that represents a fruit.
 */
public class Fruit extends PortableItem {

    private int age = 0;

    /**
     * Constructor.
     *
     * @param name the name of this fruit
     * @param displayChar the character to represent this fruit in the display
     */
    public Fruit(String name, char displayChar) {
        super(name, displayChar);
        if (name.equals("bush fruit")) {
            setEatPoints(10);
            setFeedPoints(20);
        } else if (name.equals("tree fruit")) {
            setEatPoints(5);
            setFeedPoints(20);
        }
        List<Item> items = new ArrayList<Item>();
        items.add(this);
        allowableActions.add(new EatItemsAction(items));
    }

    /**
     * Fruit on the ground can experience the passage of time.
     *
     * @param currentLocation the location of the ground on which this item lies
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        if (name.equals("tree fruit")) {
            // Set the bound for use in generating a random integer within the range [0-bound)
            // There will be 20 possible integers
            int bound = 20;
            // Drop fruit from tree by chance
            // To yield 1 when there are 20 possible integers = 5% chance
            Random random = new Random();
            if (random.nextInt(bound) == 1) {
                setCondition("dropped ");
                setEatPoints(10);
            }
        }
        if (!(getCondition().equals(""))) {
            age++;
            // Dropped fruit rots away in 15 turns
            if (age == 15) {
                currentLocation.removeItem(this);
            }
        }
    }

    /**
     * Get an action to pick up this fruit.
     *
     * @return a PickFruitAction
     */
    @Override
    public PickUpItemAction getPickUpAction() {
        return new PickFruitAction(this);
    }

    /**
     * Get an action to drop this fruit.
     *
     * @return a DropFruitAction
     */
    @Override
    public DropItemAction getDropAction() {
        return new DropFruitAction(this);
    }
}
