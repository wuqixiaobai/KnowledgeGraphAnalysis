import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;


public class CountSameAs {
	private static boolean saveSameAsPairs = true;

	public void run(String className, ClassMapping cM, 
			boolean d2y, boolean d2w, boolean d2o, boolean d2n,
			boolean y2w, boolean y2o, boolean y2n,
			boolean w2o, boolean w2n,
			boolean o2n) {
		
		
		System.out.println("Start CountSameAs.run()");
		long startTime = System.nanoTime();

		//get all instances of all classes
		//for every class (can contain more than one className for each KG)
		//for (String className : classNames) {
			HashMap<String, ArrayList<String>> classMap = cM.getClassMap(className);
			System.out.println(classMap);
			
			//initialize HashMaps for getting the instances of the kgClassNames
			//HashMap<className, HashSet<Labels>>
			HashMap<String, HashSet<String>> dInstances = new HashMap<String, HashSet<String>>();
			HashMap<String, HashSet<String>> yInstances = new HashMap<String, HashSet<String>>();
			HashMap<String, HashSet<String>> oInstances = new HashMap<String, HashSet<String>>();
			HashMap<String, HashSet<String>> nInstances = new HashMap<String, HashSet<String>>();
			HashMap<String, HashSet<String>> wInstances = new HashMap<String, HashSet<String>>();
			
			//initialize the HashMaps for the sameAs counts
			HashMap<String, Integer> d2yCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> d2wCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> d2oCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> d2nCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> y2wCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> y2oCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> y2nCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> w2oCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> w2nCountMap = new HashMap<String, Integer>();
			HashMap<String, Integer> o2nCountMap = new HashMap<String, Integer>();
			
			//initialize HashMaps with the sameAs-pairs
			HashMap <String, HashSet<Pair<String, String>>> d2yPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> d2wPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> d2oPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> d2nPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> y2wPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> y2oPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> y2nPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> w2oPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> w2nPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			HashMap <String, HashSet<Pair<String, String>>> o2nPairMap = new HashMap<String, HashSet<Pair<String, String>>>();
			
			//get all instances for the className
			getAllInstances("d", Paths.get("./InstanceLabels/"), classMap, dInstances);//"/Users/curtis/SeminarPaper_KG_files/DBpedia/resultsWithLabel/"
			getAllInstances("y", Paths.get("./InstanceLabels/"), classMap, yInstances); //"/Users/curtis/SeminarPaper_KG_files/YAGO/resultsWithLabel/"
			getAllInstances("w", Paths.get("./InstanceLabels/"), classMap, wInstances);//"/Users/curtis/SeminarPaper_KG_files/Wikidata/resultsWithLabel/"
			getAllInstances("o", Paths.get("./InstanceLabels/"), classMap, oInstances);//"/Users/curtis/SeminarPaper_KG_files/OpenCyc/resultsWithLabel/"
			getAllInstances("n", Paths.get("./InstanceLabels/"), classMap, nInstances);//"/Users/curtis/SeminarPaper_KG_files/NELL/resultsWithLabel/"
			
			String k;;
			String countString;
			
			// INIT countMap
			//initialize countMap for DBpedia 2 Y,W,O,N
			k = "d";
			if (classMap.containsKey(k)) {
				for (String kgClassName : classMap.get(k)) {
					//init x2yCountMap for each class in y
					initCountMap("y", d2yCountMap, kgClassName, classMap);
					initCountMap("w", d2wCountMap, kgClassName, classMap);
					initCountMap("n", d2nCountMap, kgClassName, classMap);
					initCountMap("o", d2oCountMap, kgClassName, classMap);				
				}
			}
			//initialize countMap for YAGO 2 W,O,N
			k = "y";
			if (classMap.containsKey(k)) {
				for (String kgClassName : classMap.get(k)) {
					//init x2yCountMap for each class in y
					initCountMap("w", y2wCountMap, kgClassName, classMap);
					initCountMap("n", y2nCountMap, kgClassName, classMap);
					initCountMap("o", y2oCountMap, kgClassName, classMap);				
				}
			}
			//initialize countMap for Wikidata 2 O,N
			k = "w";
			if (classMap.containsKey(k)) {
				for (String kgClassName : classMap.get(k)) {
					//init x2yCountMap for each class in y
					initCountMap("n", w2nCountMap, kgClassName, classMap);
					initCountMap("o", w2oCountMap, kgClassName, classMap);				
				}
			}
			//initialize countMap for OpenCyc 2 N
			k = "o";
			if (classMap.containsKey(k)) {
				for (String kgClassName : classMap.get(k)) {
					//init x2yCountMap for each class in y
					initCountMap("n", o2nCountMap, kgClassName, classMap);
				}
			}
			
			
		//count owl:sameAs link
		//DBpedia 2 Y,W,O,N
			//DBpedia to YAGO
			if (d2y) {
				//Path d2yPath = Paths.get("/Users/curtis/SeminarPaper_KG_files/DBpedia/owlSameAsFiles/yago_links.nt");
				Path d2yPath = Paths.get("./owlSameAs/DY_sameAs_union.nt");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/owlSameAsFiles/DY_sameAs_union.nt"
				Path sameAsPairsPath = Paths.get("./owlSameAs/d2y/");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/d2y/owlSameAs/"
				countString = "DBpedia:" + className + " to YAGO counts:";
				getCounts("d", "y", d2yPath, cM, className, dInstances, yInstances, d2yCountMap, countString, d2yPairMap, sameAsPairsPath);
				if (d2yPairMap.get(className) != null)
					d2yPairMap.get(className).clear();			
			}
			//DBpedia to Wikidata
			if (d2w) {
				Path d2wPath = Paths.get("./owlSameAs/DW_links.ttl");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/owlSameAsFiles/DW_links.ttl"
				Path sameAsPairsPath = Paths.get("./owlSameAs/d2w/");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/d2w/owlSameAs/"
				countString = "DBpedia:" + className + " to Wikidata counts:";
				getCounts("d","w", d2wPath, cM, className, dInstances, wInstances, d2wCountMap, countString, d2wPairMap, sameAsPairsPath);
				if (d2wPairMap.get(className) != null)
					d2wPairMap.get(className).clear();
			}
						
			//DBpedia to OpenCyc
			if (d2o) {
				//Path d2oPath = Paths.get("/Users/curtis/SeminarPaper_KG_files/DBpedia/owlSameAsFiles/opencyc_links.nt");
				Path d2oPath = Paths.get("./owlSameAs/DO_sameAs_union.nt");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/owlSameAsFiles/DO_sameAs_union.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/d2o/");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/d2o/owlSameAs/");
				countString = "DBpedia:" + className + " to OpenCyc counts:";
				getCounts("d", "o", d2oPath, cM, className, dInstances, oInstances, d2oCountMap, countString, d2oPairMap, sameAsPairsPath);
				if (d2oPairMap.get(className) != null)
					d2oPairMap.get(className).clear();
			}
			
