package searchengine;

import java.util.Scanner;

public class UserInterface {

	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		user(scnr, 1);
		while(user(scnr, 0) == true) {
			user(scnr, 0);
		}
	}
	public static boolean user(Scanner scnr, int first) {
		String filePath = "C:\\Users\\Iris\\Downloads\\Taylor Swift";
		//("C:\\Users\\Iris\\Downloads\\Taylor Swift")   - Saving my file path for testing
		SingerAnalyzer analyzer = new SingerAnalyzer(filePath);
		if(first == 1) {
			System.out.println("Hello User! Please enter your name:");
			String name = scnr.next();
			System.out.println("Greetings " + name + " welcome to our song search engine!");
		}
		System.out.println("To start a query enter the number of results you want to get up to 5.");
		System.out.println("To end your query at any time type in all caps EXIT.");
		int k = scnr.nextInt();
		if(k <= 5 && k > 0) {
			System.out.println("Please enter the term you want to search for: ");
			String term = scnr.next();
			analyzer.search(term, k);
		}
		return true;
	}
}
