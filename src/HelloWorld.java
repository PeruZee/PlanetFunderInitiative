import java.util.Random;


public class HelloWorld {

  public static void main(String []args) {
        
        long rangeMax = 14;
        
        long rangeMin = 1;
    
    	Random r = new Random();
		long randomValue = (rangeMin + (rangeMax - rangeMin) * r.nextLong());
		
		if (randomValue <= 1) {
	    	System.out.printf("That is not allowed! Nobody won, roll again!");
		}
		else {
			System.out.printf("Winning Transaction is: " + randomValue);
		}
    }
}