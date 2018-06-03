package ch.epfl.maze.physical.zoo;

import java.util.ArrayList;
import java.util.Random;
import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Hamster A.I. that remembers the previous choice it has made and the dead ends
 * it has already met.
 * 
 */

public class Hamster extends Animal 
{

	/*	On crée un variable qui indique la direction que la souris a prise au dernier tour. Elle est NONE au premier tour.
	 	On crée un tableau de Vector2D dans lequel on mettra les entrées de cul-de-sac.
	 	On crée une variable booléenne qui indique si on vient de faire face � un cul-de-sac.*/
	
	private Direction previousDirection = Direction.NONE;
	private Random random = new Random();
	private ArrayList<Vector2D> deadEnds = new ArrayList<>();
	private boolean comeFromDeadEnd = false;
	
	/**
	 * Constructs a hamster with a starting position.
	 * 
	 * @param position
	 *            Starting position of the hamster in the labyrinth
	 */

	public Hamster(Vector2D position) 
	{
		super(position);
		
	}
	
	//	Constructeur de copie utilisé dans la méthode copy().
	
	public Hamster(Hamster otherHamster)
	{
		super(otherHamster.getPosition());
	}

	/**
	 * Moves without retracing directly its steps and by avoiding the dead-ends
	 * it learns during its journey.
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
		
		/*	Sinon on test s'il y a une unique possibilité, on garde le choix fait dans previousDirection, on retourne le choix,
		    et vu que l'on vient de rencontrer un cul-de-sac on change le booléen "comesFromDeadEnd" à true. */
		
		else if (choices.length == 1)
		{
			comeFromDeadEnd = true;
			previousDirection = choices[0];
			return choices[0];
		}
		
		//	Il ne reste plus que les cases à 2,3 ou 4 choix.
		
		else 
		{
			
			/*	Si on est à la case départ on prends une direction au hasard gràce au random parmi les choix possibles,
			on garde le choix fait dans previousDirection et on retourne le choix.*/
			
			if (previousDirection == Direction.NONE)
			{
				int choix = random.nextInt(choices.length);
				previousDirection = choices[choix];
				return choices[choix];
			}
			
			
			
			else 
			{
				
				/* 	Si on vient d'un cul de sac et qu'on se retrouve à un croisement on "enlève" la posssibilité de reprendre ce chemin-là,
				   	en ajoutant au tableau "deadEnds" le Vector2D qui désigne l'entr�e du cul-de-sac.
				    On oublie pas de mettre le booléen à false. */
				
				if (comeFromDeadEnd && choices.length > 2) 
				{
					Vector2D previousPos = getPosition().addDirectionTo(previousDirection.reverse());
					deadEnds.add(previousPos);
					comeFromDeadEnd = false;
				}
				
				ArrayList<Direction> possibleWays = new ArrayList<>();
				
				//	On crée la variable qui contient la direction � enlever pour ne pas faire demi-tour.
				
				Direction wrongWay = previousDirection.reverse();
				
				// 	On initialise un compteur qui compte combien des choix possibles le mèneraient à un cul-de-sac.
				
				int compteur = 0;
				
				//	On munit possibleWays des choix en excluant wrongWay et les choix qui l'ont précèdemment men� aux cul-de-sac.
				
				for (Direction dir : choices)
				{
					if (deadEnds.contains(getPosition().addDirectionTo(dir))) 
					{
						compteur ++;
					}
					if (!(dir.equals(wrongWay)) && !(deadEnds.contains(getPosition().addDirectionTo(dir))))
					{
						possibleWays.add(dir);
					}	
				}
				
				/* 	S'il ne reste plus qu'une possibilité (en ayant enlevé les entrées de cul-de-sac), 
					on remet le booléen à true pour ne plus revenir à ce croisement. */
				
				if(compteur + 1 == choices.length)
				{
					comeFromDeadEnd = true;
				}
				
				//	On choisit une direction au hasard parmi les choix restants.
				
				int choix = random.nextInt(possibleWays.size() );
				previousDirection = possibleWays.get(choix);
				return possibleWays.get(choix);
			}	
		}
	}

	@Override
	
	/*	On crée une copy du hamster "this" en utilisant le constructeur de copie et on le retourne 
	(sera utilisé pour le reset (Maze.java)). */
	
	public Animal copy() 
	{
		Animal newHamster = new Hamster(this);
		return newHamster;
	}
}
