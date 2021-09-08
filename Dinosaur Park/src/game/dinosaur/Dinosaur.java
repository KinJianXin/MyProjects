package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.action.BreedAction;
import game.action.DrinkAction;
import game.action.EatItemsAction;
import game.ambient.Lake;
import game.ambient.Tree;
import game.behaviour.Behaviour;
import game.behaviour.SeekBehaviour;
import game.behaviour.WanderBehaviour;
import game.item.Corpse;
import game.item.Egg;
import game.item.Fish;
import game.item.PortableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that represents a dinosaur.
 */
public abstract class Dinosaur extends Actor {

    private final int ADULT_AGE;
    private final int HUNGRY_POINT;
    private final int THIRSTY_POINT;
    private final int WELL_FED_POINT;
    private final int MAX_HUNGRY_TURNS; // Maximum number of turns allowed for being unconscious due to zero hit points before dying
    private final int MAX_THIRSTY_TURNS; // Maximum number of turns allowed for being unconscious due to zero water level before dying
    protected final List<String> SUITABLE_FOOD;

    private char sex;
    private int age;
    private int water;
    private int mateCoolDownTimer; // Countdown to being cooled down completely for mating again
    private int mateCoolDownTurns; // The number of turns required to cool down completely after mating
    private int zeroHpCounter; // Count the number of turns being unconscious due to zero hit points
    private int zeroWaterCounter; // Count the number of turns being unconscious due to zero water level

    /**
     * Constructor.
     *
     * @param name the name of this dinosaur
     * @param displayChar the character to represent this dinosaur in the display
     * @param hitPoints the starting hit points of this dinosaur
     * @param sex the sex of this dinosaur
     */
    public Dinosaur(String name, char displayChar, int hitPoints, char sex,
                    int adultAge, int hungryPoint, int thirstyPoint, int wellFedPoint,
                    int maxHungryTurns, int maxThirstyTurns, List<String> suitableFood) {
        super(name, displayChar, hitPoints);
        ADULT_AGE = adultAge;
        HUNGRY_POINT = hungryPoint;
        THIRSTY_POINT = thirstyPoint;
        WELL_FED_POINT = wellFedPoint;
        MAX_HUNGRY_TURNS = maxHungryTurns;
        MAX_THIRSTY_TURNS = maxThirstyTurns;
        SUITABLE_FOOD = suitableFood;
        this.sex = sex;
        age = 0;
        water = 60;
        mateCoolDownTimer = 1;
        zeroHpCounter = 0;
        zeroWaterCounter = 0;
    }

    /**
     * Get the hit points of this dinosaur.
     *
     * @return an integer representing the hit points of this dinosaur
     */
    public int getHp() {
        return hitPoints;
    }

    /**
     * Get the sex of this dinosaur.
     *
     * @return a character representing the sex of this dinosaur
     */
    public char getSex() {
        return sex;
    }

    /**
     * Get the age of this dinosaur.
     *
     * @return an integer representing the age of this dinosaur
     */
    public int getAge() {
        return age;
    }

    /**
     * Set the age of this dinosaur with the specified value.
     *
     * @param age an integer representing the age to set for this dinosaur
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Get the water level of this dinosaur.
     *
     * @return an integer representing the water level of this dinosaur
     */
    public int getWater() {
        return water;
    }

    /**
     * Set the water level of this dinosaur with the specified value.
     *
     * @param water an integer representing the water level to set for this dinosaur
     */
    public void setWater(int water) {
        if (this instanceof Brachiosaur) {
            // Water level range for Brachiosaur = [0-200]
            this.water = Math.max(0, Math.min(water, 200));
        } else {
            // Water level range for other dinosaurs = [0-100]
            this.water = Math.max(0, Math.min(water, 100));
        }
    }

    /**
     * Get the mateCoolDownTimer of this dinosaur.
     *
     * @return an integer representing the mateCoolDownTimer of this dinosaur
     */
    public int getMateCoolDownTimer() {
        return mateCoolDownTimer;
    }

    /**
     * Set the mateCoolDownTimer of this dinosaur with the specified value.
     *
     * @param mateCoolDownTimer an integer representing the mateCoolDownTimer to set for this dinosaur
     */
    public void setMateCoolDownTimer(int mateCoolDownTimer) {
        this.mateCoolDownTimer = Math.max(0, mateCoolDownTimer);
    }

    /**
     * Get the mateCoolDownTurns of this dinosaur.
     *
     * @return an integer representing the mateCoolDownTurns of this dinosaur
     */
    public int getMateCoolDownTurns() {
        return mateCoolDownTurns;
    }

    /**
     * Set the mateCoolDownTurns of this dinosaur with the specified value.
     *
     * @param mateCoolDownTurns an integer representing the mateCoolDownTurns to set for this dinosaur
     */
    public void setMateCoolDownTurns(int mateCoolDownTurns) {
        this.mateCoolDownTurns = mateCoolDownTurns;
    }

    /**
     * Do some damage to this dinosaur while handling underflow i.e. hit points go below zero.
     *
     * @param points an integer representing the number of hit points to deduct
     */
    @Override
    public void hurt(int points) {
        hitPoints = Math.max(0, hitPoints - points);
    }

