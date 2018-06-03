package ch.epfl.maze.physical.pacman;


import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.physical.Daedalus;
import ch.epfl.maze.physical.Predator;
import ch.epfl.maze.physical.Prey;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Pink ghost from the Pac-Man game, targets 4 squares in front of its target.
 * 
 */

public class Pinky extends Predator {

	private Vector2D preyPrevPos;
	private Direction preyPrevDir;
	
	/**
	 * Constructs a Pinky with a starting position.
	 * 
	 * @param position
	 *            Starting position of Pinky in the labyrinth
	 */

	public Pinky(Vector2D position) {
		super(position);
		home = position;
		
	}
	public Pinky(Pinky otherPinky)
	{
		super(otherPinky.home);
		home = otherPinky.home;
	}

	@Override
	public Direction move(Direction[] choices, Daedalus daedalus) {
		
		Prey p =  daedalus.getPreys().get(0); // On stock la proie ciblée
		Vector2D positionPrey = p.getPosition(); // On stocke sa position
		
		// On va chercher d'ou elle vient pour savoir la direction dans laquelle elle regarde. On utilise pour ca 
		// les variables preyPrevPos (position précèdente) et preyPrevDir (direction précèdente)
		
		if (preyPrevPos == null) // Si aucune position précèdente n'est connue, on met la direction précèdente à gauche par défaut
		{
			preyPrevDir = Direction.LEFT; // Par défaut
		}
		else
		{
			preyPrevDir = p.getPosition().sub(preyPrevPos).toDirection(); // Sinon on dit que c'est la position actuelle moins la position précèdente
		}
		target = positionPrey.add(preyPrevDir.toVector().mul(4)); // On définit la cible comme 4 case devant la proie
	
		return move(choices,target); // On apelle la méthode move principale	
	}

	@Override
	public Animal copy() {
		
		Pinky newPinky = new Pinky(this);
		return (newPinky);
	}
}
