package searchengine;

import java.util.ArrayList;
import java.util.Scanner;

/**UserInterface
 * (Iris)
 * 
 * Attributes:
 * public static ArrayList<String> queries	A ArrayList of all the queries a person has made
 * 
 * Methods:
 * user(Scanner scnr, int first)	The method for the user interface
 * exitCheck(T check)			Allows to check any data type to see if it is EXIT
 */
public class UserInterface {
	public static ArrayList<String> queries = new ArrayList<String>();
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		int first = 0;
		int count = 0;
		while(user(scnr, first) == true) {
			first++;
		}
		System.out.println("Here are your queries: ");
		for(String pQuery: queries) {
			System.out.println("\t" + pQuery);
			count++;
		}
		System.out.println("You had " + count + " queries.");
		System.out.println("Goodbye! :3");
	}
	
	/**The method for the user interface
	 * 
	 * @param scnr 	importing the scanner for getting responses from the user
	 * @param first	is used to check if we need to ask for the persons name
	 * @return		returns false once EXIT is entered
	 */
	public static boolean user(Scanner scnr, int first) {
		String filePath = "C:\\Users\\Iris\\Downloads\\Copy of MoviePrefrence - Sheet1.tsv";
		int k = 3;
		//("C:\\Users\\Iris\\Downloads\\song_lyrics.tsv")   - Saving my file path for testing
		SingerAnalyzer analyzer = new SingerAnalyzer(filePath);
		if(first == 0) {
			System.out.println("Hello User! Please enter your name:");
			String name = scnr.next();
			System.out.println("Greetings " + name + " welcome to our song search engine!");
		}
		System.out.println("To start a query enter the number of results you want to get up to 5.");
		System.out.println("To end your query at any time type in all caps EXIT.");
		if (scnr.hasNextInt()) {
			 k = scnr.nextInt();
		}else {
			String k1 = scnr.next();
			if(exitCheck(k1) == false) {
				return false;
			}
		}
		if(k <= 5 && k > 0) {
			System.out.println("Please enter the term you want to search for: ");
			String term = scnr.next();
			if(exitCheck(term) == false) {
				return false;
			}
			queries.add(term);
			System.out.println(analyzer.search(term, k));
			System.out.println("---------------------------------");
		}else {
			System.out.println("Error: pleas enter a valid number between 1 and 5.");
		}
		return true;
	}
	
	/** Allows to check any data type to see if it is EXIT
	 * (Iris)
	 * 
	 * @param <T> 
	 * @param 	check	the item being checked against "EXIT"
	 * @return	returns false if the input equals EXIT
	 */
	public static <T extends Comparable<T>> boolean exitCheck(T check) {
		if(check.toString().equals("EXIT")) {
			return false;
		}
		return true;
	}
}
