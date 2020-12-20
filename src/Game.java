import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.text.*;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

/**
 * Displays GUI for game, handles gameplay
 */
public class Game implements ActionListener {
    private JFrame frame;
    private JPanel menu, menu2, menu3, check, spellingWord, checkSpelling, levelUp, levelUpMax, chooseLevel, levelDown, changeWords;
    private JLabel noWords, menuTitle, menuTitle2, menuTitle3, checkL, wordsSpelledCorrectly, wordsCorrectInRow, correctPercentage, congratsNextLvl, congratsMax, lvlDown, level, ageLabel, nameAvailable, nameExists;
    private JTextPane wordsSpelledByUser;
    private JTextField name, returnUser, spellWord;
    private JSpinner ageSpin;
    private JButton start, hearAudio, hearSentence, enterWord, Continue, continueSameLvl, lowerLvl, chooseLvlBack, lvlDownContinue, newP, returningP, resume, yes, no, nextWord, quit, tryAgain;
    private HashMap<Integer, Level> levelMap = new HashMap<>();
    private ArrayList<Integer> levelList = new ArrayList<>();
    private int wordsCorrect = 0, totalWordsSeen = 0, totalWordsCorrect = 0, wordsSeen = 0, consecutiveCorrect = 0;
    private int currentLevelIndex = 0;
    private Word currentWord;
    private User user;
    private boolean spelledCorrectly = false, isNewWord = true;

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
        frame = new JFrame("Spelling Tutor");
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
        if (levelList.isEmpty()) {
        	noWords = new JLabel("Attention: No words have been loaded into the game. Please add words to continue.");
        	menu.add(noWords);
        	newP.setEnabled(false);
        	returningP.setEnabled(false);
        }
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
        ageLabel = new JLabel("Enter Age:");
        menu2.add(ageLabel);
        SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		ageSpin = new JSpinner(model);
		ageSpin.setAlignmentX(0.0f);
		menu2.add(ageSpin);
		nameAvailable = new JLabel(" ");
		menu2.add(nameAvailable);
 
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
        nameExists = new JLabel(" ");
        menu3.add(nameExists);
        menu3.add(resume);
 
        hearAudio = new JButton("Hear Again"); 
        hearAudio.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        spellingWord.add(hearAudio);
        hearAudio.addActionListener(this);
        hearAudio.setActionCommand("hear");
        hearSentence = new JButton("Sentence"); 
        hearSentence.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        spellingWord.add(hearSentence);
        hearSentence.addActionListener(this);
        hearSentence.setActionCommand("sentence");
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
        if (levelList.size() == 1) {
        	lowerLvl.setEnabled(false);
        }
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
 
