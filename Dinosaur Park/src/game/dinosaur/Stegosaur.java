package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.*;
import game.action.*;
import game.item.PortableItem;

import java.util.ArrayList;

/**
 * A herbivorous dinosaur.
 */
public class Stegosaur extends Dinosaur {

	private int attackedCounter; // Count the number of turns gone after last being attacked

	/**
	 * Constructor.
	 *
	 * @param name the name of this Stegosaur, either "Adult Stegosaur" or "Baby Stegosaur"
	 * @param sex  the sex of this Stegosaur, whether 'M' for male or 'F' for female
	 */
	public Stegosaur(String name, char sex) {
		super(name, 's', 100, sex, 30,
				90, 40, 50,
				20, 15, new ArrayList<String>(){{
					add("bush fruit");
					add("dropped bush fruit");
					add("dropped tree fruit");
				}});
		if (name.equals("Adult Stegosaur")) {
			displayChar = 'S';
			// Adult Stegosaur starts at food level 50/100
			hurt(50);
		} else if (name.equals("Baby Stegosaur")) {
			// Baby Stegosaur starts at food level 10/100
			hurt(90);
		}
		if (sex == 'F') {
			// Female Stegosaur needs 10 turns to cool down completely after mating
			setMateCoolDownTurns(10);
		} else {
			// Male Stegosaur needs 15 turns to cool down completely after mating
			setMateCoolDownTurns(15);
		}
		attackedCounter = 0;
	}

	/**
	 * Set the attackedCounter of this dinosaur with the specified value.
	 *
	 * @param attackedCounter an integer representing the attackedCounter to set for this dinosaur
	 */
	public void setAttackedCounter(int attackedCounter) {
		this.attackedCounter = Math.max(0, attackedCounter);
	}

	/**
	 * Get a collection of actions that the other actor can do to this Stegosaur.
	 *
	 * @param otherActor the actor that might be performing attack
	 * @param direction a string representing the direction of the other actor
	 * @param map the current game map
	 * @return a collection of actions
	 */
	@Override
	public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
		Actions actions = new Actions();
		// Stegosaur can be attacked by armed Player or Allosaur
		if ((otherActor instanceof Player && !(otherActor.getWeapon() instanceof IntrinsicWeapon))
				|| (otherActor instanceof Allosaur && attackedCounter == 0)) {
			actions.add(new AttackAction(this));
		}
		// Stegosaur can be fed by Player
		if (otherActor instanceof Player) {
			for (Item item : otherActor.getInventory()) {
				if (item instanceof PortableItem
						&& (SUITABLE_FOOD.contains(((PortableItem) item).getCondition() + item.toString())
						|| item.toString().equals("vegetarian meal"))) {
					actions.add(new FeedActorAction(this, item));
				}
			}
		}
		// Well-fed Stegosaur can breed
		if (hitPoints > 50
				&& getMateCoolDownTimer() == 0
				&& (otherActor instanceof Stegosaur
				&& ((Stegosaur) otherActor).getSex() != getSex()
				&& ((Stegosaur) otherActor).hitPoints > 50
				&& ((Stegosaur) otherActor).getMateCoolDownTimer() == 0)) {
			actions.add(new BreedAction(this));
		}
		return actions;
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
		// Decrement attackedCounter
		if (attackedCounter > 0) {
			setAttackedCounter(attackedCounter - 1);
		}
		return super.playTurn(actions, lastAction, map, display);
	}
}