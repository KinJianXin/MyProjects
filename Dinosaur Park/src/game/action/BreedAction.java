package game.action;

import edu.monash.fit2099.engine.*;
import game.dinosaur.Dinosaur;
import game.dinosaur.Pterodactyl;
import game.item.Egg;

/**
 * Action to allow actors to breed.
 */
public class BreedAction extends Action {

    private Actor target;

    /**
     * Constructor.
     *
     * @param target the actor to mate with
     */
    public BreedAction(Actor target) {
        this.target = target;
    }

    /**
     * Impregnate the female dinosaur.
     *
     * @param actor the actor performing this action
     * @param map the game map the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // If this is performed by Pterodactyls, make sure they land before taking action
        if (actor instanceof Pterodactyl) {
            ((Pterodactyl) actor).setCondition("landed ");
            ((Pterodactyl) target).setCondition("landed ");
        }
        String[] tokens = actor.toString().toLowerCase().split(" ");
        if (((Dinosaur) actor).getSex() == 'F') {
            actor.addItemToInventory(new Egg(tokens[1] + " egg"));
        } else {
            target.addItemToInventory(new Egg(tokens[1] + " egg"));
        }
        // Mated dinosaurs need to cool down completely before mating again
        ((Dinosaur) actor).setMateCoolDownTimer(((Dinosaur) actor).getMateCoolDownTurns());
        ((Dinosaur) target).setMateCoolDownTimer(((Dinosaur) target).getMateCoolDownTurns());
        return menuDescription(actor);
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing this action
     * @return a string, e.g. "Adult Stegosaur mates with another Adult Stegosaur"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " mates with another " + target;
    }
}
