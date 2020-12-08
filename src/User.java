import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class User {
    private String userName = "";
    private int userLevel = 0;
    //private int userAge = 0;

    public User(String name) {//Loads a user's file based on name
        List<String> result = null;
        try {
            result = Files.readAllLines(Paths.get(name+".txt"), 
                    Charset.defaultCharset());
        } catch (Exception e) {
            System.out.println("Couldn't read file " + name+".txt");
            e.printStackTrace();
        }
    }

    public User(int age, String name, int level) {
        File file = new File(name + ".txt");
        userName = name;
        userLevel = level;
        //userAge = age;
    }

    public String getName() {
        return userName;
    }

    public int getLevel() {
        return userLevel;
    }

    /*
    public int getAge() {
        return userAge;
    }*/
}