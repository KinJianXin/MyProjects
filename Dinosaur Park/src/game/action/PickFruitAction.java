package game.action;

import edu.monash.fit2099.engine.*;
import game.Player;

import java.util.Random;

/**
 * Action to allow fruits to be picked up.
 */
public class PickFruitAction extends PickUpItemAction {

    /**
     * Constructor.
     *
     * @param fruit the fruit to pick up
     */
    public PickFruitAction(Item fruit) {
        super(fruit);
    }

    /**
     * Pick up the fruit by chance.
     *
     * @param actor the actor performing this action
     * @param map the game map the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Set the bound for use in generating a random integer within the range [0-bound)
        // There will be 5 possible integers
        int bound = 5;
        // Pick up fruit by chance
        // To yield 1 or 2 when there are 5 possible integers = 40% chance
        Random random = new Random();
        int number = random.nextInt(bound);
        if (number == 1 || number == 2) {
            String result = "\n- Your eco points increases from " + ((Player) actor).getEcoPoints();
            // Increment eco points of Player
            ((Player) actor).setEcoPoints(((Player) actor).getEcoPoints() + 10);
            result += " to " + ((Player) actor).getEcoPoints();
            return super.execute(actor, map) + result;
        }
        return "You search the tree or bush for fruit, but you can't find any ripe ones";
    }
}
