package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
	static public LinkedList<Option> options = new LinkedList<Option>();
	
	public static void main(String[] args) throws IOException {
		// Parse the command line.
		String[] manditory_args = { "mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Classify.options, manditory_args);
	
		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");

		int gd_iterations = 3;
		if (CommandLineUtilities.hasArg("gd_iterations")) {
		    gd_iterations = CommandLineUtilities.getOptionValueAsInt("gd_iterations");
		}
		double gd_eta = .01;
		if (CommandLineUtilities.hasArg("gd_eta")) {
			 gd_eta = CommandLineUtilities.getOptionValueAsFloat("gd_eta");
		}
		int num_features = 4;
		if (CommandLineUtilities.hasArg("num_features_to_select")) {
		    num_features = CommandLineUtilities.getOptionValueAsInt("num_features_to_select");
		}
		double online_learning_rate = 1.0;
		if (CommandLineUtilities.hasArg("online_learning_rate")){
			online_learning_rate = CommandLineUtilities.getOptionValueAsFloat("online_learning_rate");
		}
		int online_training_iterations = 5;
		if (CommandLineUtilities.hasArg("online_training_iterations")) {
			online_training_iterations = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");
		}
		int polynomial_kernel_exponent = 2;
		if (CommandLineUtilities.hasArg("polynomial_kernel_exponent")) {
			polynomial_kernel_exponent = CommandLineUtilities.getOptionValueAsInt("polynomial_kernel_exponent");
		}
		double cluster_lambda = 0.0;
		if (CommandLineUtilities.hasArg("cluster_lambda")) {
			cluster_lambda = CommandLineUtilities.getOptionValueAsFloat("cluster_lambda");
		}

		int num_clusters = 0;
		if (CommandLineUtilities.hasArg("num_clusters")) {
			num_clusters = CommandLineUtilities.getOptionValueAsInt("num_clusters");
		}
		int clustering_training_iterations = 5;
		if (CommandLineUtilities.hasArg("clustering_training_iterations")) {
			clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");
		}
		String weighting_scheme = "none";
		if (CommandLineUtilities.hasArg("num_clusters")) {
			weighting_scheme = CommandLineUtilities.getOptionValue("weighting_scheme");
		}
		boolean stoplist = false;
		if (CommandLineUtilities.hasArg("use_stoplist")) {
			stoplist = CommandLineUtilities.getOptionValue("use_stoplist").equals("true");
		}
		String similarity = "standard";
		if (CommandLineUtilities.hasArg("similarity")) {
			similarity = CommandLineUtilities.getOptionValue("similarity");
		}


		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}
			// Load the training data.
			CorpusReader data_reader = new CorpusReader(data, stoplist, weighting_scheme);
			List<Instance> instances = data_reader.readData();
			data_reader.close();

		//	System.out.println("NUMBER OF INSTANCES: " + instances.size());

			// Train the model.
			Predictor predictor = train(instances, algorithm, gd_iterations, gd_eta, num_features, online_learning_rate, online_training_iterations, polynomial_kernel_exponent, cluster_lambda, num_clusters, clustering_training_iterations, similarity);
			saveObject(predictor, model_file);		
			
		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}
			
			// Load the test data.
			/*
			CorpusReader corpus_reader = new CorpusReader(data);
			List<Instance> instances = corpus_reader.readData();
			corpus_reader.close();
			
			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			evaluateAndSavePredictions(predictor, instances, predictions_file); */
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	
	/** Train the classifer
	 *
	 * @param instances a List of instances to pass to the classifer
	 * @param algorithm a string to indicate which algorithm to use ('majority' or 'even_odd')
	 * */
	private static Predictor train(List<Instance> instances, String algorithm, int gd_iterations, double gd_eta, int num_features, double online_learning_rate, int online_training_iterations, int polynomial_kernel_exponent, double cluster_lambda, int num_clusters, int clustering_training_iterations, String similarity) {
		// TODO Train the model using "algorithm" on "data" DONE
		
		//System.out.println(algorithm);


		Predictor p;
/*		if (algorithm.equalsIgnoreCase("lambda_means")) {
			p = new LambdaMeansPredictor(clustering_training_iterations, cluster_lambda);
		}
		else if (algorithm.equalsIgnoreCase("ska")) {
			p = new StochasticKMeansPredictor(clustering_training_iterations, num_clusters);
		}
		else {
			p = new LambdaMeansPredictor(clustering_training_iterations, cluster_lambda);
		}
*/
		p = new StochasticKMeansPredictor(clustering_training_iterations, num_clusters, "cosine_sim");
	//	p = new LambdaMeansPredictor(clustering_training_iterations, cluster_lambda);
	//	p = new BisectingKMeansPredictor(clustering_training_iterations, similarity);
	
	//	p = new Similarity("3", "cosine_sim");
	
	
		p.train(instances); 

	
		
		return p; 
	}

	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {

		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// TODO Evaluate the model if labels are available. DONE

		

		double accuracy = 0;
		
		if (!instances.isEmpty()){
			if (instances.get(0).getLabel() != null){
				Evaluator e = new AccuracyEvaluator();
				accuracy = e.evaluate(instances, predictor);

			}
		}

		System.out.println("Accuracy: " + accuracy);
		
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			writer.writePrediction(label);
		}
		
		writer.close();
		
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
				new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}
	
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		Classify.options.add(option);		
	}
	
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		registerOption("gd_eta", "int", true, "The step size parameter for GD.");
		registerOption("gd_iterations", "int", true, "The number of GD iterations.");
		registerOption("num_features_to_select", "int", true, "The number of features to select.");
		registerOption("online_learning_rate", "double", true, "The learning rate for perceptron.");
		registerOption("online_training_iterations", "int", true, "The number of training iterations for online methods.");
		registerOption("polynomial_kernel_exponent", "int", true, "The exponent of the polynomial kernel.");
		registerOption("cluster_lambda", "double", true, "The value of lambda in lambda-means.");
		registerOption("clustering_training_iterations", "int", true, "The number of clustering iterations.");
		registerOption("num_clusters", "int", true, "The number of clusters.");
		registerOption("weighting_scheme", "String", true, "The weighting scheme ('none' or 'tf-idf')");
		registerOption("use_stoplist", "String", true, "True if you want to use a stoplist");
		registerOption("simimilarity", "String", true, "Use 'cosine' for cosine similarity");

		
		// Other options will be added here.
	}
}
