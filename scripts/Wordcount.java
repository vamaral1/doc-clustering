

import java.util.*;
import java.io.*;

public class Wordcount {

	public static void main(String[] args) throws FileNotFoundException{
		
		Scanner sc = new Scanner(new File("tagged_and_trimmed.txt"));
		HashMap<String, Integer> freq = new HashMap<String, Integer>();

		int limit = 100;
		if (args.length > 0){
			limit = Integer.parseInt(args[0]);
		}

		while (sc.hasNext()){
			String token = sc.next();
			token = token.trim();

			if (token.charAt(0) == '<'){
				continue;
			}
			else {
				if (freq.containsKey(token) ){
					freq.put(token, freq.get(token) + 1);
				}
				else {
					freq.put(token, 1);
				}
			}	
		}

	//	int toprint = 1000;
		for (String key : freq.keySet()){
			if (key.length() <= 2){
				continue;
			}
			if (freq.get(key) > limit) {
				System.out.println(key.toLowerCase() + "\t" + freq.get(key));
			}
	/*		toprint--;
			if (toprint ==0 ){
				break;
			} 
	*/
		}
	}

}
