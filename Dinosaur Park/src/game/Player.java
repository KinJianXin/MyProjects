package game;

import edu.monash.fit2099.engine.*;
import game.action.EatItemsAction;
import game.action.EndGameAction;
import game.ambient.Lake;
import game.dinosaur.Dinosaur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Class that represents the player.
 */
public class Player extends Actor {

	private boolean challengeMode;
	private int ecoPoints;
	private int ecoTarget; // Targeted amount of eco points to earn for the game in challenge mode
	private int rainCounter; // The counter used to check if it should rain
	private int turnCounter; // The counter used to check if the game in challenge mode should end
	private int turnLimit; // Maximum number of turns set for the game in challenge mode
	private Menu menu = new Menu();

	/**
	 * Constructor.
	 *
	 * @param name the name of this player
	 * @param displayChar the character to represent this player in the display
	 * @param hitPoints the starting hit points of this player
	 * @param challengeMode a boolean value representing whether the game is in challenge mode
	 * @param turnLimit an integer representing the number of moves
	 * @param ecoTarget an integer representing the number of eco points
	 */
	public Player(String name, char displayChar, int hitPoints, boolean challengeMode, int turnLimit, int ecoTarget) {
		super(name, displayChar, hitPoints);
		this.challengeMode = challengeMode;
		this.turnLimit = turnLimit;
		this.ecoTarget = ecoTarget;
		ecoPoints = 0;
	}

	/**
	 * Check if the game is in challenge mode.
	 *
	 * @return a boolean value representing whether the game is in challenge mode
	 */
	public boolean isChallengeMode() {
		return challengeMode;
	}

	/**
	 * Get the eco points of this player.
	 *
	 * @return an integer representing the eco points of this player
	 */
	public int getEcoPoints() {
		return ecoPoints;
	}

	/**
	 * Set the eco points of this player with the specified value.
	 *
	 * @param ecoPoints an integer representing the eco points to set for this player
	 */
	public void setEcoPoints(int ecoPoints) {
		this.ecoPoints = ecoPoints;
	}

	/**
	 * Get the targeted amount of eco points to earn for the game in challenge mode.
	 *
	 * @return an integer representing the targeted amount of eco points to earn
	 */
	public int getEcoTarget() {
		return ecoTarget;
	}

	/**
	 * Get the total number of moves taken by this player thus far.
	 *
	 * @return an integer representing the total number of moves taken by this player thus far
	 */
	public int getTurnCounter() {
		return turnCounter;
	}

	/**
	 * Figure out what to do next.
	 *
	 * @param actions a collection of possible actions for this actor
	 * @param lastAction the action this actor took last turn
	 * @param map the map containing the actor
	 * @param display the I/O object to which messages may be written
	 * @return
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// Allow player to terminate the game
		actions.add(new EndGameAction());
		// Check if the game is in challenge mode
		turnCounter += 1;
		if (challengeMode) {
			if (turnCounter > turnLimit) {
				map.removeActor(this);
				display.println("You have reached the turn limit!\nYou earned "
						+ ecoPoints + " eco points\nYour target eco points: " + ecoTarget);
				if (ecoPoints >= ecoTarget) {
					display.println("You won!");
				} else {
					display.println("You lost!");
				}
				return null;
			}
		}
		// Check if it should rain every 10 turns
		rainCounter += 1;
		if (rainCounter % 10 == 0) {
			// Set the bound for use in generating a random integer within the range [0-bound)
			// There will be 5 possible integers
			int bound = 5;
			// Sky rains by chance
			// To yield 0 when there are 5 possible integers = 20% chance
			Random random = new Random();
			if (random.nextInt(bound) == 0) {
				display.println("It starts to rain!");
				// Randomise the amount of rain
				double amount = (random.nextInt(6) + 1) * 0.1 * 20;
				// Loop through entire map to find unconscious dinos and lakes to fill them up
				for (int x = 0; x < 80; x++) {
					for (int y = 0; y < 25; y++) {
						Actor actor = map.at(x, y).getActor();
						Ground ground = map.at(x, y).getGround();
						if (actor instanceof Dinosaur && !(actor.isConscious())) {
							Dinosaur dinosaur = (Dinosaur) actor;
							dinosaur.setWater(dinosaur.getWater() + 10);
						}
						if (ground instanceof Lake) {
							Lake lake = (Lake) ground;
							lake.setSip(lake.getSip() + (int) amount);
						}
					}
				}
			}
		}
		// Player cannot eat item
		List<Action> eatItemsActions = new ArrayList<Action>();
		for (Action action : actions) {
			if (action instanceof EatItemsAction) {
				eatItemsActions.add(action);
			}
		}
		for (Action action : eatItemsActions) {
			actions.remove(action);
		}
		// Handle multi-turn actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		// Display a menu to the user and have them select an option
		return menu.showMenu(this, actions, display);
	}
}
