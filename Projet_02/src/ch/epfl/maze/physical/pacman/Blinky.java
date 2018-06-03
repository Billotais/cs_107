package ch.epfl.maze.physical.pacman;




import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.physical.Daedalus;
import ch.epfl.maze.physical.Predator;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Red ghost from the Pac-Man game, chases directly its target.
 * 
 */

public class Blinky extends Predator {

	
	
	
	/**
	 * Constructs a Blinky with a starting position.
	 * 
	 * @param position
	 *            Starting position of Blinky in the labyrinth
	 */

	public Blinky(Vector2D position) { // Constructeur 
		super(position);
		home = position;
		
		
		
		
	}
	public Blinky(Blinky otherBlinky)
	{
		super(otherBlinky.home);
		home = otherBlinky.home;
		
	}

	@Override
	public Direction move(Direction[] choices, Daedalus daedalus) {
		
		target = daedalus.getPreys().get(0).getPosition(); // On définit la cible comme la position du pacman à la position 0 du tableau
		return move(choices, target); // On appelle la méthode move principale
		
		
	
	}

	@Override
	public Animal copy() {
		Blinky newBlinky = new Blinky(this);
		return (newBlinky);
	}
}
