package game.action;

import edu.monash.fit2099.engine.*;
import game.Player;
import game.item.Egg;

import java.util.Map;

/**
 * Action to allow the vending machine to be used.
 */
public class VendingAction extends Action {

    private static final char EXIT = 'e';
    private Map<Character, Item> keyToItemMap;
    private Map<Character, Integer> keyToPriceMap;

    /**
     * Constructor.
     *
     * @param keyToItemMap a map storing key-to-item pairs
     * @param keyToPriceMap a map storing key-to-price pairs
     */
    public VendingAction(Map<Character, Item> keyToItemMap, Map<Character, Integer> keyToPriceMap) {
        this.keyToItemMap = keyToItemMap;
        this.keyToPriceMap = keyToPriceMap;
    }

    /**
     * Deduct the eco points of the player and add the item to the player's inventory.
     *
     * @param actor the actor performing this action
     * @param map the game map the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor instanceof Player) {
            showItemMenu();
            Display display = new Display();
            display.println("You have " + ((Player) actor).getEcoPoints()
                    + " eco points, please select an item:");
            char c = display.readChar();
            if (c != EXIT) {
                int ecoPoints = ((Player) actor).getEcoPoints();
                int price = keyToPriceMap.get(c);
                if (ecoPoints >= price) {
                    int balance = ecoPoints - price;
                    ((Player) actor).setEcoPoints(balance);
                    Item item = keyToItemMap.get(c);
                    // Make the egg mature enough for dropping
                    if (item instanceof Egg) {
                        ((Egg) item).makeDroppable();
                    }
                    actor.addItemToInventory(item);
                    return menuDescription(actor) + " and buys the " + item;
                }
            }
            return menuDescription(actor) + " but buys nothing";
        }
        return null;
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing this action
     * @return a string, e.g. "Player uses the vending machine"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " uses the vending machine";
    }

    /**
     * Display a menu to the player and have them select an item.
     */
    private void showItemMenu() {
        Display display = new Display();
        display.println("Welcome to Dino Park's Vending Machine!");
        char[] keys = new char[]{'f', 't', 'v', 'c', 's', 'b', 'a', 'p', 'l'};
        for (char key : keys) {
            display.println(key + ": " + keyToItemMap.get(key) + " (" + keyToPriceMap.get(key)
                    + " points)");
        }
        display.println("e: Exit");
    }
}
