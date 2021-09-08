package game.item;

import edu.monash.fit2099.engine.Item;
import game.action.EatItemsAction;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a meal kit.
 */
public class MealKit extends PortableItem {

    /**
     * Constructor.
     *
     * @param name the name of this meal kit
     */
    public MealKit(String name) {
        super(name, 'm');
        if (name.equals("vegetarian meal")) {
            setEatPoints(160);
            setFeedPoints(160);
        } else if (name.equals("carnivore meal")) {
            setEatPoints(100);
            setFeedPoints(100);
        }
        List<Item> items = new ArrayList<Item>();
        items.add(this);
        allowableActions.add(new EatItemsAction(items));
    }
}
