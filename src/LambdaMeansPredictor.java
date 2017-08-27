package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class LambdaMeansPredictor extends Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	int[] feature_indexes;
	int iterations;
	double lambda;
	ArrayList<FeatureVector> mu;
	int[] r;

	public LambdaMeansPredictor(int iterations, double lambda) {
		this.iterations = iterations;
		this.lambda = lambda;
	}

	public void train(List<Instance> instances){

		// compute mu = xbar = average of all instances

		mu = new ArrayList<FeatureVector>();
		FeatureVector xbar = new FeatureVector();
		for (Instance i : instances){
			for (String feature : i.getFeatureVector().keySet() ){
				xbar.add(feature, xbar.get(feature) + i.getFeatureVector().get(feature));
			}
		}
		double num_insts = (double)instances.size();
		for (String feature : xbar.keySet() ){
			xbar.add(feature, xbar.get(feature) / num_insts);
		}

		mu.add(xbar);

		if (lambda == 0.0){
			// compute sum of (xi - xbar)^2
			for (Instance i : instances){
				lambda += sum_squares (i.getFeatureVector(), xbar);
			}
			lambda = lambda / num_insts;
		}

		r = new int[instances.size()];
		
		//System.out.println("Leggo. Iterations = " + iterations + "lambda = " + lambda);
		train(instances, iterations);
		System.out.println("Done predicting. Num Clusters: " + mu.size());

		int mycluster = r[1];
		System.out.println("mycluster: " + mycluster);
		for (int i = 0; i < r.length; i++){
			if (r[i] == mycluster ){
				System.out.println( (i+1) );
			}
		}
/*		int[] numper = new int[800];
		for (int i = 0; i < r.length; i++){
			if (r[i] < numper.length){
				numper[r[i]] += 1;
			}
		}
	*/
	/*	System.out.println("\n\nCluster sizes:");
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
		if (iters == 0) {
			return;
		}
		iters = iters - 1;
		System.out.print(iters + " ");

		// E STEP
		
		for (int n = 0; n < instances.size(); n++) { 
			Instance i = instances.get(n);
			
			//find min j
			double min = Double.POSITIVE_INFINITY;
			int min_j = 0;
			for (int j = 0; j < mu.size(); j++){
				double k = sum_squares(i.getFeatureVector(), mu.get(j));
				if (k < min){
					min = k;
					min_j = j;
				}
			}
			// update r_nk
			if (min <= lambda){
				r[n] = min_j;
			}
			else {
				r[n] = mu.size(); // note that mu.size() = num_clusters
						// also note that we are zero indexing
				mu.add(i.getFeatureVector());
			}

		}
		
		// M STEP
		ArrayList<FeatureVector> temp_mu = new ArrayList<FeatureVector>();
		for (int k = 0; k < mu.size(); k++){
			double cluster_size = 0; 
			FeatureVector vec_sum = new FeatureVector();
			for (int n = 0; n < instances.size(); n++){
				if (r[n] == k){
					cluster_size++;
					for (String feature : instances.get(n).getFeatureVector().keySet() ){
						vec_sum.add(feature, vec_sum.get(feature) + instances.get(n).getFeatureVector().get(feature) ); 
					}
				}
			}
			if (cluster_size == 0){

			}
			else {
				for (String feature : vec_sum.keySet()){
					vec_sum.add(feature, vec_sum.get(feature)/cluster_size);
				}
			}
			temp_mu.add(vec_sum);
		}
		mu = temp_mu;

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

	private double sum_squares(FeatureVector x, FeatureVector y){
		double sum = 0;
		for ( String i : x.keySet() ){
			sum += Math.pow(x.get(i) - y.get(i), 2);
		}
		return sum;
	}

	private void print_vec(FeatureVector vector) {
	
		for (String k : vector.keySet()){
			System.out.println("key: " + k + ", value: " + vector.get(k) );
		}
	}

}
