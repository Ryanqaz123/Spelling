import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class User {
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
    	
    	try (Scanner sc = new Scanner("users.txt")){
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
    	}
    	createTempUser();
    }

    public User(int age, String name, int level) {
        //File file = new File("users.txt");
        userName = name;
        userLevel = level;
        //userAge = age;
        try (FileWriter fw = new FileWriter("users.txt", true);
        		BufferedWriter bw = new BufferedWriter(fw);) {
        	bw.write(name);
        	bw.newLine();
        	bw.write(Integer.toString(level));
        	bw.newLine();
        }
        catch (IOException ex) {
        	
        }
        createTempUser();
    }
    
    private void createTempUser() {
    	File tempUser = new File("temp_" + userName + ".txt");
    	
    }

    public String getName() {
        return userName;
    }

    public int getLevel() {
        return userLevel;
    }
    
    public static boolean userExists(String name) {
    	try (Scanner sc = new Scanner("users.txt")){
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