//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
import java.io.*;
import java.util.Scanner;

public class User {
	private static String userFileName = "users.txt";
    private String userName = "";
    private int userLevel = 0;
    //private int userAge = 0;

    /**
     * Find the user associated with the given name and load that user's information
     * @param name
     */
    public User(String name) {//Loads a user's file based on name
    	/*
        List<String> result = null;
        try {
            result = Files.readAllLines(Paths.get(name+".txt"), 
                    Charset.defaultCharset());
        } catch (Exception e) {
            System.out.println("Couldn't read file " + name+".txt");
            e.printStackTrace();
        }*/
    	File userFile = new File(userFileName);
    	try (Scanner sc = new Scanner(userFile)){
    		while (sc.hasNextLine()) {
    			String nextLine = sc.nextLine();
    			if (nextLine.equals(name)) {
    				userName = name;
    				userLevel = Integer.parseInt(sc.nextLine());
    			}
    			else {
    				sc.nextLine();
    			}
    		}
    	} catch (FileNotFoundException e) {
    		
		}
    }
    
    /**
     * Only intended for use after running User.userLevel(name) and receiving a positive integer<br>
     * Initialize an existing user with the given information<br>
     * <b>Warning: does not check that the user exists, does not read or write from users file</b>
     * @param name
     * @param level
     */
    public User(String name, int level) {
    	userName = name;
    	userLevel = level;
    }

    /**
     * Initialize a user with the given information and append the user to the users file
     * @param age
     * @param name
     * @param level
     */
    public User(int age, String name, int level) {
        File userFile = new File(userFileName);
        userName = name;
        userLevel = level;
        //userAge = age;
        try (FileWriter fw = new FileWriter(userFile, true);
        		BufferedWriter bw = new BufferedWriter(fw);) {
        	bw.newLine();
        	bw.write(name);
        	bw.newLine();
        	bw.write(Integer.toString(level));
        }
        catch (IOException ex) {
        	
        }
    }
    
    /**
     * Set the user's level
     * @param level
     */
    public void setLevel(int level) {
    	userLevel = level;
    }
    
    /**
     * write the user in the temp user file to the main file
     */
    public void writeLevel() {
    	File userFile = new File(userFileName);
    	File tempUserFile;
    	try {
			tempUserFile = File.createTempFile("temp_users", ".txt");
		} catch (IOException e) {
			tempUserFile = null;
		}
    	try (Scanner sc = new Scanner(userFile);
    			PrintWriter pwt = new PrintWriter(tempUserFile);) {
    		while (sc.hasNextLine()) {
    			String nextLine = sc.nextLine();
    			pwt.println(nextLine);
    			if (sc.hasNextLine()) {
    				if (nextLine.equals(userName)) {
    					sc.nextLine();
    					pwt.println(Integer.toString(userLevel));
    				}
    				else {
    					pwt.println(sc.nextLine());
    				}
    			}
    			
    		}
    	} catch (FileNotFoundException e) {
    		
		}
    	try (Scanner sct = new Scanner(tempUserFile);
    			PrintWriter pw = new PrintWriter(userFile);) {
    		boolean hasNextLine = sct.hasNextLine();
    		while (hasNextLine) {
    			pw.print(sct.nextLine());
    			hasNextLine = sct.hasNextLine();
    			if (hasNextLine) {
    				pw.println();
    			}
    		}
    		
    	} catch (FileNotFoundException e) {
    		
		}
    	if (tempUserFile != null) {
    		tempUserFile.deleteOnExit();
    	}
    }
 
    /**
     * @return the user's name
     */
    public String getName() {
        return userName;
    }

    /**
     * @return the user's level
     */
    public int getLevel() {
        return userLevel;
    }
    
    /**
     * Go through the users file and look for the given name
     * If the name exists, return the user's level
     * If the name exists, but the level cannot be found, return 0
     * If the user cannot be found, return -1
     * @param name
     * @return
     */
    public static int userLevel(String name) {
    	try (Scanner sc = new Scanner(userFileName)){
    		while (sc.hasNextLine()) {
    			String nextLine = sc.nextLine();
    			if (nextLine.equals(name)) {
    				if (sc.hasNextLine()) {
    					int userLevel;
    					try {
    						userLevel = Integer.parseInt(sc.nextLine());
    					}
    					catch (NumberFormatException ex) {
    						userLevel = 0;
    					}
    					return userLevel;
    				}
    				return 0;
    			}
    			else if (sc.hasNextLine()) {
    				 sc.nextLine();
    			}
    		}
    	}
    	return -1;
    }

    /*
    public int getAge() {
        return userAge;
    }*/
}