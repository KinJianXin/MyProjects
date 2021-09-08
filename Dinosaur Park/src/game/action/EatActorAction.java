package game.action;

import edu.monash.fit2099.engine.*;
import game.dinosaur.Dinosaur;
import game.dinosaur.Pterodactyl;

/**
 * Action to allow actors to be eaten.
 */
public class EatActorAction extends Action {

    private Actor target;

    /**
     * Constructor.
     *
     * @param target the actor to eat
     */
    public EatActorAction(Actor target) {
        this.target = target;
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
        String result = "\n- Hit points increases from " + ((Dinosaur) actor).getHp();
        map.removeActor(target);
        actor.heal(100);
        result += " to " + ((Dinosaur) actor).getHp();
        return menuDescription(actor) + result;
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing this action
     * @return a string, e.g. "Adult Allosaur eats the landed Adult Pterodactyl"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " eats the " + ((Pterodactyl) target).getCondition() + target;
    }

}
