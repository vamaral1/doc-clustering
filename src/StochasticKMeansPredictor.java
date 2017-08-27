package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class StochasticKMeansPredictor extends Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	int[] feature_indexes;
	int iterations;
	int num_clusters;
	ArrayList<FeatureVector> mu;
	int[] r;
	String sim;

	public StochasticKMeansPredictor(int iterations, int num_clusters, String sim) {
		this.iterations = iterations;
		this.num_clusters = num_clusters;
		this.sim = sim;
	}

	public void train(List<Instance> instances){

		mu = new ArrayList<FeatureVector>();
		r = new int[instances.size()];
		for (int i = 0; i < r.length; i++){
			r[i] = -1;
		}

		// initialize the first k clusters
		for (int k = 0; k < num_clusters; k++){
			mu.add(instances.get(k).getFeatureVector());
			r[k] = k;
		}
		
//		System.out.println("Leggo. Iterations = " + iterations + " num_clusters = " + num_clusters);
//		System.out.println("Iteration number: ");
		train(instances, iterations);

		System.out.println();
		int mycluster = r[2];
		for (int i = 0; i < r.length; i++){
			if (r[i] == mycluster ){
				System.out.print( (i+1) + " " );
			}
		}
		System.out.println();
/*
		int[] numper = new int[num_clusters];
		System.out.println("Assignments: ");
		for (int i = 0; i < r.length; i++){
			if (r[i] < numper.length){
				numper[r[i]] += 1;
			}
		}
		System.out.println("\n\nCluster sizes:");
		for (int i = 0; i < numper.length; i++){
			System.out.println("Cluster " + i + ":  \t" + numper[i]);
		}
*/		
		ArrayList<Integer> relevant_docs = new ArrayList<Integer>();
		for (int i = 0; i < r.length; i++){
			if (r[i] == mycluster ){
				relevant_docs.add( i+1);
			}
		}
		int[] correct = {1067, 32, 34, 35, 4, 1, 65, 26, 45, 50, 12, 24, 1439, 38, 20, 15, 5, 29, 49, 13, 223, 16, 28, 84, 33};
		double num_correct = 0.0;
		for (int i = 0; i < correct.length; i++){
			if (relevant_docs.contains(correct[i])){
				num_correct++;
			}
		}
		double recall = num_correct/25;
		double prec = num_correct/relevant_docs.size();
		System.out.println( recall + "\t" + prec);

		
	}

	private void train(List<Instance> instances, int iters) {
		System.out.print(iters + " ");
		if (iters == 0) {
			return;
		}
		iters = iters - 1;

		// E STEP
		
		for (int n = 0; n < instances.size(); n++) { 
			Instance i = instances.get(n);
			
			//find min j
			double min = Double.POSITIVE_INFINITY;
			int min_j = 0;
			for (int j = 0; j < mu.size(); j++){
				double k = 0.0; 
				if (sim.equals("cosine")) {
					k = cosine_sim(i.getFeatureVector(), mu.get(j));
				}
				else {
					k = sum_diffs(i.getFeatureVector(), mu.get(j));
				}
				if (k < min){
					min = k;
					min_j = j;
				}
			}
			// update r_nk

			ArrayList<Integer> temp_assignments = new ArrayList<Integer>();
			if (r[n] == min_j || r[n] == -1){
				temp_assignments.add(min_j);
				r[n] = min_j;
			}
			else {
				int old_assignment = r[n];
				int new_assignment = min_j;
				r[n] = new_assignment;

			// update mu(newk) and mu(oldk)
				temp_assignments.add(old_assignment);
				temp_assignments.add(new_assignment);
			}

			for (int k : temp_assignments){
				double cluster_size = 0; 
				//sum all the vectors with this assignment
				FeatureVector vec_sum = new FeatureVector();
				for (int num = 0; num < instances.size(); num++){
					if (r[num] == k){
						cluster_size++;
						for (String feature : instances.get(num).getFeatureVector().keySet() ){
							vec_sum.add(feature, vec_sum.get(feature) + instances.get(num).getFeatureVector().get(feature) ); 
						}
					}
				}
				//get the mean
				for (String feature : vec_sum.keySet()){
					vec_sum.add(feature, vec_sum.get(feature)/cluster_size);
				}
				// update mu
				mu.set(k, vec_sum);
			
			}
		}

		train(instances, iters);
	}
	
	public Label predict(Instance instance){

		double min = Double.POSITIVE_INFINITY;
		int min_j = 0;
		for (int j = 0; j < mu.size(); j++){
			double k = sum_squares(instance.getFeatureVector(), mu.get(j));
			if (k < min){
				min = k;
				min_j = j;
			}
		}

		return new ClassificationLabel(min_j);
	}

	private double sum_diffs(FeatureVector x, FeatureVector y){
		double sum = 0;
		for ( String i : x.keySet() ){
			sum += Math.pow(x.get(i) - y.get(i), 2);
		}
		return sum;
	}

	private double sum_squares(FeatureVector x, FeatureVector y){
		double sum = 0;
		for ( String i : x.keySet() ){
			sum += x.get(i) * y.get(i);
		}
		return sum;
	}


	private double cosine_sim(FeatureVector x, FeatureVector y) {
		return sum_squares(x, y) /( Math.sqrt(sum_squares(x, x)) * Math.sqrt(sum_squares(y, y)  ));
	}

	private void print_vec(FeatureVector vector) {
	
		for (String k : vector.keySet()){
			System.out.println("key: " + k + ", value: " + vector.get(k) );
		}
	}

}