    /**
     * Kill this dinosaur.
     */
    public void kill() {
        zeroHpCounter = MAX_HUNGRY_TURNS;
        zeroHpCounter = MAX_THIRSTY_TURNS;
    }

    /**
     * Check if this dinosaur is still conscious.
     *
     * @return true only if this dinosaur has positive hit points and positive water level
     */
    @Override
    public boolean isConscious() {
        return hitPoints > 0 && water > 0;
    }

    /**
     * Check if this dinosaur is hungry.
     *
     * @return true if the hit points of this dinosaur is below a certain point
     */
    public boolean isHungry() {
        return hitPoints < HUNGRY_POINT;
    }

    /**
     * Check if this dinosaur is thirsty.
     *
     * @return true if the water level of this dinosaur is below a certain point
     */
    public boolean isThirsty() {
        return water < THIRSTY_POINT;
    }

    /**
     * Check if this dinosaur is well-fed.
     *
     * @return true if the hit points of this dinosaur is above a certain point
     */
    public boolean isWellFed() {
        return hitPoints > WELL_FED_POINT;
    }

    /**
     * Figure out what to do next.
     *
     * @param actions a collection of possible actions for this actor
     * @param lastAction the action this actor took last turn
     * @param map the game map containing the actor
     * @param display the I/O object to which messages may be written
     * @return the action to be performed
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        // Decrement 1 hp and 1 water every turn
        hurt(1);
        setWater(water - 1);
        // Increment age of baby dino: If it has experienced enough turns, grow into an adult
        if (name.contains("Baby")) {
            age += 1;
            if (age >= ADULT_AGE) {
                name.replace("Baby", "Adult");
                displayChar = Character.toUpperCase(displayChar);
            }
        }
        // Decrement mateCoolDownTimer of adult dino
        if (name.contains("Adult") && mateCoolDownTimer > 0) {
            setMateCoolDownTimer(mateCoolDownTimer - 1);
        }
        if (isConscious()) {
            // If dino is conscious, reset unconscious counters
            zeroHpCounter = 0;
            zeroWaterCounter = 0;
            // Display suitable message when dino gets hungry and/or thirsty
            int x = map.locationOf(this).x();
            int y = map.locationOf(this).y();
            if (isHungry()) {
                display.println(name + " at (" + x + ", " + y + ") is getting hungry!");
            }
            if (isThirsty()) {
                display.println(name + " at (" + x + ", " + y + ") is getting thirsty!");
            }
            //
            if (this instanceof Pterodactyl && sex == 'F') {
                for (Item item : inventory) {
                    if (item instanceof Egg && item.getDropAction() == null) {
                        return new DoNothingAction();
                    }
                }
            }
            // If dino is pregnant for enough turns, lay the egg
            for (Action action : actions) {
                if (action instanceof DropItemAction) {
                    return action;
                }
            }
            // If dino is thirsty, find lake to drink from
            if (isThirsty()) {
                // If this dino is a Pterodactyl flying over lake
                if (map.locationOf(this).getGround() instanceof Lake
                        && ((Lake) map.locationOf(this).getGround()).getSip() > 0) {
                    return new DrinkAction((Lake) map.locationOf(this).getGround());
                }
                Action action = getNearestMove(map, 0);
                if (action != null) {
                    return action;
                } else {
                    for (Exit exit : map.locationOf(this).getExits()) {
                        if (exit.getDestination().getGround() instanceof Lake
                                && ((Lake) exit.getDestination().getGround()).getSip() > 0) {
                            return new DrinkAction((Lake) exit.getDestination().getGround());
                        }
                    }
                }
            }
            // If there is a chance to breed, take it
            for (Action action : actions) {
                if (action instanceof BreedAction) {
                    return action;
                }
            }
            // If dino is hungry, find food at current location
            if (isHungry()) {
                Action action = getEatItemsAction(actions, map);
                if (action != null) {
                    return action;
                }
            }
            // Check if there is any dino-specific action to take
            if (getDinoSpecificAction(actions) != null) {
                return getDinoSpecificAction(actions);
            }
            // Otherwise
            // Situation 1: If dino is full and cooled down, go to the nearest potential mate
            // Situation 2: If dino is not well-fed or not cooled down, go to the nearest suitable food
            // Situation 3: Otherwise, go to the nearest potential mate or suitable food
            Action action;
            if (!isHungry() && mateCoolDownTimer == 0) {
                action = getNearestMove(map, 1); // Situation 1
            } else if (!isWellFed() || mateCoolDownTimer > 0) {
                action = getNearestMove(map, 2, SUITABLE_FOOD); // Situation 2
            } else {
                action = getNearestMove(map, 3, SUITABLE_FOOD); // Situation 3
            }
            if (action != null) {
                return action;
            } else {
                Behaviour behaviour = new WanderBehaviour();
                action = behaviour.getAction(this, map);
                if (action != null) {
                    return action;
                }
            }
        } else {
            // If dino is unconscious, increment relevant counter
            if (hitPoints == 0) {
                zeroHpCounter += 1;
            }
            if (water == 0) {
                zeroWaterCounter += 1;
            }
            // If either one counter is fulfilled, dino dies
            if (zeroHpCounter >= MAX_HUNGRY_TURNS || zeroWaterCounter >= MAX_THIRSTY_TURNS) {
                String[] tokens = name.toLowerCase().split(" ");
                Item corpse = new Corpse(tokens[1] + " corpse");
                map.locationOf(this).addItem(corpse);
                map.removeActor(this);
            }
        }
        return new DoNothingAction();
    }

    /**
     * Get an EatItemsAction if there is any available to take at current location.
     *
     * @param actions a collection of possible actions for this actor
     * @param map the game map containing the actor
     * @return an action
     */
    public Action getEatItemsAction(Actions actions, GameMap map) {
        for (Action action : actions) {
            if (action instanceof EatItemsAction) {
                Item item = ((EatItemsAction) action).getItem();
                if (SUITABLE_FOOD.contains(((PortableItem) item).getCondition() + item.toString())) {
                    return action;
                }
            }
        }
        return null;
    }

