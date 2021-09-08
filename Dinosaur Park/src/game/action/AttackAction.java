package game.action;

import edu.monash.fit2099.engine.*;
import game.dinosaur.Allosaur;
import game.dinosaur.Dinosaur;
import game.dinosaur.Stegosaur;
import game.item.Corpse;

import java.util.Random;

/**
 * Action to allow actors to be attacked by player or Allosaur.
 */
public class AttackAction extends Action {

	protected Actor target;

	/**
	 * Constructor.
	 * 
	 * @param target the actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
	}

	/**
	 * Do some damage to the target.
	 *
	 * @param actor the actor performing this action
	 * @param map the game map the actor is on
	 * @return a suitable description to display in the UI
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		// Every attack has a 25% chance to miss
		Random random = new Random();
		if (random.nextInt(4) == 0) {
			return actor + " misses the " + target;
		}
		int oriStegosaur = ((Dinosaur) target).getHp();
		Weapon weapon = actor.getWeapon();
		int damage = weapon.damage();
		target.hurt(damage);
		// If this attack is performed by Allosaur, it deserves some hit points
		int oriAllosaur = 0;
		if (actor instanceof Allosaur) {
			oriAllosaur = ((Allosaur) actor).getHp();
			actor.heal(damage);
			((Stegosaur) target).setAttackedCounter(20);
		}
		String result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage";
		// If the target is no longer conscious, it dies
		if (!target.isConscious()) {
			String[] tokens = target.toString().toLowerCase().split(" ");
			Item corpse = new Corpse(tokens[1] + " corpse");
			map.locationOf(target).addItem(corpse);
			map.removeActor(target);
			result += "\n- " + target + " is killed";
		} else {
			result += "\n- Hit points of " + target + " decreases from " + oriStegosaur + " to "
					+ ((Dinosaur) target).getHp();
		}
		if (actor instanceof Allosaur) {
			result += "\n- Hit points of " + actor + " increases from " + oriAllosaur + " to "
					+ ((Allosaur) actor).getHp();
		}
		return result;
	}

	/**
	 * Describe the action in a format suitable for displaying in the menu.
	 *
	 * @param actor the actor performing this action
	 * @return a string, e.g. "Player attacks the Adult Stegosaur"
	 */
	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks the " + target;
	}
}
