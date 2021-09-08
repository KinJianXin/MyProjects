package game.item;

import edu.monash.fit2099.engine.*;
import game.action.VendingAction;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents a vending machine.
 */
public class VendingMachine extends Item {

    private Map<Character, Item> keyToItemMap;
    private Map<Character, Integer> keyToPriceMap;

    /***
     * Constructor.
     */
    public VendingMachine() {
        super("vending machine", 'V', false);
        keyToItemMap = new HashMap<>(){{
            put('f', new Fruit("bush fruit", '*'));
            put('t', new Fruit("tree fruit", '*'));
            put('v', new MealKit("vegetarian meal"));
            put('c', new MealKit("carnivore meal"));
            put('s', new Egg("stegosaur egg"));
            put('b', new Egg("brachiosaur egg"));
            put('a', new Egg("allosaur egg"));
            put('p', new Egg("pterodactyl egg"));
            put('l', new LaserGun());
        }};
        keyToPriceMap = new HashMap<>(){{
            put('f', 30);
            put('t', 30);
            put('v', 100);
            put('c', 500);
            put('s', 200);
            put('b', 500);
            put('a', 1000);
            put('p', 200);
            put('l', 500);
        }};
        allowableActions.add(new VendingAction(keyToItemMap, keyToPriceMap));
    }
}
