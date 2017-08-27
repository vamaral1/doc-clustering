package cs475;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {

	private int label;

	public ClassificationLabel(int label) {
		// TODO Auto-generated constructor stub DONE
		this.label = label;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub DONE
		return label + "";
	}

	public int getLabel(){
		return this.label;
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof ClassificationLabel){
			return this.toString().equals(o.toString());
		}
		return false;
	}

}
