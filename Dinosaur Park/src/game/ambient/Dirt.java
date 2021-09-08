package game.ambient;

import edu.monash.fit2099.engine.*;

import java.util.Random;

/**
 * A class that represents a bare dirt.
 */
public class Dirt extends Ground {

	/**
	 * Constructor.
	 */
	public Dirt() {
		super('.');
	}

	/**
	 * Dirt can experience the passage of time.
	 *
	 * @param location the location of this ground
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);
		// Count the number of trees and bushes in the eight adjacent exits
		int treeCount = 0;
		int bushCount = 0;
		for (Exit exit : location.getExits()) {
			Location destination = exit.getDestination();
			Ground ground = destination.getGround();
			if (ground instanceof Tree) {
				treeCount += 1;
			} else if (ground instanceof Bush) {
				bushCount += 1;
			}
		}
		// Set the bound based on different situations
		// For use in generating a random integer within the range [0-bound)
		int bound;
		if (treeCount == 0) {
			if (bushCount >= 2) {
				bound = 10; // Situation 1: There will be 10 possible integers
			} else {
				bound = 100; // Situation 2: There will be 100 possible integers
			}
		} else {
			bound = 1; // Situation 3: 0 is the only one possible integer
		}
		// Convert dirt to bush by chance
		// Situation 1: To yield 1 when there are 10 possible integers = 10% chance
		// Situation 2: To yield 1 when there are 100 possible integers = 1% chance
		// Situation 3: To yield 1 when 0 is the only one possible integer = 0% chance
		Random random = new Random();
		if (random.nextInt(bound) == 1) {
			location.setGround(new Bush());
		}
	}
}
