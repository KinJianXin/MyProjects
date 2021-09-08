package game.item;

import edu.monash.fit2099.engine.*;
import game.Player;
import game.action.EatItemsAction;
import game.dinosaur.Allosaur;
import game.dinosaur.Brachiosaur;
import game.dinosaur.Pterodactyl;
import game.dinosaur.Stegosaur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that represents an egg.
 */
public class Egg extends PortableItem {

    private int carryCounter; // Count the number of turns being carried
    private int hatchCounter; // Count the number of turns being on ground
    private int carryTurns; // The number of turns required to be mature enough for dropping
    private int hatchTurns; // The number of turns required to be mature enough for hatching
    private int hatchEcoPoints;

    /**
     * Constructor.
     *
     * @param name the name of this egg
     */
    public Egg(String name) {
        super(name, 'o');
        carryCounter = 0;
        hatchCounter = 0;
        switch (name) {
            case "stegosaur egg":
            case "pterodactyl egg":
                carryTurns = 10;
                hatchTurns = 20;
                hatchEcoPoints = 100;
                break;
            case "brachiosaur egg":
                carryTurns = 30;
                hatchTurns = 30;
                hatchEcoPoints = 1000;
                break;
            case "allosaur egg":
                carryTurns = 20;
                hatchTurns = 50;
                hatchEcoPoints = 100;
                break;
        }
        setEatPoints(10);
        setFeedPoints(10);
        List<Item> items = new ArrayList<Item>();
        items.add(this);
        allowableActions.add(new EatItemsAction(items));
    }

    /**
     * Carried egg can experience the passage of time.
     *
     * @param currentLocation the location of the actor carrying this Item
     * @param actor the actor carrying this item
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        super.tick(currentLocation, actor);
        // Egg does not age in player's inventory
        if (!(actor instanceof Player)) {
            carryCounter++;
        }
        // 3 possible situations for a carried egg
        // Situation 1: Bought from vending machine and carried by Player, carryCounter = 0 always
        // Situation 2: Picked up from ground and carried by Player, carryCounter > 10/30/20 always
        // Situation 3: Carried by a pregnant dinosaur, carryCounter >= 1 and <= 10/30/20 always
    }

    /**
     * Egg on the ground can experience the passage of time.
     *
     * @param currentLocation the location of the ground on which this item lies
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        hatchCounter++;
        // Randomise the sex of new born dinosaur
        int bound = 2;
        Random random = new Random();
        char[] sexes = {'M', 'F'};
        char sex = sexes[random.nextInt(bound)];
        // Used to increment eco points of player
        GameMap map = currentLocation.map();
        Player player = null;
        for (int x = 0; x < 80; x++) {
            for (int y = 0; y < 25; y++) {
                if (map.at(x, y).getActor() instanceof Player) {
                    player = (Player) map.at(x, y).getActor();
                    break;
                }
            }
        }
        // If the egg is mature enough for hatching
        if (hatchCounter >= hatchTurns) {
            currentLocation.removeItem(this);
            // If current square is occupied by an actor, find the nearest unoccupied location
            if (currentLocation.getActor() != null) {
                List<Location> potentialLocations = new ArrayList<Location>();
                for (int x = 0; x < 80; x++) {
                    for (int y = 0; y < 25; y++) {
                        if (map.at(x, y).getActor() == null) {
                            potentialLocations.add(map.at(x, y));
                        }
                    }
                }
                if (!(potentialLocations.isEmpty())) {
                    // Find the nearest location
                    Location nearestLocation = potentialLocations.get(0);
                    int minDistance = distance(nearestLocation, currentLocation);
                    for (Location location : potentialLocations) {
                        int distance = distance(location, currentLocation);
                        if (distance < minDistance) {
                            nearestLocation = location;
                            minDistance = distance;
                        }
                    }
                    currentLocation = nearestLocation;
                    switch (name) {
                        case "stegosaur egg":
                            currentLocation.addActor(new Stegosaur("Baby Stegosaur", sex));
                            break;
                        case "brachiosaur egg":
                            currentLocation.addActor(new Brachiosaur("Baby Brachiosaur", sex));
                            break;
                        case "allosaur egg":
                            currentLocation.addActor(new Allosaur("Baby Allosaur", sex));
                            break;
                        case "pterodactyl egg":
                            currentLocation.addActor(new Pterodactyl("Baby Pterodactyl", sex));
                            break;
                    }
                    // Increment eco points of Player
                    // Due to engine limitation, this can only be done if Player is in the same map
                    if (player != null) {
                        player.setEcoPoints(player.getEcoPoints() + hatchEcoPoints);
                    }
                }
            }
        }
    }

    /**
     * Get an action to drop or lay the egg.
     *
     * @return a DropItemAction
     */
    @Override
    public DropItemAction getDropAction() {
        if (portable) {
            // 3 possible situations to drop a carried egg
            // Situation 1: Player can drop the egg bought from vending machine when carryCounter = 10/20/30
            // Situation 2: Player can drop the egg picked up from ground when carryCounter >= 10/30/20
            // Situation 3: Female dinosaur can lay the egg when carryCounter >= 10/30/20
            // If the egg is mature enough for dropping
            if (carryCounter >= carryTurns) {
                return new DropItemAction(this);
            }
        }
        return null;
    }

    /**
     * Make this egg mature enough for dropping.
     */
    public void makeDroppable() {
        carryCounter = carryTurns;
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
