package game.action;

import edu.monash.fit2099.engine.*;
import game.ambient.Lake;
import game.dinosaur.Dinosaur;
import game.dinosaur.Pterodactyl;
import game.item.Corpse;
import game.item.PortableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Action to allow many items to be eaten in a single turn.
 */
public class EatItemsAction extends Action {

    private List<Item> items;
    private Item item;

    /**
     * Constructor.
     *
     * @param items a list of items to eat
     */
    public EatItemsAction(List<Item> items) {
        this.items = new ArrayList<Item>();
        this.items = items;
        this.item = items.get(0);
    }

    /**
     * Get the item to eat.
     *
     * @return the item to eat
     */
    public Item getItem() {
        return item;
    }

    /**
     * Add points to the actor's hit points total.
     *
     * @param actor the actor performing this action
     * @param map the game map the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // If this is performed by Pterodactyl flying over non-lake ground,
        // make sure it lands before taking action
        if (actor instanceof Pterodactyl && !(map.locationOf(actor).getGround() instanceof Lake)) {
            ((Pterodactyl) actor).setCondition("landed ");
        }
        String result = "\n- Hit points increases from " + ((Dinosaur) actor).getHp();
        for (Item item : items) {
            // Pterodactyl can only eat a corpse slowly
            if (actor instanceof Pterodactyl && item instanceof Corpse) {
                int originalEatPoints = ((Corpse) item).getEatPoints();
                ((Corpse) item).setEatPoints(originalEatPoints - 10);
                int newEatPoints = ((Corpse) item).getEatPoints();
                if (newEatPoints == 0) {
                    map.locationOf(actor).removeItem(item);
                }
                actor.heal(10);
            } else {
                map.locationOf(actor).removeItem(item);
                actor.heal(((PortableItem) item).getEatPoints());
            }
        }
        result += " to " + ((Dinosaur) actor).getHp();
        return menuDescription(actor) + result;
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing this action
     * @return a string, e.g. "Adult Brachiosaur eats several tree fruits"
     */
    @Override
    public String menuDescription(Actor actor) {
        if (items.size() == 1) {
            return actor + " eats the " + ((PortableItem)item).getCondition() + item;
        } else {
            return actor + " eats several " + ((PortableItem)item).getCondition() + item;
        }
    }

}
