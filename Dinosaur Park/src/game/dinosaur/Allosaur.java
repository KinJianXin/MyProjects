package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.*;
import game.action.*;

import java.util.ArrayList;

/**
 * A carnivorous dinosaur.
 */
public class Allosaur extends Dinosaur {

    /**
     * Constructor.
     *
     * @param name the name of this Allosaur, either "Adult Allosaur" or "Baby Allosaur"
     * @param sex  the sex of this Allosaur, either 'M' for male or 'F' for female
     */
    public Allosaur(String name, char sex) {
        super(name, 'a', 100, sex, 50,
                90, 40, 50,
                20, 15, new ArrayList<String>() {{
                    add("stegosaur corpse");
                    add("brachiosaur corpse");
                    add("allosaur corpse");
                    add("pterodactyl corpse");
                    add("stegosaur egg");
                    add("brachiosaur egg");
                    add("pterodactyl egg");
                }});
        if (name.equals("Adult Allosaur")) {
            displayChar = 'A';
            // Adult Allosaur starts at food level 50/100
            hurt(50);
        } else if (name.equals("Baby Allosaur")) {
            // Baby Allosaur starts at food level 20/100
            hurt(80);
        }
        if (sex == 'F') {
            // Female Allosaur needs 20 turns to cool down completely after mating
            setMateCoolDownTurns(20);
        } else {
            // Male Allosaur needs 15 turns to cool down completely after mating
            setMateCoolDownTurns(15);
        }
    }

    /**
     * Get a collection of actions that the other actor can do to this Allosaur.
     *
     * @param otherActor the actor that might be performing attack
     * @param direction a string representing the direction of the other actor
     * @param map the current game map
     * @return a collection of actions
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = new Actions();
        // Allosaur can be fed by Player
        if (otherActor instanceof Player) {
            for (Item item : otherActor.getInventory()) {
                if (SUITABLE_FOOD.contains(item.toString())
                        || item.toString().equals("carnivore meal")) {
                    actions.add(new FeedActorAction(this, item));
                }
            }
        }
        // Well-fed Allosaur can breed
        if (hitPoints > 50
                && getMateCoolDownTimer() == 0
                && (otherActor instanceof Allosaur
                && ((Allosaur) otherActor).getSex() != getSex()
                && ((Allosaur) otherActor).hitPoints > 50
                && ((Allosaur) otherActor).getMateCoolDownTimer() == 0)) {
            actions.add(new BreedAction(this));
        }
        return actions;
    }

    /**
     * Get an intrinsic weapon of this Allosaur.
     *
     * @return an IntrinsicWeapon
     */
    @Override
    protected IntrinsicWeapon getIntrinsicWeapon() {
        IntrinsicWeapon intrinsicWeapon = null;
        if (name.equals("Adult Allosaur")) {
            intrinsicWeapon = new IntrinsicWeapon(20, "bites");
        } else if (name.equals("Baby Allosaur")) {
            intrinsicWeapon = new IntrinsicWeapon(10, "bites");
        }
        return intrinsicWeapon;
    }

    /**
     * Get a dino-specific action if there is any available to take.
     *
     * @param actions a collection of possible actions for this actor
     * @return an action
     */
    @Override
    public Action getDinoSpecificAction(Actions actions) {
        // If there is an adjacent landed Pterodactyl, eat it
        for (Action action : actions) {
            if (action instanceof EatActorAction) {
                return action;
            }
        }
        // If there is an adjacent living Stegosaur, attack it
        for (Action action : actions) {
            if (action instanceof AttackAction) {
                return action;
            }
        }
        return null;
    }
}