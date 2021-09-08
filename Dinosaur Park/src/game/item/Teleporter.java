package game.item;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Item;

/**
 * A class that represents a teleporter.
 */
public class Teleporter extends Item {

    /***
     * Constructor.
     */
    public Teleporter() {
        super("Teleporter", '.', false);
    }

    /**
     * Add an allowable action to this item.
     *
     * @param action an allowable action
     */
    public void addAction(Action action){
        this.allowableActions.add(action);
    }
}