			//DBpedia to NELL
			if (d2n) {
				Path d2nPath = Paths.get("./owlSameAs/DN_over_wikipedia.nt");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/owlSameAsFiles/DN_over_wikipedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/d2n/");//"/Users/curtis/SeminarPaper_KG_files/DBpedia/d2n/owlSameAs/");
				countString = "DBpedia:" + className + " to NELL counts:";
				getCounts("d", "n", d2nPath, cM, className, dInstances, nInstances, d2nCountMap, countString, d2nPairMap, sameAsPairsPath);
				if (d2nPairMap.get(className) != null)
					d2nPairMap.get(className).clear();
			}
		//YAGO 2 W,O,N
			if (y2w) {
				Path y2wPath = Paths.get("./owlSameAs/YW_over_dbpedia.nt");//"/Users/curtis/SeminarPaper_KG_files/YAGO/YW_over_dbpedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/y2w/");//"/Users/curtis/SeminarPaper_KG_files/YAGO/y2w/owlSameAs/");
				countString = "YAGO:" + className + " to Wikidata counts:";
				getCounts("y", "w", y2wPath, cM, className, yInstances, wInstances, y2wCountMap, countString, y2wPairMap, sameAsPairsPath);
				if (y2wPairMap.get(className) != null)
					y2wPairMap.get(className).clear();		
			}
			if (y2o) {
				Path y2oPath = Paths.get("./owlSameAs/YO_over_dbpedia.nt");//"/Users/curtis/SeminarPaper_KG_files/YAGO/YO_over_dbpedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/y2o/");//"/Users/curtis/SeminarPaper_KG_files/YAGO/y2o/owlSameAs/");
				countString = "YAGO:" + className + " to OpenCyc counts:";
				getCounts("y", "o", y2oPath, cM, className, yInstances, oInstances, y2oCountMap, countString, y2oPairMap, sameAsPairsPath);
				if (y2oPairMap.get(className) != null)
					y2oPairMap.get(className).clear();
			}
			if (y2n) {
				Path y2nPath = Paths.get("./owlSameAs/YN_over_dbpedia.nt");//"/Users/curtis/SeminarPaper_KG_files/YAGO/YN_over_dbpedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/y2n/");//"/Users/curtis/SeminarPaper_KG_files/YAGO/y2n/owlSameAs/");
				countString = "YAGO:" + className + " to NELL counts:";
				getCounts("y", "n", y2nPath, cM, className, yInstances, nInstances, y2nCountMap, countString, y2nPairMap, sameAsPairsPath);
				if (y2nPairMap.get(className) != null)
					y2nPairMap.get(className).clear();			
			}
		// Wikidata 2 O,N
			if (w2o) {
				Path w2oPath = Paths.get("./owlSameAs/WO_over_dbpedia.nt");//"/Users/curtis/SeminarPaper_KG_files/Wikidata/WO_over_dbpedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/w2o/");//"/Users/curtis/SeminarPaper_KG_files/Wikidata/w2o/owlSameAs/");
				countString = "Wikidata:" + className + " to OpenCyc counts:";
				getCounts("w", "o", w2oPath, cM, className, wInstances, oInstances, w2oCountMap, countString, w2oPairMap, sameAsPairsPath);
				if (w2oPairMap.get(className) != null)
					w2oPairMap.get(className).clear();			
			}
			if (w2n) {
				Path w2nPath = Paths.get("./owlSameAs/WN_over_dbpedia.nt");//"/Users/curtis/SeminarPaper_KG_files/Wikidata/WN_over_dbpedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/w2n/");//"/Users/curtis/SeminarPaper_KG_files/Wikidata/w2n/owlSameAs/");
				countString = "Wikidata:" + className + " to NELL counts:";
				getCounts("w", "n", w2nPath, cM, className, wInstances, nInstances, w2nCountMap, countString, w2nPairMap, sameAsPairsPath);
				if (w2nPairMap.get(className) != null)
					w2nPairMap.get(className).clear();			
			}
		// OpenCyc 2 N
			if (o2n) {
				Path o2nPath = Paths.get("./owlSameAs/ON_over_dbpedia.nt");//"/Users/curtis/SeminarPaper_KG_files/OpenCyc/ON_over_dbpedia.nt");
				Path sameAsPairsPath = Paths.get("./owlSameAs/o2n/");//"/Users/curtis/SeminarPaper_KG_files/OpenCyc/o2n/owlSameAs/");
				countString = "OpenCyc:" + className + " to NELL counts:";
				getCounts("o", "n", o2nPath, cM, className, oInstances, nInstances, o2nCountMap, countString, o2nPairMap, sameAsPairsPath);
				if (o2nPairMap.get(className) != null)
					o2nPairMap.get(className).clear();			
			}
			/*
			//opencyc
			if (o2d) {
				k = "o";
				Path filePath = Paths.get("/Users/curtis/SeminarPaper_KG_files/OpenCyc/resultsWithLabel/");
				getAllInstances(k, filePath, classMap, oInstances);
				//initialize countMap for YAGO
				if (classMap.containsKey(k)) {
					for (String kgClassName : classMap.get(k)) {
						o2dCountMap.put(kgClassName, 0);
					}
				}
				//count owl:sameAs link
				Path o2dPath = Paths.get("/Users/curtis/SeminarPaper_KG_files/OpenCyc/opencyc-latest.nt");
				
				Path sameAsPairsPath = Paths.get("/Users/curtis/SeminarPaper_KG_files/OpenCyc/o2d/owlSameAs/");
				
				countString = "OpenCyc:" + className + " to DBpedia counts:"; 
				getCounts("o", o2dPath, cM, className, oInstances, o2dCountMap, countString, o2dPairMap, sameAsPairsPath);
				if (o2dPairMap.get(className) != null)
					o2dPairMap.get(className).clear();
			}
*/
		//}
		

		

		
	
