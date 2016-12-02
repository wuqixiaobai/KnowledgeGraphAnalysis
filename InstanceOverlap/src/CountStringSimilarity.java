import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.stream.Stream;


public class CountStringSimilarity {

	public void run(ArrayList<String> classNames, ClassMapping cM, StringMeasures stringMeasures) {
		System.out.println("Start CountStringSimilarity.run()");
		long startTime = System.nanoTime();
		
		
		//for each class
		for (String className : classNames) {
			//result Object: HashMap<stringMeasureName, <HashMap<x2y, intScore>>
			HashMap<String, HashMap<String, Integer>> results = new HashMap<String, HashMap<String, Integer>>();
		
			//instanceLabels: HashMap<k, <HashMap<kgClass,<HashMap<instanceURI, <HashSet<englishLabels>>>>
			HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels = new HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>();
			HashMap<String, ArrayList<String>> classMap = cM.getClassMap(className);//key: d,w,y,o,n ; value:kgC
			System.out.println(classMap);
			
			//get instances for each kgClass with all labels
			instanceLabels = getInstanceLabels(classMap);
			/*System.out.println(instanceLabels.get("d"));
			System.out.println(instanceLabels.get("y"));
			System.out.println(instanceLabels.get("n"));
			System.out.println(instanceLabels.get("o"));
			System.out.println(instanceLabels.get("w"));
			*/
			results = getMatchedStringCounts(instanceLabels, stringMeasures);
			
			//print results
			System.out.println(results);
		}
			
		
		System.out.println("EXECUTION TIME: " +  ((System.nanoTime() - startTime)/1000000000) + " seconds." );
	}

	private HashMap<String, HashMap<String, Integer>> getMatchedStringCounts(
			HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels,
			StringMeasures stringMeasures) {
		HashMap<String, HashMap<String, Integer>> results = new HashMap<String, HashMap<String, Integer>>();
		//for each kg
		for (String k : instanceLabels.keySet()) {
			switch (k) {
			case "d":
				//results.putAll(
				compareDtoOthers(instanceLabels, stringMeasures);
				break;
			case "y":
				break;
			case "o":
				break;
			case "n":
				break;
			case "w":
				break;
			}
		}
				
		return results;
	}

	private void compareDtoOthers(
			HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels,
			StringMeasures stringMeasures) {
		String fK = "d";
		
		//for each kgClass
		for (String kgClass :instanceLabels.get(fK).keySet()) {
			for (Entry<String, HashSet<String>> instanceWithLabels : instanceLabels.get(fK).get(kgClass).entrySet()) {
				compareLabelsWithYago(instanceWithLabels.getValue(), instanceLabels.get("y"), stringMeasures);
				
				}
			}
	
		}
		
		
		//return null;
	private void compareLabelsWithYago(HashSet<String> labels,
			HashMap<String, HashMap<String, HashSet<String>>> kgClasses,
			StringMeasures stringMeasures) {	
		// for each label
		for (String label : labels) {
			//for each kg class in yago
			for (String kgClass : kgClasses.keySet()) {
				//for each instance in yago
				for (Entry<String, HashSet<String>> yagoInstanceWithLabels : kgClasses.get(kgClass).entrySet()) {
					for (String yagoLabel : yagoInstanceWithLabels.getValue()) {
						/*double jaccardScore = stringMeasures.getJaccardScore(label, yagoLabel);
						double jaroScore = stringMeasures.getJaroScore(label, yagoLabel);
						double jwScore = stringMeasures.getJaroWinklerScore(label, yagoLabel);
						double slS = stringMeasures.getScaledLevenstein(label, yagoLabel);
						double tfidf = stringMeasures.getTfidfScore(label, yagoLabel);
						System.out.println(label + " AND " + yagoLabel + ": " + jaccardScore + ", " + jaroScore + ", " + jwScore + ", " + slS + ", " + tfidf);
						*/
						System.out.println(label + " AND " + yagoLabel);
						System.out.println(stringMeasures.getSimilarityScores(label, yagoLabel));
						System.out.println(stringMeasures.getSimilarityResult(label, yagoLabel));
						
					}
				}
				
			}
			
		}
		
	}


	

	private HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> getInstanceLabels(
			HashMap<String, ArrayList<String>> classMap) {
		HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> instanceLabels = new HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>();
		
		for (String k : classMap.keySet()) {
		    for (String kgClass : classMap.get(k)) {
		    	//System.out.println(kgClass);
		    	//get all instance labels for the kgClass and save them in the instanceLabels object
		    	HashMap<String, HashMap<String, HashSet<String>>> instanceLabelsForSingleKgClass = new HashMap<String, HashMap<String,HashSet<String>>>();
		    	instanceLabelsForSingleKgClass.put(kgClass, getInstanceLabelsForKgClass(k, kgClass)); 
		    	instanceLabels.put(k, instanceLabelsForSingleKgClass);
		    }
		}
		return instanceLabels;
	}

	private HashMap<String, HashSet<String>> getInstanceLabelsForKgClass(
			String k, String kgClass) {
		HashMap<String, HashSet<String>> instanceLabelsForSingleKgClass = new HashMap<String, HashSet<String>>();
		
		//get file paths 
		Path filePath = null;				
		
		//System.out.println(k + ": "+kgClass);
		switch (k) {
			case "d":
				
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/DBpedia/resultsWithLabelTest/");
				break;
			case "y":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/YAGO/resultsWithLabelTest/");
				break;
			case "o":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/OpenCyc/resultsWithLabelTest/");
				break;
			case "n":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/NELL/resultsWithLabelTest/");
				break;
			case "w":
				filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/Wikidata/resultsWithLabelTest/");
				break;
			default:
				System.out.println("error in getInstanceLabelsForKgClass(). No matching k found");
		}
		instanceLabelsForSingleKgClass = readFile(filePath, kgClass);
		
		return instanceLabelsForSingleKgClass;
	}

	private HashMap<String, HashSet<String>> readFile(Path filePath,
			String kgClass) {
		HashMap<String, HashSet<String>> instanceLabelsForSingleKgClass = new HashMap<String, HashSet<String>>();
		Path fileName = Paths.get(filePath + "/" + kgClass + "InstancesWithLabels.txt");
		try (Stream<String> stream = Files.lines(fileName)) {
			stream.forEach(line -> addLineToHashMap(line, kgClass, instanceLabelsForSingleKgClass));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instanceLabelsForSingleKgClass;
	}

	private static void addLineToHashMap(String line, String kgClass,
			HashMap<String, HashSet<String>> instanceLabelsForSingleKgClass) {
		String[] words = line.split("\\t");
		HashSet<String> allLabels = new HashSet<String>();
		for (int i = 1; i < words.length; i++) {
			allLabels.add(words[i]);
			//System.out.println(words[i]);
		}
		
		instanceLabelsForSingleKgClass.put(words[0], allLabels);		
	}

}
