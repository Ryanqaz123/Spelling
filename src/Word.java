/**
 * Represents a single word, handles checking of spelling
 */
public class Word {

    private String wordIdent;
    private String[] wordParts;
    
    /**
     * Constructs a word with the given spelling.  The spelling is converted to lower case.
     * @param word
     */
    public Word(String word){
        wordIdent = word.toLowerCase();
    }
    
    /**
     * @return the word's spelling
     */
    public String getWord() {
        return wordIdent;
    }
    
    /**
     * Checks the spelling of the given spelling against the spelling of the word
     * @param spelling
     * @return [first section before the first letter out of place, 
     * middle section between first and last letters out of place, 
     * last section starting after last letter out of place]
     */
    public String[] checkSpelling(String spelling) {
        String front = "";
        String end = "";
        int i = 0;
        boolean correct = true;
        String spacer = "";
        while (correct && (i < wordIdent.length()) && (i < spelling.length())) {//finds correct letters from the front
            if (spelling.charAt(i) == wordIdent.charAt(i)) {
                front = front + spelling.substring(i,i+1);
            }
            else {
                correct = false;    
            }
            i++;
        }        
        i = Math.min(spelling.length(), wordIdent.length());
        int j = i;
        correct = true;
        while (correct && (i > front.length())) { //finds the correct letters from the back end
            if (spelling.charAt(spelling.length()-j+i-1) == wordIdent.charAt(wordIdent.length()-j+i-1)) {
                end = spelling.substring(spelling.length()-j+i-1,spelling.length()-j+i) + end;
            }
            else {
                correct = false;    
            }
            i--;
        }
        
        spacer = spelling.substring(front.length(), Math.max(front.length(), spelling.length() - end.length()));
        if (spacer.isEmpty() && wordIdent.length() != front.length() + end.length()) {//indicate whether letters are missing
            spacer = "*";
        }
        wordParts = new String[]{front,spacer,end};
        return wordParts;
    }

    public String head() {
        return wordParts[0];
    }
    
    public String tail() {
        return wordParts[2];
    }
    
    public String incorrect() {
        return wordParts[1];
    }
    
    /**
     * @return whether the given spelling is equal to the word's spelling
     * @param spelling
     */
    public boolean isCorrect(String spelling) {
    	return wordIdent.equals(spelling);
    }
    
    /**
     * @return the word's spelling
     */
    @Override
	public String toString() {
		// pretty much an exact copy of getWord()
		return wordIdent;
	}
    
}