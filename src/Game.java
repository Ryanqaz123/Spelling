import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
 
public class Game implements ActionListener {
    private JFrame frame;
    private JPanel menu, menu2, menu3, check, spellingWord, checkSpelling, levelUp, levelUpMax, chooseLevel, levelDown, changeWords;
    private JLabel menuTitle, menuTitle2, menuTitle3, checkL, wordsSpelledCorrectly, wordsSpelledByUser, wordsCorrectInRow, correctPercentage, congratsNextLvl, congratsMax, lvlDown, level;
    private JTextField name, returnUser, age, spellWord;
    private JButton start, hearAudio, enterWord, Continue, continueSameLvl, lowerLvl, chooseLvlBack, lvlDownContinue, newP, returningP, resume, yes, no, nextWord, quit;
    String[] levelStrings = {"Level 1","Level 2","Level 3","Level 4", "Level 5"};
    private JComboBox<String> levels;
    private HashMap<Integer, Level> levelMap;
    private ArrayList<Integer> levelList;
    private int wordsCorrect = 0, totalWordsSeen = 0, totalWordsCorrect = 0, wordsSeen = 0;
    private SoundPlayback soundPlayer = new SoundPlayback();
    //User user;
    // private int PL = user.getLevel();
 
    public Game() {
    	// Load Words
    	levelMap = new HashMap<>();
    	File wordFolder = new File("Words");
    	File[] levelFiles = wordFolder.listFiles();
    	for (int i = 0; i < levelFiles.length; i++) {
			// load level
			File levelFile = levelFiles[i];
			String fileName = levelFile.getName();
			int levelNum = Integer.parseInt(fileName.substring(0, fileName.indexOf(".")));
			levelList.add(Integer.valueOf(levelNum));
			levelMap.put(levelNum, new Level(levelNum));
		}
    	Collections.sort(levelList);
    	
    	// GUI Components
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.PINK));
        menu  = new JPanel();
        menu.setBackground(Color.PINK);
        menu2 = new JPanel();
        menu2.setBackground(Color.PINK);
        menu3 = new JPanel();
        menu3.setBackground(Color.PINK);
        spellingWord  = new JPanel(); 
        spellingWord.setBackground(Color.PINK);
        checkSpelling  = new JPanel(); 
        checkSpelling.setBackground(Color.PINK);
        levelUp  = new JPanel(); 
        levelUp.setBackground(Color.PINK);
        levelUpMax  = new JPanel();
        levelUpMax.setBackground(Color.PINK);
        chooseLevel  = new JPanel(); 
        chooseLevel.setBackground(Color.PINK);
        levelDown  = new JPanel(); 
        levelDown.setBackground(Color.PINK);
        changeWords  = new JPanel();
        changeWords.setBackground(Color.PINK);
        check = new JPanel();
        check.setBackground(Color.PINK);
 
        menuTitle = new JLabel("SPELLING TUTOR"); 
        menuTitle2 = new JLabel("SPELLING TUTOR"); 
        menuTitle3 = new JLabel("SPELLING TUTOR"); 
        
        checkL = new JLabel();
        check.add(checkL);
        yes = new JButton("Yes");
        yes.addActionListener(this);
        yes.setActionCommand("yes");
        check.add(yes);
        no = new JButton("No");
        no.addActionListener(this);
        no.setActionCommand("no");
        check.add(no);
        
        menuTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        menu.add(menuTitle);
        menu2.add(menuTitle2);
        menu3.add(menuTitle3);
        newP = new JButton("New Player?");
        newP.addActionListener(this);
        newP.setActionCommand("new");
        menu.add(newP);
        returningP = new JButton("Returning?");
        returningP.addActionListener(this);
        returningP.setActionCommand("return");
        menu.add(returningP);
 
        congratsNextLvl = new JLabel();
        congratsNextLvl.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        levelUp.add(congratsNextLvl);
        congratsMax = new JLabel();
        congratsMax.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        levelUpMax.add(congratsMax);
        lvlDown = new JLabel("You have moved down a level");
        lvlDown.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        levelDown.add(lvlDown);
 
        name = new JTextField("Name"); 
        menu2.add(name);
        age = new JTextField("Age"); 
        menu2.add(age);
        levels = new JComboBox<>(levelStrings);
        menu2.add(levels);
 
        //level = new JLabel("Level: " + PL);
        //spellingWord.add(level);
        spellWord = new JTextField("Please Spell the Word");
        spellingWord.add(spellWord);
 
        start = new JButton("Start"); 
        start.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        menu2.add(start);
        start.addActionListener(this);
        start.setActionCommand("check");
 
        returnUser = new JTextField("Enter Name");
        menu3.add(returnUser);
        resume = new JButton("Resume"); 
        resume.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        resume.addActionListener(this);
        resume.setActionCommand("Menu2");
        menu3.add(resume);
 
        hearAudio = new JButton("Hear Again"); 
        hearAudio.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        spellingWord.add(hearAudio);
        hearAudio.addActionListener(this);
        hearAudio.setActionCommand("hear");
        enterWord = new JButton("Enter");
        enterWord.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        spellingWord.add(enterWord);
        enterWord.addActionListener(this);
        enterWord.setActionCommand("spell");
        congratsNextLvl = new JLabel("CONGRATULATIONS! You have moved up to the next level");
        levelUp.add(congratsNextLvl);
        Continue = new JButton("Continue");
        levelUp.add(Continue);
        Continue.addActionListener(this);
        continueSameLvl = new JButton("Continue On This Level"); 
        levelUpMax.add(continueSameLvl);
        continueSameLvl.addActionListener(this);
        continueSameLvl.setActionCommand("stay");
        lowerLvl = new JButton("Go Down A Level"); 
        levelUpMax.add(lowerLvl);
        lowerLvl.addActionListener(this);
        lowerLvl.setActionCommand("downMax");
        chooseLvlBack = new JButton("Choose Level");
        chooseLevel.add(chooseLvlBack);
        chooseLvlBack.addActionListener(this);
        chooseLvlBack.setActionCommand("pick");
        lvlDownContinue = new JButton("Continue");
        lvlDownContinue.addActionListener(this);
        lvlDownContinue.setActionCommand("down");
        levelDown.add(lvlDownContinue);
 
        String user = spellWord.getText();
        wordsSpelledByUser = new JLabel("Word Spelled By User: "+ user);
        wordsSpelledByUser.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        wordsSpelledByUser.setBackground(Color.WHITE);
        wordsSpelledByUser.setOpaque(true);
        checkSpelling.add(wordsSpelledByUser);
        wordsSpelledCorrectly = new JLabel("Word Spelled Correctly: ");
        wordsSpelledCorrectly.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        wordsSpelledCorrectly.setBackground(Color.WHITE);
        wordsSpelledCorrectly.setOpaque(true);
        checkSpelling.add(wordsSpelledCorrectly);
        wordsCorrectInRow = new JLabel("Words Correct In a Row: ");
        wordsCorrectInRow.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        wordsCorrectInRow.setBackground(Color.WHITE);
        wordsCorrectInRow.setOpaque(true);
        checkSpelling.add(wordsCorrectInRow);
        correctPercentage = new JLabel("Percentage of Words Spelled Correctly: ");
        correctPercentage.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        correctPercentage.setBackground(Color.WHITE);
        correctPercentage.setOpaque(true);
        checkSpelling.add(correctPercentage);
        nextWord = new JButton("Next");
        nextWord.addActionListener(this);
        nextWord.setActionCommand("checkSpelling");
        checkSpelling.add(nextWord);    
 
        quit = new JButton("Quit");
        quit.addActionListener(this);
        quit.setActionCommand("quit");
 
        menu.setLayout(new BoxLayout(menu , BoxLayout.PAGE_AXIS));
        menu2.setLayout(new BoxLayout(menu2 , BoxLayout.PAGE_AXIS));
        menu3.setLayout(new BoxLayout(menu3 , BoxLayout.PAGE_AXIS));
        check.setLayout(new BoxLayout(check , BoxLayout.PAGE_AXIS));
        spellingWord.setLayout(new BoxLayout(spellingWord ,BoxLayout.PAGE_AXIS));
        checkSpelling.setLayout(new BoxLayout(checkSpelling ,BoxLayout.PAGE_AXIS));
        levelUp.setLayout(new BoxLayout(levelUp ,BoxLayout.PAGE_AXIS));
        levelUpMax.setLayout(new BoxLayout(levelUpMax ,BoxLayout.PAGE_AXIS));
        chooseLevel.setLayout(new BoxLayout(chooseLevel ,BoxLayout.PAGE_AXIS));
        levelDown.setLayout(new BoxLayout(levelDown ,BoxLayout.PAGE_AXIS));
        changeWords.setLayout(new BoxLayout(changeWords ,BoxLayout.PAGE_AXIS));
 
        frame.setContentPane(menu);
        frame.pack();
        frame.setVisible(true);
    }
    private static void runGUI() {
        Game a1 = new Game();
    }
    public static void main(String[] args) {
        runGUI();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String eventName = e.getActionCommand();
        if(eventName.equals("new")){
            menu.setVisible(false);
            menu.add(quit);
            menu2.setVisible(true);
            frame.setContentPane(menu2);
            frame.pack();
        }
        else if(eventName.equals("return")) {
            menu.setVisible(false);
            menu3.add(quit);
            menu3.setVisible(true);
            frame.setContentPane(menu3);
            frame.pack();
        }
        else if(eventName.equals("Menu2")) {
            menu.setVisible(false);
            menu3.setVisible(false);
            spellingWord.add(quit);
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            frame.pack();
        } 
        else if(eventName.equals("check")) {
            String checkString = name.getText();
            menu2.setVisible(false);
            checkL.setText("Is this correct?" + checkString);
            check.setVisible(true);
            check.add(quit);
            frame.setContentPane(check);
            frame.pack();
        }
        else if(eventName.equals("yes")) {
            check.setVisible(false);
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            frame.pack();
        }
        else if(eventName.equals("no")) {
            check.setVisible(false);
            menu2.setVisible(true);
            menu2.add(quit);
            frame.setContentPane(menu2);
            frame.pack();
        }
        else if(eventName.equals("spell")) {
            wordsSeen++;
            totalWordsSeen++;
            spellingWord.setVisible(false);
            checkSpelling.setVisible(true);
            checkSpelling.add(quit);
            frame.setContentPane(checkSpelling);
            frame.pack();
        }
        //}
        //when hear button is click make noise
        else if(eventName.equals("hear")) {
        	
        }
        //screens
        else if(eventName.equals("checkSpelling")) {
            //if((wordsSeen == 10 && wordsCorrect < 7) && PL != 1) {
                //checkSpelling.setVisible(false);
                //PL--;
                //levelDown.setVisible(true);
                //levelDown.add(quit);
                //frame.setContentPane(levelDown);
            //}else 
                if(wordsSeen == 10 && wordsCorrect == 10) {
                checkSpelling.setVisible(false);
                levelUp.setVisible(true);
                //PL++;
                frame.setContentPane(levelUp);
                frame.pack();
                
            //}else if(wordsSeen == 10 && wordsCorrect == 10 && (level.equals("Level 5") || user.getLevel() == 5)) {
                //checkSpelling.setVisible(false);
                //levelUpMax.setVisible(true);
                //levelUpMax.add(quit);
                //frame.setContentPane(levelUpMax);
            }else {
                checkSpelling.setVisible(false);
                //when next button is click send to next word - change word noise
                spellingWord.setVisible(true);
                frame.setContentPane(spellingWord);
                frame.pack();
            }
        }
        else if(eventName.equals("down")) {
            levelDown.setVisible(false);
            //implement way to make words less difficult
            wordsSeen = 0;
            wordsCorrect = 0;
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            frame.pack();
        }
        else if(eventName.equals("up")) {
            levelUp.setVisible(false);
            //implement way to make words more difficult 
            wordsSeen = 0;
            wordsCorrect = 0;
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            frame.pack();
        }
        else if(eventName.equals("max")) {
            
        }
        else if(eventName.equals("quit")) {
            menu.setVisible(true);
            menu2.setVisible(false);
            menu3.setVisible(false);
            check.setVisible(false); 
            spellingWord.setVisible(false);
            checkSpelling.setVisible(false);
            levelUp.setVisible(false); 
            levelUpMax.setVisible(false); 
            chooseLevel.setVisible(false); 
            levelDown.setVisible(false);
            changeWords.setVisible(false);
            frame.setContentPane(menu);
            frame.pack();
        }
    }
}
