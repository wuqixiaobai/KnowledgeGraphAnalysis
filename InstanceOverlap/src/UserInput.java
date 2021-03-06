import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class UserInput {
	
	/**
	 * Get classNames Array from user input
	 * @param classNames
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> getClassNames(ArrayList<String> classNames) throws IOException {
		ArrayList<String> classNamesUserInput = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean newInput = true;
		while (newInput) {
		System.out.print("Enter class name, 'all', or 'start': ");
        String userInput = br.readLine();
        	if (userInput.equals("start")) {
        		if (classNamesUserInput.size()>0) {
        			newInput = false;
        		} else {
        			System.out.println("please enter at least one valid class first.");
        			System.out.println(classNames.toString());
        		}
        	} else if (userInput.equals("all")) {
        		classNamesUserInput = classNames;
        	} else {
        		if (classNames.contains(userInput)) {
        			classNamesUserInput.add(userInput);
        		} else {
        			System.out.println("Class not found. Please enter one of the following classes");
        			System.out.println(classNames.toString());
        		}	
        	}
		}
		return classNamesUserInput;
	}

	public int getMaxBlockSize() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
		System.out.print("Enter maximum block size: ");
        String userInput = br.readLine();
		return Integer.parseInt(userInput);
	}

}
