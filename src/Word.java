import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class Word implements ActionListener{
	JFrame frame;
	JPanel contentPane;
	JLabel prompt;
	JTextField newSpell;
	private String wordIdent;
	private String sentence;
	private javax.sound.sampled.Clip clip;

	Word(String word){
		wordIdent = word.toLowerCase();
	}

	public void editWord() {
		frame = new JFrame("edit word");    
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();    
		contentPane.setLayout(new GridLayout(2, 0, 10, 5)); 
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		newSpell = new JTextField(20);
		newSpell.addActionListener(this);
		prompt = new JLabel("Enter the new spelling: ");
		frame.setContentPane(contentPane);
		contentPane.add(prompt);
		contentPane.add(newSpell);
		frame.pack();    
		frame.setVisible(true);
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

	public void actionPerformed(ActionEvent e) {
		wordIdent = newSpell.getText();
		frame.setVisible(false);
	}

	public static void printSpellCheck(String[] input) {
		System.out.print(input[0]);
		System.out.print(", ");
		System.out.print(input[1]);
		System.out.print(", ");
		System.out.println(input[2]);
	}

	private static void runGUI() {    
		JFrame.setDefaultLookAndFeelDecorated(true);   
		Word garbage = new Word("garbage");
		printSpellCheck(garbage.checkSpelling("garbage"));
		printSpellCheck(garbage.checkSpelling("garxage"));
		printSpellCheck(garbage.checkSpelling("garbag"));
		printSpellCheck(garbage.checkSpelling("arbage"));
		//garbage.editWord();
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

	public static void main(String[] args) {      
		javax.swing.SwingUtilities.invokeLater(new Runnable() {        
			public void run() {          
				runGUI();        
			}    
		});  
	}


}