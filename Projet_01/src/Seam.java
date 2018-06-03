/**
 * @author Nicolas Jeitziner et Loïs Bilat
 */

import java.util.ArrayList;

public final class Seam {

    /**
     * Compute shortest path between {@code from} and {@code to}
     * @param successors adjacency list for all vertices
     * @param costs weight for all vertices
     * @param from first vertex
     * @param to last vertex
     * @return a sequence of vertices, or {@code null} if no path exists
     */
    public static int[] path(int[][] successors, float[] costs, int from, int to) 
    {
	    	
    	float[] distance = new float[costs.length]; // Tableau contenant les distances jusqu'à chaque vertix depuis le début du parcours
    	int[] bestPredecessor = new int[costs.length]; // Tableau des meilleurs predécesseurs de chaque vertix
    	
    	// Par défaut : distance = infini et meilleur predecesseur = -1 (-> aucun)
    	
    	for (int i = 0; i < costs.length; ++i)
    	{
    		distance[i] = Float.POSITIVE_INFINITY;
    		bestPredecessor[i]=-1;
    	}
    	
    	distance[from] = costs[from]; // On initialise le premier éléement du tableau distance

        boolean modified = true;
        
        // Algorithme de Dijkstra
        
        while(modified) // Tant que tout n'a pas été défini
        {
        	modified = false;
        	for (int vertix = 0; vertix < successors.length; ++vertix) // On parcourt les vertix
            {
            	
            	for (int successor = 0; successor < successors[vertix].length; ++successor) // On parcourt les successeurs des vertix
            	{
            		int next = successors[vertix][successor];
            		if (next > -1) // On vérifie que le vertix possède bien un successeur
            		{
         
            			if (distance[next] > distance[vertix] + costs[next]) // Si le chemin par où on passe est meilleur que celui enregistré sur le vertix
                       	{
            				
                       		bestPredecessor[next] = vertix; // On dit qu'on est le meilleur chemin
                       		
                       		distance[next] = distance[vertix] + costs[next]; // On modifie alors la distance pour accèder à ce successeur
                       		modified = true;
                       	}
            		}
            	}
            }
        }
        
        
    	if (bestPredecessor[to] == -1) // Si l'élément d'arrivée n'a pas de meilleur prédecesseur
    	{
    		return null;
    	}
    	
    	ArrayList<Integer> pathInverted = new ArrayList<>(); // ArrayList comtenant le chemin parcouru de bas en haut
    	  	
    	pathInverted.add(to); // On met l'élément d'arrivée comme premier élément du chemin
    	
    	int index = to; // On initialise un index utile pour la suite
    	
    	while (index != from) // Tant qu'on est pas arrivé au premier élément
    	{    		
    		pathInverted.add(bestPredecessor[index]); //  on ajoute le meilleur predecesseur
        	index = bestPredecessor[index]; // On remonte d'un cran dans le tableau
    	}
    	
    	// On inverse cette ArrayList et on la transforme en tableau classique
    	
    	int[] path = new int[pathInverted.size()];
    	
    	for (int k = pathInverted.size()- 1; k >= 0; --k)
    	{
    		path[pathInverted.size()-k-1] = pathInverted.get(k); 
    	}
    	
    	// Et on le return
    	
    	return path;  
    }
    
