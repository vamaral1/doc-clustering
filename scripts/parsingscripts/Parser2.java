

import java.io.*;
import java.util.*;

/*
 * DEPARTMENT
 * NUMBER
 * NAME
 * DESCRIPTION
 * INSTRUCTOR
 * PREREQ/COREQ
 * */

public class Parser2 {
	
	public static void main(String[] args) throws FileNotFoundException{ 
		
		Scanner in = new Scanner(new File("allcoursestrim.txt"));

		int num_courses = 0;
		String ss = ".";
		while(in.hasNextLine()){
			String line = in.nextLine();
			if (line.contains("<a name=")){
				System.out.println("<CLASS>");
				String classnumber = line.split("\"")[1];
				if (classnumber.contains(".")){
					System.out.println("<DEPARTMENT>");
					System.out.println(classnumber.split("\\.")[0]);
					System.out.println("<NUMBER>");
					System.out.println(classnumber.split("\\.")[1]);
				}
				else {
					System.out.println("<NUMBER>");
					System.out.println(classnumber);

				}
			}
			else if (line.toLowerCase().contains("href")){
				continue;
			}
			else if(line.contains("<h3>")) {
				System.out.println("<NAME>");
				System.out.println(line.substring(line.indexOf(" ")+1));
			}
			else if(line.contains("<I>")) {
				line = line.substring(line.indexOf("<I>") + 3);
				System.out.println("<INSTRUCTOR>");
				System.out.println(line.split("</I>")[0]);
			}
			else if(line.split(" ")[0].toLowerCase().contains("prereq")){
				System.out.println("<PREREQUISITE>");
				System.out.println(line.substring(12));
			}
			else if(line.split(" ")[0].toLowerCase().contains("coreq")){
				System.out.println("<COREQUISITE>");
				System.out.println(line.substring(8));
			}
			else if (line.contains("</p><!--end-->")){
				System.out.println();
				num_courses++;
			}
			else if(line.length() > 20 && !line.contains("<b>Lecture:</b>")){
				System.out.println("<DESCRIPTION>");
				System.out.println(line.substring(4));
			}
		}
		System.out.println("NUM_COURSES: " + num_courses);

	}

}
