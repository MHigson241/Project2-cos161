package searchengine;

import java.util.ArrayList;
import java.util.Map;

public class SearchSimulation {
	public static long start;
	public static long end;
	public static void main(String[] args) {
		ArrayList<Song> s = SingerAnalyzer.loadFile("C:\\Users\\Eris Nyx\\Downloads\\song_lyrics (1).tsv");
		//("C:\\Users\\Iris\\Downloads\\song_lyrics.tsv")   - Saving my file path for testing
		//("C:\\Users\\Eris Nyx\\Downloads\\song_lyrics (1).tsv") saving more file paths
		SingerAnalyzer sing = new SingerAnalyzer(s);
		ArrayList<String> queeries = new ArrayList<String>();
		queeries.add("heartbreak");
		queeries.add("fever");
		queeries.add("money");
		queeries.add("chain");
		queeries.add("pizza");
		queeries.add("rock of ages");
		queeries.add("set fire");
		queeries.add("right hand");
		queeries.add("book");
		queeries.add("limelight");
		for (String q :queeries) {
			double start = System.nanoTime();
			Map<String, Integer> a = SingerAnalyzer.getTermFrequency(q);
			sing.getIDFTry2();
			double end = System.nanoTime();
			double time = (end - start)/1000000000;
			System.out.print(time + a.toString());
			System.out.println(sing.search(q, 5));
		}
	}
}
