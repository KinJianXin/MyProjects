package game.action;

import edu.monash.fit2099.engine.*;
import game.Player;
import game.dinosaur.Dinosaur;
import game.dinosaur.Pterodactyl;
import game.item.PortableItem;
import game.item.Fruit;

/**
 * Action to allow actors to be fed.
 */
public class FeedActorAction extends Action {

    private Actor subject;
    private Item item;

    /**
     * Constructor.
     *
     * @param subject the actor to feed
     * @param item the item to be fed for subject
     */
    public FeedActorAction(Actor subject, Item item) {
        this.subject = subject;
        this.item = item;
    }

    /**
     * Add points to the subject's hit points total.
     *
     * @param actor the actor performing this action
     * @param map the game map the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // If this is performed on Pterodactyl, make sure it lands before taking action
        if (subject instanceof Pterodactyl) {
            ((Pterodactyl) subject).setCondition("landed ");
        }
        String result = "\n- Hit points of " + subject + " increases from "
                + ((Dinosaur) subject).getHp();
        actor.removeItemFromInventory(item);
        subject.heal(((PortableItem) item).getFeedPoints());
        result += " to " + ((Dinosaur) subject).getHp();
        // Increment eco points of Player
        result += "\n- Your eco points increases from " + ((Player) actor).getEcoPoints();
        if (item instanceof Fruit || subject instanceof Pterodactyl) {
            ((Player) actor).setEcoPoints(((Player) actor).getEcoPoints() + 10);
        }
        result += " to " + ((Player) actor).getEcoPoints();
        return menuDescription(actor) + result;
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing this action
     * @return a string, e.g. "Player feeds the Adult Stegosaur with a bush fruit"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " feeds the " + subject + " with a " + item;
    }
}
