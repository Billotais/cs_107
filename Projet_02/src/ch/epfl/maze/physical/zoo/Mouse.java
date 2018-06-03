package ch.epfl.maze.physical.zoo;

import java.util.ArrayList;
import java.util.Random;
import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Mouse A.I. that remembers only the previous choice it has made.
 * 
 */


public class Mouse extends Animal 
{

	//	On crée un variable qui indique la direction que la souris a prise au dernier tour. Elle est NONE au premier tour.
	
	private Direction previousDirection = Direction.NONE;
	private Random random = new Random();
	
	/**
	 * Constructs a mouse with a starting position.
	 * 
	 * @param position
	 *            Starting position of the mouse in the labyrinth
	 */

	public Mouse(Vector2D position) 
	{
		super(position);
	}
	
	//	Constructeur de copie utilisé dans la méthode copy().
	
	public Mouse(Mouse otherMouse)
	{
		super(otherMouse.getPosition());
	}

	/**
	 * Moves according to an improved version of a <i>random walk</i> : the
	 * mouse does not directly retrace its steps.
	 */

	@Override
	public Direction move(Direction[] choices) 
	{
		
		/*	S'il n'y a pas de choix ou qu'il y a plus que quatre possibilités 
			(on se protège contre les labyrinthes non-conventionels ou contre les paramètres de la méthode non-conformes) */
		
		if (choices.length == 0 || choices.length > 4)
		{
			return Direction.NONE;
		}
		
		//	Sinon on test s'il y a une unique possibilité, on garde le choix fait dans previousDirection et on retourne le choix.
		
		else if (choices.length == 1)
		{
			previousDirection = choices[0];
			return choices[0];
		}
		
		//	Il ne reste plus que les cases � 2,3 ou 4 choix.
		
		else 
		{
			
			/*	Si on est à la case d�part on prends une direction au hasard grâce au random parmi les choix possibles,
				on garde le choix fait dans previousDirection et on retourne le choix.*/
			
			if (previousDirection == Direction.NONE)
			{
				int choix = random.nextInt(choices.length);
				previousDirection = choices[choix];
				return choices[choix];
			}
			
			/*	Sinon on enlève la possibilité de faire demi-tour parmi les choix et on prends une direction au hasard
			    parmi les choix restants. */
			
			else 
			{
				ArrayList<Direction> possibleWays = new ArrayList<>();
				
				/*	On crée la variable wrongWay de type Direction qui est l'oppos� du choix qu'on a fait au tour précèdent.
				  	c'est-à-dire le choix qu'il faut exclure pour ne pas faire demi-tour. */
				
				Direction wrongWay = previousDirection.reverse();
				
				// 	On munit possibleWays des choix en excluant wrongWay.
				
				for (Direction dir : choices)
				{
					if (!(dir.equals(wrongWay)))
					{
						possibleWays.add(dir);
					}	
				}
				
				//On choisit parmi les chemins possibles.
				
				int choix = random.nextInt(possibleWays.size());
				previousDirection = possibleWays.get(choix);
				return possibleWays.get(choix);
			}	
		}		
	}

	@Override
	
	/*	On crée une copy de la souris this en utilisant le constructeur de copie et on le retourne 
		(sera utilisé pour le reset (Maze.java)). */
	
	public Animal copy() 
	{
		Mouse newMouse = new Mouse(this);
		return (newMouse);
	}
}
