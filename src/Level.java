import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Level {
    private int level;
    //private int numLevels;
    private ArrayList<Word> words = new ArrayList<>();
    private ArrayList<Word> newWords = new ArrayList<>();
    //private File wordsSpelledFile = new File("wordsSpelled.txt");
    //private FileWriter fw;
    //private PrintWriter pw;
    
    /**
     * Create a new Level whose level number is the given level
     * Loads all the words into a word list, also into a new word list
     * @param level
     */
    public Level(int level) {
        this.level = level;
        File file = new File("Words/" + Integer.toString(level) + ".txt");
		try (Scanner levelScanner = new Scanner(file)) {
			while (levelScanner.hasNextLine()) {
				String spelling = levelScanner.nextLine();
				Word newWord = new Word(spelling);
				words.add(newWord);
				newWords.add(newWord);
			}
		}
		catch (FileNotFoundException ex1) {
			
		}
		/*
        //level file
        if (!file.exists()) { //if a file doesnt exist
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        } else { //if file DOES exist
            try {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {//reads file line by line and adds to arrayList
                    words.add(new Word(reader.nextLine()));
                }
            } catch (Exception e) {    
            }
        }
        */
        //creates a wordsSpelled file if it doesn't exist
        /*if (!wordsSpelledFile.exists()) {
            try {
                wordsSpelledFile.createNewFile();
            } catch (IOException e) {
            }
        }*/
    }
    
    /**
     * @return level number
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * @return the level's words
     */
    public ArrayList<Word> getWords(){
        return words;
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<Word> getNewWords() {
    	return newWords;
    }
    
    /**
     * Remove the given word from the new words list
     */
    public void addToSpelled(Word word) {
    	newWords.remove(word);
    }

    /**
     * Get a random word from the word list
     * @return
     */
    public Word getRandWord() {
    	return words.get((int)(Math.random() * words.size()));
    }

    /**
     * Get a random word from the new words list
     * @return
     */
    public Word getRandNewWord() {
    	return newWords.get((int)(Math.random() * newWords.size()));
    }

    /**
     * Get a random new word if there is one, otherwise get a random word
     * @return
     */
    public Word getAWord() {
    	if (newWords.size() == 0) {
    		return getRandWord();
    	}
    	return getRandNewWord();
    }

    /**
     * Put all words in the words list back into the new words list
     */
    public void restoreNewWords() {
    	newWords = new ArrayList<>(words);
    }
    
    /*
    public void addToSpelled(String word) {
        //write to wordsSpelled file
        try {
            fw = new FileWriter(wordsSpelledFile, true);
            pw = new PrintWriter(fw);
            pw.println(word);
            pw.close();
        } catch(Exception e) {
        }
        
    }
    */
}