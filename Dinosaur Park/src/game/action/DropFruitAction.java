package game.action;

import edu.monash.fit2099.engine.*;
import game.item.PortableItem;

/**
 * Action to allow fruits to be dropped.
 */
public class DropFruitAction extends DropItemAction {

    /**
     * Constructor.
     *
     * @param fruit the fruit to drop
     */
    public DropFruitAction(Item fruit) {
        super(fruit);
    }

    /**
     * Drop the fruit.
     *
     * @param actor the actor performing this action
     * @param map the gameMap the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        ((PortableItem) item).setCondition("dropped ");
        ((PortableItem) item).setEatPoints(10);
        return super.execute(actor, map);
    }
}
