package game.item;

import edu.monash.fit2099.engine.Item;
import game.action.EatItemsAction;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a fish.
 */
public class Fish extends PortableItem {

    /***
     * Constructor.
     */
    public Fish() {
        super("fish", '~');
        setEatPoints(5);
        List<Item> items = new ArrayList<Item>();
        items.add(this);
        allowableActions.add(new EatItemsAction(items));
    }
}
