package searchengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * readFile(String filePath)						for reading an individual file
 * getSongsOrdered() 							orders the names of songs
 * calculateTF()									to save each songs tf to a map
 * search(String query)							searching for relevant songs
 * topK(Map<String, Integer> songScore, int k)	finding the top k songs
 * getTermFrequency(String input)				for figuring out how often a term occurs in one song
 * readFiles(String directoryPath)				for reading files
 */
public class SingerAnalyzer {
	private Map<String, String> titleLyricsMap;
	private Map<String, Map<String, Integer>> tf;
	private Map<String, Integer> idf;
	
	/**Constructor
	 * (imported from class activity-Iris)
	 * 
	 * creates an object of singer analyzer
	 * 
	 * @param directoryPath directory where file lyrics live
	 */
	public SingerAnalyzer(String directoryPath) {
		this.titleLyricsMap = SingerAnalyzer.readFiles(directoryPath);
		this.tf = calculateTF();
		this.idf = getIDF();
	}
	
	/** for reading an individual file
	 * (imported from class activity-Iris)
	 * 
	 * @param filePath the location of the file
	 * @return String with the lyrics of the file
	 */
	public static String readFile(String filePath) {
		Path path = Paths.get(filePath);
		try {
			return Files.readString(path).strip();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**orders the names of songs
	 * (imported from class activity-Iris)
	 * 
	 * @return TreeSet<String>  with the name of the songs
	 */
	public Set<String> getSongsOrdered() {		
		return new TreeSet<String>(this.titleLyricsMap.keySet());
	}
	
	/** to save each songs frequency map
	 * (imported from class activity-Iris)
	 * 
	 * @return map<String, Map<String,Integer>> where the String is the song name and the Map<STring, Integer> is the frequency data
	 */
	private Map<String, Map<String,Integer>> calculateTF(){
		Map<String, Map<String,Integer>> tf = new HashMap<String, Map<String, Integer>>();
		
		for(String songTitle: titleLyricsMap.keySet()) {
			String lyrics = titleLyricsMap.get(songTitle);
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
	private static Map<String, Integer> getTermFrequency(String input){
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
	private Map<String, Integer> getIDF()
	{
		Map<String, Integer> idfMap = new HashMap<>();
		int n = titleLyricsMap.keySet().size();
		
		for (String title : titleLyricsMap.keySet())
		{
			String cleanStr = titleLyricsMap.get(title).toLowerCase().replaceAll("[^a-z0-9]", " ");
			String[] words = cleanStr.split("\\s+");
			
			for (String word : words)
			{
				if (!idfMap.keySet().contains(word))
				{
					int matchCount = 0;
					for (String song : titleLyricsMap.keySet()) if (titleLyricsMap.get(song).contains(word)) matchCount++;
					idfMap.put(word, n / matchCount);
				}
			}
		}
		return idfMap;
	}
	
	/**for reading files
	 * (imported from class activity-Iris)
	 * 
	 * @param directoryPath Takes the path of the folder
	 * @return Map<String, String> with the first string being the file name and the second being the lyrics
	 */
	public static Map<String, String> readFiles(String directoryPath){
		HashMap<String, String> songLyricsMap = new HashMap<String, String>();
		File directory = new File(directoryPath);
		//Get all files and directories in the folder
		File[] files = directory.listFiles();
		for(File file: files) {
			if(file.isFile()) {
				String fileName = file.getName();
				String filePath = file.getAbsolutePath();
				String lyrics = SingerAnalyzer.readFile(filePath);
				// go
				songLyricsMap.put(fileName.split("\\.")[0]/*removes the .txt at the end of the file name*/, lyrics);
			}
		}
		return songLyricsMap;
	}
}
