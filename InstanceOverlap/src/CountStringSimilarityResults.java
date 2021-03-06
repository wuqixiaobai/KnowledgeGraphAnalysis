import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;


public class CountStringSimilarityResults {
	//x2y<kgClass<simMeasure, instanceCount>
	//private HashMap<String, HashMap<String, HashMap<String, Integer>>> simResults  = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
	
	//private HashMap<Triple<String, String, String>, Integer> simResultsT = new HashMap<Triple<String,String,String>, Integer>(); 
	private HashMap<Pair<String, String>, Integer>  simResultsP = new HashMap<Pair<String, String>, Integer>();
	//private HashMap<Pair<String, String>, HashSet<Pair<String, String>>>  simResultsI = new HashMap<Pair<String, String>, HashSet<Pair<String, String>>>();
	//private boolean saveMatchedLabels = true;
	
	public CountStringSimilarityResults() {
	
	}
	//fK, fromKgClass, tK, toKgClass,
	public void addInstanceCount(String fK, String fromKgClass, String tK, String toKgClass, String simMeasure, Double t) {
		//ImmutableTriple<String, String, String> t = new ImmutableTriple<String,String,String>(x2y, kgClass, simMeasure);
		String key = getKey(fK, fromKgClass, tK, toKgClass);
		ImmutablePair<String, String> p = new ImmutablePair<String, String>(key, simMeasure+t.toString());
		//check if t is already in simResultT
		if (this.simResultsP.containsKey(p)) {	
			this.simResultsP.put(p, this.simResultsP.get(p) + 1);//increase count
		} else {
			this.simResultsP.put(p, 1);//inizialize count
		}
	}
	
	/**
	   * Get the number of instances that overlap
	   * @param x2y (fromKG-toKG, available values:d,y,o,n,w. e.g. d2y)
	   * @param kgClass (class of the fromKG)
	   * @return int (instance overlap count)
	   */
	public int getInstanceOverlapCount(String key, String simMeasure) {
		//return this.simResults.get(x2y).get(kgClass).get(simMeasure);
		//ImmutableTriple<String, String, String> t = new ImmutableTriple<String,String,String>(x2y, kgClass, simMeasure);
		//String key = getKey(fK, fromKgClass, tK, toKgClass);
		ImmutablePair<String, String> p = new ImmutablePair<String, String>(key, simMeasure);
		return this.simResultsP.get(p);
	}
	private String getKey(String fK, String fromKgClass, String tK,
			String toKgClass) {
		String key = fK + "_" + fromKgClass + "_2_" + tK + "_" + toKgClass;
		return key;
	}
	public Set<Pair<String, String>> getPairs() {
		return this.simResultsP.keySet();
		
	}
	
	
}
