package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.ambient.Lake;
import game.dinosaur.Brachiosaur;
import game.dinosaur.Dinosaur;
import game.dinosaur.Pterodactyl;

/**
 * Action to allow dinosaurs to drink from a lake.
 */
public class DrinkAction extends Action {

    private Lake lake;

    /**
    * Constructor.
    *
    * @param lake the lake to drink from
     */
    public DrinkAction(Lake lake) {
        this.lake = lake;
    }

    /**
     * Add points to the actor's water level total.
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
        String result = "\n- Water level increases from " + ((Dinosaur) actor).getWater();
        lake.setSip(lake.getSip() - 1);
        if (actor instanceof Brachiosaur) {
            ((Brachiosaur) actor).setWater(((Brachiosaur) actor).getWater() + 80);
        } else {
            ((Dinosaur) actor).setWater(((Dinosaur) actor).getWater() + 30);
        }
        result += " to " + ((Dinosaur) actor).getWater();
        return menuDescription(actor) + result;
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing this action
     * @return a string, e.g. "Adult Stegosaur drinks from the lake"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " drinks from the lake";
    }
}
