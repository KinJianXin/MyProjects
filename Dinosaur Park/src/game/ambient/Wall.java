package game.ambient;

import edu.monash.fit2099.engine.*;

/**
 * A class that represents the wall of a building.
 */
public class Wall extends Ground {

	/**
	 * Constructor.
	 */
	public Wall() {
		super('#');
	}

	/**
	 * Check whether this wall can be entered.
	 *
	 * @param actor the actor to check
	 * @return a boolean value
	 */
	@Override
	public boolean canActorEnter(Actor actor) {
		return false;
	}

	/**
	 * Check whether this wall can block thrown objects.
	 *
	 * @return a boolean value
	 */
	@Override
	public boolean blocksThrownObjects() {
		return true;
	}
}
