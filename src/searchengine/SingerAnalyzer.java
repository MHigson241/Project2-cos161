package searchengine;

import java.io.File;
import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.*;

/**SingerAnalyzer class
 * (imported from class activity-Iris)
 * 
 * Attributes:
 * private Map<String, String> titleLyricsMap	The map of songs names and lyrics
 * private Map<String, Map<String, Integer>> tf	The Map of song names and tf values as integer
 * 
 * Methods:
 * SingerAnalyzer(String directoryPath)			creates an object of singer analyzer
 * calculateTF()									to save each songs tf to a map
 * search(String query)							searching for relevant songs
 * topK(Map<String, Integer> songScore, int k)	finding the top k songs
 * getTermFrequency(String input)				for figuring out how often a term occurs in one song
 * loadFile(String directoryPath) 				for reading the tsv file to an arraylist
 */
public class SingerAnalyzer {
	private Map<String, Song> titleLyricsMap;
	private Map<String, Map<String, Integer>> tf;
	private Map<String, Integer> idf;
	
	/**Constructor
	 * (imported from class activity-Iris)
	 * 
	 * creates an object of singer analyzer
	 * 
	 * @param directoryPath directory where file lyrics live
	 */
	public SingerAnalyzer(ArrayList<Song> songs) {
		this.titleLyricsMap = readFiles(songs);
		//System.out.println("map of lyrics all set");
		this.tf = calculateTF();
		//System.out.println("tf all set");
		this.idf = getIDFTry2();
		//System.out.println("idf all set");
	}	
	/** to save each songs frequency map
	 * (imported from class activity-Iris)
	 * 
	 * @return map<String, Map<String,Integer>> where the String is the song name and the Map<STring, Integer> is the frequency data
	 */
	private Map<String, Map<String,Integer>> calculateTF(){
		Map<String, Map<String,Integer>> tf = new HashMap<String, Map<String, Integer>>();
		
		for(String songTitle: titleLyricsMap.keySet()) {
			String lyrics = titleLyricsMap.get(songTitle).getLyrics();
			Map<String, Integer> currentTF = getTermFrequency(lyrics);
			tf.put(songTitle, currentTF);
		}
		return tf;
	}
	
	/**searching for relevant songs
	 * (imported from class activity-Iris)
	 * 
	 * @param query the set of words as a string we are looking for
	 * @return 
	 */
	public List<String> search(String query, int k){ 
		String cleanStr = query.toLowerCase().replaceAll("[^a-z0-9]", " ");
		String[] queryTerms = cleanStr.split("\\s+");
		
		Map<String, Integer> songScore = new HashMap<String, Integer>();
	
		for(String song:tf.keySet()) {
			int score = 0;
			Map<String, Integer> currentTF = tf.get(song);
			for(String queryTerm: queryTerms) {
				if(currentTF.containsKey(queryTerm)) {
					score +=currentTF.get(queryTerm) * idf.get(queryTerm);
				}
				songScore.put(song, score);
			}
		}
		return topK(songScore, k);
	}
	
	/**finding the top k songs
	 * (imported from class activity-Iris)
	 * 
	 * @param songScore a map that contains the scores of the song
	 * @param k the number of results that you want
	 * @return List<String> with the name of the top songs
	 */
	private List<String> topK(Map<String, Integer> songScore, int k){
		List<String> best = new ArrayList<String>();
		while(k>0) {
			int max =-1;
			String maxSong = "";
			for(String song: songScore.keySet()) {
				int currentScore = songScore.get(song);
				if(currentScore > max) {
					maxSong = song;
					max = currentScore;
				}
			}
			best.add(maxSong);
			songScore.remove(maxSong);
			k--;
		}
 		return best;
	}
	/**for figuring out how often a term occurs in one song
	 * (imported from class activity-Iris)
	 * 
	 * @param input String of the lyrics
	 * @return a Map<String, Integer> where the string is the word and the Integer is how often it occurs
	 */
	public static Map<String, Integer> getTermFrequency(String input){
		String cleanStr = input.toLowerCase().replaceAll("[^a-z0-9]", " ");
		
		String[] words = cleanStr.split("\\s+");
		Map<String, Integer> frequencyMap = new HashMap<>();
		for(String word: words) {
			frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);/* getOrDefault prevents from crashing when it first finds a word*/
		}
		return frequencyMap;
	}
	
	/**Get Inverse Document Frequency
	 * (Maddy)
	 * 
	 * Loops over the lyrics of every song and adds the IDF value for each unique word to an IDF map
	 * 
	 * @return Map<String, Integer>		A map of unique words and their respective IDF values
	 */
