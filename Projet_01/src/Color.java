/**
 * @author Nicolas Jeitziner et Loïs Bilat
 */
public final class Color {

    /**
     * Returns red component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(float, float, float)
     */
    public static float getRed(int rgb) 
    {
    	int decale = rgb >> 16; // Décale de 16 pour que la valeur du rouge se retrouve à la fin
    	return (decale & 0b11111111)/255.0f; // On prend seulement les 8 derniers bits = valeur rouge, et on la transforme en float entre 0 et 1
    }

    /**
     * Returns green component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getBlue
     * @see #getRGB(float, float, float)
     */
    public static float getGreen(int rgb) 
    {  
    	int decale = rgb >> 8; // Décale de 8 pour que la valeur du vert se retrouve à la fin
    	return (decale & 0b11111111)/255.0f; // On prend seulement les 8 derniers bits = valeur verte, et on la transforme en float entre 0 et 1
    }

    /**
     * Returns blue component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getGreen
     * @see #getRGB(float, float, float)
     */
    public static float getBlue(int rgb) 
    {
        return (rgb & 0b11111111)/255.0f; // On prend seulement les 8 derniers bits = valeur bleue, et on la transforme en float entre 0 et 1
    }
    
    /**
     * Returns the average of red, green and blue components from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(float)
     */
    public static float getGray(int rgb) 
    {
        return (getBlue(rgb) + getGreen(rgb) + getRed(rgb))/3; // Moyenne des 3 composantes de la couleur
    }

    /**
     * Returns packed RGB components from given red, green and blue components.
     * @param red a float between 0.0 and 1.0
     * @param green a float between 0.0 and 1.0
     * @param blue a float between 0.0 and 1.0
     * @return 32-bits RGB color
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     */
    public static int getRGB(float red, float green, float blue) 
    {
        // On vérifie que les nombres entrés sont entre 0 et 1, et on les change dans le cas contraire
    	
    	// On utilise un tableau pour vérifier les trois couleurs, sans faire de code redondant
    	
    	float[] couleurs = {red, green, blue};
    	for (float couleur : couleurs)
    	{
    		if (couleur > 1.0)
        	{
        		couleur = 1.0f;
        	}
        	if (couleur < 0.0)
        	{
        		couleur = 0.0f;
        	}
    	}
	
		// On transforme ces float en nombres entre 0 et 255
		
		int r = (int) (red * 255);
		int g = (int) (green * 255);
		int b = (int) (blue * 255);

		// On les transforme en parties de int binaire en les décalant pour les mettre à la bonne position
		
		int rBin = r << 16;
		int gBin = g << 8;
		int bBin = b;
		
		// On les combine et on retourne ce resultat
		
		return rBin + gBin + bBin;
    }
    
    /**
     * Returns packed RGB components from given grayscale value.
     * @param red a float between 0.0 and 1.0
     * @param green a float between 0.0 and 1.0
     * @param blue a float between 0.0 and 1.0
     * @return 32-bits RGB color
     * @see #getGray
     */
    public static int getRGB(float gray) {
    	
        // On crée une valeur int binaire qui représente la couleur grise en mettant la valeur de gris pour chaque composante
    	
        return getRGB(gray, gray, gray);
    }

    /**
     * Converts packed RGB image to grayscale float image.
     * @param image a HxW int array
     * @return a HxW float array
     * @see #toRGB
     * @see #getGray
     */
    public static float[][] toGray(int[][] image) {
        
    	// Nouveau tableau qui va contenir des valeur (en float) de gris qui est de même taille que l'image
    	
    	float [][] gray = new float[image.length][image[0].length];
    	
    	// Pour chaque Pixel de l'image originale, on met sa version grise dans le nouveau tableau
    	
    	for (int row = 0; row < image.length; ++row) // On parcourt les lignes de l'image
		{
			for(int column = 0; column < image[0].length; ++column) // On parcourt les colonnes de l'image
			{
				gray[row][column] = getGray(image[row][column]); // On ajoute la valeur dans le tableau des valeurs de gris
			}
		}
    	
    	// On retourne le tableau des valeurs de gris
    	
    	return gray;
        
    }

    /**
     * Converts grayscale float image to packed RGB image.
     * @param channels a HxW float array
     * @return a HxW int array
     * @see #toGray
     * @see #getRGB(float)
     */
    public static int[][] toRGB(float[][] gray) {
        
    	// On recrée un tableau qui créera l'image en noir en blanc en y mettant les vraie valeurs des pixels
    	
    	int [][] back = new int[gray.length][gray[0].length]; // Tableau de même taille que gray
    	
    	// Comme précedement, on parcourt les lignes et colonnes
    	
    	for (int row = 0; row < gray.length; ++row)
		{
			for(int column = 0; column < gray[0].length; ++column)
			{
				back[row][column] = getRGB(gray[row][column]); // On ajoute la valeur transformée grâce a getRGB
			}
		}
    	
        return back; // On retourne le tableau
    }

}
