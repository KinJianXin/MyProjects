package game.behaviour;

import edu.monash.fit2099.engine.*;

/**
 * A class that figures out a MoveActorAction that will move the actor one step
 * closer to a targeted location.
 */
public class SeekBehaviour implements Behaviour {

    private Location there;

    /**
     * Constructor.
     *
     * @param there the targeted location
     */
    public SeekBehaviour(Location there) {
        this.there = there;
    }

    /**
     * Get a MoveActorAction to move the actor one step closer to the targeted location if possible.
     *
     * @param actor the actor performing this behaviour
     * @param map the game map the actor is on
     * @return an action, or null if no MoveActorAction is possible
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        Location here = map.locationOf(actor);
        int currentDistance = distance(here, there);
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = distance(destination, there);
                if (newDistance < currentDistance) {
                    return new MoveActorAction(destination, exit.getName());
                }
            }
        }
        return null;
    }

    /**
     * Compute the Manhattan distance between two locations.
     *
     * @param a the first location
     * @param b the second location
     * @return the number of steps between a and b if you only move in the four cardinal directions
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}
