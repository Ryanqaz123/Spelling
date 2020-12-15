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
     * @deprecated use User.load(name) instead
     * @param name
     */
    @Deprecated
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
     * Initialize a user with the given information and append the user to the users file
     * @deprecated use User.create(name, level) instead
     * @param age
     * @param name
     * @param level
     */
    @Deprecated
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
     * Initialize a User with the given information<br>
     * <b>Warning: does not check whether or not the user exists, does not read or write from users file</b><br>
     * Use {@code User.load(name)} or{@code  User.create(name, level)} to create existing/new user objects
     * @param name
     * @param level
     */
    public User(String name, int level) {
    	userName = name;
    	userLevel = level;
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
    public static int levelOf(String name) {
    	File userFile = new File(userFileName);
    	try (Scanner sc = new Scanner(userFile)){
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
    	catch (FileNotFoundException ex) {
    		return -1;
    	}
    	return -1;
    }
    
    /**
     * returns a User object corresponding to the given name, with the level of that user<br>
     * returns null if there is no current User with that name, or cannot read from file<br>
     * {@code getLevel()} of the user is 0 if the level cannot be found
     * @param name
     * @return
     */
    public static User load(String name) {
    	int level = levelOf(name);
    	if (level == -1) {
    		return null;
    	}
    	return new User(name, level);
    }
    
    /**
     * returns a User object corresponding to the given name<br>
     * writes the new user to users file<br>
     * returns null if the user already exists
     * @param name
     * @param level
     * @return
     */
    public static User create(String name, int level, boolean checkExist) {
    	if (checkExist) {
    		int existingLevel = levelOf(name);
    		if (existingLevel != -1) {
    			return null;
    		}
    	}
    	File userFile = new File(userFileName);
        try (FileWriter fw = new FileWriter(userFile, true);
        		BufferedWriter bw = new BufferedWriter(fw);) {
        	bw.newLine();
        	bw.write(name);
        	bw.newLine();
        	bw.write(Integer.toString(level));
        }
        catch (IOException ex) {
        	
        }
        return new User(name, level);
    }

    /*
    public int getAge() {
        return userAge;
    }*/
}