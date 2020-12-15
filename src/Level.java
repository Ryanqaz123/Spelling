import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Structure for a single level of words, and the list of new words during a game
 */
public class Level {
    private int level;
    private ArrayList<Word> words = new ArrayList<>();
    private ArrayList<Word> newWords = new ArrayList<>();
    
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
     * @return the level's new words
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
     * @return a random word from the word list
     */
    public Word getRandWord() {
    	return words.get((int)(Math.random() * words.size()));
    }

    /**
     * @return a random word from the new words list
     */
    public Word getRandNewWord() {
    	return newWords.get((int)(Math.random() * newWords.size()));
    }

    /**
     * @return a random new word if there is one, otherwise get a random word
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
    
}