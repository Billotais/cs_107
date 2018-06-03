package ch.epfl.maze.physical.zoo;

import java.util.ArrayList;
import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Bear A.I. that implements the Pledge Algorithm.
 * 
 */

public class Bear extends Animal 
{
	
	//	On crée une variable qui indique la direction que l'ours a prise au dernier tour. Elle est NONE au premier tour.
	
	private Direction previousDirection = Direction.NONE;
	
	// 	On crée une variable qui indique sa direction préférée (dans le référentiel du labyrinthe). Elle est NONE au départ.
	
	private Direction favoriteDirection = Direction.NONE;
	
	//	On crée le compteur dont l'algorithme de Pledge a besoin.
	
	private int compteur = 0;
	
	
	/**
	 * Constructs a bear with a starting position.
	 * 
	 * @param position
	 *            Starting position of the bear in the labyrinth
	 */

	public Bear(Vector2D position) {
		super(position);
	}
	
	//	Constructeur de copie.
	
	public Bear(Bear otherBear)
	{
		super(otherBear.getPosition());
	}

	/**
	 * Moves according to the <i>Pledge Algorithm</i> : the bear tries to move
	 * towards a favorite direction until it hits a wall. In this case, it will
	 * turn right, put its paw on the left wall, count the number of times it
	 * turns right, and subtract to this the number of times it turns left. It
	 * will repeat the procedure when the counter comes to zero, or until it
	 * leaves the maze.
	 */

	@Override
	public Direction move(Direction[] choices) 
	{
		
		// Liste dynamique des choix que l'ours a dans son "référentiel".
		
		ArrayList<Direction> bearChoices= new ArrayList<>();
		
		// On la remplit.
		for (Direction dir : choices)
		{
			bearChoices.add(previousDirection.relativeDirection(dir));
		}
		
		/*	S'il n'y a pas de choix ou qu'il y a plus que quatre possibilités 
		(on se protège contre les labyrinthes non-conventionels ou contre les paramètres de la méthode non-conformes) */
		
		if (choices.length == 0 || choices.length > 4)
		{
			return Direction.NONE;
		}
		
		// 	S'il est à la case départ, il détermine sa direction préférée, c'est-à-dire le premier choix possible, et la retourne.
		
		if (previousDirection.equals(Direction.NONE))
		{
			favoriteDirection = choices[0];
			previousDirection = favoriteDirection;
			return favoriteDirection;
		}
		
		/*	Si le compteur est à 0 (donc il est dans le sens de sa direction préférée) 
			et qu'il peut suivre sa direction préférée (en allant tout droit dans son référentiel) il le fait.*/
				
		if (compteur==0 && bearChoices.contains(Direction.UP))
		{
			previousDirection=previousDirection.unRelativeDirection(Direction.UP);
			return previousDirection;
		}
		
		/* 	Si le compteur est à 0 et qu'il ne peux pas aller tout droit(dans sa direction préférée) 
		    c'est-à-dire quand il rencontre un mur, 
		  	il ira ou bien à droite une fois(s'il peut le faire) sinon il va à droite deux fois en faisant demi-tour, 
		  	ce qui changera le compteur à respectivement 1 ou 2. */
		
		else if (compteur==0 && !(bearChoices.contains(Direction.UP)))
		{
			if (bearChoices.contains(Direction.RIGHT))
			{
				compteur ++;
				previousDirection=previousDirection.unRelativeDirection(Direction.RIGHT);
				return previousDirection;
			}
			else if(bearChoices.contains(Direction.DOWN))
			{
				compteur+=2;
				previousDirection=previousDirection.unRelativeDirection(Direction.DOWN);
				return previousDirection;
			}
		}
		
		//	Si le compteur n'est pas à 0.
		
		else if(compteur!=0)
		{
			//	Il va à gauche dès qu'il peut, ce qui descends son compteur de 1.
			
			if(bearChoices.contains(Direction.LEFT))
			{
				compteur--;
				previousDirection=previousDirection.unRelativeDirection(Direction.LEFT);
				return previousDirection;
			}
			
			//	Sinon il va tout droit, ce qui ne change rien à son compteur.
			
			else if(bearChoices.contains(Direction.UP))
			{
				previousDirection=previousDirection.unRelativeDirection(Direction.UP);
				return previousDirection;
			}
			
			//Sinon il va à droite, ce qui incrémente son compteur de 1.
			
			else if (bearChoices.contains(Direction.RIGHT))
			{
				compteur++;
				previousDirection=previousDirection.unRelativeDirection(Direction.RIGHT);
				return previousDirection;
			}
			
			//	Sinon il fait demi-tour, ce qui rajoute deux au compteur(il doit tourner deux fois à droite).
			
			else if(bearChoices.contains(Direction.DOWN))
			{
				compteur+=2;
				previousDirection=previousDirection.unRelativeDirection(Direction.DOWN);
				return previousDirection;
			}
		}
		return Direction.NONE;
	}

	@Override
	
	/*	On crée une copy de l'ours this en utilisant le constructeur de copie et on le retourne 
	(sera utilisé pour le reset (Maze.java)). */
	
	public Animal copy() 
	{
		Bear newBear = new Bear(this);
		return (newBear);
	}
}
