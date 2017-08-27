package cs475;

import java.io.Serializable;

public class Instance implements Serializable {

	Label _label = null;
	FeatureVector _feature_vector = null;
	String _id;

	public Instance(FeatureVector feature_vector, Label label, String id) {
		this._feature_vector = feature_vector;
		this._label = label;
		this._id = id;
	}

	public Label getLabel() {
		return _label;
	}

	public String getId(){
		return _id;
	}

	public int getLabelValue() {
		if (_label.toString().equals("1")){
			return 1;
		}
		return 0;
	}

	public void setLabel(Label label) {
		this._label = label;
	}

	public FeatureVector getFeatureVector() {
		return _feature_vector;
	}

	public void setFeatureVector(FeatureVector feature_vector) {
		this._feature_vector = feature_vector;
	}
	
	
}
