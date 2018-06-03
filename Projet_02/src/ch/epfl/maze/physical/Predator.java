package ch.epfl.maze.physical;

import java.util.ArrayList;
import java.util.Random;


import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Predator that kills a prey when they meet with each other in the labyrinth.
 * 
 */

abstract public class Predator extends Animal {

	/* constants relative to the Pac-Man game */
	public static final int SCATTER_DURATION = 14;
	public static final int CHASE_DURATION = 40;
	
	private Random random = new Random();
	
	
	// Variables communes � tous les prédateurs
	
	protected Direction prevDir = Direction.NONE;	
	protected int compteur = -1;
	protected boolean isChasing = true;
	protected Vector2D target = new Vector2D(0, 0);
	protected Vector2D home;
	/**
	 * Constructs a predator with a specified position.
	 * 
	 * @param position
	 *            Position of the predator in the labyrinth
	 */

	public Predator(Vector2D position) 
	{
		super(position);
	}
	/**
	 * Moves according to a <i>random walk</i>, used while not hunting in a
	 * {@code MazeSimulation}.
	 * 
	 */
	public Direction getPrevDir()
	{
		return prevDir;
	}
	@Override
	public final Direction move(Direction[] choices) { // Mouvement aléatoire,même code que la souris
		
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
	public Direction move(Direction[] choices, Vector2D target)
	{
		
		
		compteur ++; // Compteur pour gérer le mode SCATTER/CHASE
		
		
		if (compteur == CHASE_DURATION && isChasing) // SI on est en mode chase et qu'on est arrivé aux 40 pas, on pase en mode scatter
		{
			
			compteur = -1; 
			isChasing = false;
			
		}
		
		else if (compteur == SCATTER_DURATION && !isChasing) // si on est en mode scatter et qu'on a attent les 14 pas
		{
			
			compteur = -1;
			isChasing = true; // On repart en mode chase
			
		}
		if(isChasing) // Avant chaque mouvement, si on est en mode chase, on enregistre la cible entrée en argument dans ls variable target
		{
			target = this.target;
		}
		else if (!isChasing) // Si on est en mode scatter, on définit la cible comme la case maison
		{
			target = home;
		}
		
		
		if (choices.length == 0 || choices.length > 4) // Si aucun choix ou tableau de choix incorrect
		{
			return Direction.NONE;
		}
		
		else if (choices.length == 1) // Un seul chemin, un retourne celui-là
		{
			prevDir = choices[0];
			return choices[0];
		}
		
		else 
		{
			
			ArrayList<Direction> possibleDirections = new ArrayList<>();
			ArrayList<Double> distances = new ArrayList<>();
			
			for (Direction dir : choices) // On parcourt le tableau des chemins possibles...
			{
				if(dir != prevDir.reverse()) // ... en enlevant celui qui nous fait faire demi-tour
				{	
					possibleDirections.add(dir); // Et on les met dans une liste
					
					// Pour chacune des cases alentour, on calcue le vecteur entre celle-ci et la cible
					
					Vector2D directWay = new Vector2D(getPosition().addDirectionTo(dir).getX() - target.getX(), getPosition().addDirectionTo(dir).getY() - target.getY()); 
					distances.add(directWay.dist()); // On transforme en distance et on l'ajoute dans une liste
					
				}
			}
			// On cherche le minimum de cette liste
			
			double min = distances.get(0);
			for (int i = 0; i < distances.size(); ++i)
			{
				if (distances.get(i) < min)
				{
					min = distances.get(i);
				}
			}
			// On enregistre son index et on prend la direction correspondante
			
			int indexMin = distances.indexOf(min);
			prevDir = possibleDirections.get(indexMin);
			return prevDir;
			
		}
	}

	/**
	 * Retrieves the next direction of the animal, by selecting one choice among
	 * the ones available from its position.
	 * <p>
	 * In this variation, the animal knows the world entirely. It can therefore
	 * use the position of other animals in the daedalus to hunt more
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
	abstract public Animal copy();
	
}
