package game;

import edu.monash.fit2099.engine.*;
import game.ambient.*;
import game.dinosaur.Allosaur;
import game.dinosaur.Brachiosaur;
import game.dinosaur.Pterodactyl;
import game.dinosaur.Stegosaur;
import game.item.Teleporter;
import game.item.VendingMachine;

import java.util.Arrays;
import java.util.List;

/**
 * The main class for the Jurassic World game.
 */
public class Application {

	public static void main(String[] args) {
		Display display = new Display();

		World world = new World(display);

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Bush(), new Dirt(),
				new Floor(), new Lake(), new Tree(), new Wall());

		List<String> map = Arrays.asList(
		"................................................................................",
		"................................................................................",
		".....#######...........................~........................................",
		".....#_____#....................................................................",
		".....#_____#....................................................................",
		".....###.###......................................................~.............",
		"................................................................................",
		"......................~...............+++.......................................",
		".......................................++++.....................................",
		"...................................+++++........................................",
		".....................................++++++.....................................",
		"......................................+++.......................................",
		".....................................+++........................................",
		"................................................................................",
		"............+++.................................................................",
		".............+++++..............................................................",
		"...............++........................................+++++..................",
		".............+++....................................++++++++....................",
		"............+++.......................................+++.......................",
		"................................................................................",
		".........................................................................++.....",
		"..............................~.........................................++.++...",
		".........................................................................++++...",
		"......................................................~...................++....",
		"................................................................................");
		GameMap gameMap = new GameMap(groundFactory, map);

		List<String> map2 = Arrays.asList(
		"................................................................................",
		"................................................................................",
		".......~~~~~~............................................++.....................",
		".......~~~~~~..........................................+++++....................",
		".......~~~~~~........................................++++++++++.................",
		"..................................................++++++++++....................",
		"....................................................++++++++++..................",
		"..................................+..............++++++++++.....................",
		"..................................++................+++++.......................",
		"++...............................++++................++........................+",
		"++++..............................+++++.......................................++",
		"++++..............................++++.......................................+++",
		"++++++............................++++....................................++++++",
		"++++...............................++....................................+++++++",
		"+++++...............................+.....................................++++++",
		"+++...................+....................................................+++++",
		"++...................++......................................................+++",
		"++...................+++......................................................++",
		"++..................+++++............................~~~~~~....................+",
		"+....................+++.............................~~~~~~.....................",
		".....................++..............................~~~~~~.....................",
		"......................+.........................................................",
		"................................................................................",
		"................................................................................",
		"................................................................................");
		GameMap gameMap2 = new GameMap(groundFactory, map2);

		world.addGameMap(gameMap);
		world.addGameMap(gameMap2);

		// Place dinosaurs in the middle of the first game map
//		gameMap.at(30, 12).addActor(new Stegosaur("Adult Stegosaur", 'M'));
//		gameMap.at(32, 12).addActor(new Stegosaur("Adult Stegosaur", 'F'));
//		gameMap.at(30, 15).addActor(new Brachiosaur("Adult Brachiosaur", 'M'));
//		gameMap.at(45, 15).addActor(new Brachiosaur("Adult Brachiosaur", 'M'));
//		gameMap.at(32, 15).addActor(new Brachiosaur("Adult Brachiosaur", 'F'));
//		gameMap.at(47, 15).addActor(new Brachiosaur("Adult Brachiosaur", 'F'));
//		gameMap.at(30, 18).addActor(new Allosaur("Adult Allosaur", 'M'));
//		gameMap.at(32, 18).addActor(new Allosaur("Adult Allosaur", 'F'));
		gameMap.at(45, 18).addActor(new Pterodactyl("Adult Pterodactyl", 'M'));
		gameMap.at(47, 18).addActor(new Pterodactyl("Adult Pterodactyl", 'F'));

		// Place a vending machine inside the building within the first game map
		gameMap.at(6, 3).addItem(new VendingMachine());

		// Add teleporters to enable Player travel between maps
		for (int i = 0; i < 80; i++) {
			Teleporter teleporter = new Teleporter();
			teleporter.addAction(new MoveActorAction(gameMap2.at(i, 24), "to next map"));
			gameMap.at(i, 0).addItem(teleporter);
		}
		for (int i = 0; i < 80; i++) {
			Teleporter teleporter = new Teleporter();
			teleporter.addAction(new MoveActorAction(gameMap.at(i, 0), "to next map"));
			gameMap2.at(i, 24).addItem(teleporter);
		}

		// Sophisticated game driver
		boolean running = true;
		while (running) {
			display.println("Welcome to Dino Park!\n1) SandBox Mode\n2) Challenge Mode\n3) Exit" +
					"\nPlease Select your game mode:");
			char input = display.readChar();
			if (input == '1') {
				// In sandbox mode, the game runs as normal
				Player player = new Player("Player", '@', 100,
						false, 0, 0);
				world.addPlayer(player, gameMap.at(8, 4));
				world.run();
			} else if (input == '2') {
				// In challenge mode, player can choose a number of moves and a number of eco points
				try {
					display.println("Enter a value between 1-9 to choose your target eco points," +
							"\nthe target eco points will be the value you entered x1000");
					input = display.readChar();
					int ecoPoints = Integer.parseInt(String.valueOf(input));
					display.println("Enter a value between 1-9 to choose your turn limit," +
							"\nthe turn limit will be the value you entered x10");
					input = display.readChar();
					int turnLimit = Integer.parseInt(String.valueOf(input));
					Player player = new Player("Player", '@', 100,
							true, turnLimit * 10, ecoPoints * 1000);
					world.addPlayer(player, gameMap.at(8, 4));
					world.run();
				} catch (Exception e) {
					if (e instanceof NullPointerException) {
						continue;
					} else {
						display.println("Invalid input, please try again");
					}
				}
			} else if (input == '3') {
				// End the current game
				running = false;
			} else {
				display.println("Invalid input, please try again");
			}
		}
	}
}
