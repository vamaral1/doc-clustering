package cs475;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class FeatureVector implements Serializable {

	private HashMap<String, Double> backingMap;

	public FeatureVector(){
		this.backingMap = new HashMap<String, Double>();
	}

	public void add(String key, double value) {
		// TODO Auto-generated method stub DONE
		backingMap.put(key, value);
	}
	
	public double get(String key) {
		// TODO Auto-generated method stub DONE
		if (backingMap.containsKey(key)){
			return backingMap.get(key);
		}
		return 0.0;
	}

	public Set<String> keySet(){
		return backingMap.keySet();
	}

	public int size(){
		return backingMap.size();
	}

}
