/**
 * @author Nicolas Jeitziner et Loïs Bilat
 */
public final class Filter {

    /**
     * Get a pixel without accessing out of bounds
     * @param gray a HxW float array
     * @param row Y coordinate
     * @param col X coordinate
     * @return nearest valid pixel color
     */
    public static float at(float[][] gray, int row, int col) {
        
    	// On gère les cas ou on est en dehors du tableau
    	
    	if (row < 0) // En dehors en haut
    	{
    		row = 0; // On se remet dans le tabeau
    	}
    	else if (row > gray.length-1) // En dehors en bas
    	{
    		row = gray.length - 1; // On se remet dans le tabeau
    	}
    	if (col < 0) // En dehors à droite
    	{
    		col = 0; // On se remet dans le tabeau
    	}
    	else if (col > gray[0].length - 1 ) // En dehors à droite
    	{
    		 col = gray[0].length - 1; // On se remet dans le tabeau
    	}
        return gray[row][col]; // On retourne la valeur à la position souhaitée
    }

    /**
     * Convolve a single-channel image with specified kernel.
     * @param gray a HxW float array
     * @param kernel a MxN float array, with M and N odd
     * @return a HxW float array
     */
    public static float[][] filter(float[][] gray, float[][] kernel) {
        
    	// On stocke la largeur et la hauteur du kernel
    	
    	int m = kernel.length;
    	int n = kernel[0].length;
    	
    	if (m % 2 == 0 || n % 2 == 0) // On vérifie qu'ils ne soient pas pairs
    	{
    		return null; // On quitte la méthode si c'est le cas car on n'accepte pas les kernel de taille paire
    	}
    	
    	float[][] filtred = new float[gray.length][gray[0].length]; // On crée un tableau (de la même taille que le tableau de gris) pour accueillir la version avec le filtre 
    	
    	float total = 0.0f;
    	
    	// On parcourt l'image originale
    	
        for (int row = 0; row < gray.length; ++row )
        {
        	for (int col = 0; col < gray[0].length; ++col)
        	{
        		// On parcourt le kernel
        		
        		for (int i = 0; i < m; ++i)
        		{
        			for (int j = 0; j < n; ++j)
        			{
        				int decalageN = n/2; 
        				int decalageM = m/2;
        				
        				// On calcule la valeur du pixel en appliquant le kernel sur les pixels adjacents 
        				
        				total += kernel[i][j] * at(gray,  row-decalageM+i, col-decalageN+j);
        			}
        		}
        		
        		// On met cette valeur dans la matrice qu'on retourne
        		
        		filtred[row][col] = total;
        		total = 0.0f;
        	}
        }
        return filtred;               
    }

    /**
     * Smooth a single-channel image
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] smooth(float[][] gray) {
        
    	// On applique le kernel de lissage grâe à la méthode filter
    	
    	float[][] kernel = {{0.1f, 0.1f, 0.1f},{0.1f, 0.2f, 0.1f},{0.1f, 0.1f, 0.1f}};
    	return filter(gray, kernel);      
    }

    /**
     * Compute horizontal Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelX(float[][] gray) {
    	
    	// On applique le kernel de Soble X grâe à la méthode filter
    	
    	float[][] kernel = {{-1.0f, 0.0f, 1.0f},{-2.0f, 0.0f, 2.0f},{-1.0f, 0.0f, 1.0f}};
    	return filter(gray, kernel);
    }

    /**
     * Compute vertical Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelY(float[][] gray) {
    	
    	// On applique le kernel de Sobel Y grâce à la méthode filter
    	
    	float[][] kernel = {{-1.0f, -2.0f, -1.0f},{0.0f, 0.0f, 0.0f},{1.0f, 2.0f, 1.0f}};
    	return filter(gray, kernel);
    }

    /**
     * Compute the magnitude of combined Sobel filters
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobel(float[][] gray) {
    	
    	// On crée le tableau final ainsi que les tabelaux Soblel x et Sobel y
    	
        float[][] sobel = new float[gray.length][gray[0].length];
        float[][] sobelX = sobelX(gray);
        float[][] sobelY = sobelY(gray);
        
        // Pour chaque élément de sobelX et solbelY on calcule pythagore (norme euclidienne) et on l'ajoute dans le tableau de retour
        
        for (int row = 0; row < gray.length; ++row)
        {
        	for (int col = 0; col < gray[0].length; ++col)
        	{
        		sobel[row][col] =  (float) Math.sqrt(sobelX[row][col]*sobelX[row][col] + sobelY[row][col]*sobelY[row][col]);
        	}
        }
        return sobel;
    }
}
