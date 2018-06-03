package ch.epfl.maze.physical;


import java.util.ArrayList;
import java.util.List;

import javax.jws.Oneway;

import ch.epfl.maze.util.Direction;
import ch.epfl.maze.util.Vector2D;

/**
 * World that is represented by a labyrinth of tiles in which an {@code Animal}
 * can move.
 * 
 */

public abstract class World {

	/* tiles constants */
	public static final int FREE = 0;
	public static final int WALL = 1;
	public static final int START = 2;
	public static final int EXIT = 3;
	public static final int NOTHING = -1;

	private int[][] labyrinth;
	/**
	 * Constructs a new world with a labyrinth. The labyrinth must be rectangle.
	 * 
	 * @param labyrinth
	 *            Structure of the labyrinth, an NxM array of tiles
	 */

	public World(int[][] labyrinth) {
		
		// Avant d'enregistrer le labyrinthe entré en paramètre, on vérifie qu'il est rectangulaire. Si ce n'est pas le cas, on retourne une erreur
		
		int length = labyrinth[0].length;
		for (int i = 1; i < labyrinth.length; ++i)
		{
			if (labyrinth[i].length != length)
			{
				System.err.println("Le labyrinthe n'est pas rectangulaire");
				throw new IllegalArgumentException();
			}
		}
		
		
		this.labyrinth = labyrinth;
	}

	/**
	 * Determines whether the labyrinth has been solved by every animal.
	 * 
	 * @return <b>true</b> if no more moves can be made, <b>false</b> otherwise
	 */

	abstract public boolean isSolved();

	/**
	 * Resets the world as when it was instantiated.
	 */

	abstract public void reset();

	/**
	 * Returns a copy of the list of all current animals in the world.
	 * 
	 * @return A list of all animals in the world
	 */

	abstract public List<Animal> getAnimals();

	/**
	 * Checks in a safe way the tile number at position (x, y) in the labyrinth.
	 * 
	 * @param x
	 *            Horizontal coordinate
	 * @param y
	 *            Vertical coordinate
	 * @return The tile number at position (x, y), or the NONE tile if x or y is
	 *         incorrect.
	 */

	public final int getTile(int x, int y) {
		
		if (x < 0 || x >= labyrinth[0].length || y < 0 || y >= labyrinth.length)
		{
			return NOTHING;
		}
		else if (labyrinth[y][x] == -1)
		{ 
			return NOTHING;
		}
		return labyrinth[y][x];
	}

	/**
	 * Determines if coordinates are free to walk on.
	 * 
	 * @param x
	 *            Horizontal coordinate
	 * @param y
	 *            Vertical coordinate
	 * @return <b>true</b> if an animal can walk on tile, <b>false</b> otherwise
	 */

	public final boolean isFree(int x, int y) {
		if (getTile(x, y) == NOTHING || getTile(x, y) == 1)
		{
			return false;
		}
		return true;
	}

	/**
	 * Computes and returns the available choices for a position in the
	 * labyrinth. The result will be typically used by {@code Animal} in
	 * {@link ch.epfl.maze.physical.Animal#move(Direction[]) move(Direction[])}
	 * 
	 * @param position
	 *            A position in the maze
	 * @return An array of all available choices at a position
	 */

	public final Direction[] getChoices(Vector2D position) {
		// TODO
		ArrayList<Direction> directionArray = new ArrayList<>();
		
		
		// On calcule les positiions des 4 cases adjacentes.
		
		Vector2D rightPos = position.addDirectionTo(Direction.RIGHT);
		Vector2D leftPos = position.addDirectionTo(Direction.LEFT);
		Vector2D upPos = position.addDirectionTo(Direction.UP);
		Vector2D downPos = position.addDirectionTo(Direction.DOWN);
		
		// On regarde si les cases adjacentes sont libres en utilisant la méthode isFree et en lui donnant comme paramètre les coordonnées de la case adjacente
		
		if (isFree(rightPos.getX(),rightPos.getY()))
		{
			directionArray.add(Direction.RIGHT);
		}
		if (isFree(leftPos.getX(),leftPos.getY()))
		{
			directionArray.add(Direction.LEFT);
		}
		if (isFree(downPos.getX(),downPos.getY()))
		{
			directionArray.add(Direction.DOWN);
		}
		if (isFree(upPos.getX(),upPos.getY()))
		{
			directionArray.add(Direction.UP);
		}
		if (directionArray.size() == 0) // Si aucune direction libre, on met Direction.NONE comme seule possibilité
		{
			directionArray.add(Direction.NONE);
		}
		// On transforme l'ArrayList en tableau classique de directions
		
		Direction[] direction = new Direction[directionArray.size()];
		for (int i = 0; i < directionArray.size(); ++i)
		{
			direction[i] = directionArray.get(i);
		}
		return direction;
	}

	/**
	 * Returns horizontal length of labyrinth.
	 * 
	 * @return The horizontal length of the labyrinth
	 */

	public final int getWidth() {
		return labyrinth[0].length;
	}

	/**
	 * Returns vertical length of labyrinth.
	 * 
	 * @return The vertical length of the labyrinth
	 */

	public final int getHeight() {
		return labyrinth.length;
	}

	/**
	 * Returns the entrance of the labyrinth at which animals should begin when
	 * added.
	 * 
	 * @return Start position of the labyrinth, null if none.
	 */

	public final Vector2D getStart() {
		
		// On parcourt toutes les cases du labyrinthe jusqu'a trouver celle qui correspons à l'entrée
		
		for (int i = 0; i < getWidth(); i++)
		{
			for (int j = 0; j < getHeight(); j++)
			{
				if (getTile(i, j) == START)
				{
					return new Vector2D(i, j);
				}


			}
	
		}
		return null;
	}

	/**
	 * Returns the exit of the labyrinth at which animals should be removed.
	 * 
	 * @return Exit position of the labyrinth, null if none.
	 */

	public final Vector2D getExit() {
		for (int i = 0; i < getWidth(); i++)
		{
			for (int j = 0; j < getHeight(); j++)
			{
				if (getTile(i, j) == EXIT)
				{
					return new Vector2D(i, j);

				}
			}
	
		}
		return null;
	}
}
