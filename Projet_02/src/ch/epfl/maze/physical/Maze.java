package ch.epfl.maze.physical;

import java.util.ArrayList;
import java.util.List;

/**
 * Maze in which an animal starts from a starting point and must find the exit.
 * Every animal added will have its position set to the starting point. The
 * animal is removed from the maze when it finds the exit.
 * 
 */

public final class Maze extends World 
{
	
	// 	Tableau d'animaux.
	
	private List<Animal> animals; 
	
	/*	Copie du tableau d'animaux initial, qui servira à ne pas perdre les animaux
		qui ont été supprimés lorsqu'ils ont trouvé la sortie (cette copie nous servira pour le reset).*/
	
	private List<Animal> animalsCopy;

	/**
	 * Constructs a Maze with a labyrinth structure.
	 * 
	 * @param labyrinth
	 *            Structure of the labyrinth, an NxM array of tiles
	 */

	public Maze(int[][] labyrinth) 
	{
		super(labyrinth);
		animals = new ArrayList<>();
		animalsCopy = new ArrayList<>();
	}

	@Override
	public boolean isSolved() 
	{
		
		/*	Retourne true si le tableau des animaux ne contient plus d'animaux, 
			c'est à dire quand tous les animaux ont trouvé la sortie.*/
	
		return (animals.size() == 0);
	}

	@Override
	public List<Animal> getAnimals() 
	{
		return animals;
	}

	/**
	 * Determines if the maze contains an animal.
	 * 
	 * @param a
	 *            The animal in question
	 * @return <b>true</b> if the animal belongs to the world, <b>false</b>
	 *         otherwise.
	 */

	public boolean hasAnimal(Animal a) 
	{
		return(animals.contains(a));
	}

	/**
	 * Adds an animal to the maze.
	 * 
	 * @param a
	 *            The animal to add
	 */

	public void addAnimal(Animal a) 
	{
		
		//	On met l'animal à la case départ pour éviter qu'il n'apparaisse à un endroit différent.
		
		a.setPosition(getStart());
		
		//	On l'ajoute au tableau principal et à sa copie.
		
		animals.add(a);
		animalsCopy.add(a);
	}

	/**
	 * Removes an animal from the maze.
	 * 
	 * @param a
	 *            The animal to remove
	 */

	public void removeAnimal(Animal a) 
	{
		//	On regarde si l'animal qu'on veut enlever est bien dans la liste.
		
		if (hasAnimal(a))
		{
			
			/*	On ne l'enlève à la liste originale et pas à sa copie,
				car cette copie est là pour savoir quels sont les animaux présents au départ 
				et non après qu'ils sortent du maze et qu'ils sont enlevés de la liste d'origine. */
			
			animals.remove(a);
		}
	}

	@Override
	public void reset() 
	{
		//	On enlève tout ce qui reste dans la liste d'origine.
		
		animals.clear();
		
		/*	On parcourt la copie du tableau et on crée une copie de chacun de ces animaux
			en les mettant à "conditions originales" et on les ajoute au tableau d'origine.*/
		
		for (Animal a : animalsCopy)
		{	
			Animal newAnimal = a.copy();
			newAnimal.setPosition(getStart());
			animals.add(newAnimal);
		}
	}
}
