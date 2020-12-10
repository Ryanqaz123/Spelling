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
    
    public void setLevel(int level) {
    	userLevel = level;
    	
    }
    
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
 

    public String getName() {
        return userName;
    }

    public int getLevel() {
        return userLevel;
    }
    
    public static boolean userExists(String name) {
    	try (Scanner sc = new Scanner(userFileName)){
    		while (sc.hasNextLine()) {
    			String nextLine = sc.nextLine();
    			if (nextLine.equals(name)) {
    				return true;
    			}
    			else if (sc.hasNextLine()) {
    				 sc.nextLine();
    			}
    		}
    	}
    	return false;
    }

    /*
    public int getAge() {
        return userAge;
    }*/
}