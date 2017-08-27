package cs475;

import java.util.List;

public class AccuracyEvaluator extends Evaluator {

	public AccuracyEvaluator(){

	}

	public double evaluate(List<Instance> instances, Predictor predictor){
		double successes = 0.0;
		for ( Instance inst : instances ){
			if (predictor.predict(inst).equals(inst.getLabel())){
				successes++;
			}
		}
		return successes/((double)instances.size());
	}
}
