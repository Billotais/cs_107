package ch.epfl.maze.physical.pacman;


import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.physical.Daedalus;
import ch.epfl.maze.physical.Predator;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Blue ghost from the Pac-Man game, targets the result of two times the vector
 * from Blinky to its target.
 * 
 */

public class Inky extends Predator {

	/**
	 * Constructs a Inky with a starting position.
	 * 
	 * @param position
	 *            Starting position of Inky in the labyrinth
	 */

	public Inky(Vector2D position) {
		super(position);
		home = position;
		
	}
	public Inky(Inky otherInky)
	{
		super(otherInky.home);
		home = otherInky.home;
	}

	@Override
	public Direction move(Direction[] choices, Daedalus daedalus) {
		
		
		// On regarde si il y a un Blinky dans le niveau et on le stocke dans une variable pour pouvoir utiliser sa position
		
		Blinky blinky = null;
		for (Predator p : daedalus.getPredators()) // On parcourt tous les prédateurs du daedalus
		{
			if (p instanceof Blinky) // Si on trouve un Blinky on le stocke
			{
				blinky = (Blinky) p;
			}
		}
		if (blinky == null) // Si aucun blinky n'est trouvé, on retourne return quand même quelque choses
		{
			return Direction.NONE;
		}
		else // Sinon
		{
			target = daedalus.getPreys().get(0).getPosition().mul(2).sub(blinky.getPosition()); // On définit la cible comme demandé : double de la distance entre blinky et la proie 0 du tableau des proies
			return move(choices, target); // On appelle la méthode move principale
		}	
	}

	@Override
	public Animal copy() {
		Inky newInky = new Inky(this);
		return (newInky);
	}
}
