import javax.sound.sampled.*;

public class Word{
	private String wordIdent;
	private Clip sentence;
	private Clip sound;

	public Word(String word){
		wordIdent = word;
	}
	
	public Word(String word, Clip sound, Clip sentence) {
		wordIdent = word;
		this.sentence = sentence;
		this.sound = sound;
	}

	public String getWord() {
		return wordIdent;
	}

	public Clip getSentence() {
		return sentence;
	}

	public Clip getSound() {
		return sound;
	}

	public String[] checkSpelling(String spelling) {
		String front = "";
		String end = "";
		int i = 0;
		boolean correct = true;
		String spacer = "";
		while(correct && (i < wordIdent.length()) && (i < spelling.length())) {//finds correct letters from the front
			if(spelling.charAt(i) == wordIdent.charAt(i)) {
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
		//System.out.println(i);
		//System.out.println(front.length());
		while(correct && (i > front.length())) {//finds the correct letters from the back end
			//System.out.println("bababooey");
			if(spelling.charAt(spelling.length()-j+i-1) == wordIdent.charAt(wordIdent.length()-j+i-1)) {
				end = spelling.substring(spelling.length()-j+i-1,spelling.length()-j+i) + end;
			}
			else {
				correct = false;	
			}
			i--;
		}
		for(int x = 0; x < (spelling.length() - front.length() - end.length()); x++) {//creates a spacer
			spacer = spacer + "_";
		}
		if (spacer.isEmpty() && wordIdent.length() != front.length() + end.length()) {//indicate whether letters are missing
			spacer = "*";
		}
		return new String[]{front,spacer,end};
	}
	
	


	@Override
	public String toString() {
		// pretty much an exact copy of getWord()
		return wordIdent;
	}

	public static void printSpellCheck(String[] input) {
		System.out.print(input[0]);
		System.out.print(", ");
		System.out.print(input[1]);
		System.out.print(", ");
		System.out.println(input[2]);
	}

	public static void main(String[] args) {      
		Word garbage = new Word("garbage");
		printSpellCheck(garbage.checkSpelling("garbage"));
		printSpellCheck(garbage.checkSpelling("garxage"));
		printSpellCheck(garbage.checkSpelling("garbag"));
		printSpellCheck(garbage.checkSpelling("arbage"));
		Word spam = new Word("spam");
		printSpellCheck(spam.checkSpelling("spam"));
		printSpellCheck(spam.checkSpelling("sam"));
		printSpellCheck(spam.checkSpelling("am"));
		printSpellCheck(spam.checkSpelling("spa"));
		printSpellCheck(spam.checkSpelling("egg"));
		printSpellCheck(spam.checkSpelling("bacon"));
		printSpellCheck(spam.checkSpelling("spamspam"));
		printSpellCheck(spam.checkSpelling("spamegg"));
		printSpellCheck(spam.checkSpelling("sspam"));
		printSpellCheck(spam.checkSpelling("sspa"));
		printSpellCheck(spam.checkSpelling("spapam"));
		printSpellCheck(spam.checkSpelling("sausage"));
		printSpellCheck(spam.checkSpelling("spom"));
		printSpellCheck(spam.checkSpelling(""));
		Word none = new Word("");
		printSpellCheck(none.checkSpelling("spam"));
		printSpellCheck(none.checkSpelling(""));
		Word egg = new Word("egg");
		printSpellCheck(egg.checkSpelling("egg"));
		printSpellCheck(egg.checkSpelling("ggg"));
		printSpellCheck(egg.checkSpelling("g"));
		printSpellCheck(egg.checkSpelling("z"));
		printSpellCheck(egg.checkSpelling("spamegg"));
		printSpellCheck(egg.checkSpelling("egge"));
		printSpellCheck(egg.checkSpelling("eggspam"));
		printSpellCheck(egg.checkSpelling("ege"));
		printSpellCheck(egg.checkSpelling("spm"));
		printSpellCheck(egg.checkSpelling("eg"));
		printSpellCheck(egg.checkSpelling("eeg"));
		printSpellCheck(egg.checkSpelling("lobsterthermidor"));
		printSpellCheck(egg.checkSpelling("Egg"));
		printSpellCheck(egg.checkSpelling("EGG"));  
	}


}