    /**
     * Find best seam
     * @param energy weight for all pixels
     * @return a sequence of x-coordinates (the y-coordinate is the index)
     */
    public static int[] find(float[][] energy) 
    {
    	int[][] numbers = new int[energy.length][energy[0].length]; // Tableau qui contient les numéros de pixel
    	
    	ArrayList<Float> costsArray = new ArrayList<>(); // Tableau des couts pour chaque pixel
    	
    	// On remplit les deux tableaux avec les couts et les numeros
    	
    	for (int i = 0; i < energy.length; ++i) // On parcourt les lignes
    	{
    		for (int j = 0; j < energy[0].length; ++j) // On parcourt les colonnes
        	{
        		numbers[i][j] = i*energy[0].length + j; // On calcule le numéro de pixel
        		
        		costsArray.add(energy[i][j]); // On ajoute son cout dans le tableau prévu à cet effet
        	}
		}
    	
    	// On ajoute les pixels de début et de fin (Pixels fictifs qui permettent de représenter le haut et le bas de l'image)
    	
    	costsArray.add(0.0f);
    	costsArray.add(0.0f);
    	
    	// On transforme l'ArrayList des couts en tableau normal
    	
    	float[] costs = new float[costsArray.size()];
    	for(int i = 0; i < costsArray.size(); ++i)
    	{
    		costs[i] = costsArray.get(i);
    	}
    	
    	// On crée le tableau des successeurs
    	
    	int[][] successors = new int[costs.length][];
    	
    	// On parcourt le tableau représentant les numéros des pixels
    	
    	for (int row = 0; row < numbers.length; ++row)
    	{	
    		for (int col = 0; col < numbers[0].length; ++col)
        	{
    			// En principe, chaque pixel possède 3 successeurs, on crée donc un tableau de cette longueur avec des valeur de -1 qui vont représenter une absence de successeur
    			
    			int [] temp = {-1, -1, -1};
    			
    			// Successeurs des pixels vers le centre de l'image
    			
        		if (col > 0 && row < numbers.length-1 && col < numbers[0].length-1)
    			{
        			// On cherche les 3 successeurs
        			
        			for (int l = 0; l < 3; ++l)
            		{
        				temp[l] = numbers[row+1][col-1+l]; 
            		}		
    			}
        		
        		// Successeurs des pixel de la colonne de droite
        		
        		else if (col == 0 && row < numbers.length-1)
        		{
        			// Dans ce cas seulement deux successeurs
        			for (int l = 0; l < 2; ++l)
            		{
        				temp[l] = numbers[row+1][col+l]; 
            		}
        		}
        		
        		// Successeurs des pixel de la colonne de gauche
        		
        		else if(col == numbers[0].length-1 && row < numbers.length-1)
        		{
        			// A nouveau seuleemnt 2 successeurs
        			
        			for (int l = 0; l < 2; ++l)
            		{
        				temp[l] = numbers[row+1][col+l-1]; 
            		}
        		}
        		
        		// Successeurs des pixel de la ligne du bas
        		
        		else if(row == numbers.length-1)
        		{
        			// On seul successeur qui est le pixel fictif du bas
        			
        			temp[0] = costs.length-1; 
        		}
        		// On ajoute cette ligne temp dans le tableau successors
        		
        		successors[numbers[row][col]] = temp;
        	} 		
    	} 
    	
    	// Successeur des pixel fictifs 
    	
    	successors[costs.length-1] = new int[] {}; // Celui d'en bas n'a pas de successeur
    	
    	successors[costs.length-2] = numbers[0]; // Celui d'en haut à toute la première ligne comme successeurs 
    	
    	
    	
    	
    	int[] path = path(successors, costs, costs.length-2, costs.length-1); // On cherche le meilleur chemin en utiisant la méthode path implémentée avant
    	ArrayList<Integer> find = new ArrayList<>();
    	
    	// On transforme le numero de vertix en numero de colonne
    	
    	for (int v = 0; v < path.length; ++v)
    	{
    		find.add(path[v]%(numbers[0].length));
    	}
    	
    	// On enleve le premier et dernier car ils ne font pas partie de l'image
    	
    	find.remove(0);
    	find.remove(find.size()-1);
    	
    	// On transforme l'ArrayList en tabbleau
    	
    	int[] findArray = new int[find.size()];
    	for (int i = 0; i< find.size(); ++i)
    	{
    		findArray[i] = find.get(i);
    	}
    	
        return findArray;	
    }
    
    /**
     * Draw a seam on an image
     * @param image original image
     * @param seam a seam on this image
     * @return a new image with the seam in blue
     */
    public static int[][] merge(int[][] image, int[] seam) {
        // Copy image
        int width = image[0].length;
        int height = image.length;
        int[][] copy = new int[height][width];
        for (int row = 0; row < height; ++row)
            for (int col = 0; col < width; ++col)
                copy[row][col] = image[row][col];

        // Paint seam in blue
        for (int row = 0; row < height; ++row)
            copy[row][seam[row]] = 0xff0000;

        return copy;
    }

    /**
     * Remove specified seam
     * @param image original image
     * @param seam a seam on this image
     * @return the new image (width is decreased by 1)
     */
    public static int[][] shrink(int[][] image, int[] seam) {
        int width = image[0].length;
        int height = image.length;
        int[][] result = new int[height][width - 1];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < seam[row]; ++col)
                result[row][col] = image[row][col];
            for (int col = seam[row] + 1; col < width; ++col)
                result[row][col - 1] = image[row][col];
        }
        return result;
    }

}
