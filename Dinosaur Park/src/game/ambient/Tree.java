package game.ambient;

import edu.monash.fit2099.engine.*;
import game.item.Fruit;
import game.Player;

import java.util.Random;

/**
 * A class that represents a tree.
 */
public class Tree extends Ground {

	private int age;

	/**
	 * Constructor.
	 */
	public Tree() {
		super('+');
		age = 0;
	}

	/**
	 * Tree can experience the passage of time.
	 *
	 * @param location the location of this ground
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);
		age++;
		if (age == 10)
			displayChar = 't';
		if (age == 20)
			displayChar = 'T';
		// Set the bound for use in generating a random integer within the range [0-bound)
		// There will be 2 possible integers
		int bound = 2;
		// Produce fruit in tree by chance
		// To yield 1 when there are 2 possible integers = 50% chance
		Random random = new Random();
		if (random.nextInt(bound) == 1) {
			location.addItem(new Fruit("tree fruit", displayChar));
			// Increment eco points of Player
			// Due to engine limitation, this can only be done if Player is in the same map
			GameMap map = location.map();
			for (int x = 0; x < 80; x++) {
				for (int y = 0; y < 25; y++) {
					if (map.at(x, y).getActor() instanceof Player) {
						Player player = (Player) map.at(x, y).getActor();
						player.setEcoPoints(player.getEcoPoints() + 1);
					}
				}
			}
		}
	}
}
