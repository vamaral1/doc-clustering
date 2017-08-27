

import java.io.*;
import java.util.*;

public class Parser1 {
	
	public static void main(String[] args) throws FileNotFoundException{ 
		
		Scanner in = new Scanner(new File("allcourses.txt"));

		int num_courses = 0;
		boolean printing = false;
		while(in.hasNextLine()){
			String line = in.nextLine();
			if (line.contains("<a name=")){
				printing = true;
				System.out.println(line.split("\"")[1]);
			}
			if (printing && !line.contains("img")) { 
				//if (line.contains("<I>")){
					//System.out.println(line.substring(line.indexOf("<I>") + 3, line.length() - 8));
				//}
				//else {
					System.out.println(line); 
				//}
			}
			if (line.contains("</p><!--end-->")){
				System.out.println();
				printing = false;
				num_courses++;
			}
		}
		System.out.println("NUM_COURSES: " + num_courses);

	}

}
