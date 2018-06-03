package ch.epfl.maze.physical.zoo;

import java.util.ArrayList;
import java.util.Random;
import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Panda A.I. that implements Trémeaux's Algorithm.
 * 
 */
public class Panda extends Animal 
{
	/* 	On initialise deux tableaux qui contiendront respectivement la position d'un case et la couleur que celle-ci aura.
	  	Si le panda n'est pas encore pass� par cette case elle ne figurera pas dans ces tableaux.
	  	le tableau de couleur prendra 1 pour la première et 2 pour la deuxième couleur. */
	
	private ArrayList<Vector2D> positions = new ArrayList<>();
	private ArrayList<Integer> colors = new ArrayList<>(); 
	private Random random = new Random();
	
	//	On crée un variable qui indique la direction que la souris a prise au dernier tour. Elle est NONE au premier tour.
	
	private Direction previousDirection = Direction.NONE;
	
	/**
	 * Constructs a panda with a starting position.
	 * 
	 * @param position
	 *            Starting position of the panda in the labyrinth
	 */

	public Panda(Vector2D position) 
	{
		super(position);
	}
	
	//	Constructeur de copie utilisé dans la méthode copy().
	
	public Panda(Panda otherPanda)
	{
		super(otherPanda.getPosition());
	}

	/**
	 * Moves according to <i>Tr�meaux's Algorithm</i>: when the panda
	 * moves, it will mark the ground at most two times (with two different
	 * colors). It will prefer taking the least marked paths. Special cases
	 * have to be handled, especially when the panda is at an intersection.
	 */

	@Override
	public Direction move(Direction[] choices) 
	{		
		/*	S'il n'y a pas de choix ou qu'il y a plus que quatre possibilités 
		(on se protège contre les labyrinthes non-conventionels ou contre les paramêtres de la méthode non-conformes) */
		
		if (choices.length == 0 || choices.length > 4)
		{
			return Direction.NONE;
		}
		
		//On crée trois tableaux qui contiendront les choix qui mènent à une case � aucune, 1 ou 2 couleurs, respectivement.
		
		ArrayList<Direction> noColor = new ArrayList<>();
		ArrayList<Direction> firstColor = new ArrayList<>();
		ArrayList<Direction> secondColor = new ArrayList<>();
		
		// On parcourt la liste des chemins possibles et on remplit les tableaux ci-dessus.
		
		for (Direction dir : choices) 
		{
			Vector2D choice = getPosition().addDirectionTo(dir);
			
			if (!positions.contains(choice)) // Si aucune couleur
			{
				noColor.add(dir);
			}
			else
			{
				int index = positions.indexOf(choice);
				if (colors.get(index) == 1) // Si couleur 1
				{
					firstColor.add(dir);
				}
				else if (colors.get(index) == 2) // Si couleur 2
				{
					secondColor.add(dir);
				}
			}	
		}
		
		/*	On met les couleurs à la case o� on est.
		 	
		 	Si début du labyrinthe ou la case sur laquelle on est n'a pas de couleur. */
		
		if (positions.size() == 0 || !(positions.contains(getPosition()))) 
		{
			// S'il n'y a pas de choix à aucune couleur et qu'il y en a un à une couleur(on est à un cul-de-sac).
			
			if (noColor.size() == 0 && firstColor.size() == 1) 
			{
				positions.add(getPosition());
				colors.add(2);
			}
			
			//	Sinon on met la couleur 1.
			
			else
			{
				positions.add(getPosition());
				colors.add(1);
			}	
		}
		else // Sinon on met la couleur 2 
		{	
			
			//	Quand parmi les choix il n'y a pas de choix sans couleur et qu'il y a un choix avec couleur.
			
			if ((noColor.size() == 0 && firstColor.size() == 1))
			{
				int index = positions.indexOf(getPosition());
				colors.set(index, 2);
			}
			
			//	Quand parmi les choix il n'y a pas de choix sans couleur et qu'il y a deux choix avec couleur.
			
			else if ((noColor.size() == 0 && firstColor.size() == 2))
			{
				int index = positions.indexOf(getPosition());
				colors.set(index, 2);
			}
			
			// 	Quand parmi les choix il n'y a que des choix à deux couleurs.
			
			else if (secondColor.size() == choices.length)
			{
				int index = positions.indexOf(getPosition());
				colors.set(index, 2);
			}		
		}
		
		// On choisit maintenant quel chemin prendre.
		
		//Si on est dans un cul-de-sac on fait demi-tour
		
		if (choices.length == 1) // Si seulement un chemin (cul-de-sac) ->demi-tour
		{
			previousDirection = choices[0];
			return previousDirection;
		}
		
		// Si il ya des chemins sans couleur
		
		else if (noColor.size() != 0) 
		{
			// On choisit au hasard parmis ces chemins
			
			int randomWay = random.nextInt(noColor.size());
			previousDirection = noColor.get(randomWay); 
			return noColor.get(randomWay);
		}
		
		// Si il y a des chemins avec la couleur 1
		
		else if (firstColor.size() != 0) 
		{
			// Tous les chemins sont de la première couleur et on est à un croisement. On fait alors demi-tour.
			
			if (firstColor.size() == choices.length && choices.length > 2) 
			{
				previousDirection = previousDirection.reverse();
				return previousDirection;
			}
			
			//	S'il n'y a seulement un chemin à une couleur on le retourne.
			
			else if (firstColor.size() == 1)
			{
				previousDirection = firstColor.get(0);
				return previousDirection;
			}
			
			// Sinon il y a aussi des chemins de la deuxième couleur et il y a plus qu'un choix de première couleur.
			
			else 
			{
				//	Si la case d'où on vient est de la première couleur, on l'enl�ve. 
				
				if (firstColor.contains(previousDirection.reverse()))
				{
					firstColor.remove(previousDirection.reverse());
				}
				
				//	On choisit au hasard parmi les possibilités de première couleurs restantes.
				
				int randomWay = random.nextInt(firstColor.size());
				previousDirection = firstColor.get(randomWay); 
				return firstColor.get(randomWay);
			}
		}
		
		//	Si il n'y a que des chemins avec la deuxième couleur.
		
		else 
		{
			/*	On choisit au hasard entre les chemins possibles
			  	en enlevant la possibilité de faire demi-tour, sauf si c'est un cul-de-sac. */
			
			if (secondColor.size()==1)
			{
				previousDirection = secondColor.get(0);
				return previousDirection;
			}
			else
			{
				secondColor.remove(previousDirection.reverse());
				int randomWay = random.nextInt(secondColor.size());
				previousDirection = secondColor.get(randomWay); 
				return previousDirection;
			}
		}
	}

	@Override
	
	/*	On crée une copy du panda this en utilisant le constructeur de copie et on le retourne 
	(sera utilisé pour le reset (Maze.java)). */

	public Animal copy() 
	{
		Panda newPanda = new Panda(this);
		return (newPanda);
	}
}
