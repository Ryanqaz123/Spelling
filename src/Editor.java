import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Editor {
	
	// frames and panels
	private JFrame frame = new JFrame("Editor");
	private JPanel mainMenu = new JPanel(), editMenu = new JPanel();
	// main menu components
	private JLabel levelListLabel, wordListLabel;
	private JList<Integer> levels;
	private JList<String> words;
	private JScrollPane levelPane, wordPane;
	private JButton addWord = new JButton("Add"), editWord = new JButton("Edit"),
			removeWord = new JButton("Remove"), dontRemove = new JButton("Cancel");
	// main menu data
	private DefaultListModel<Integer> levelList = new DefaultListModel<>();
	private HashMap<Integer, DefaultListModel<String>> wordList = new HashMap<>();
	// edit word menu components
	private JLabel wordLabel, soundLabel, recordLengthLabel, sentenceLabel, levelLabel, statusLabel;
	private JTextField wordField, levelField, recordLengthField;
	private JButton previewSound, recordSound, previewSentence, recordSentence, saveButton, cancelButton;
	// edit word menu data
	private String currentSpelling;
	private Integer currentLevel;

	public Editor() {
		// load words into lists and create levels
		loadWords();
		// ==========set up frame and main menu==========
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // FUTURE warn before closing, cancel edit word upon close
		// FUTURE sort words in list
		mainMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainMenu.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// level list
		c.gridx = 0;
		c.gridy = 0;
		levelListLabel = new JLabel("Select level: ");
		mainMenu.add(levelListLabel, c);
		c.gridx = 1;
		c.gridwidth = 2;
		levels = new JList<>(levelList);
		levels.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		levels.setVisibleRowCount(-1);
		levels.setPrototypeCellValue(levelList.lastElement());
		if (levels.getFixedCellWidth() < 30) {
			levels.setFixedCellWidth(30);
		}
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) levels.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		levels.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (!levelList.isEmpty()) {
						Integer key = levels.getSelectedValue();
						words.setModel(wordList.get(key));
					}					
				}
			}
		});
		levelPane = new JScrollPane(levels);
		levelPane.setMinimumSize(new Dimension(200, 50));
		levelPane.setPreferredSize(new Dimension(200, 50));
		mainMenu.add(levelPane, c);
		// word list
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		wordListLabel = new JLabel("Select Word:");
		mainMenu.add(wordListLabel, c);
		c.gridy = 2;
		c.gridheight = 3;
		words = new JList<>();
		words.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (words.getSelectedValue() == null) {
						editWord.setEnabled(false);
						removeWord.setEnabled(false);
					}
					else {
						editWord.setEnabled(true);
						removeWord.setEnabled(true);
					}
				}
			}
		});
		wordPane = new JScrollPane(words);
		wordPane.setMinimumSize(new Dimension(150, 200));
		wordPane.setPreferredSize(new Dimension(150, 200));
		mainMenu.add(wordPane, c);
		// buttons
		c.gridx = 1;
		c.gridheight = 1;
		mainMenu.add(removeWord, c);
		removeWord.addActionListener(new RemoveWordListener());
		removeWord.setActionCommand("remove");
		c.gridx = 2;
		mainMenu.add(dontRemove, c);
		dontRemove.setVisible(false);
		dontRemove.addActionListener(new RemoveWordListener());
		dontRemove.setActionCommand("cancel");
		c.gridx = 1;
		c.gridy = 3;
		mainMenu.add(editWord, c);
		editWord.addActionListener(new OpenEditMenu());
		editWord.setActionCommand("edit");
		c.gridy = 4;
		mainMenu.add(addWord, c);
		addWord.addActionListener(new OpenEditMenu());
		addWord.setActionCommand("add");
		editWord.setEnabled(false);
		removeWord.setEnabled(false);
		// ==========Edit menu==========
		editMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		editMenu.setLayout(new GridBagLayout());
		// labels
		wordLabel = new JLabel("Spelling");
		c.gridx = 0; c.gridy = 0;
		editMenu.add(wordLabel, c);
		c.gridy++;
		soundLabel = new JLabel("Pronunciation");
		editMenu.add(soundLabel, c);
		c.gridy++;
		sentenceLabel = new JLabel("Sentence");
		editMenu.add(sentenceLabel, c);
		c.gridy++;
		recordLengthLabel = new JLabel("Record Time (3 to 30 sec.)");
		editMenu.add(recordLengthLabel, c);
		c.gridy++;
		levelLabel = new JLabel("Level");
		editMenu.add(levelLabel, c);
		c.gridy += 2;
		c.gridwidth = 3;
		statusLabel = new JLabel(" ");
		editMenu.add(statusLabel, c);
		// save and cancel buttons
		c.gridy--; c.gridx++;
		c.gridwidth = 1;
		saveButton = new JButton("Save");
		editMenu.add(saveButton, c);
		saveButton.addActionListener(new CloseEditMenu());
		saveButton.setActionCommand("save");
		c.gridx++;
		cancelButton = new JButton("Cancel");
		editMenu.add(cancelButton, c);
		cancelButton.addActionListener(new CloseEditMenu());
		cancelButton.setActionCommand("cancel");
		// text fields
		c.gridx = 1; c.gridy = 0;
		c.gridwidth = 2;
		wordField = new JTextField(8);
		editMenu.add(wordField, c);
		c.gridy = 3;
		c.gridwidth = 1;
		recordLengthField = new JTextField(5);
		editMenu.add(recordLengthField, c);
		c.gridy = 4;
		levelField = new JTextField(5);
		editMenu.add(levelField, c);
		// preview and record sound buttons
		c.gridy = 1;
		previewSound = new JButton("Preview");
		editMenu.add(previewSound, c);
		previewSound.addActionListener(new PreviewSound());
		previewSound.setActionCommand("spelling");
		c.gridx++;
		recordSound = new JButton("Record");
		editMenu.add(recordSound, c);
		recordSound.addActionListener(new RecordSound());
		recordSound.setActionCommand("spelling");
		c.gridx = 1; c.gridy = 2;
		previewSentence = new JButton("Preview");
		editMenu.add(previewSentence, c);
		previewSentence.addActionListener(new PreviewSound());
		previewSentence.setActionCommand("sentence");
		c.gridx++;
		recordSentence = new JButton("Record");
		editMenu.add(recordSentence, c);
		recordSentence.addActionListener(new RecordSound());
		recordSentence.setActionCommand("sentence");
		// set main menu
		frame.setContentPane(mainMenu);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * put words from word files into class attributes (lists)
	 */
	private void loadWords() {
		// clear existing contents
		levelList.clear();
		wordList.clear();
		// set up level files
		File wordFolder = new File("Words");
		File[] levelFiles = wordFolder.listFiles();
		int[] levelNumbers = new int[levelFiles.length];
		for (int i = 0; i < levelFiles.length; i++) {
			// load level
			File levelFile = levelFiles[i];
			String fileName = levelFile.getName();
			int levelNum = Integer.parseInt(fileName.substring(0, fileName.indexOf(".")));
			levelNumbers[i] = levelNum;
			// load words in level
			DefaultListModel<String> levelWordList = new DefaultListModel<>();
			try (Scanner levelScanner = new Scanner(levelFile)) {
				while (levelScanner.hasNextLine()) {
					 String spelling = levelScanner.nextLine();
					 levelWordList.addElement(spelling);
				}
			}
			catch (FileNotFoundException ex1) {
				
			}
			wordList.put(levelNum, levelWordList);
		}
		Arrays.sort(levelNumbers);
		for (int levelNum: levelNumbers) {
			levelList.addElement(Integer.valueOf(levelNum));
		}
	}
	
	/**
	 * remove word from level file
	 * @param word
	 * @param level
	 */
	private void deleteWord(String word, Integer level) {
		// set up files
		File wordLevel = new File("Words/" + level.toString() + ".txt");
		File levelCopy = new File("word_copy.txt");
		try {
			levelCopy.createNewFile();
		}
		catch (IOException ex) {
			
		}
		// copy level file without word
		try (Scanner levelScanner = new Scanner(wordLevel);
				PrintWriter copyPrinter = new PrintWriter(levelCopy);){
			while (levelScanner.hasNextLine()) {
				String nextWord = levelScanner.nextLine();
				if (!nextWord.equals(word)) {
					copyPrinter.println(nextWord);
				}
			}
		}
		catch (FileNotFoundException ex) {

		}
		// copy back to level
		boolean lastRemoved = true;
		try (Scanner copyScanner = new Scanner(levelCopy);
				PrintWriter levelPrinter = new PrintWriter(wordLevel);){
			while (copyScanner.hasNextLine()) {
				lastRemoved = false;
				levelPrinter.println(copyScanner.nextLine());
			}
		}
		catch (FileNotFoundException ex) {

		}
		// delete copy (and level after removing last word)
		System.gc();
		levelCopy.delete();
		if (lastRemoved) {
			wordLevel.delete();
		}
	}
	
	/**
	 * add word to level file
	 * @param word
	 * @param level
	 */
	private void addWord(String word, Integer level) {
		// set up files
		File wordLevel = new File("Words/" + level.toString() + ".txt");
		File levelCopy = new File("word_copy.txt");
		try {
			wordLevel.createNewFile();
			levelCopy.createNewFile();
		}
		catch (IOException ex) {
			
		}
		try (Scanner levelScanner = new Scanner(wordLevel);
				PrintWriter copyPrinter = new PrintWriter(levelCopy);){
			copyPrinter.println(word);
			while (levelScanner.hasNextLine()) {
				String nextWord = levelScanner.nextLine();
				copyPrinter.println(nextWord);
			}
		}
		catch (FileNotFoundException ex) {
			
		}
		// copy back to level
		try (Scanner copyScanner = new Scanner(levelCopy);
				PrintWriter levelPrinter = new PrintWriter(wordLevel);){
			while (copyScanner.hasNextLine()) {
				levelPrinter.println(copyScanner.nextLine());
			}
		}
		catch (FileNotFoundException ex) {

		}
		// delete copy
		System.gc();
		levelCopy.delete();
	}
	
	/**
	 * Attached to remove word
	 * Prompts confirm/cancel, cancels remove upon cancellation, or removes word upon confirmation
	 */
	private class RemoveWordListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// prompt confirm or cancel
			if (e.getActionCommand().equals("remove")) {
				removeWord.setText("Confirm");
				removeWord.setActionCommand("confirm");
				dontRemove.setVisible(true);
			}
			// remove word
			else if (e.getActionCommand().equals("confirm")) {
				removeWord.setText("Remove");
				removeWord.setActionCommand("remove");
				dontRemove.setVisible(false);
				Integer currentLevel = levels.getSelectedValue();
				String oldSpelling = words.getSelectedValue();
				File oldSound = new File("Recordings/" + oldSpelling + ".wav");
				File oldSentence = new File("Sentences/" + oldSpelling + ".wav");
				System.gc();
				deleteWord(oldSpelling, currentLevel);
				oldSound.delete();
				oldSentence.delete();
				loadWords();
				levels.setModel(levelList);
				words.setModel(new DefaultListModel<>());
			}
			// cancel remove
			else if (e.getActionCommand().equals("cancel")) {
				removeWord.setText("Remove");
				removeWord.setActionCommand("remove");
				dontRemove.setVisible(false);
			}
		}
	}
	
	/**
	 * Attached to add and edit buttons
	 * Opens word edit window
	 */
	private class OpenEditMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// load current word if necessary
			// audio clips can be loaded by file
			File newSound = new File("temp_word.wav");
			File newSentence = new File("temp_sentence.wav");
			System.gc();
			newSound.delete();
			newSentence.delete();
			// load word info if available
			currentLevel = -1;
			if (e.getActionCommand().equals("edit")) {
				currentSpelling = words.getSelectedValue();
				currentLevel = levels.getSelectedValue();
				wordField.setText(currentSpelling);
				levelField.setText(currentLevel.toString());
				frame.setTitle("Edit " + currentSpelling);
			}
			else {
				// adding word: put blank info
				wordField.setText("");
				levelField.setText("");
				previewSound.setEnabled(false);
				previewSentence.setEnabled(false);
				currentSpelling = null;
				frame.setTitle("Add Word");
			}
			recordLengthField.setText("3.0");
			statusLabel.setText(" ");
			// change to edit menu
			frame.setContentPane(editMenu);
			frame.pack();
		}
	}
	
	/**
	 * Attached to save and cancel buttons
	 * Closes word edit window
	 */
	private class CloseEditMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// FUTURE confirm cancel
			File newSound = new File("Recordings/" + wordField.getText() + ".wav");
			File updatedSound = new File("temp_word.wav");
			File newSentence = new File("Sentences/" + wordField.getText() + ".wav");
			File updatedSentence = new File("temp_sentence.wav");
			// input validation
			if (e.getActionCommand().equals("save")) {
				// check that word is there
				String newSpelling = wordField.getText();
				if (newSpelling.isEmpty()) {
					statusLabel.setText("Word not provided");
					return;
				}
				// check that word contains only letters
				// FUTURE allow hyphens in middle, allow spaces in middle (spaces require extra handling to convert to and from underscores in filenames)
				/*if (!newSpelling.matches("[A-Za-z]|[A-Za-z][A-Za-z-]*[A-Za-z]")) {
					statusLabel.setText("Word contains non-letter characters");
					return;
				}*/
				if (!newSpelling.matches("[A-Za-z]+")) {
					statusLabel.setText("Word contains non-letter characters");
					return;
				}
				// check that word does not already exist
				if (currentSpelling == null
						|| (currentSpelling != null && !currentSpelling.equals(newSpelling))) {
					if (newSound.exists()) {
						statusLabel.setText("Word already exists");
						return;
					}
				}
				// check for pronunciation and sentence files for new words
				if (currentSpelling == null) {
					if (!updatedSound.exists()) {
						statusLabel.setText("Pronunciation is missing");
						return;
					}
					if (!updatedSentence.exists()) {
						statusLabel.setText("Sentence is missing");
						return;
					}
				}
				// check that level number is okay
				try {
					int levelNumber = Integer.parseInt(levelField.getText());
					if (levelNumber <= 0) {
						throw new NumberFormatException();
					}
				}
				catch (NumberFormatException ex) {
					statusLabel.setText("Malformed level (should be a positive integer)");
					return;
				}
			}
			// delete temp files
			System.gc();
			if (e.getActionCommand().equals("cancel")) {
				updatedSound.delete();
				updatedSentence.delete();
			}
			else {
				// rename temp files
				newSound.delete();
				updatedSound.renameTo(newSound);
				newSentence.delete();
				updatedSentence.renameTo(newSentence);
			}
			if (e.getActionCommand().equals("save")) {
				// update word list
				String newWord = wordField.getText();
				//newWord.replace(' ',  '_');
				Integer newLevel = Integer.parseInt(levelField.getText());
				if (currentSpelling != null && (!currentSpelling.equals(newWord)
						|| !currentLevel.equals(newLevel))) {
					deleteWord(currentSpelling, currentLevel);
					addWord(newWord, newLevel);
				}
				else if (currentSpelling == null) {
					addWord(newWord, newLevel);
				}
				// delete old sound files
				if (currentSpelling != null && !currentSpelling.equals(newWord)) {
					File oldSound = new File("Recordings/" + currentSpelling + ".wav");
					File oldSentence = new File("Sentences/" + currentSpelling + ".wav");
					oldSound.delete();
					oldSentence.delete();
				}
				loadWords();
				levels.setModel(levelList);
				words.setModel(new DefaultListModel<>());
			}
			// switch back to main menu
			frame.setContentPane(mainMenu);
			frame.pack();
			frame.setTitle("Editor");
			
		}
	}
	
	/**
	 * Attached to preview buttons
	 * Play current sound file
	 */
	private class PreviewSound implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SoundPlayback player = new SoundPlayback();
			// find and play sound file
			if (e.getActionCommand().equals("spelling")) {
				File soundFile = new File("temp_word.wav");
				if (currentSpelling == null || soundFile.exists()) {
					player.play("temp_word.wav");
				}
				else {
					player.play("Recordings/" + currentSpelling + ".wav");
				}
			}
			else {
				File sentenceFile = new File("temp_sentence.wav");
				if (currentSpelling == null || sentenceFile.exists()) {
					player.play("temp_sentence.wav");
				}
				else {
					player.play("Recordings/" + currentSpelling + ".wav");
				}
				
			}
		}
	}
	
	/**
	 * Attached to record buttons
	 * Record new sound
	 */
	private class RecordSound implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SoundCapture recorder;
			// create temporary sound file
			if (e.getActionCommand().equals("spelling")) {
				recorder = new SoundCapture("temp_word.wav");
			}
			else {
				recorder = new SoundCapture("temp_sentence.wav");
			}
			// try to capture sound
			try {
				long recordTime = (long)(Double.parseDouble(recordLengthField.getText()) * 1000);
				if (recordTime > 30000 || recordTime < 3000) {
					throw new Exception();
				}
				recorder.startCapture(recordTime);
				if (e.getActionCommand().equals("spelling")) {
					previewSound.setEnabled(true);
				}
				else {
					previewSentence.setEnabled(true);
				}
			}
			catch (Exception ex) {
				
			}
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				Editor editor = new Editor();
			}
		});
	}

}

// FIXME lots and lots and lots of testing, make sure to create backup copies of files beforehand (it may already be too late)
// Nothing has been tested yet because I'm scared about what may happen to the files