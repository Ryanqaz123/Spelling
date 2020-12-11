import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
//import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.text.*;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//import java.util.Scanner;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.IOException;
 
public class Game implements ActionListener {
    private JFrame frame;
    private JPanel menu, menu2, menu3, check, spellingWord, checkSpelling, levelUp, levelUpMax, chooseLevel, levelDown, changeWords;
    private JLabel menuTitle, menuTitle2, menuTitle3, checkL, wordsSpelledCorrectly, wordsCorrectInRow, correctPercentage, congratsNextLvl, congratsMax, lvlDown, level;
    private JTextPane wordsSpelledByUser;
    private JTextField name, returnUser, age, spellWord;
    private JButton start, hearAudio, enterWord, Continue, continueSameLvl, lowerLvl, chooseLvlBack, lvlDownContinue, newP, returningP, resume, yes, no, nextWord, quit;
    private String[] levelStrings = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
    private JComboBox<String> levels;
    private HashMap<Integer, Level> levelMap = new HashMap<>();
    private ArrayList<Integer> levelList = new ArrayList<>();
    private int wordsCorrect = 0, totalWordsSeen = 0, totalWordsCorrect = 0, wordsSeen = 0;
    private int currentLevelIndex = 0;
    private Word currentWord;
    private User user;
    // TODO add sentence
    //private int PL;
 
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
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // save user level on close with close button
        frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				if (user != null) {
					user.writeLevel();
				}
				frame.dispose();
			}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
        });
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
 
        level = new JLabel("Level: ");
        spellingWord.add(level);
        spellWord = new JTextField("Please Spell the Word");
        spellingWord.add(spellWord);
        spellWord.addActionListener(this);
        spellWord.setActionCommand("spell");
 
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
        Continue.setActionCommand("up");
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
        wordsSpelledByUser = new JTextPane();
        wordsSpelledByUser.setText("Word Spelled By User: "+ user);
        wordsSpelledByUser.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        wordsSpelledByUser.setBackground(Color.WHITE);
        wordsSpelledByUser.setOpaque(true);
        wordsSpelledByUser.setEditable(false);
        wordsSpelledByUser.setSelectedTextColor(null);
        wordsSpelledByUser.setSelectionColor(Color.WHITE);
        wordsSpelledByUser.setAlignmentX(0f);
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
 
        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));
        menu2.setLayout(new BoxLayout(menu2, BoxLayout.PAGE_AXIS));
        menu3.setLayout(new BoxLayout(menu3, BoxLayout.PAGE_AXIS));
        check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
        spellingWord.setLayout(new BoxLayout(spellingWord, BoxLayout.PAGE_AXIS));
        checkSpelling.setLayout(new BoxLayout(checkSpelling, BoxLayout.PAGE_AXIS));
        levelUp.setLayout(new BoxLayout(levelUp, BoxLayout.PAGE_AXIS));
        levelUpMax.setLayout(new BoxLayout(levelUpMax, BoxLayout.PAGE_AXIS));
        chooseLevel.setLayout(new BoxLayout(chooseLevel, BoxLayout.PAGE_AXIS));
        levelDown.setLayout(new BoxLayout(levelDown, BoxLayout.PAGE_AXIS));
        changeWords.setLayout(new BoxLayout(changeWords, BoxLayout.PAGE_AXIS));
 
        frame.setContentPane(menu);
        frame.pack();
        frame.setVisible(true);
    }
    private static void runGUI() {
        @SuppressWarnings("unused")
		Game a1 = new Game();
    }
    public static void main(String[] args) {
        runGUI();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String eventName = e.getActionCommand();
        // main menu: new user button
        if(eventName.equals("new")){
            menu.setVisible(false);
            menu.add(quit);
            menu2.setVisible(true);
            frame.setContentPane(menu2);
            frame.pack();
        }
        // main menu: returning user button
        else if(eventName.equals("return")) {
            menu.setVisible(false);
            menu3.add(quit);
            menu3.setVisible(true);
            frame.setContentPane(menu3);
            frame.pack();
        }
        // returning user menu: resume button
        else if(eventName.equals("Menu2")) {
        	// TODO input validation (user already exists)
        	user = new User(returnUser.getText());
        	int previousLevel = user.getLevel();
        	currentLevelIndex = levelList.indexOf(previousLevel);
        	if (currentLevelIndex == -1) {
        		// TODO if previous level no longer exists
        		currentLevelIndex = 0;
        	}
        	//PL = user.getLevel();
            menu.setVisible(false);
            menu3.setVisible(false);
            spellingWord.add(quit);
            spellingWord.setVisible(true);
            level.setText("Level: " + Integer.toString(playerLevel()));
            frame.setContentPane(spellingWord);
            // TODO reset new words in level
            setCurrentWord();
            frame.pack();
        } 
        // new user menu: start button
        else if(eventName.equals("check")) {
        	// TODO input validation (user doesn't already exist)
        	// TODO input age with buttons
            String checkString = name.getText();
            // TODO level should be determined by age or dropdown menu, not both
            currentLevelIndex = levels.getSelectedIndex();
            menu2.setVisible(false);
            checkL.setText("Is this correct?" + checkString);
            check.setVisible(true);
            check.add(quit);
            frame.setContentPane(check);
            frame.pack();
        }
        // review new user: yes
        else if(eventName.equals("yes")) {
        	// TODO level should be determined by age or dropdown menu, not both
        	user = new User(Integer.parseInt(age.getText()), name.getText(), playerLevel());
        	//PL = user.getLevel();
            check.setVisible(false);
            spellingWord.setVisible(true);
            level.setText("Level: " + Integer.toString(playerLevel()));
            frame.setContentPane(spellingWord);
            // TODO reset new words in level
            setCurrentWord();
            frame.pack();
        }
        // review new user: no
        else if(eventName.equals("no")) {
            check.setVisible(false);
            menu2.setVisible(true);
            menu2.add(quit);
            frame.setContentPane(menu2);
            frame.pack();
        }
        // spelling word menu: enter (check spelling)
        else if(eventName.equals("spell")) {
        	// check spelling of response
        	String user = spellWord.getText();
            String[] userCheck = currentWord.checkSpelling(user);
            // show underlined word
            wordsSpelledByUser.setText("");
            Style style = wordsSpelledByUser.addStyle("Black", null);
            StyleConstants.setAlignment(style, StyleConstants.ALIGN_RIGHT);
            StyleConstants.setSpaceAbove(style, 4);
            StyleConstants.setSpaceBelow(style, 4);
            StyleConstants.setForeground(style, Color.BLACK);
            try {
            	wordsSpelledByUser.getDocument().insertString(0, "Word Spelled By User: " + userCheck[0], style);
            } catch (BadLocationException ex) {
            	
            }
            style = wordsSpelledByUser.addStyle("Red Underline", style);
            StyleConstants.setForeground(style, Color.RED);
            StyleConstants.setUnderline(style, true);
            try {
            	wordsSpelledByUser.getDocument().insertString(wordsSpelledByUser.getText().length(), userCheck[1], style);
            } catch (BadLocationException ex) {
            	
            }
            wordsSpelledByUser.removeStyle("Red Underline");
            StyleConstants.setForeground(style, Color.BLACK);
            StyleConstants.setUnderline(style, false);
            try {
            	wordsSpelledByUser.getDocument().insertString(wordsSpelledByUser.getText().length(), userCheck[2], style);
            } catch (BadLocationException ex) {
            	
            }
            // update stats
            wordsSeen++;
            totalWordsSeen++;
            if (currentWord.isCorrect(user)) {
            	wordsCorrect++;
            	totalWordsCorrect++;
            	levelMap.get(Integer.valueOf(playerLevel())).addToSpelled(currentWord);
            }
            spellingWord.setVisible(false);
            checkSpelling.setVisible(true);
            checkSpelling.add(quit);
            frame.setContentPane(checkSpelling);
            frame.pack();
        }
        //}
        // spelling word: when hear button is click make noise
        else if(eventName.equals("hear")) {
        	SoundPlayback soundPlayer = new SoundPlayback();
        	try {
				soundPlayer.play("Recordings/" + currentWord.getWord() + ".wav");
			} catch (UnsupportedAudioFileException e1) {
				
			} catch (LineUnavailableException e1) {
				
			} catch (IOException e1) {
				
			}
        }
        //screens
        // review spelling: next word
        else if(eventName.equals("checkSpelling")) {
        	// TODO add words spelled correctly in a row, total percentage correct
            if((wordsSeen == 10 && wordsCorrect < 7) && currentLevelIndex != 0) { // level down
                checkSpelling.setVisible(false);
                levelDown.setVisible(true);
                levelDown.add(quit);
                frame.setContentPane(levelDown);
            }else if(wordsSeen == 10 && wordsCorrect >= 7 && (currentLevelIndex + 1 == levelList.size())) { // max level up
                checkSpelling.setVisible(false);
                levelUpMax.setVisible(true);
                levelUpMax.add(quit);
                frame.setContentPane(levelUpMax);
                frame.pack();
                
            }else if(wordsSeen == 10 && wordsCorrect >= 7) { // level up
                checkSpelling.setVisible(false);
                levelUp.setVisible(true);
                frame.setContentPane(levelUp);
                frame.pack();
            }else { // no level change
            	// TODO show correct spelling after user spells word right or gives up
            	// TODO allow user to try again or give up after spelling word wrong
                checkSpelling.setVisible(false);
                spellingWord.setVisible(true);
                frame.setContentPane(spellingWord);
                //when next button is click send to next word - change word noise
                setCurrentWord();
                frame.pack();
            }
        }
        // level down screen: down button
        else if(eventName.equals("down")) {
            levelDown.setVisible(false);
            decrementLevel();
            wordsSeen = 0;
            wordsCorrect = 0;
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            setCurrentWord();
            frame.pack();
        }
        // level up screen: up button
        else if(eventName.equals("up")) {
            levelUp.setVisible(false);
            incrementLevel();
            wordsSeen = 0;
            wordsCorrect = 0;
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            setCurrentWord();
            frame.pack();
        }
        // max level screen: stay at level
        else if(eventName.equals("max")) {
            // TODO stay at level
        }
        // max level screen: go down level
        else if (eventName.equals("downMax")) {
        	// TODO go down level
        }
        // Quit button
        else if(eventName.equals("quit")) {
        	user.writeLevel();
        	user = null;
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
        // TODO level selector?
    }
    
    /**
     * Get a word from the player's current level
     */
    public void setCurrentWord() {
    	currentWord = levelMap.get(playerLevel()).getAWord();
    }
    
    /**
     * Set the level to the next level (increment the level index)
     */
    public void incrementLevel() {
    	currentLevelIndex++;
    	user.setLevel(playerLevel());
    	level.setText("Level: " + Integer.toString(playerLevel()));
    }
    
    /**
     * Set the level to the previous level (decrement the level index)
     */
    public void decrementLevel() {
    	currentLevelIndex--;
    	user.setLevel(playerLevel());
    	level.setText("Level: " + Integer.toString(playerLevel()));
    }
    
    /**
     * Set the level to the level associated with the given index
     * @param index
     */
    public void setLevelWithIndex(int index) {
    	currentLevelIndex = index;
    	user.setLevel(playerLevel());
    	level.setText("Level: " + Integer.toString(playerLevel()));
    }
    
    /**
     * Return the level associated the current level index
     * @return
     */
    public int playerLevel() {
    	return levelList.get(currentLevelIndex).intValue();
    }
    
}
