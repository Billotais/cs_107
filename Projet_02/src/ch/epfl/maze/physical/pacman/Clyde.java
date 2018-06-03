package ch.epfl.maze.physical.pacman;



import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.physical.Daedalus;
import ch.epfl.maze.physical.Predator;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Orange ghost from the Pac-Man game, alternates between direct chase if far
 * from its target and SCATTER if close.
 * 
 */

public class Clyde extends Predator {

	/**
	 * Constructs a Clyde with a starting position.
	 * 
	 * @param position
	 *            Starting position of Clyde in the labyrinth
	 */

	public Clyde(Vector2D position) {
		super(position);
		home = position; // On enregistre la position à laquelle Clyde a été crée, ce qui correspond à sa zone maison
		
	}
	public Clyde(Clyde otherClyde) // Constructeur de copie
	{
		super(otherClyde.home);
		home = otherClyde.home;
	}

	@Override
	public Direction move(Direction[] choices, Daedalus daedalus) {
		
		// On calcule notre distance jusqu'a la proie
		
		Vector2D vectToPrey = daedalus.getPreys().get(0).getPosition().sub(getPosition()); 
		double distance = vectToPrey.dist();
		
		target = daedalus.getPreys().get(0).getPosition(); // On définit la cible comme la position de la première proie
		
		if (distance <= 4) // Si elle est à 4 case ou moins
		{
			isChasing = false; // Clyde arrète de chasser
			target = home; // Sa position est définie comme sa maison
			return move(choices, target); // On appelle la méthode move principale
		}
		else // Sinon on appelle avec la position de la proie comme argument "target"
		{
			isChasing = true;
			return move(choices, target);
		}	
	}

	@Override
	public Animal copy() { // On retourne un animal ayant les mêmes propriètés que le Clyde entré comme paramètre
		Clyde newClyde = new Clyde(this);
		return (newClyde);
	}
}
