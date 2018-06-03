package ch.epfl.maze.physical.zoo;

import java.util.ArrayList;
import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Monkey A.I. that puts its hand on the left wall and follows it.
 * 
 */

public class Monkey extends Animal 
{
	
	//	On crée une variable qui indique la direction que le singe a prise au dernier tour. Elle est NONE au premier tour.

	private Direction previousDirection = Direction.NONE;
	
	/**
	 * Constructs a monkey with a starting position.
	 * 
	 * @param position
	 *            Starting position of the monkey in the labyrinth
	 */
	
	public Monkey(Vector2D position) 
	{
		super(position);
	}
	
	//	Constructeur de copie utilisé dans la méthode copy().
	
	public Monkey(Monkey otherMonkey)
	{
		super(otherMonkey.getPosition());
	}

	/**
	 * Moves according to the relative left wall that the monkey has to follow.
	 */

	@Override
	public Direction move(Direction[] choices) 
	{
		
		//	Liste dynamique des choix du singe de son point de vue.
		
		ArrayList<Direction> monkeyChoices= new ArrayList<>();
		
		/*	S'il n'y a pas de choix ou qu'il y a plus que quatre possibilités 
		(on se protège contre les labyrinthes non-conventionels ou contre les paramètres de la méthode non-conformes) */
		
		if (choices.length == 0 || choices.length > 4)
		{
			return Direction.NONE;
		} 
		
		//	On rempli la liste dynamique avec les choix que le singe a de son point de vue.
		if (previousDirection == Direction.NONE)
		{
			previousDirection = choices[0];
		}
		
		for (Direction dir : choices)
		{
			monkeyChoices.add(previousDirection.relativeDirection(dir));
		}
		
		
		
		//	Le singe va a gauche dès qu'il peut.
		
		if (monkeyChoices.contains(Direction.LEFT))
		{
			Direction choice = previousDirection.unRelativeDirection(Direction.LEFT);
			previousDirection = choice;
			return choice;
		}
		
		//	Sinon il va tout droit.
		
		else if (monkeyChoices.contains(Direction.UP))
		{
			Direction choice = previousDirection.unRelativeDirection(Direction.UP);
			previousDirection = choice;
			return choice;
		}
		
		//	Sinon il va a droite.
		
		else if (monkeyChoices.contains(Direction.RIGHT))
		{
			Direction choice = previousDirection.unRelativeDirection(Direction.RIGHT);
			previousDirection = choice;
			return choice;
		}
		
		//	Sinon il fait demi-tour.
		
		else if (monkeyChoices.contains(Direction.DOWN))
		{
			Direction choice = previousDirection.unRelativeDirection(Direction.DOWN);
			previousDirection = choice;
			return choice;
		}
		else return Direction.NONE;
	}
	
	@Override
	
	/*	On crée une copy du singe this en utilisant le constructeur de copie et on le retourne 
	(sera utilisé pour le reset (Maze.java)). */
	
	public Animal copy() 
	{
		Monkey newMonkey = new Monkey(this);
		return newMonkey;
	}
}
