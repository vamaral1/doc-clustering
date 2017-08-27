package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class Similarity extends Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	String sim;
	int id;

	public Similarity(String id, String sim) {
		this.id = Integer.parseInt(id);
		this.sim = sim;
	}

	public void train(List<Instance> instances){
		System.out.println("Courses most similar to " + id + ":\t");

		double[] distances = new double[instances.size()];

		FeatureVector x = instances.get(id - 1).getFeatureVector();

		for (int i = 0; i < instances.size(); i++){
			distances[i] = cosine_sim(x, instances.get(i).getFeatureVector());
		}

		distances[getmaxindex(distances)] = -1;
		for (int i =0; i < 70; i++){
			int minidx = getmaxindex(distances);
			System.out.printf("%d\t%4f\n", (minidx + 1), distances[minidx]);
			distances[minidx] = -1;
		}
		System.out.println();
	}

	public int getmaxindex(double[] arr){
		double max = 0.0;
		int index = 0;
		for (int i =0; i < arr.length; i++){
			if (arr[i] > max){
				max = arr[i];
				index = i;
			}
		}
		return index;

	}

	public Label predict(Instance i){
		return new ClassificationLabel(0);
	}

	private double sum_squares(FeatureVector x, FeatureVector y){
		double sum = 0;
		for ( String i : x.keySet() ){
			sum += x.get(i) * y.get(i);
		}
		return sum;
	}

	private double cosine_sim(FeatureVector x, FeatureVector y) {
		double cossim = sum_squares(x, y) /( Math.sqrt(sum_squares(x, x)) * Math.sqrt(sum_squares(y, y)  ));
		return cossim;
	}

	private void print_vec(FeatureVector vector) {
	
		for (String k : vector.keySet()){
			System.out.println("key: " + k + ", value: " + vector.get(k) );
		}
	}

}
