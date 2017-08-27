

import java.util.*;
import java.io.*;

public class Classcount {

	public static void main(String[] args) throws FileNotFoundException{
		
		Scanner sc = new Scanner(new File("tagged_and_trimmed.txt"));
		HashMap<String, Integer> freq = new HashMap<String, Integer>();

		int id = 1;
		while (sc.hasNextLine()){
			String line = sc.nextLine();
			if (line.trim().equals("<CLASS>")) {
				System.out.println(line.trim() + "\n" + id);
				id++;
			}
			else {
				System.out.println(line);
			}

		}
	}

}
