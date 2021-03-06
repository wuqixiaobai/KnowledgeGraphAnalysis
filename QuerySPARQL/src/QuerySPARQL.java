import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class QuerySPARQL {

	public static void main(String[] args) {
		Map<String, CountObject> results = new HashMap<String, CountObject>();
		Map<String, InstanceObject> resultsInstance = new HashMap<String, InstanceObject>();
		int endpoint = 0;
		
		// query DBpedia
		//QueryObject dbpedia = new QueryObject();
		//results = dbpedia.queryEndpoint(endpoint);
		//printCountHeader();
		//printResults(results);
				
		
		// query YAGO
		endpoint = 1;
		QueryObject yago = new QueryObject();
		results = yago.queryEndpoint(endpoint);
		printCountHeader();
		printResults(results);
		//writeResultsToFile(resultsInstance);
		
		
		// query Wikidata
		//QueryWikidataObject wiki = new QueryWikidataObject();
		//wiki.queryEndpoint();
		 	
		
	
	}
	private static void writeResultsToFile(
			Map<String, InstanceObject> resultsInstance) {
		int noLabelCounter = 0;
		Map<String, Set<String>> classInstancesWithLabel = new HashMap<String, Set<String>>();
		//combine the results
		for (Entry<String, InstanceObject> entry : resultsInstance.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().getNumberOfInstancesMap() + " instances");
			Set<String> instanceWithLabel = new HashSet<String>();
			for (Entry<String, Set<String>> instanceWithLabels : entry.getValue().getInstancesMap().entrySet()) {
				String allLabels = "";
				for (String label : instanceWithLabels.getValue()) {
					if (allLabels.equals("")) {
						allLabels = label;
					} else {
						allLabels = allLabels + "\t" + label;
					}
				}
				instanceWithLabel.add(instanceWithLabels.getKey() + "\t" + allLabels);
			}
			
			classInstancesWithLabel.put(entry.getKey(), instanceWithLabel);
		}	
		
		//write to file
		//http://stackoverflow.com/questions/2885173/how-to-create-a-file-and-write-to-a-file-in-java
		//for (Entry<String, Set<String>> entry : classInstances.entrySet()) {
		for (Entry<String, Set<String>> entry : classInstancesWithLabel.entrySet()) {
			//write instances to disk
			Path fileName = Paths.get("yagoResults/" + entry.getKey() + "InstancesWithLabels.txt");		
			try {
				Files.write(fileName, entry.getValue(), Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ERROR WHILE WRITING TO FILE");
			}
			
		}
	}
	private static void printResults(Map<String, CountObject> results) {
		for (Map.Entry<String, CountObject> entry : results.entrySet()) {
		    results.get(entry.getKey()).printAll();
		}
	}
	private static void printResultsInstance(Map<String, InstanceObject> results) {
		for (Map.Entry<String, InstanceObject> entry : results.entrySet()) {
		    results.get(entry.getKey()).printAll();
		}
	}
	public static void printCountHeader() {
		System.out.println("className, count, classIndegree, classOutdegree, "
				+ "classInstanceIndegreeMin, classInstanceIndegreeAvg, classInstanceIndegreeMed, classInstanceIndegreeMax, "
				+ "classInstanceOutdegreeMin, classInstanceOutdegreeAvg, classInstanceOutdegreeMed, classInstanceOutdegreeMax");
	}


}
