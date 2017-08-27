package cs475;

import java.io.Serializable;

public class RegressionLabel extends Label implements Serializable {

	private double label;

	public RegressionLabel(double label) {
		// TODO Auto-generated constructor stub DONE
		this.label = label;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub DONE
		return String.format("%.3f", label);
	}

}
