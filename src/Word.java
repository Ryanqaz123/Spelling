public class Word {

    private String wordIdent;
    private String[] wordParts;

    public Word(String word){
        wordIdent = word.toLowerCase();
    }

    public String getWord() {
        return wordIdent;
    }

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
        for (int x = 0; x < (spelling.length() - front.length() - end.length()); x++) {//creates a spacer
            spacer = spacer + "_";
        }
        if (spacer.isEmpty() && wordIdent.length() != front.length() + end.length()) {//indicate whether letters are missing
            spacer = "_";
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
        String incorrect = wordParts[0] + wordParts[1] + wordParts[2];
        return incorrect;
    }
    
    @Override
	public String toString() {
		// pretty much an exact copy of getWord()
		return wordIdent;
	}
    
}