package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.*;
import game.action.*;
import game.item.PortableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A herbivorous dinosaur.
 */
public class Brachiosaur extends Dinosaur {

    /**
     * Constructor.
     *
     * @param name the name of this Brachiosaur, either "Adult Brachiosaur" or "Baby Brachiosaur"
     * @param sex  the sex of this Brachiosaur, either 'M' for male or 'F' for female
     */
    public Brachiosaur(String name, char sex) {
        super(name, 'b', 160, sex, 50,
                140, 40, 70,
                15, 15, new ArrayList<String>() {{
                    add("tree fruit");
                }});
        if (name.equals("Adult Brachiosaur")) {
            displayChar = 'B';
            // Adult Brachiosaur starts at food level 100/160
            hurt(60);
        } else if (name.equals("Baby Brachiosaur")) {
            // Baby Brachiosaur starts at food level 10/160
            hurt(150);
        }
        if (sex == 'F') {
            // Female Brachiosaur needs 30 turns to cool down completely after mating
            setMateCoolDownTurns(30);
        } else {
            // Male Brachiosaur needs 15 turns to cool down completely after mating
            setMateCoolDownTurns(15);
        }
    }

    /**
     * Get a collection of actions that the other actor can do to this Brachiosaur.
     *
     * @param otherActor the actor that might be performing attack
     * @param direction  a string representing the direction of the other actor
     * @param map        the current game map
     * @return a collection of actions
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = new Actions();
        // Brachiosaur can be fed by Player
        if (otherActor instanceof Player) {
            for (Item item : otherActor.getInventory()) {
                if (item instanceof PortableItem
                        && (SUITABLE_FOOD.contains(((PortableItem) item).getCondition() + item.toString())
                        || item.toString().equals("vegetarian meal"))) {
                    actions.add(new FeedActorAction(this, item));
                }
            }
        }
        // Well-fed Brachiosaur can breed
        if (hitPoints > 70
                && getMateCoolDownTimer() == 0
                && (otherActor instanceof Brachiosaur
                && ((Brachiosaur) otherActor).getSex() != getSex()
                && ((Brachiosaur) otherActor).hitPoints > 70
                && ((Brachiosaur) otherActor).getMateCoolDownTimer() == 0)) {
            actions.add(new BreedAction(this));
        }
        return actions;
    }

    /**
     * Get an EatItemsAction if there is any available to take at current location.
     *
     * @param actions a collection of possible actions for this actor
     * @param map the game map containing the actor
     * @return an action
     */
    @Override
    public Action getEatItemsAction(Actions actions, GameMap map) {
        // Brachiosaur can eat as many fruits as it finds in a tree
        int hpGap = maxHitPoints - hitPoints;
        List<Item> items = new ArrayList<Item>();
        int hpAdded = 0;
        for (Action action : actions) {
            if (action instanceof EatItemsAction) {
                Item item = ((EatItemsAction) action).getItem();
                if (SUITABLE_FOOD.contains(((PortableItem) item).getCondition() + item.toString())) {
                    if (hpAdded <= hpGap) {
                        items.add(item);
                        hpAdded += ((PortableItem) item).getEatPoints();
                    }
                }
            }
        }
        if (!(items.isEmpty())) {
            return new EatItemsAction(items);
        }
        return null;
    }
}