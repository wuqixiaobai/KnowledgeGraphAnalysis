import com.wcohen.ss.*;
import com.wcohen.ss.api.StringWrapper;
import com.wcohen.ss.tokens.SimpleTokenizer;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class StringMeasures {
	//boolean
	private boolean jaccard;
	private boolean jaro;
	private boolean scaledLevenstein;
	private boolean tfidf;
	private boolean jaroWinkler;
	private boolean exactMatch;
	private boolean softTfidf;
	private boolean internalSoftTfidf;
	//classes
	private Jaccard jaccardC;
	private Jaro jaroC;
	private ScaledLevenstein scaledLevensteinC;
	private TFIDF tfidfC;
	private JaroWinkler jaroWinklerC;
	private SoftTFIDF softTfidfC;
	private Jaccard softTfidfJaccardC;
	private JaroWinkler softTfidfJaroWinklerC;
	private ScaledLevenstein softTfidfScaledLevensteinC;
	//thresholds
	private double jaccardT;
	private double jaroT;
	private double scaledLevensteinT;
	private double tfidfT;
	private double jaroWinklerT;
	private double softTfidfT;
	private double internalSoftTfidfT;
	private List<Double> thresholds;
	//strings
	private String jaccardS = "jaccard";
	private String jaroS = "jaro";
	private String scaledLevensteinS = "scaledLevenstein";
	private String tfidfS = "tfidf";
	private String jaroWinklerS ="jaroWinkler";
	private String exactMatchS = "exactMatch";
	private String softTfidfS = "softTfidf";
	private String internalSoftTfidfS; 
	//keys
	private Pair<String, Double> exactMatchPair = new ImmutablePair<String, Double>(this.exactMatchS, 1.0);
	private Pair<String, Double> jaccardPair1 = new ImmutablePair<String, Double>(this.jaccardS, 1.0);
	private Pair<String, Double> jaccardPair9 = new ImmutablePair<String, Double>(this.jaccardS, 0.9);
	private Pair<String, Double> jaccardPair8 = new ImmutablePair<String, Double>(this.jaccardS, 0.8);
	private Pair<String, Double> jaroPair1 = new ImmutablePair<String, Double>(this.jaroS, 1.0);
	private Pair<String, Double> jaroPair9 = new ImmutablePair<String, Double>(this.jaroS, 0.9);
	private Pair<String, Double> jaroPair8 = new ImmutablePair<String, Double>(this.jaroS, 0.8);
	private Pair<String, Double> jaroWinklerPair1 = new ImmutablePair<String, Double>(this.jaroWinklerS, 1.0);
	private Pair<String, Double> jaroWinklerPair9 = new ImmutablePair<String, Double>(this.jaroWinklerS, 0.9);
	private Pair<String, Double> jaroWinklerPair8 = new ImmutablePair<String, Double>(this.jaroWinklerS, 0.8);
	private Pair<String, Double> scaledLevensteinPair1 = new ImmutablePair<String, Double>(this.scaledLevensteinS, 1.0);
	private Pair<String, Double> scaledLevensteinPair9 = new ImmutablePair<String, Double>(this.scaledLevensteinS, 0.9);
	private Pair<String, Double> scaledLevensteinPair8 = new ImmutablePair<String, Double>(this.scaledLevensteinS, 0.8);
	private Pair<String, Double> softTfidfPair1 = new ImmutablePair<String, Double>(this.softTfidfS, 1.0);
	private Pair<String, Double> softTfidfPair9 = new ImmutablePair<String, Double>(this.softTfidfS, 0.9);
	private Pair<String, Double> softTfidfPair8 = new ImmutablePair<String, Double>(this.softTfidfS, 0.8);
	/**
	   * StringMeasures constructor
	   * @param exactMatch boolean
	   * @param jaccard boolean
	   * @param jaccardT threshold value (double)
	   * @param jaro boolean
	   * @param jaroT threshold value (double)
	   * @param scaledLevenstein boolean
	   * @param scaledLevensteinT threshold value (double)
	   * @param tfidf boolean
	   * @param tfidfT threshold value (double)
	   * @param jaroWinkler boolean
	   * @param jaroWinklerT threshold value (double)
	   * @param softTfidf boolean
	   * @param softTfidfT threshold value (double) 
	   * @param internalSoftTfidf  boolean: true if internal sim measure should be used for softTFIDF
	   * @param internalSoftTfidfS internal sim measure for softTFIDF ("jaroWinkler", "jaccard", or "scaledLevenstein")
	   * @param internalSoftTfidfT threshold value for internal sim measure of softTFIDF
	   */
	public StringMeasures(boolean exactMatch, boolean jaccard, double jaccardT, boolean jaro, double jaroT, boolean scaledLevenstein, double scaledLevensteinT, boolean tfidf, double tfidfT, boolean jaroWinkler, double jaroWinklerT, boolean softTfidf, double softTfidfT, boolean internalSoftTfidf, String internalSoftTfidfS, double internalSoftTfidfT) {		
		String config = "";
		this.exactMatch = exactMatch;
		if (exactMatch)
			config = "exactMatch, ";
		this.jaccard = jaccard;
		this.jaro = jaro;
		this.scaledLevenstein = scaledLevenstein;
		this.tfidf = tfidf;
		this.jaroWinkler = jaroWinkler;
		this.softTfidf = softTfidf;
		
		if (jaccard) {
			this.jaccardC = new Jaccard();
			this.jaccardT = jaccardT;
			config = config + "jaccard ("+ jaccardT+"), ";
		}
		if (jaro) {
			this.jaroC = new Jaro();
			this.jaroT = jaroT;
			config = config + "jaro ("+ jaroT+"), ";
		}
		if (scaledLevenstein) {
		 this.scaledLevensteinC = new ScaledLevenstein();
		 this.scaledLevensteinT = scaledLevensteinT;
		 config = config + "scaledLevenstein ("+ scaledLevensteinT+"), ";
		}
		if (tfidf) {
			this.tfidfC = new TFIDF();
			this.tfidfT = tfidfT;
			config = config + "tfidf ("+ tfidfT+"), ";
		}
		if (jaroWinkler) {
			this.jaroWinklerC = new JaroWinkler();
			this.jaroWinklerT = jaroWinklerT;
			config = config + "jaroWinkler ("+ jaroWinklerT+"), ";
		}
		if (softTfidf) {
			this.softTfidfT = softTfidfT;
			config = config + "softTfidf ("+ softTfidfT+"), ";
			this.internalSoftTfidf = internalSoftTfidf;
			if (internalSoftTfidf) {
				//set threshold and internal sim measure string
				this.internalSoftTfidfT = internalSoftTfidfT;
				this.internalSoftTfidfS = internalSoftTfidfS;
				//get internal sim measure
				if (internalSoftTfidfS.equals(this.jaccardS)) {
					this.softTfidfJaccardC = new Jaccard();
					this.softTfidfC = new SoftTFIDF(new SimpleTokenizer(true, true), this.softTfidfJaccardC , internalSoftTfidfT);
					config = config + "internalSoftTfidf:jaccard ("+ internalSoftTfidfT+"), ";
				} else if (internalSoftTfidfS.equals(this.jaroWinklerS)) {
					this.softTfidfJaroWinklerC = new JaroWinkler();
					this.softTfidfC = new SoftTFIDF(new SimpleTokenizer(true, true), softTfidfJaroWinklerC , internalSoftTfidfT);
					config = config + "internalSoftTfidf:jaroWinkler ("+ internalSoftTfidfT+"), ";
				} else if (internalSoftTfidfS.equals(this.scaledLevensteinS)) {
					this.softTfidfScaledLevensteinC = new ScaledLevenstein();
					this.softTfidfC = new SoftTFIDF(new SimpleTokenizer(true, true), softTfidfScaledLevensteinC , internalSoftTfidfT);
					config = config + "internalSoftTfidf:scaledLevenstein ("+ internalSoftTfidfT+"), ";
				} else {
					System.out.println("Internal SoftTFIDF sim measure not found. Please use jaccard, jaroWinkler or scaledLevenstein.");
				}
			} else { //use without internal sim measure
				this.softTfidfC = new SoftTFIDF();
			}
			
		}
		System.out.println("Configuration: " + config.substring(0, config.length()-2));
	}
	/**
	   * StringMeasures constructor
	   * exactMatch, jaccard, jaro, scaledLevenstein, jaroWinkler, softTfidf
	   * 
	   */
	public StringMeasures(ArrayList<Double> thresholds) {
		this.thresholds = thresholds;
		
		this.exactMatch = true;
		this.jaccard = true;
		this.jaro = true;
		this.scaledLevenstein = true;
		this.jaroWinkler = true;
		this.softTfidf = true;
		
		this.jaccardC = new Jaccard();
		this.jaroC = new Jaro();
		this.scaledLevensteinC = new ScaledLevenstein();
		this.jaroWinklerC = new JaroWinkler();
		this.softTfidfC = new SoftTFIDF();
	}

	public double getJaccardScore(String s1, String s2) {
		return jaccardC.score(jaccardC.prepare(s1), jaccardC.prepare(s2));
	}
	public double getJaroScore(String s1, String s2) {
		return jaroC.score(jaroC.prepare(s1), jaroC.prepare(s2));
	}
	public double getScaledLevenstein(String s1, String s2) {
		return scaledLevensteinC.score(scaledLevensteinC.prepare(s1), scaledLevensteinC.prepare(s2));
	}
	public double getTfidfScore(String s1, String s2) {
		return tfidfC.score(tfidfC.prepare(s1), tfidfC.prepare(s2));
	}
	public double getJaroWinklerScore(String s1, String s2) {
		return jaroWinklerC.score(jaroWinklerC.prepare(s1), jaroWinklerC.prepare(s2));
	}
	public double getSoftTfidfScore(String s1, String s2) {
		return softTfidfC.score(softTfidfC.prepare(s1), softTfidfC.prepare(s2));
	}
	
	/**
	   * Get string equality score
	   * @param s1
	   * @param s2
	   * @return double (1.0 if strings are equal; 0.0 otherwise)
	   */
	public double getExactMatchScore(String s1, String s2) {
		double score = 0.0;
		if (s1.equals(s2)) {
			score = 1.0;
		}
		return score;
	}
	/**
	   * Check if two strings are equal
	   * @param s1
	   * @param s2
	   * @return boolean
	   */
	public boolean getExactMatch(String s1, String s2) {
		if (s1.equals(s2)) { 
			return true;
		}
		return false;
	}
	/**
	   * Get similarity scores for two strings s1 and s2
	   * @param s1
	   * @param s2
	   * @return HashMap<String, Double> with similarity measure name and result score
	   */
	public HashMap<String, Double> getSimilarityScores(String s1, String s2) {
		HashMap<String, Double> resultScores = new HashMap<String, Double>();
		if (this.exactMatch) {
			resultScores.put(this.exactMatchS, getExactMatchScore(s1, s2));
		}
		if (this.jaccard) {
			resultScores.put(this.jaccardS, getJaccardScore(s1, s2));
		}
		if (this.jaro) {
			resultScores.put(this.jaroS, getJaroScore(s1, s2));
		}
		if (this.scaledLevenstein) {
			resultScores.put(this.scaledLevensteinS, getScaledLevenstein(s1, s2));
		}
		if (this.tfidf) {
			resultScores.put(this.tfidfS, getTfidfScore(s1, s2));
		}
		if (this.jaroWinkler) {
			resultScores.put(this.jaroWinklerS, getJaroWinklerScore(s1, s2));
		}	
		if (this.softTfidf) {
			resultScores.put(this.softTfidfS, getSoftTfidfScore(s1, s2));
		}
		return resultScores;
	}
	/**
	   * Get similarity results for two strings s1 and s2
	   * @param s1
	   * @param s2
	 * @param thresholds 
	   * @return HashMap<String, Boolean> with similarity measure name and result
	   */
	public HashMap<Pair<String, Double>, Boolean> getSimilarityResult(String s1, String s2, ArrayList<Double> thresholds) {
		HashMap<Pair<String, Double>, Boolean> results = new HashMap<Pair<String, Double>, Boolean>();
		if (this.exactMatch) {
			
			results.put(getKeyPair(this.exactMatchS, 1.0), getExactMatch(s1, s2));
		}
		//check scores against thresholds
		HashMap<String, Double> resultScores = getSimilarityScores(s1, s2);
		//get all scores
		for (Entry<String, Double> entry : resultScores.entrySet()) {
			//for each threshold
			for (Double t : thresholds) {
				//check sim measure & check threshold
				if (entry.getKey().equals(this.jaccardS)) {
					results.put(getKeyPair(this.jaccardS, t), checkThreshold(entry.getValue().doubleValue(), t));
					/*if (checkThreshold(entry.getValue().doubleValue(), this.jaccardT))
						System.out.println(s1 + " matched with " + s2);*/
				} else if (entry.getKey().equals(this.jaroS)) {
					results.put(getKeyPair(this.jaroS, t), checkThreshold(entry.getValue().doubleValue(), t));
					/*if (checkThreshold(entry.getValue().doubleValue(), this.jaroT))
						System.out.println(s1 + " matched with " + s2);*/
				} else if (entry.getKey().equals(this.scaledLevensteinS)) {
					results.put(getKeyPair(this.scaledLevensteinS, t), checkThreshold(entry.getValue().doubleValue(), t));
					/*if (checkThreshold(entry.getValue().doubleValue(), this.scaledLevensteinT))
						System.out.println(s1 + " matched with " + s2);*/
				/*} else if (entry.getKey().equals(this.tfidfS)) {
					results.put(getKeyPair(this.tfidfS, t), checkThreshold(entry.getValue().doubleValue(), t));	
					/*if (checkThreshold(entry.getValue().doubleValue(), this.tfidfT))
						System.out.println(s1 + " matched with " + s2);*/
				} else if (entry.getKey().equals(this.jaroWinklerS)) {
					results.put(getKeyPair(this.jaroWinklerS, t), checkThreshold(entry.getValue().doubleValue(), t));
					/*if (checkThreshold(entry.getValue().doubleValue(), this.jaroWinklerT))
						System.out.println(s1 + " matched with " + s2);*/
				} else if (entry.getKey().equals(this.softTfidfS)) {
					results.put(getKeyPair(this.softTfidfS, t), checkThreshold(entry.getValue().doubleValue(), t));
					/*if (checkThreshold(entry.getValue().doubleValue(), this.softTfidfT))
						System.out.println(s1 + " matched with " + s2);*/
				}
			}
		}
		return results;
	}
	private Pair<String, Double> getKeyPair(String simMeasure, Double t) {
		switch(simMeasure) {
		case "exactMatch":
			return getExactMatchPair();
		case "jaccard":
			if (t == 1.0)
				return getJaccardPair1();
			else if (t == 0.9)
				return getJaccardPair9();
			else if (t == 0.8)
				return getJaccardPair8();
			break;
		case "jaro":
			if (t == 1.0)
				return getJaroPair1();
			else if (t == 0.9)
				return getJaroPair9();
			else if (t == 0.8)
				return getJaroPair8();
			break;
		case "scaledLevenstein":
			if (t == 1.0)
				return getScaledLevensteinPair1();
			else if (t == 0.9)
				return getScaledLevensteinPair9();
			else if (t == 0.8)
				return getScaledLevensteinPair8();
			break;
		case "jaroWinkler":
			if (t == 1.0)
				return getJaroWinklerPair1();
			else if (t == 0.9)
				return getJaroWinklerPair9();
			else if (t == 0.8)
				return getJaroWinklerPair8();
			break;
		case "softTfidf":
			if (t == 1.0)
				return getSoftTfidfPair1();
			else if (t == 0.9)
				return getSoftTfidfPair9();
			else if (t == 0.8)
				return getSoftTfidfPair8();
			break;
		}
		return null;
	}
	/**
	   * Check if value is at least the threshold (v>=t) 
	   * @return boolean
	   */
	private boolean checkThreshold(double v, double t) {
		boolean r = false;
		if (v>=t)
			r = true;	
		return r;
	}
	/**
	   * Get blank HashMap with all similarity measures 
	 * @param thresholds2 
	   * @return HashMap
	   */
	public HashMap<Pair<String,Double>, Boolean> getBlankInstanceResultsContainer(ArrayList<Double> thresholds2) {
		HashMap<Pair<String,Double>, Boolean> instanceResults = new HashMap<Pair<String, Double>, Boolean>();
		if (this.exactMatch) {
			instanceResults.put(getKeyPair(this.exactMatchS, 1.0), false);
		}
		for (Double t : thresholds) {			
			if (this.jaccard) {
				instanceResults.put(getKeyPair(this.jaccardS, t), false);
			}
			if (this.jaro) {
				instanceResults.put(getKeyPair(this.jaroS, t), false);
			}
			if (this.scaledLevenstein) {
				instanceResults.put(getKeyPair(this.scaledLevensteinS, t), false);
			}
			if (this.tfidf) {
				instanceResults.put(getKeyPair(this.tfidfS, t), false);
			}
			if (this.jaroWinkler) {
				instanceResults.put(getKeyPair(this.jaroWinklerS, t), false);
			}
			if (this.softTfidf) {
				instanceResults.put(getKeyPair(this.softTfidfS, t), false);
			}
		}
		return instanceResults;
	}
	/**
	   * Check if TFIDF or SoftTFIDF is used 
	 * @param kKgClassInstanceLabels 
	   * @return boolean
	   */
	public boolean checkTFIDF() {
		if (this.tfidf || this.softTfidf)
			return true;
		return false;
	}

	public void trainTFIDF(HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> kKgClassInstanceLabels
			//Collection<HashSet<String>> labels,
			//HashMap<String, HashMap<String, HashSet<String>>> toKgClasses
			) {
		//System.out.println("TRAIN TFIDF");
		//reset
		
		if (this.internalSoftTfidf) {
			if (this.internalSoftTfidfS.endsWith(this.jaccardS)) {
				this.softTfidfJaccardC = new Jaccard();
				this.softTfidfC = new SoftTFIDF(new SimpleTokenizer(true, true), this.softTfidfJaccardC , this.internalSoftTfidfT);
			} else if (this.internalSoftTfidfS.equals(this.jaroWinklerS)) {
				this.softTfidfJaroWinklerC = new JaroWinkler();
				this.softTfidfC = new SoftTFIDF(new SimpleTokenizer(true, true), softTfidfJaroWinklerC , this.internalSoftTfidfT);
			} else if (this.internalSoftTfidfS.equals(this.scaledLevensteinS)) {
				this.softTfidfScaledLevensteinC = new ScaledLevenstein();
				this.softTfidfC = new SoftTFIDF(new SimpleTokenizer(true, true), softTfidfScaledLevensteinC , this.internalSoftTfidfT);
			}
		} else {
			this.softTfidfC = new SoftTFIDF();
		}
		
		this.tfidfC = new TFIDF();
		
		//train on labels
		Set<StringWrapper> labelsW = new HashSet<StringWrapper>();
		
		//for each KG
		for (String fk : kKgClassInstanceLabels.keySet()) {
			//for each kg class
			for (String kgClass : kKgClassInstanceLabels.get(fk).keySet()) {
				for (String instance : kKgClassInstanceLabels.get(fk).get(kgClass).keySet()) {
					for (String label : kKgClassInstanceLabels.get(fk).get(kgClass).get(instance)) {
						labelsW.add(this.tfidfC.prepare(label));
					}
				}
			}
		}
		if (this.softTfidf)
			this.softTfidfC.train(new BasicStringWrapperIterator(labelsW.iterator()));
		if (this.tfidf)
			this.tfidfC.train(new BasicStringWrapperIterator(labelsW.iterator()));
		
		//System.out.println("TFIDF is trained on labels with collection size of " + this.softTfidfC.getCollectionSize());
		
	}
	public double getJaccardT() {
		return jaccardT;
	}

	public double getJaroT() {
		return jaroT;
	}

	public double getScaledLevensteinT() {
		return scaledLevensteinT;
	}

	public double getTfidfT() {
		return tfidfT;
	}

	public double getJaroWinklerT() {
		return jaroWinklerT;
	}

	public double getSoftTfidfT() {
		return softTfidfT;
	}

	public double getInternalSoftTfidfT() {
		return internalSoftTfidfT;
	}
	
	public void setJaccardT(double jaccardT) {
		this.jaccardT = jaccardT;
	}

	public void setJaroT(double jaroT) {
		this.jaroT = jaroT;
	}

	public void setScaledLevensteinT(double scaledLevensteinT) {
		this.scaledLevensteinT = scaledLevensteinT;
	}

	public void setTfidfT(double tfidfT) {
		this.tfidfT = tfidfT;
	}

	public void setJaroWinklerT(double jaroWinklerT) {
		this.jaroWinklerT = jaroWinklerT;
	}

	public void setSoftTfidfT(double softTfidfT) {
		this.softTfidfT = softTfidfT;
	}

	public void setInternalSoftTfidfT(double internalSoftTfidfT) {
		this.internalSoftTfidfT = internalSoftTfidfT;
	}
	
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	public List<String> getUsedMeasures() {
		List<String> usedMeasures = new ArrayList<String>();
		if (this.exactMatch)
			usedMeasures.add(this.exactMatchS);
		if (this.jaccard)
			usedMeasures.add(this.jaccardS);
		if (this.jaro)
			usedMeasures.add(this.jaroS);
		if (this.jaroWinkler)
			usedMeasures.add(this.jaroWinklerS);
		if (this.scaledLevenstein)
			usedMeasures.add(this.scaledLevensteinS);
		if (this.softTfidf)
			usedMeasures.add(this.softTfidfS);
		if (this.tfidf)
			usedMeasures.add(this.tfidfS);
		return usedMeasures;
	}

	public Pair<String, Double> getExactMatchPair() {
		return exactMatchPair;
	}
	public Pair<String, Double> getJaccardPair1() {
		return jaccardPair1;
	}
	public Pair<String, Double> getJaccardPair9() {
		return jaccardPair9;
	}
	public Pair<String, Double> getJaccardPair8() {
		return jaccardPair8;
	}
	public Pair<String, Double> getJaroPair1() {
		return jaroPair1;
	}
	public Pair<String, Double> getJaroPair9() {
		return jaroPair9;
	}
	public Pair<String, Double> getJaroPair8() {
		return jaroPair8;
	}
	public Pair<String, Double> getJaroWinklerPair1() {
		return jaroWinklerPair1;
	}
	public Pair<String, Double> getJaroWinklerPair9() {
		return jaroWinklerPair9;
	}
	public Pair<String, Double> getJaroWinklerPair8() {
		return jaroWinklerPair8;
	}
	public Pair<String, Double> getScaledLevensteinPair1() {
		return scaledLevensteinPair1;
	}
	public Pair<String, Double> getScaledLevensteinPair9() {
		return scaledLevensteinPair9;
	}
	public Pair<String, Double> getScaledLevensteinPair8() {
		return scaledLevensteinPair8;
	}
	public Pair<String, Double> getSoftTfidfPair1() {
		return softTfidfPair1;
	}
	public Pair<String, Double> getSoftTfidfPair9() {
		return softTfidfPair9;
	}
	public Pair<String, Double> getSoftTfidfPair8() {
		return softTfidfPair8;
	}


}
