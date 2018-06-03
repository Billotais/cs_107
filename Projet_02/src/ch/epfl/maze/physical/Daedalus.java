package ch.epfl.maze.physical;

import java.util.ArrayList;
import java.util.List;



/**
 * Daedalus in which predators hunt preys. Once a prey has been caught by a
 * predator, it will be removed from the daedalus.
 * 
 */

public final class Daedalus extends World {

	private ArrayList<Predator> predators = new ArrayList<>(); // Tableau des pr�dateurs
	private ArrayList<Predator> copyPredators = new ArrayList<>(); // Utile pour la m�thode reset()
	private ArrayList<Prey> preys = new ArrayList<>(); // Tableau des pr�dateurs
	private ArrayList<Prey> copyPreys = new ArrayList<>(); // Utile pour la m�thode reset()

	
	/**
	 * Constructs a Daedalus with a labyrinth structure
	 * 
	 * @param labyrinth
	 *            Structure of the labyrinth, an NxM array of tiles
	 */

	public Daedalus(int[][] labyrinth)  // Contructeur qui appelle le constructeur de la super-classe World
	{
		super(labyrinth);		
	}

	@Override
	
	public boolean isSolved() // Regarde si le tableau des proie est vide pour en déduire si la partie est finie
	{
		return (preys.isEmpty());	
	}

	
	/**
	 * Adds a predator to the daedalus.
	 * 
	 * @param p
	 *            The predator to add
	 */

	public void addPredator(Predator p) // On ajoute un prédateur dans le teablau des prédateurs ainsi que dans le tableau utilisé pour la méthode rest()
	{	
		predators.add(p);
		copyPredators.add(p);
	}

	/**
	 * Adds a prey to the daedalus.
	 * 
	 * @param p
	 *            The prey to add
	 */

	public void addPrey(Prey p)  // On ajoute une proie dans le tableau des proies ainsi que dans le tableau utilisé pour la méthode rest()
	{
		preys.add(p);
		copyPreys.add(p);	
	}
	

	/**
	 * Removes a predator from the daedalus.
	 * 
	 * @param p
	 *            The predator to remove
	 */

	public void removePredator(Predator p) 
	{	
		predators.remove(p);	
	}

	/**
	 * Removes a prey from the daedalus.
	 * 
	 * @param p
	 *            The prey to remove
	 */

	public void removePrey(Prey p) 
	{
		preys.remove(p);
	}
	
	@Override
	public List<Animal> getAnimals() 
	{	
		List<Animal> animals = new ArrayList<>(); // Tableau qui va être retourné
		
		// On ajoute toutes les proies
		
		for (Prey prey : preys) 
		{
			animals.add(prey);
		}
		
		// On ajoute tous les prédateurs
		
		for (Predator predator : predators)
		{
			animals.add(predator);
		}
		
		
		return animals;
	}

	/**
	 * Returns a copy of the list of all current predators in the daedalus.
	 * 
	 * @return A list of all predators in the daedalus
	 */

	public List<Predator> getPredators() 
	{	
		return predators;
	}

	/**
	 * Returns a copy of the list of all current preys in the daedalus.
	 * 
	 * @return A list of all preys in the daedalus
	 */

	public List<Prey> getPreys() 
	{	
		return preys;
	}

	/**
	 * Determines if the daedalus contains a predator.
	 * 
	 * @param p
	 *            The predator in question
	 * @return <b>true</b> if the predator belongs to the world, <b>false</b>
	 *         otherwise.
	 */

	public boolean hasPredator(Predator p) 
	{
		return (predators.contains(p)); // On regarde s'il y a le prédateur entré en argument dans la liste
	}

	/**
	 * Determines if the daedalus contains a prey.
	 * 
	 * @param p
	 *            The prey in question
	 * @return <b>true</b> if the prey belongs to the world, <b>false</b>
	 *         otherwise.
	 */

	public boolean hasPrey(Prey p) 
	{ 
		return (preys.contains(p)); // On regarde s'il y a la proie entreé en argument dans la liste
	}

	@Override
	public void reset() 
	{
		// On commence par vider les listes pour pouvoir les remplir proprement
		
		predators.clear();
		preys.clear();
		
		// On parcourt la liste contenant les prédateurs 
		
		for (Predator predator : copyPredators)
		{
			// On crée un nouveau prédateur qui copie celui d'origine, et on le met dans la liste principale des prédateurs
			
			Animal newPred = predator.copy(); // On crée un nouveau prédateur qui copie celui d'origine, et on les met dans la liste principale des prédateurs
			predators.add((Predator) newPred);
		}
		
		// On parcourt la liste contenant les proies
		
		for (Prey prey : copyPreys)
		{
			// On crée une nouvelle proie qui copie celle d'origine, et on la met dans la liste principale des proies
			
			Animal newPrey = prey.copy(); 
			preys.add((Prey) newPrey);
		}	
	}
}
