import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class Editor {
	
	// frames and panels
	private JFrame frame = new JFrame("Editor");
	private JDialog editDialog = new JDialog(frame);
	private boolean editDialogSavedCancelled;
	private JPanel mainMenu = new JPanel(), editMenu = new JPanel();
	// main menu components
	private JLabel levelListLabel = new JLabel("Select level: "),
			wordListLabel = new JLabel("Select Word:");
	private JList<Integer> levels;
	private JList<String> words;
	private JScrollPane levelPane, wordPane;
	private JButton addWord = new JButton("Add"), editWord = new JButton("Edit"),
			removeWord = new JButton("Remove"), dontRemove = new JButton("Cancel");
	// main menu data
	private DefaultListModel<Integer> levelList = new DefaultListModel<>();
	private HashMap<Integer, DefaultListModel<String>> wordList = new HashMap<>();
	// edit word menu components
	private JLabel wordLabel = new JLabel("Spelling"), soundLabel = new JLabel("Pronunciation"),
			recordLengthLabel = new JLabel("Recording time (2 to 15 sec.)"),
			sentenceLabel = new JLabel("Sentence"), levelLabel = new JLabel("Level"), statusLabel = new JLabel(" ");
	private JTextField wordField = new JTextField(10), levelField = new JTextField(5), recordLengthField = new JTextField(5);
	private JButton previewSound = new JButton("Preview"), recordSound = new JButton("Record"),
			previewSentence= new JButton("Preview"), recordSentence = new JButton("Record"),
			saveButton = new JButton("Save"), cancelButton = new JButton("Cancel");
	// edit word menu data
	private String currentSpelling;
	private Integer currentLevel;

	public Editor() {
		// load words into lists and create levels
		loadWords();
		// ==========set up frame and main menu==========
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainMenu.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);
		// level list
		c.gridx = 0;
		c.gridy = 0;
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
					Integer key = levels.getSelectedValue();
					DefaultListModel<String> keyList = wordList.get(key);
					if (keyList != null) {
						words.setModel(keyList);
					}
				}
			}
		});
		levelPane = new JScrollPane(levels);
		levelPane.setMinimumSize(new Dimension(210, 50));
		levelPane.setPreferredSize(new Dimension(210, 50));
		mainMenu.add(levelPane, c);
		// word list
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		mainMenu.add(wordListLabel, c);
		c.gridy = 2;
		c.gridheight = 3;
		words = new JList<>();
		words.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					// enable remove/edit buttons
					if (words.getSelectedValue() == null) {
						editWord.setEnabled(false);
						removeWord.setEnabled(false);
					}
					else {
						editWord.setEnabled(true);
						removeWord.setEnabled(true);
					}
					// discard Confirm/Cancel remove
					if (dontRemove.isVisible()) {
						removeWord.setText("Remove");
						removeWord.setActionCommand("remove");
						dontRemove.setVisible(false);
					}
				}
			}
		});
		wordPane = new JScrollPane(words);
		wordPane.setMinimumSize(new Dimension(150, 210));
		wordPane.setPreferredSize(new Dimension(150, 210));
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
		c.gridx = 0;
		c.gridy = 0;
		editMenu.add(wordLabel, c);
		c.gridy++;
		editMenu.add(soundLabel, c);
		c.gridy++;
		editMenu.add(sentenceLabel, c);
		c.gridy++;
		editMenu.add(recordLengthLabel, c);
		c.gridy++;
		editMenu.add(levelLabel, c);
		c.gridy += 2;
		c.gridwidth = 3;
		editMenu.add(statusLabel, c);
		// save and cancel buttons
		c.gridy--;
		c.gridx++;
		c.gridwidth = 1;
		editMenu.add(saveButton, c);
		saveButton.addActionListener(new CloseEditMenu());
		saveButton.setActionCommand("save");
		c.gridx++;
		editMenu.add(cancelButton, c);
		cancelButton.addActionListener(new CloseEditMenu());
		cancelButton.setActionCommand("cancel");
		// text fields
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		editMenu.add(wordField, c);
		c.gridy = 3;
		c.gridwidth = 1;
		editMenu.add(recordLengthField, c);
		c.gridy = 4;
		editMenu.add(levelField, c);
		// preview and record sound buttons
		c.gridy = 1;
		editMenu.add(previewSound, c);
		previewSound.addActionListener(new PreviewSound());
		previewSound.setActionCommand("spelling");
		c.gridx++;
		editMenu.add(recordSound, c);
		recordSound.addActionListener(new RecordSound());
		recordSound.setActionCommand("spelling");
		c.gridx = 1;
		c.gridy = 2;
		editMenu.add(previewSentence, c);
		previewSentence.addActionListener(new PreviewSound());
		previewSentence.setActionCommand("sentence");
		c.gridx++;
		editMenu.add(recordSentence, c);
		recordSentence.addActionListener(new RecordSound());
		recordSentence.setActionCommand("sentence");
		// edit word dialog
		editDialog.setContentPane(editMenu);
		editDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		editDialog.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				// close button
				if (!editDialogSavedCancelled) {
					File updatedSound = new File("temp_word.wav");
					File updatedSentence = new File("temp_sentence.wav");
					System.gc();
					updatedSound.delete();
					updatedSentence.delete();
					frame.setEnabled(true);
					editDialog.setVisible(false);
				}
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});
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
			// sort word lists
			String[] levelWordArray = new String[levelWordList.size()];
			for (int j = 0; j < levelWordList.size(); j++) {
				levelWordArray[j] = levelWordList.get(j);
			}
			Arrays.sort(levelWordArray);
			levelWordList.clear();
			for (String spelling: levelWordArray) {
				levelWordList.addElement(spelling);
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
				levelPrinter.print(copyScanner.nextLine());
				if (copyScanner.hasNextLine()) {
					levelPrinter.println();
				}
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
		// copy level with extra word
		try (Scanner levelScanner = new Scanner(wordLevel);
				PrintWriter copyPrinter = new PrintWriter(levelCopy);){
			while (levelScanner.hasNextLine()) {
				copyPrinter.println(levelScanner.nextLine());
			}
			copyPrinter.print(word);
		}
		catch (FileNotFoundException ex) {
			
		}
		// copy back to level
		try (Scanner copyScanner = new Scanner(levelCopy);
				PrintWriter levelPrinter = new PrintWriter(wordLevel);){
			while (copyScanner.hasNextLine()) {
				levelPrinter.print(copyScanner.nextLine());
				if (copyScanner.hasNextLine()) {
					levelPrinter.println();
				}
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
				if (levelList != null) {
					levels.setModel(levelList);
				}
				else {
					levels.setModel(new DefaultListModel<Integer>());
				}
				words.setModel(new DefaultListModel<String>());
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
			editDialogSavedCancelled = false;
			// discard Confirm/Cancel remove
			if (dontRemove.isVisible()) {
				removeWord.setText("Remove");
				removeWord.setActionCommand("remove");
				dontRemove.setVisible(false);
			}
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
				previewSound.setEnabled(true);
				previewSentence.setEnabled(true);
				editDialog.setTitle("Edit " + currentSpelling);
			}
			else {
				// adding word: put blank info
				wordField.setText("");
				levelField.setText("");
				previewSound.setEnabled(false);
				previewSentence.setEnabled(false);
				currentSpelling = null;
				editDialog.setTitle("Add Word");
			}
			recordLengthField.setText("3.0");
			statusLabel.setText(" ");
			// change to edit menu
			editDialog.pack();
			frame.setEnabled(false);
			editDialog.setVisible(true);
		}
	}
	
	/**
	 * Attached to save and cancel buttons
	 * Closes word edit window
	 */
	private class CloseEditMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String newSpelling = wordField.getText().toLowerCase();
			File newSound = new File("Recordings/" + newSpelling + ".wav");
			File updatedSound = new File("temp_word.wav");
			File newSentence = new File("Sentences/" + newSpelling + ".wav");
			File updatedSentence = new File("temp_sentence.wav");
			if (e.getActionCommand().equals("save")) {
				// input validation
				// check that word is there
				if (newSpelling.isEmpty()) {
					statusLabel.setText("Word not provided");
					return;
				}
				// check that word contains only letters
				if (!newSpelling.matches("[a-z]+")) {
					statusLabel.setText("Word should contain only letters");
					return;
				}
				// check that word does not already exist
				if (currentSpelling == null
						|| (currentSpelling != null && !currentSpelling.equals(newSpelling))) {
					if (newSound.exists()) {
						// get level of existing word
						Integer levelOfExistingWord = null;
						outer:
						for (Integer levelNum: wordList.keySet()) {
							for (Object levelWord: wordList.get(levelNum).toArray()) {
								String levelString = (String) levelWord;
								if (levelString.equals(newSpelling)) {
									levelOfExistingWord = levelNum;
									break outer;
								}
							}
						}
						if (levelOfExistingWord != null) {
							statusLabel.setText("Word already exists (level " + levelOfExistingWord.toString() + ")");
						}
						else {
							statusLabel.setText("Word already exists");
						}
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
			editDialogSavedCancelled = true;
			System.gc();
			if (e.getActionCommand().equals("save")) {
				// rename temp files if they exist
				boolean soundRenamed = false;
				if (updatedSound.exists()) {
					newSound.delete();
					soundRenamed = updatedSound.renameTo(newSound);
				}
				boolean sentenceRenamed = false;
				if (updatedSentence.exists()) {
					newSentence.delete();
					sentenceRenamed = updatedSentence.renameTo(newSentence);
				}
				// update word list
				Integer newLevel = Integer.parseInt(levelField.getText());
				if (currentSpelling != null && (!currentSpelling.equals(newSpelling)
						|| !currentLevel.equals(newLevel))) {
					deleteWord(currentSpelling, currentLevel);
					addWord(newSpelling, newLevel);
				}
				else if (currentSpelling == null) {
					addWord(newSpelling, newLevel);
				}
				// delete or rename old sound files
				if (currentSpelling != null && !currentSpelling.equals(newSpelling)) {
					File oldSound = new File("Recordings/" + currentSpelling + ".wav");
					File oldSentence = new File("Sentences/" + currentSpelling + ".wav");
					if (soundRenamed) {
						oldSound.delete();
					} else {
						oldSound.renameTo(newSound);
					}
					if (sentenceRenamed) {
						oldSentence.delete();
					} else {
						oldSentence.renameTo(newSentence);
					}
				}
				loadWords();
				if (levelList != null) {
					levels.setModel(levelList);
				}
				else {
					levels.setModel(new DefaultListModel<Integer>());
				}
				words.setModel(new DefaultListModel<String>());
			}
			else {
				// delete temp files upon cancel
				updatedSound.delete();
				updatedSentence.delete();
			}
			// switch back to main menu
			frame.setEnabled(true);
			editDialog.setVisible(false);
		}
	}
	
	/**
	 * Attached to preview buttons
	 * Play current sound file
	 */
	private class PreviewSound implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			statusLabel.setText(" ");
			SoundPlayback player = new SoundPlayback();
			// find and play sound file
			File soundFile;
			String fileName;
			if (e.getActionCommand().equals("spelling")) {
				fileName = "temp_word.wav";
				soundFile = new File(fileName);
				// play current recording if word exists and a new recording hasn't been made
				if (!(currentSpelling == null || soundFile.exists())) {
					fileName = "Recordings/" + currentSpelling + ".wav";
				}
			}
			else {
				fileName = "temp_sentence.wav";
				soundFile = new File(fileName);
				if (!(currentSpelling == null || soundFile.exists())) {
					fileName = "Sentences/" + currentSpelling + ".wav";
				}
				
			}
			try {
				player.play(fileName);
			}
			catch (UnsupportedAudioFileException ex){
				statusLabel.setText("Audio file format is not supported");
			}
			catch (LineUnavailableException ex) {
				statusLabel.setText("Audio line for playback is not available");
			}
			catch (IOException ex) {
				statusLabel.setText("Error in playing audio file");
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
			statusLabel.setText(" ");
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
				if (recordTime > 15000 || recordTime < 2000) {
					throw new NumberFormatException();
				}
				recorder.startCapture(recordTime);
				if (e.getActionCommand().equals("spelling")) {
					previewSound.setEnabled(true);
				}
				else {
					previewSentence.setEnabled(true);
				}
			}
			catch (NumberFormatException ex) {
				statusLabel.setText("Please enter a valid recording duration");
			}
			catch (LineUnavailableException ex) {
				statusLabel.setText("Audio line for recording is unavailable or not supported");
			}
			catch (IOException ex) {
				statusLabel.setText("Error in writing to file");
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

// TODO make recording time a slider or buttons
// TODO figure out how to select levels