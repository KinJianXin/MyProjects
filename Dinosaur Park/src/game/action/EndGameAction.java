package game.action;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.Player;

/**
 * Action to allow the game to be terminated by the player.
 */
public class EndGameAction extends Action {

    /**
     * Remove the actor from the system.
     *
     * @param actor the actor performing the action
     * @param map the game map the actor is on
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        map.removeActor(actor);
        String result = "\nYou played for " + ((Player) actor).getTurnCounter()
                + " turns\nYou earned " + ((Player) actor).getEcoPoints() + " eco points";
        // If the game is in challenge mode, check and show the result
        if (((Player) actor).isChallengeMode()) {
            result += "\nYour target eco points: " + ((Player) actor).getEcoTarget();
            if (((Player) actor).getEcoPoints() >= ((Player) actor).getEcoTarget()) {
                result += "\nYou won!";
            } else {
                result += "\nYou lost!";
            }
        }
        return menuDescription(actor) + result;
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @param actor the actor performing the action
     * @return the string "Player ends the game"
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Player ends the game";
    }
}
