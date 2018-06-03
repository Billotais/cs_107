package ch.epfl.maze.physical.pacman;


import java.util.ArrayList;
import java.util.Random;

import ch.epfl.maze.physical.Animal;
import ch.epfl.maze.physical.Daedalus;
import ch.epfl.maze.physical.Predator;
import ch.epfl.maze.physical.Prey;
import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * Pac-Man character, from the famous game of the same name.
 * 
 */

public class PacMan extends Prey {

	
	
	private Vector2D home;
	
	boolean isChased = true;
	
	public PacMan(Vector2D position) {
		super(position);
		home = position;
		
		
	}

	public PacMan(PacMan otherPacman)
	{
		super(otherPacman.home);
		prevDir = Direction.NONE;
	}
	
	
	@Override
	public Direction move(Direction[] choices, Daedalus daedalus) 
	{
		ArrayList<Double> predatorDistances = new ArrayList<>(); // On crée un tableau qui va stocker la distance entre pacman et chacun des prédateur
		
		Vector2D vectToPred;
		double min;
		
		
		for(Predator predator : daedalus.getPredators()) // On parcourt la liste des prédateurs
		{
			vectToPred = predator.getPosition().sub(getPosition()); // On calcule les vecteur entre pacman et un prédateur
			predatorDistances.add(vectToPred.dist()); // On le transforme en distance
		}
		if (predatorDistances.isEmpty()) // Si aucun prédateur, on dit au pacman de pas bouger
		{
			return Direction.NONE;
		}
		// On cherche le prédateur le plus proche
		
		min = predatorDistances.get(0);
		for (double value : predatorDistances)
		{
			
			if (value <= min)
			{
				min = value;
			}
		}
		
		
		int index = predatorDistances.indexOf(min); // On récupere l'index de ce prédateur
		
		vectToPred = daedalus.getPredators().get(index).getPosition().sub(getPosition()); // On récupère la vecteur entre pacman et le prédateur
		
		Vector2D target = getPosition().sub(vectToPred); // On l'utilise pour choisir la cible de pacman (a l'ooposé du prédateur)
		
		// On vérifie les cas limites
		
		if (choices.length == 0 || choices.length > 4)
		{
			return Direction.NONE;
		}
		// SI un seul chemin, on prend celui-la
		
		else if (choices.length == 1)
		{
			prevDir = choices[0];
			return choices[0];
		}
		
		else 
		{
			ArrayList<Direction> possibleDirections = new ArrayList<>();
			ArrayList<Double> distances = new ArrayList<>();
			for (Direction dir : choices) // On parcourt les choix proposé par le tableau choices
			{
				// La premiere partie du if enpèche de faire demi-tour
				// La deuxièeme l'autorise à condition que le pédateur le plus proche soit très proche
				if(dir != prevDir.reverse() || (dir == prevDir.reverse() && min < 2))
				{	// On calcule pour chaque chemin de combien il nous aproche de notre cible
					possibleDirections.add(dir);
					Vector2D directWay = new Vector2D(getPosition().addDirectionTo(dir).getX() - target.getX(), getPosition().addDirectionTo(dir).getY() - target.getY());
					distances.add(directWay.dist());
					
				}

			}
			// On cherche le chemin qui nous aproche le plus de la cible
			
			min = distances.get(0);
			for (int i = 0; i < distances.size(); ++i)
			{
				if (distances.get(i) < min)
				{
					min = distances.get(i);
				}
			}
			int indexMin = distances.indexOf(min);
			prevDir = possibleDirections.get(indexMin);
			return prevDir;
			
		}
	
	}

	@Override
	public Animal copy() {
		PacMan newPacman = new PacMan(this); // On appelle le constructeur de copie
		return (newPacman);
	}
}