        String userText = spellWord.getText();
        wordsSpelledByUser = new JTextPane();
        wordsSpelledByUser.setText("Word Spelled By User: "+ userText);
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
        wordsCorrectInRow = new JLabel("Number of Words Spelled in a Row: ");
        wordsCorrectInRow.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        wordsCorrectInRow.setBackground(Color.WHITE);
        wordsCorrectInRow.setOpaque(true);
        checkSpelling.add(wordsCorrectInRow);
        correctPercentage = new JLabel("Percentage of Words Spelled Correctly: ");
        correctPercentage.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        correctPercentage.setBackground(Color.WHITE);
        correctPercentage.setOpaque(true);
        checkSpelling.add(correctPercentage);
        tryAgain = new JButton("Try again");
        tryAgain.addActionListener(this);
        tryAgain.setActionCommand("try again");
        checkSpelling.add(tryAgain);
        tryAgain.setVisible(false);
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
            menu2.add(quit);
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
        	user = User.load(returnUser.getText());
        	if (user == null) {
        		nameExists.setText("User doesn't exist");
        	}
        	else {
        		nameExists.setText(" ");
        		int previousLevel = user.getLevel();
        		currentLevelIndex = levelList.indexOf(previousLevel);
        		if (currentLevelIndex == -1) {
        			// if previous level no longer exists, or could not find level
        			// not very good, but works
        			currentLevelIndex = 1;
        			int difference = Math.abs(levelList.get(0) - previousLevel);
        			while (currentLevelIndex < levelList.size() && difference > Math.abs(playerLevel() - previousLevel) ) {
        				difference = Math.abs(playerLevel() - previousLevel);
        				currentLevelIndex++;
        			}
        			currentLevelIndex--;
        			user.setLevel(playerLevel());
        		}
        		menu.setVisible(false);
        		menu3.setVisible(false);
        		spellingWord.setVisible(true);
        		level.setText("Level: " + Integer.toString(playerLevel()));
        		frame.setContentPane(spellingWord);
        		for (Level levelVal: levelMap.values()) {
        			levelVal.restoreNewWords();
        		}
        		setCurrentWord();
        		frame.pack();
        	}
        	
        } 
        // new user menu: start button
        else if(eventName.equals("check")) {
        	String checkString = name.getText();
        	if (User.levelOf(checkString) == -1) {
        		nameAvailable.setText(" ");
        		int startAge = (Integer)ageSpin.getValue();
        		int startLevelIndex = Math.max((int)((levelList.size() - 1) * (startAge - 5) / 5.0), 0);
        		if (startLevelIndex != 0) {
        			currentLevelIndex = Math.min(startLevelIndex, levelList.size() - 1);
        		}
        		else {
        			currentLevelIndex = 0;
        		}
        		menu2.setVisible(false);
        		checkL.setText("Is this correct? Name: " + checkString + ", Age: " + startAge);
        		check.setVisible(true);
        		check.add(quit);
        		frame.setContentPane(check);
        		frame.pack();
        	}
        	else {
        		nameAvailable.setText("User already exists");
        	}
        }
        // review new user: yes
        else if(eventName.equals("yes")) {
        	user = User.create(name.getText(), playerLevel(), false);
            check.setVisible(false);
            spellingWord.setVisible(true);
            level.setText("Level: " + Integer.toString(playerLevel()));
            frame.setContentPane(spellingWord);
            for (Level levelVal: levelMap.values()) {
            	levelVal.restoreNewWords();
            }
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
        	String userSpelling = spellWord.getText().toLowerCase();
            String[] userCheck = currentWord.checkSpelling(userSpelling);
            spelledCorrectly = currentWord.isCorrect(userSpelling);
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
            StyleConstants.setItalic(style, true);
            try {
            	wordsSpelledByUser.getDocument().insertString(wordsSpelledByUser.getText().length(), userCheck[1], style);
            } catch (BadLocationException ex) {
            	
            }
            wordsSpelledByUser.removeStyle("Red Underline");
            StyleConstants.setForeground(style, Color.BLACK);
            StyleConstants.setUnderline(style, false);
            StyleConstants.setItalic(style, false);
            try {
            	wordsSpelledByUser.getDocument().insertString(wordsSpelledByUser.getText().length(), userCheck[2], style);
            } catch (BadLocationException ex) {
            	
            }
            // update stats
            if (isNewWord) {
            	wordsSeen++;
            	totalWordsSeen++;
            }
            if (spelledCorrectly) { // TODO more colors for correct answer
            	if (isNewWord) {
            		wordsCorrect++;
            		totalWordsCorrect++;
            	}
            	consecutiveCorrect++;
            	nextWord.setActionCommand("checkSpelling");
            	nextWord.setText("Next");
            	tryAgain.setVisible(false);
            	levelMap.get(Integer.valueOf(playerLevel())).addToSpelled(currentWord);
            	wordsSpelledCorrectly.setText("Word Spelled Correctly: " + currentWord.getWord());
            }
            else {
            	tryAgain.setVisible(true);
            	nextWord.setActionCommand("give up");
            	nextWord.setText("Give Up");
            	wordsSpelledCorrectly.setText("");
            }
            isNewWord = false;
            spellingWord.setVisible(false);
            checkSpelling.setVisible(true);
            checkSpelling.add(quit);
            wordsCorrectInRow.setText("Number of Words Spelled in a Row: " + Integer.toString(consecutiveCorrect));
            correctPercentage.setText("Percentage of Words Spelled Correctly: " + (int)(((totalWordsCorrect * 100.0) / totalWordsSeen) + 0.5) + "%");
            frame.setContentPane(checkSpelling);
            frame.pack();
        }
        // review spelling: try again
        else if (eventName.equals("try again")) {
        	checkSpelling.setVisible(false);
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            frame.pack();
        }
        // review spelling: give up
        else if (eventName.equals("give up")) {
        	consecutiveCorrect = 0;
        	tryAgain.setVisible(false);
        	wordsSpelledCorrectly.setText("Word Spelled Correctly: " + currentWord.getWord());
        	wordsCorrectInRow.setText("Number of Words Spelled in a Row: " + Integer.toString(consecutiveCorrect));
        	nextWord.setActionCommand("checkSpelling");
        	nextWord.setText("Next");
        	frame.pack();
        }
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
        // spelling word: play sentence noise
        else if (eventName.equals("sentence")) {
        	SoundPlayback soundPlayer = new SoundPlayback();
        	try {
				soundPlayer.play("Sentences/" + currentWord.getWord() + ".wav");
			} catch (UnsupportedAudioFileException e1) {
				
			} catch (LineUnavailableException e1) {
				
			} catch (IOException e1) {
				
			}
        }
        // screens
        // review spelling: next word
        else if(eventName.equals("checkSpelling")) {
        	/* Move down if 10 words have been seen AND (strictly) fewer than 2/3 of words have been spelled correctly on the first try, and not on lowest level
        	 * Otherwise, after 10 words have been spelled correctly on the first try, move up a level, or ask to stay or move down if on the maximum level
        	 */
        	if (wordsSeen >= 10 && 3 * wordsCorrect < 2 * wordsSeen && currentLevelIndex != 0) { // level down
        		checkSpelling.setVisible(false);
        		levelDown.setVisible(true);
        		levelDown.add(quit);
        		frame.setContentPane(levelDown);
        		frame.pack();
        	}
        	else if (wordsCorrect >= 10) {
        		if (currentLevelIndex + 1 == levelList.size()) { // level up max
        			checkSpelling.setVisible(false);
        			levelUpMax.setVisible(true);
        			levelUpMax.add(quit);
        			frame.setContentPane(levelUpMax);
        			frame.pack();
        		}
        		else { // level up
        			checkSpelling.setVisible(false);
        			levelUp.setVisible(true);
        			frame.setContentPane(levelUp);
        			frame.pack();
        		}
        	}
        	else { // no level change
        		checkSpelling.setVisible(false);
        		spellingWord.setVisible(true);
        		frame.setContentPane(spellingWord);
        		// when next button is click send to next word - change word noise
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
        else if(eventName.equals("stay")) {
        	wordsSeen = 0;
        	wordsCorrect = 10;
        	levelUpMax.setVisible(false);
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            // when next button is click send to next word - change word noise
            setCurrentWord();
            frame.pack();
        }
        // max level screen: go down level
        else if (eventName.equals("downMax")) {
        	wordsSeen = 0;
            wordsCorrect = 0;
        	levelUpMax.setVisible(false);
            spellingWord.setVisible(true);
            frame.setContentPane(spellingWord);
            decrementLevel();
            // when next button is click send to next word - change word noise
            setCurrentWord();
            frame.pack();
        }
        // Quit button
        else if(eventName.equals("quit")) {
        	if (user != null) {
        		user.writeLevel();
        	}
        	user = null;
        	wordsCorrect = 0;
        	totalWordsSeen = 0;
        	totalWordsCorrect = 0;
        	wordsSeen = 0;
        	consecutiveCorrect = 0;
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
        // XXX we don't have enough time to care about a level selector
    }
    
    /**
     * Get a word from the player's current level
     */
    public void setCurrentWord() {
    	currentWord = levelMap.get(playerLevel()).getAWord();
    	isNewWord = true;
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
     * @return the level associated the current level index
     */
    public int playerLevel() {
    	return levelList.get(currentLevelIndex).intValue();
    }
    
}

/* FIXME known issues
 * text fields show previous response when revisiting a window
 * adding a new user and then quitting results in user not being created
 * sometimes the program crashes after trying to spell a word again
 */