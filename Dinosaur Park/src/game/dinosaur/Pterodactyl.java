package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.Player;
import game.action.*;
import game.ambient.Lake;
import game.ambient.Tree;
import game.item.Corpse;
import game.item.Egg;
import game.item.Fish;
import game.item.PortableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A carnivorous dinosaur.
 */
public class Pterodactyl extends Dinosaur {

    private int flyingFuel;
    private String condition; // The condition of this dinosaur, whether "flying " or "landed "

    /**
     * Constructor.
     *
     * @param name the name of this Pterodactyl, either "Adult Pterodactyl" or "Baby Pterodactyl"
     * @param sex  the sex of this Pterodactyl, whether 'M' for male or 'F' for female
     */
    public Pterodactyl(String name, char sex) {
        super(name, 'p', 100, sex, 30,
                90, 40, 50,
                20, 15, new ArrayList<String>(){{
                    add("stegosaur corpse");
                    add("brachiosaur corpse");
                    add("allosaur corpse");
                    add("pterodactyl corpse");
                    add("stegosaur egg");
                    add("brachiosaur egg");
                    add("allosaur egg");
                    add("fish");
                }});
        if (name.equals("Adult Pterodactyl")) {
            displayChar = 'P';
            // Adult Pterodactyl starts at food level 50/100
            hurt(50);
        } else if (name.equals("Baby Pterodactyl")) {
            // Baby Pterodactyl starts at food level 10/100
            hurt(90);
        }
        if (sex == 'F') {
            // Female Pterodactyl needs 10 turns to cool down completely after mating
            setMateCoolDownTurns(10);
        } else {
            // Male Pterodactyl needs 15 turns to cool down completely after mating
            setMateCoolDownTurns(15);
        }
        flyingFuel = 30;
        condition = "flying ";
    }

    /**
     * Set the flying fuel of this dinosaur with the specified value.
     *
     * @param flyingFuel an integer representing the flying fuel to set for this dinosaur
     */
    public void setFlyingFuel(int flyingFuel) {
        this.flyingFuel = Math.max(0, Math.min(flyingFuel, 30));
    }

    /**
     * Get the condition of this dinosaur.
     *
     * @return a string representing the condition of this dinosaur
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Set the condition of this dinosaur with the specified string.
     *
     * @param condition a string representing the condition to set for this dinosaur
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Get a collection of actions that the other actor can do to this Pterodactyl.
     *
     * @param otherActor the actor that might be performing attack
     * @param direction a string representing the direction of the other actor
     * @param map the current game map
     * @return a collection of actions
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions actions = new Actions();
        // Pterodactyl landed on non-tree ground can be eaten by Allosaur
        if (condition.equals("landed ")
                && !(map.locationOf(this).getGround() instanceof Tree)
                && otherActor instanceof Allosaur) {
            actions.add(new EatActorAction(this));
        }
        // Pterodactyl can be fed by Player
        if (otherActor instanceof Player && !(map.locationOf(this).getGround() instanceof Lake)) {
            for (Item item : otherActor.getInventory()) {
                if (item instanceof PortableItem
                        && (SUITABLE_FOOD.contains(((PortableItem) item).getCondition() + item.toString())
                        || item.toString().equals("carnivore meal"))) {
                    actions.add(new FeedActorAction(this, item));
                }
            }
        }
        // Well-fed Pterodactyl can breed
        if (isWellFed()
                && getMateCoolDownTimer() == 0
                && map.locationOf(this).getGround() instanceof Tree
                && otherActor instanceof Pterodactyl
                && ((Pterodactyl) otherActor).getSex() != getSex()
                && ((Pterodactyl) otherActor).isWellFed()
                && ((Pterodactyl) otherActor).getMateCoolDownTimer() == 0
                && map.locationOf(otherActor).getGround() instanceof Tree) {
            // Make sure the female is not thirsty
            // To make sure it will stay conscious on the tree throughout pregnancy
            if (getSex() == 'F' && !(isThirsty())) {
                actions.add(new BreedAction(this));
            } else if (((Pterodactyl) otherActor).getSex() == 'F'
                    && !(((Pterodactyl) otherActor).isThirsty())) {
                actions.add(new BreedAction(this));
            }
        }
        return actions;
    }

    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        // If Pterodactyl reaches current square by flying, decrement flyingFuel
        if (condition.equals("flying ") && lastAction instanceof MoveActorAction) {
            setFlyingFuel(flyingFuel - 1);
            if (flyingFuel == 0) {
                condition = "landed ";
                // If Pterodactyl runs out of flyingFuel upon reaching a lake, it drops and drowns
                if (map.locationOf(this).getGround() instanceof Lake) {
                    hurt(maxHitPoints);
                    setWater(0);
                }
            }
        }
        if (getHp() - 1 > 0 && getWater() - 1 > 0) {
            // If Pterodactyl reaches a tree consciously, recharge flyingFuel to the max
            if (flyingFuel < 10 && map.locationOf(this).getGround() instanceof Tree) {
                setFlyingFuel(30);
                condition = "flying ";
            }
        } else {
            condition = "landed ";
            // If Pterodactyl gets unconscious upon reaching a lake, it drops and drowns
            if (map.locationOf(this).getGround() instanceof Lake) {
                kill();
            }
        }
        return super.playTurn(actions, lastAction, map, display);
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
        // Pterodactyl flying over a lake can catch maximum 2 fish
        if (map.locationOf(this).getGround() instanceof Lake) {
            if (isHungry()) {
                int bound = 3; // Maximum 2 fish
                Random random = new Random();
                int maxCatch = random.nextInt(bound);
                List<Item> items = new ArrayList<Item>();
                int fishCaught = 0;
                for (Action action : actions) {
                    if (action instanceof EatItemsAction
                            && ((EatItemsAction) action).getItem() instanceof Fish) {
                        if (fishCaught < maxCatch) {
                            items.add(((EatItemsAction) action).getItem());
                            fishCaught += 1;
                        } else {
                            break;
                        }
                    }
                }
                if (!(items.isEmpty())) {
                    return new EatItemsAction(items);
                }
            }
        } else {
            // Pterodactyl can eat corpse or egg on non-lake ground
            if (isHungry()) {
                for (Action action : actions) {
                    if (action instanceof EatItemsAction) {
                        // Pterodactyl can stay and eat a corpse if there are no other dinos around
                        if (((EatItemsAction) action).getItem() instanceof Corpse) {
                            boolean hasDinosaur = false;
                            for (Exit exit : map.locationOf(this).getExits()) {
                                if (exit.getDestination().getActor() instanceof Dinosaur) {
                                    hasDinosaur = true;
                                    break;
                                }
                            }
                            if (!hasDinosaur) {
                                return action;
                            }
                        } else if (((EatItemsAction) action).getItem() instanceof Egg) {
                            return action;
                        }
                    }
                }
            }
        }
        return null;
    }
}