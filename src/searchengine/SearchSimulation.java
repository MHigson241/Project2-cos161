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
		System.out.println("file loaded! :3");
		SingerAnalyzer sing = new SingerAnalyzer(s);
		ArrayList<String> queeries = new ArrayList<String>();
		queeries.add("a");
		queeries.add("b");
		queeries.add("c");
		queeries.add("g");
		queeries.add("h");
		queeries.add("j");
		queeries.add("o");
		queeries.add("k");
		queeries.add("l");
		queeries.add("p");
		for (String q :queeries) {
			long start = System.nanoTime();
			Map<String, Integer> a = SingerAnalyzer.getTermFrequency(q);
			sing.getIDFTry2();
			long end = System.nanoTime();
			long time = (end - start);
			System.out.print(time + a.toString());
			System.out.println(sing.search(q, 5));
		}
	}
}