		System.out.println("EXECUTION TIME: " +  ((System.nanoTime() - startTime)/1000000000) + " seconds." );
	}
	private void initCountMap(String y,
			HashMap<String, Integer> x2yCountMap, String kgClassName,
			HashMap<String, ArrayList<String>> classMap) {
		//for each class in y
		if (classMap.containsKey(y)) {
			for (String kgClassName2 : classMap.get(y)) {
				x2yCountMap.put(kgClassName+"_"+kgClassName2, 0);
			}
		}
		
	}
	/**
	 * Get all instances for the specified KG and classMap
	 * @param k
	 * @param filePath of the directory with all the class instances including the labels (filePath/<kgClassName>InstancesWithLabels.txt)
	 * @param classMap
	 * @param instances HashMap to save the instances
	 */
	private static void getAllInstances(String k, 
			Path filePath,
			HashMap<String, ArrayList<String>> classMap,
			HashMap<String, HashSet<String>> instances) {
		//for each className in the KG
		if (classMap.containsKey(k)) {
			for (String kgClassName : classMap.get(k)) {
				instances.put(kgClassName, new HashSet<String>());
				Path fileName = Paths.get(filePath + "/" + k + "_" + kgClassName + "InstancesWithLabels.txt");
				try (Stream<String> stream = Files.lines(fileName)) {
					stream.forEach(line -> getInstance(line, kgClassName, instances));
				} catch (IOException e) {
					e.printStackTrace();
				}
			//System.out.println(dInstances.get(kgClassName));
			}
		}
		
	}
	/**
	 * Read the sameAsPairs file and count (and save) pairs that match the class
	 * @param kg
	 * @param path with all the pairs
	 * @param cM
	 * @param className
	 * @param kgInstances
	 * @param countMap
	 * @param countString
	 * @param pairMap
	 * @param sameAsPairsPath to save the pairs
	 */
	private static void getCounts(String fromKG,
			String toKG,
			Path path, 
			ClassMapping cM,
			String className,
			HashMap<String, HashSet<String>> leftInstances,
			HashMap<String, HashSet<String>> rightInstances,
			HashMap<String, Integer> countMap, 
			String countString, 
			HashMap<String, 
			HashSet<Pair<String, String>>> pairMap, 
			Path sameAsPairsPath) {
		try (Stream<String> stream = Files.lines(path)) {
			stream.forEach(line -> checkAndCountLinks(line, fromKG, toKG, cM, className, leftInstances, rightInstances, countMap, pairMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(countString + countMap);
		countMap.clear();
		if (saveSameAsPairs) {		
			HashMap<String, ArrayList<String>> classMap = cM.getClassMap(className);
			//check for empty classes first
			if (classMap.get(fromKG) != null && classMap.get(toKG) != null) {
				for (String kgClassName : classMap.get(fromKG)) {
					for (String kgClassName2 : classMap.get(toKG)) {
						saveSameAsPairsToFile(fromKG, toKG, classMap, kgClassName, kgClassName2, pairMap, sameAsPairsPath);
					}
				}
			}
		}
	}
	/**
	 * Save the pairs to disk
	 * @param kg
	 * @param classMap
	 * @param kgClassName
	 * @param pairMap
	 * @param sameAsPairsPath
	 */
	private static void saveSameAsPairsToFile(String fromKG,
			String toKG,
			HashMap<String, ArrayList<String>> classMap, 
			String kgClassName,
			String kgClassName2,
			HashMap<String, HashSet<Pair<String, String>>> pairMap, 
			Path sameAsPairsPath) {
		
		Path fileName = Paths.get(sameAsPairsPath + "/" + kgClassName + "_" + kgClassName2 + ".tsv");
		try {
			BufferedWriter out = Files.newBufferedWriter(fileName, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			String key = kgClassName + "_" + kgClassName2;
			if (pairMap.get(key) != null) {
				for (Pair<String, String> p : pairMap.get(key)) {
					String s = p.getLeft() + "\t" + p.getRight() + "\n";	
					out.write(s);
				}
				out.close();
				System.out.println(pairMap.get(key).size() + " line(s) written to " + fileName.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Split line, check for instances, count (and save) pairs
	 * @param line
	 * @param kg
	 * @param cM
	 * @param className
	 * @param instances
	 * @param countMap
	 * @param pairMap
	 */
	private static void checkAndCountLinks(String line, 
			String fromKG,
			String toKG,
			ClassMapping cM, 
			String className, 
			HashMap<String, HashSet<String>> leftInstances,
			HashMap<String, HashSet<String>> rightInstances,
			HashMap<String, Integer> countMap, 
			HashMap<String, HashSet<Pair<String, String>>> pairMap) {
		//split file on whitespace
		String[] words = line.split("\\s+");
			HashMap<String, ArrayList<String>> classMap = cM.getClassMap(className);
			//for all kgClassNames
			if (classMap.containsKey(fromKG) && classMap.containsKey(toKG)) {
				for (String kgClassName : classMap.get(fromKG)) {
					for (String kgClassName2 : classMap.get(toKG)) {
						String key = kgClassName + "_" + kgClassName2;
						//check if s (word[0] is contained in the leftInstances or o (word[2]) is contained in rightInstances
						if (words[0] != null && words[2] != null) {
							//special case for OpenCyc (full file has to be used)
							//if (!kg.equals("o") || 
							//		(words[1] != null && words[1].equals("<http://www.w3.org/2002/07/owl#sameAs>") &&
							//		words[2] != null && words[2].contains("<http://dbpedia.org/resource/"))
							//		) {
								if(leftInstances.containsKey(kgClassName) && rightInstances.containsKey(kgClassName2)) {
									if(leftInstances.get(kgClassName).contains(words[0]) || 
											rightInstances.get(kgClassName2).contains(words[2])) {
										//System.out.println(words[0]);
										countMap.put(key, countMap.get(key) + 1);
										
										//saveSameAsPairs if true
										if (saveSameAsPairs) {
											ImmutablePair<String, String> sameAsPair = new ImmutablePair<String, String>(words[0], words[2]);
											if(pairMap.containsKey(key)) {
												pairMap.get(key).add(sameAsPair);
											}else{
											HashSet<Pair<String, String>> sameAsPairSet = new HashSet<Pair<String, String>>();
											sameAsPairSet.add(sameAsPair);
												pairMap.put(key, sameAsPairSet);
											}
												
										}
									}
								}
								//check if p is owl:sameAs
							/*	if (words[1] != null) {
									if (!words[1].equals("<http://www.w3.org/2002/07/owl#sameAs>")) {
										break;
									}
								}*/
							//} //OpenCyc case
							
							
						}//end if (words[0] != null)
					}//end for (String kgClassName2 : classMap.get(toKG)) {
				}//end for (String kgClassName : classMap.get(fromKG))
			}//end if (classMap.containsKey(kg))
			
		//}
	}

	/**
	 * Split line on tab and add first element (instance uri) to HashMap
	 * @param line
	 * @param kgClassName 
	 * @param dInstances 
	 */
	private static void getInstance(String line, String kgClassName, HashMap<String, HashSet<String>> dInstances) {
		String[] words = line.split("\\t");
		dInstances.get(kgClassName).add(words[0]);
	}

	
}
