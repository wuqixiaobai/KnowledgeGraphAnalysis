import com.wcohen.ss.*;

import java.util.*;

import com.wcohen.ss.tokens.*;
import com.wcohen.ss.api.*;

public class StringMeasures {
	private Jaccard jaccard;
	private Jaro jaro;
	private ScaledLevenstein scaledLevenstein;
	private TFIDF tfidf;
	private JaroWinkler jaroWinkler;
	
	public StringMeasures() {
		this.jaccard = new Jaccard();
		this.jaro = new Jaro();
		this.scaledLevenstein = new ScaledLevenstein();
		this.tfidf = new TFIDF();
		this.jaroWinkler = new JaroWinkler();
	}
	
	public double getJaccardScore(String s1, String s2) {
		return jaccard.score(jaccard.prepare(s1), jaccard.prepare(s2));
	}
	public double getJaroScore(String s1, String s2) {
		return jaro.score(jaro.prepare(s1), jaro.prepare(s2));
	}
	public double getScaledLevenstein(String s1, String s2) {
		return scaledLevenstein.score(scaledLevenstein.prepare(s1), scaledLevenstein.prepare(s2));
	}
	public double getTfidfScore(String s1, String s2) {
		return tfidf.score(tfidf.prepare(s1), tfidf.prepare(s2));
	}
	public double getJaroWinklerScore(String s1, String s2) {
		return jaroWinkler.score(jaroWinkler.prepare(s1), jaroWinkler.prepare(s2));
	}
	
	public double exactMatch(String s1, String s2) {
		double score = 0.0;
		if (s1.equals(s2)) {
			score = 1.0;
		}
		return score;
	}
	
}