//	public Map<String, Integer> getIDF()
//	{
//		Map<String, Integer> idfMap = new HashMap<>();
//		int n = titleLyricsMap.keySet().size();
//		for (String title : titleLyricsMap.keySet())
//		{
//			String cleanStr = titleLyricsMap.get(title).getLyrics().toLowerCase().replaceAll("[^a-z0-9]", " ");
//			String[] words = cleanStr.split("\\s+");
//			
//			for (String word : words)
//			{
//				if (!idfMap.keySet().contains(word))
//				{
//					int matchCount = 0;
//					for (String song : titleLyricsMap.keySet()) 
//						if (titleLyricsMap.get(song).getLyrics().contains(word)) {
//							matchCount++;
//							idfMap.put(word, n / matchCount);
//						}
//				}
//			}
//		}
//		return idfMap;
//	}
	/**Get Inverse Document Frequency
	 * (Iris)
	 * 
	 * Loops over the lyrics of every song and adds the IDF value for each unique word to an IDF map
	 * 
	 * @return Map<String, Integer>		A map of unique words and their respective IDF values
	 */
//	public Map<String, Integer> getIDFTry(){
//		Map<String, Integer> idfMap = new HashMap<>();
//		Set<String> uniqueTerms = new HashSet<>();
//		int n = titleLyricsMap.size();
//		LinkedList<Integer> df = new  LinkedList<Integer>();
//		int dfCount = 0;
//		//ensures no dupes in our idf map
//		for (String title : titleLyricsMap.keySet()){
//			String cleanStr = titleLyricsMap.get(title).getLyrics().toLowerCase().replaceAll("[^a-z0-9]", " ");
//			String[] words = cleanStr.split("\\s+");
//			for(int i = 0; i < words.length -1; i++) {
//				uniqueTerms.add(words[i]);
//			}
//			//finds number of lyrics containing term
//			int matchCount = 0;
//			for(String term: uniqueTerms) {
//				if(titleLyricsMap.get(title).getLyrics().contains(term)) {
//					matchCount++;
//				}
//			}
//			df.add(matchCount);
//		}
//		System.out.println("unique terms found");
//		//calcs idf now that have 
//		for(String term: uniqueTerms) {
//			idfMap.put(term, n / df.get(dfCount));
//			dfCount++;
//		}
//		return idfMap;
//	}
	/**Get Inverse Document Frequency
	 * (Maddy and Iris)
	 * 
	 * Loops over the lyrics of every song and adds the IDF value for each unique word to an IDF map
	 * 
	 * @return Map<String, Integer>		A map of unique words and their respective IDF values
	 */
	public Map<String, Integer> getIDFTry2() {
	    Map<String, Integer> idfMap = new HashMap<>();
	    Map<String, Integer> dfMap = new HashMap<>(); // term -> number of songs containing it
	    int n = titleLyricsMap.size();
	    // Loop through each song
	    for (String title : titleLyricsMap.keySet()) {
	        String cleanStr = titleLyricsMap.get(title).getLyrics().toLowerCase().replaceAll("[^a-z0-9]", " ");
	        String[] words = cleanStr.split("\\s+");
	        // Unique terms for THIS song only
	        Set<String> uniqueTerms = new HashSet<>();
	        for (String word : words) {
	            if (!word.isEmpty()) {
	                uniqueTerms.add(word);
	            }
	        }
	        // Update document frequency (DF)
	        for (String term : uniqueTerms) {
	            dfMap.put(term, dfMap.getOrDefault(term, 0) + 1);
	        }
	    }
	    // Compute IDF = totalSongs / documentFrequency
	    for (String term : dfMap.keySet()) {
	        int df = dfMap.get(term);
	        idfMap.put(term, n / df);
	    }

	    return idfMap;
	}

	/**for loading the file
	 * (Iris)
	 * 
	 * @param directoryPath the path of the file
	 * @return returns a array list of type song
	 */
	public static ArrayList<Song>  loadFile(String directoryPath) {
		ArrayList<Song >songInfo = new ArrayList<>();
		int count = 300000;
		try {
            File file = new File(directoryPath);
            Scanner scanner = new Scanner(file);
            
            // Skip the header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine() && count > 0) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
                
                if (parts.length >= 5) {
                    String name = parts[0];
                    String tag = parts[1];
                    String artist = parts[2];
                    String year = parts[3];
                    String views = parts[4];
                    String lyrics = parts[5].strip();
                    Song currentSong = new Song(name, tag, artist, year, views, lyrics);
                    songInfo.add(currentSong);
                }
                count--;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + directoryPath);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from file");
            e.printStackTrace();
        }
		return songInfo;

	}
	/**For making a map of the song and name
	 * (Iris)
	 * 
	 * @param songs 	the arraylist of type song
	 * @return				Returns a Map<String, Song> in which the string is the songs name and the Song contains all the information of a song
	 */
	public static Map<String, Song> readFiles(ArrayList<Song> songs){
		HashMap<String, Song> songLyricsMap = new HashMap<String, Song>();
		for(Song s: songs) {
			songLyricsMap.put(s.getName(), s);
		}
		return songLyricsMap;
	}
}
