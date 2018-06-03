package ch.epfl.maze.physical;

import java.util.ArrayList;
import java.util.Random;

import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Prey that is killed by a predator when they meet each other in the labyrinth.
 * 
 */

abstract public class Prey extends Animal {

	protected Direction prevDir = Direction.NONE;
	private Random random = new Random();
	/**
	 * Constructs a prey with a specified position.
	 * 
	 * @param position
	 *            Position of the prey in the labyrinth
	 */

	public Prey(Vector2D position) {
		super(position);
		prevDir = Direction.NONE;
		
	}
	

	/**
	 * Moves according to a <i>random walk</i>, used while not being hunted in a
	 * {@code MazeSimulation}.
	 * 
	 */

	@Override
	public final Direction move(Direction[] choices) { // Mouvement aléatoire, pareil que pour la souris
		
		if (choices.length == 0 || choices.length > 4)
		{
			return Direction.NONE;
		}
		else if (choices.length == 1)
		{
			prevDir = choices[0];
			return choices[0];
		}
		else 
		{
			if (prevDir == Direction.NONE)
			{
				int choix = random.nextInt(choices.length);
				prevDir = choices[choix];
				return choices[choix];
			}
			else 
			{
				ArrayList<Direction> possibleWays = new ArrayList<>();
				Direction wrongWay = prevDir.reverse();
				
				for (Direction dir : choices)
				{
					if (!(dir.equals(wrongWay)))
					{
						possibleWays.add(dir);
					}		
				}
				int choix = random.nextInt(possibleWays.size() );
				prevDir = possibleWays.get(choix);
				return possibleWays.get(choix);
			}	
		}
	}
	
	

	/**
	 * Retrieves the next direction of the animal, by selecting one choice among
	 * the ones available from its position.
	 * <p>
	 * In this variation, the animal knows the world entirely. It can therefore
	 * use the position of other animals in the daedalus to evade predators more
	 * effectively.
	 * 
	 * @param choices
	 *            The choices left to the animal at its current position (see
	 *            {@link ch.epfl.maze.physical.World#getChoices(Vector2D)
	 *            World.getChoices(Vector2D)})
	 * @param daedalus
	 *            The world in which the animal moves
	 * @return The next direction of the animal, chosen in {@code choices}
	 */

	abstract public Direction move(Direction[] choices, Daedalus daedalus);
}