    /**
     * Get a dino-specific action if there is any available to take.
     *
     * @param actions a collection of possible actions for this actor
     * @return an action
     */
    public Action getDinoSpecificAction(Actions actions) {
        return null;
    }

    /**
     * Get a MoveActorAction that will move this dinosaur one step closer to the nearest target.
     *
     * @param map a collection of possible actions for this actor
     * @param situation an integer representing the situation that this dinosaur is in
     * @return an action
     */
    public Action getNearestMove(GameMap map, int situation) {
        return getNearestMove(map, situation, new ArrayList<String>());
    }

    /**
     * Get a MoveActorAction that will move this dinosaur one step closer to the nearest target.
     *
     * @param map the game map containing this dinosaur
     * @param situation an integer representing the situation that this dinosaur is in
     * @param suitableFood a list of food suitable for this dinosaur
     * @return an action
     */
    public Action getNearestMove(GameMap map, int situation, List<String> suitableFood) {
        int currentX = map.locationOf(this).x();
        int currentY = map.locationOf(this).y();
        List<Location> potentialLocations = new ArrayList<Location>();
        // Loop through entire map to find all lakes, trees, food or dinosaurs based on situation
        for (int x = 0; x < 80; x++) {
            for (int y = 0; y < 25; y++) {
                if (x == currentX && y == currentY) {
                    continue;
                }
                // Find lake
                if (situation == 0) {
                    if (map.at(x, y).getGround() instanceof Lake
                            && ((Lake) map.at(x, y).getGround()).getSip() > 0) {
                        potentialLocations.add(map.at(x, y));
                    }
                    continue;
                }
                // Find tree
                if (this instanceof Pterodactyl
                        && ((Pterodactyl) this).getCondition().equals("landed ")
                        && map.at(x, y).getGround() instanceof Tree) {
                    potentialLocations.add(map.at(x, y));
                }
                // Find suitable food
                if (situation == 2 || situation == 3) {
                    for (Item item : map.at(x, y).getItems()) {
                        if (item instanceof PortableItem
                                && suitableFood.contains(((PortableItem) item).getCondition() + item.toString())) {
                            // Landed Pterodactyl cannot reach fish in the lake
                            if (item instanceof Fish
                                    && ((Pterodactyl) this).getCondition().equals("landed ")) {
                                continue;
                            }
                            potentialLocations.add(map.at(x, y));
                        }
                    }
                } else if (situation == 1 || situation == 3 || this instanceof Allosaur) {
                    Actor actor = map.at(x, y).getActor();
                    // Find living Stegosaur
                    if (actor != null
                            && this instanceof Allosaur
                            && situation != 1
                            && actor instanceof Stegosaur) {
                        potentialLocations.add(map.at(x, y));
                    }
                    // Find potential mate
                    if (actor != null
                            && situation != 2
                            && actor.toString().equals(name)
                            && ((Dinosaur) actor).getSex() != getSex()) {
                        // Pterodactyls have to stand on adjacent trees for breeding
                        if (this instanceof Pterodactyl
                                && map.locationOf(actor).getGround() instanceof Tree) {
                            for (Exit exit : map.locationOf(actor).getExits()) {
                                if (exit.getDestination().getGround() instanceof Tree) {
                                    potentialLocations.add(map.at(x, y));
                                }
                            }
                            continue;
                        }
                        potentialLocations.add(map.at(x, y));
                    }
                }
            }
        }
        if (!(potentialLocations.isEmpty())) {
            // Find the nearest location
            Location nearestLocation = potentialLocations.get(0);
            int minDistance = distance(nearestLocation, map.locationOf(this));
            for (Location location : potentialLocations) {
                int distance = distance(location, map.locationOf(this));
                if (distance < minDistance) {
                    nearestLocation = location;
                    minDistance = distance;
                }
            }
            // Get a respective MoveActorAction
            Behaviour behaviour = new SeekBehaviour (nearestLocation);
            if (behaviour != null) {
                return behaviour.getAction(this, map);
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