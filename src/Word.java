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
			if(spelling.substring(i,i+1).equals(wordIdent.substring(i,i+1))) {
				front = front + spelling.substring(i,i+1);
			}
			else {
				correct = false;	
			}
			i++;
		}		
		i = spelling.length();
		correct = true;
		System.out.println(i);
		System.out.println(front.length());
		while(correct && (i > front.length())) {//finds the correct letters from the back end
			System.out.println("bababooey");
			if(spelling.substring(i-1,i).equals(wordIdent.substring(i-1,i))) {
				end = spelling.substring(i-1,i) + end;
			}
			else {
				correct = false;	
			}
			i--;
		}
		for(int x = 0; x < (spelling.length() - front.length() - end.length()); x++) {//creates a spacer
			spacer = spacer + "_";
		}
		return new String[]{front,spacer,end};
	}
	public void actionPerformed(ActionEvent e) {
		wordIdent = newSpell.getText();
		frame.setVisible(false);
	}
	public void printSpellCheck(String[] input) {
		System.out.print(input[0]);
		System.out.print(input[1]);
		System.out.print(input[2]);
		System.out.println("\n");
	}
	private static void runGUI() {    
		   JFrame.setDefaultLookAndFeelDecorated(true);   
		   Word garbage = new Word("garbage");
		   //garbage.printSpellCheck(garbage.checkSpelling("garbage"));
		   //garbage.printSpellCheck(garbage.checkSpelling("garxage"));
		   //garbage.printSpellCheck(garbage.checkSpelling("garbag"));
		   garbage.printSpellCheck(garbage.checkSpelling("arbage"));
		   //garbage.editWord();  
	}
	public static void main(String[] args) {      
		  javax.swing.SwingUtilities.invokeLater(new Runnable() {        
			  public void run() {          
				  runGUI();        
			  }    
		  });  
	}
	

}