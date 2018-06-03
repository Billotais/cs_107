package ch.epfl.maze.physical.zoo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Space Invader A.I. that implements an algorithm of your choice.
 * <p>
 * Note that this class is considered as a <i>bonus</i>, meaning that you do not
 * have to implement it (see statement: section 6, Extensions libres).
 * <p>
 * If you consider implementing it, you will have bonus points on your grade if
 * it can exit a simply-connected maze, plus additional points if it is more
 * efficient than the animals you had to implement.
 * <p>
 * The way we measure efficiency is made by the test case {@code Competition}.
 * 
 * @see ch.epfl.maze.tests.Competition Competition
 * 
 */

public class SpaceInvader extends Animal {

	private Direction previousDirection = Direction.NONE;
	
	private ArrayList<Vector2D> intersections = new ArrayList<>(); // Contient les positions de toutes les cases ou est passé
	private ArrayList<Direction> lastDirection = new ArrayList<>(); // Contient la dernière direction empruntée à chacune des cases du tableau intersections
	private boolean restarted; // Permet de savoir si le chemin à déja été trouvé

	/**
	 * Constructs a space invader with a starting position.
	 * 
	 * @param position
	 *            Starting position of the mouse in the labyrinth
	 */

	public SpaceInvader(Vector2D position) {
		super(position);
		restarted = true;

	}

	// COnstructeur de copie, prend un paramètre les deux tableau permettant de reprendre le meilleur chemin	
	
	public SpaceInvader(SpaceInvader otherSpaceInvader, ArrayList<Vector2D> intersections, ArrayList<Direction> lastDirection) {
		super(otherSpaceInvader.getPosition());
		if (intersections.size() == 0) // SI la fois précédente aucun chemin n'a été crée
		{
			restarted = true; // La fois d'après on recherchera un chemin
		}
		else // Dans le cas contraire, on aura qu'a suivre le chemin trouvé
		{
			restarted = false;
		}
		// On transmet les listes aux prochains spaceinvader
		this.intersections = intersections;
		this.lastDirection = lastDirection;
		
		
	}

	/**
	 * Moves according to (... please complete with as many details as you can).
	 */

	@Override
	public Direction move(Direction[] choices) {
		
		
		if (restarted) { // Si aucun chemin n'est encore trouvé, on fait l'algorithme du singe

			//Liste dynamique des choix du singe de son point de vue.
			
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
				
				addIntersection(getPosition(), previousDirection); // On appelle la méthode addIntersection
				
				return choice;
			}
			
			//	Sinon il va tout droit.
			
			else if (monkeyChoices.contains(Direction.UP))
			{
				Direction choice = previousDirection.unRelativeDirection(Direction.UP);
				previousDirection = choice;
				
				addIntersection(getPosition(), previousDirection); // On appelle la méthode addIntersection
				
				return choice;
			}
			
			//	Sinon il va a droite.
			
			else if (monkeyChoices.contains(Direction.RIGHT))
			{
				Direction choice = previousDirection.unRelativeDirection(Direction.RIGHT);
				previousDirection = choice;
				
				addIntersection(getPosition(), previousDirection); // On appelle la méthode addIntersection
				
				return choice;
			}
			
			//	Sinon il fait demi-tour.
			
			else if (monkeyChoices.contains(Direction.DOWN))
			{
				Direction choice = previousDirection.unRelativeDirection(Direction.DOWN);
				previousDirection = choice;
				
				addIntersection(getPosition(), previousDirection); // On appelle la méthode addIntersection
				
				return choice;
			}
			else return Direction.NONE;
		}
		else // On suit simplement le chemin enregistré
		{
			if(intersections.contains(getPosition())) // On vérife que l'intersection est bien enregistrée
			{
				
				int index = intersections.indexOf(getPosition()); // On cherche son index
				if (previousDirection == lastDirection.get(index).reverse() && previousDirection != Direction.NONE)
				{
					restarted = true;
					lastDirection.clear();
					intersections.clear();
					previousDirection = Direction.NONE;
					return Direction.NONE;
				}
				previousDirection = lastDirection.get(index); // Et on retourne la direction associée à cette intersection
				
				
				return lastDirection.get(index);
			}
			
			
			
		}
		return Direction.NONE;

	}
	private void addIntersection(Vector2D pos, Direction prevDir)
	{
		
		if (intersections.contains(pos)) 
		{
			// On change juste la valeur de la direction prise
			
			int index = intersections.indexOf(pos);
			lastDirection.set(index, prevDir);
		} 
		else 
		{
			// Sinon on l'ajoute
			
			intersections.add(pos);
			lastDirection.add(prevDir);
		}
	}
	@Override
	public Animal copy() {
		SpaceInvader newSpaceInvader = new SpaceInvader(this, this.intersections, this.lastDirection);
		return (newSpaceInvader);
	}
}
