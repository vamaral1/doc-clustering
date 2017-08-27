package cs475;

import java.io.*;
import java.util.*;

public class CorpusReader {

	private Scanner sc;
	private Scanner stopl_scanner;
	private boolean use_stoplist;
	private HashMap<String, Integer> stoplist;
	private HashMap<String, Double> freq;
	private String weighting_scheme;
	private double DEPT_WEIGHT = 1.0;
	private double NUM_WEIGHT = 1.0;
	private double NAME_WEIGHT = 2.0;
	private double PREREQ_WEIGHT = 0.5;
	private double DESC_WEIGHT = 1.0;
	private double INSTR_WEIGHT = 3.0;

	public CorpusReader(String filename, boolean use_stoplist, String weighting_scheme ) throws FileNotFoundException {
		this.use_stoplist = use_stoplist;
		this.weighting_scheme = weighting_scheme;
		if (use_stoplist) {
			this.sc = new Scanner(new File("data/stoplist.txt"));
			stoplist = new HashMap<String, Integer>();
			while (sc.hasNext() ){
				stoplist.put(sc.next().trim(), 1);
			}
		}
		if (weighting_scheme.equals("tf-idf")) {
			this.sc = new Scanner(new File("data/freq.txt"));
			freq = new HashMap<String, Double>();
			while (sc.hasNext() ){
				String key = sc.next().trim();
				double val = sc.nextDouble();
				freq.put(key, val);
			}
		}
		this.sc = new Scanner(new BufferedInputStream(new FileInputStream(filename)));
	}
	
	public void close() {
		this.sc.close();
	}
	
	public List<Instance> readData() {
		ArrayList<Instance> instances = new ArrayList<Instance>();
		Instance curr = null;

		double curr_weight = 0.0;
		
		while (this.sc.hasNextLine()) {
			String line = sc.nextLine().trim();
			if (line.length() == 0){
				continue;
			}
			if (line.charAt(0) == '<'){
				if (line.equals("<CLASS>")){
					if (curr != null){
						instances.add(curr);
					}
					String id = sc.nextLine().trim();
					curr = new Instance(new FeatureVector(), null, id);
				}
				else {
					switch (line) {

						case "<DEPARTMENT>" : curr_weight = DEPT_WEIGHT;
						case "<NUMBER>" : curr_weight = NUM_WEIGHT;
						case "<NAME>" : curr_weight = NAME_WEIGHT;
						case "<PREREQUISITE>" : curr_weight = PREREQ_WEIGHT;
						case "<DESCRIPTION>" : curr_weight = DESC_WEIGHT;
						case "<INSTRUCTOR>" : curr_weight = INSTR_WEIGHT;
							
					}
				}

			}
			else {
				double use_weight = curr_weight;
				if (weighting_scheme.equals("tf-idf") && freq.containsKey(line) ){
					use_weight = use_weight/freq.get(line);
				}
				if (use_stoplist && !stoplist.containsKey(line)){ 
					curr.getFeatureVector().add(line, curr.getFeatureVector().get(line) + use_weight);
				}
				else if (!use_stoplist){
					curr.getFeatureVector().add(line, curr.getFeatureVector().get(line) + use_weight);
				}
			}

		}		
		instances.add(curr);
		
		return instances;
	}

}

