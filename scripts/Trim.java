

import java.util.*;
import java.io.*;

public class Trim {

	public static void main(String[] args) throws FileNotFoundException{
		
		Scanner sc = new Scanner(new File("tagged_descriptions.txt"));
		int num_classes = 0;

		while (sc.hasNext()){
			String token = sc.next();

			if (token.charAt(0) == '<'){
				System.out.println(token);
				if (token.trim().equals("<INSTRUCTOR>")){
					sc.nextLine();
					String instrs = sc.nextLine();
					String[] instr_array = instrs.split(",");
					for (String inst : instr_array){
						System.out.println(inst.trim().toLowerCase());
					}
				}
				else if (token.trim().equals("<DEPARTMENT>") || token.trim().equals("<NUMBER>")){
					sc.nextLine();
					System.out.println(sc.nextLine().trim());
				}
				else if(token.trim().equals("<CLASS>")){
					num_classes++;
					System.out.println(num_classes);
				}
			}
			else {
				String mystr = modified(token);
				if (mystr.length() > 2) {
					System.out.println(mystr.toLowerCase());
				}
			}	
		}
	}

	public static String modified(final String input){
		final StringBuilder builder = new StringBuilder();
		for(final char c : input.toCharArray())
			if(Character.isLetterOrDigit(c))
				builder.append(Character.isLowerCase(c) ? c : Character.toLowerCase(c));
		 return builder.toString();
	}
}
