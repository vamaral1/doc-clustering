package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class BisectingKMeansPredictor extends Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	int[] feature_indexes;
	int iterations;
	int[] r;
	String sim;
	Random rand;

	public BisectingKMeansPredictor(int iterations, String sim) {
		this.iterations = iterations;
		this.sim = sim;
		this.rand = new Random();
	}

	public void train(List<Instance> instances){

		// every instance is assigned to a cluster
		r = new int[instances.size()];
		for (int i = 0; i < r.length; i++){
			r[i] = 0;
		}

		//System.out.println("Leggo. Iterations = " + iterations);
		train(instances, 0, 0);

		int[] numper = new int[iterations];
	//	System.out.println("Assignments: ");
		for (int i = 0; i < r.length; i++){
			//System.out.println(r[i]);
			if (r[i] < numper.length){
				numper[r[i]] += 1;
			}
		}
	/*	System.out.println("\n\nCluster sizes:");
		for (int i = 0; i < numper.length; i++){
			System.out.println("Cluster " + i + ":  \t" + numper[i]);
		}
*/
		int mycluster = r[2];
		ArrayList<Integer> relevant_docs = new ArrayList<Integer>();
		for (int i = 0; i < r.length; i++){
			if (r[i] == mycluster ){
				relevant_docs.add( i+1);
			}
		}
		int[] correct = {1067, 32, 34, 35, 4, 1, 65, 26, 45, 50, 12, 24, 1439, 38, 20, 15, 5, 29, 49, 13, 223, 16, 28, 84, 33, 80, 69, 56, 73, 14, 31, 40, 86, 62, 61, 1595, 79};
		double num_correct = 0.0;
		for (int i = 0; i < correct.length; i++){
			if (relevant_docs.contains(correct[i])){
				num_correct++;
			}
		}
		double recall = num_correct/correct.length;
		double prec = num_correct/relevant_docs.size();
		System.out.println( recall + "\t" + prec);

		
	}

	private void train(List<Instance> instances, int iters, int cluster_num) {
//		System.out.print(iters + " ");
		if (iters == iterations) {
			return;
		}
		iters++;
		// NOW, ITERS EQUALS NUM_CLUSTERS

		double cluster_size = 0.0;
		FeatureVector mean = new FeatureVector();
		ArrayList<Integer> relevant_indexes = new ArrayList<Integer>();

		// Compute the cluster mean
		for ( int i = 0; i < instances.size(); i++){
			if (r[i] == cluster_num){
				cluster_size ++;
				relevant_indexes.add(i);
				for ( String key : instances.get(i).getFeatureVector().keySet() ){
					mean.add( key, mean.get(key) + instances.get(i).getFeatureVector().get(key) );
				}
			}
		}

//	System.out.println("MEAN SIZE: " + mean.size());
		for (String key : mean.keySet()){
			mean.add(key, mean.get(key) / cluster_size);
		}

		// pick a random point in the cluster, ra. compute rb

		FeatureVector ra = new FeatureVector();//instances.get( rand.nextInt(relevant_indexes.size()) ).getFeatureVector();
		for (String key : mean.keySet()){
			ra.add(key, rand.nextDouble() - 0.5);
		}	
		FeatureVector rb = new FeatureVector();

		for (String key : mean.keySet()){
			rb.add(key, mean.get(key) - (ra.get(key) - mean.get(key) ) );
		}
// System.out.println("sizes: " + ra.size() + " " + rb.size());


		// separate cluster into two
		for ( int i = 0; i < instances.size(); i++){
			if ( r[i] != cluster_num) continue;
			if ( normofdiff(instances.get(i).getFeatureVector(), ra) <= normofdiff(instances.get(i).getFeatureVector(), rb) ){
				r[i] = iters;
			}
		}
		
		//lastly, go through and find the largest cluster and recurse!

		int[] cluster_reps = new int[iters+1];

		for (int i = 0; i < r.length; i++){
			cluster_reps[r[i]] ++;
		}
		int max = -1;
		int cluster_toret = 0;
		for (int i = 0; i < cluster_reps.length; i++){
			if (cluster_reps[i] > max){
				max = cluster_reps[i];
				cluster_toret = i;
			}
		}
// System.out.println("We chose cluster " + cluster_toret);
// System.out.println("cluster size:    " + max);

		train(instances, iters, cluster_toret);
	}
	
	public Label predict(Instance instance){

		int min_j = 0;

		return new ClassificationLabel(min_j);
	}

	private double normofdiff(FeatureVector x, FeatureVector y){
		double sum = 0.0;
		for (String key : x.keySet() ){
			sum += (x.get(key) - y.get(key)) * (x.get(key) - y.get(key));
		}
		return Math.sqrt(sum);
	}

	private void print_vec(FeatureVector vector) {
	
		for (String k : vector.keySet()){
			System.out.println("key: " + k + ", value: " + vector.get(k) );
		}
	}

}
