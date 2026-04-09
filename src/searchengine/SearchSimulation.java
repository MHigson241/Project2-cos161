package searchengine;

import java.util.ArrayList;
import java.util.Map;

public class SearchSimulation {
	public static long start;
	public static long end;
	public static void main(String[] args) {
		SingerAnalyzer sing = new SingerAnalyzer("C:\\Users\\Iris\\Downloads\\Copy of MoviePrefrence - Sheet1.tsv");
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
			sing.getIDF();
			long end = System.nanoTime();
			long time = (end - start);
			System.out.print(time + a.toString());
			System.out.println(sing.search(q, 5));
		}
	}

}
