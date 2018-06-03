

/**
 * @author Nicolas Jeitziner et Loïs Bilat
 */
public final class Main {

    public static void main(String[] args) {
    	
    	String nom = "wp";
    	
        // Load image
        System.out.println("Load image...");
        int[][] image = Helper.read(nom + ".jpg");
        Helper.show(image, "Original");
        
        // Convert to grayscale
        System.out.println("Convert to grayscale...");
        float[][] gray = Color.toGray(image);
        Helper.show(Color.toRGB(gray), "Grayscale");
        

        // Smooth it
        System.out.println("Smooth image...");
        float[][] smooth = Filter.smooth(gray);
        
        Helper.show(Color.toRGB(smooth), "Smooth");
        
        // Apply Sobel
        System.out.println("Compute Sobel filter...");
        float[][] sobel = Filter.sobel(gray);
        Helper.show(Color.toRGB(sobel), "Sobel");
        
        // Find best seam
        System.out.println("Find best seam...");
        int[] seam = Seam.find(sobel);
        Helper.show(Seam.merge(image, seam), "Best seam");

        // Shrink until it is a square
        
        int count = image[0].length - image.length;
        //int count = image[0].length - 16*image.length/9;
        System.out.println("Shrink by removing " + count + " seams...");
        for (int i = 0; i < count; ++i) {        	
            sobel = Filter.sobel(Filter.smooth(Color.toGray(image)));
            seam = Seam.find(sobel);          
            image = Seam.shrink(image, seam);           
            System.out.println("Seam " + (i + 1) + "/" + count);
        }
        System.out.println("Done");
        Helper.show(image, "Shrink");

        // Save result
        Helper.write(nom + "_shrink.jpg", image);
    }
}
