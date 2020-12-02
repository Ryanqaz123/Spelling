import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class Editor {
	
	private JFrame frame = new JFrame("Editor");
	private JPanel mainMenu = new JPanel(), editMenu = new JPanel();
	// main menu
	private JLabel levelListLabel, wordListLabel;
	private JList<Integer> levels;
	private JList<Word> words;
	private DefaultListModel<Integer> levelList = new DefaultListModel<>();
	private HashMap<Integer, DefaultListModel<Word>> wordList = new HashMap<>();
	private JScrollPane levelPane, wordPane;
	private JButton addWord = new JButton("Add"), editWord = new JButton("Edit"),
			removeWord = new JButton("Remove"), dontRemove = new JButton("Cancel");
	// edit word menu
	private JLabel wordLabel, soundLabel, recordLengthLabel, sentenceLabel, levelLabel;
	private JTextField wordField, levelField, recordLengthField;
	private JButton previewSound, recordSound, previewSentence, recordSentence, saveButton, cancelButton;
	private String currentSpelling;
	private Integer currentLevel;

	public Editor() {
		// load words and create levels
		loadWords();
		// ==========set up frame==========
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // FUTURE warn before closing, cancel edit word upon close
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
		levels.setFixedCellWidth(30);
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
		wordPane = new JScrollPane(words);
		wordPane.setMinimumSize(new Dimension(150, 200));
		wordPane.setPreferredSize(new Dimension(150, 200));
		mainMenu.add(wordPane, c);
		// buttons
		c.gridx = 1;
		c.gridheight = 1;
		mainMenu.add(removeWord, c);
		c.gridx = 2;
		mainMenu.add(dontRemove, c);
		dontRemove.setVisible(false);
		c.gridx = 1;
		c.gridy = 3;
		mainMenu.add(editWord, c);
		editWord.addActionListener(new OpenEditMenu());
		editWord.setActionCommand("edit");
		c.gridy = 4;
		mainMenu.add(addWord, c);
		addWord.addActionListener(new OpenEditMenu());
		addWord.setActionCommand("add");
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
		recordLengthLabel = new JLabel("Record Length");
		editMenu.add(recordLengthLabel, c);
		c.gridy++;
		levelLabel = new JLabel("Level");
		editMenu.add(levelLabel, c);
		c.gridy++; c.gridx++;
		// save and cancel buttons
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
	
	/*
	 * get set of key-value pairs of name of sound file and associated clip
	 * @param audioFolder
	 * @return
	 */
	/*public static HashMap<String, Clip> getClips(String audioFolder){
		HashMap<String, Clip> clipMap = new HashMap<>();
		File soundDirectory = new File(audioFolder);
		File[] soundFiles = soundDirectory.listFiles();
		for (File audioFile: soundFiles) {
			try {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				String soundFileName = audioFile.getName();
				clipMap.put(soundFileName.substring(0, soundFileName.indexOf(".")), (Clip) AudioSystem.getLine(info));
			}
			catch (UnsupportedAudioFileException ex) {
				System.out.println("The specified audio file is not supported.");
				ex.printStackTrace();
			}
			catch (LineUnavailableException ex) {
				System.out.println("Audio line for playing back is unavailable.");
				ex.printStackTrace();
			}
			catch (IOException ex) {
				System.out.println("Error playing the audio file.");
				ex.printStackTrace();
			}
		}
		return clipMap;
	}
	*/
	
	/**
	 * put words from word files into class attributes (lists)
	 */
	private void loadWords() {
		levelList.clear();
		wordList.clear();
		File wordFolder = new File("Words");
		File[] levelFiles = wordFolder.listFiles();
		for (File levelFile: levelFiles) {
			// load level
			String fileName = levelFile.getName();
			Integer levelNum = Integer.valueOf(fileName.substring(0, fileName.indexOf(".")));
			levelList.addElement(levelNum);
			// load words in level
			DefaultListModel<Word> levelWordList = new DefaultListModel<>();
			try (Scanner levelScanner = new Scanner(levelFile)) {
				while (levelScanner.hasNextLine()) {
					 String spelling = levelScanner.nextLine();
					 levelWordList.addElement(new Word(spelling));
				}
			}
			catch (FileNotFoundException ex1) {
				
			}
			wordList.put(levelNum, levelWordList);
		}
	}
	
	/**
	 * remove word from level file
	 * @param word
	 * @param level
	 */
	private void removeWord(String word, Integer level) {
		// set up files
		File wordLevel = new File("Words/" + level.toString() + ".txt");
		File levelCopy = new File("Words/copy.txt");
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
		File wordLevel = new File("Words/" + level.toString() + ".txt");
		File levelCopy = new File("Words/copy.txt");
		try {
			levelCopy.createNewFile();
		}
		catch (IOException ex) {
			
		}
		// copy level with extra word
		try (Scanner levelScanner = new Scanner(wordLevel);
				PrintWriter copyPrinter = new PrintWriter(levelCopy);){
			while (levelScanner.hasNextLine()) {
				String nextWord = levelScanner.nextLine();
				copyPrinter.println(nextWord);
			}
			copyPrinter.println(word);
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
		System.gc();
		levelCopy.delete();
	}
	
	// TODO implement remove word, confirm remove
	
	/**
	 * Attached to add and edit buttons
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
			Word currentWord = null;
			currentLevel = -1;
			if (e.getActionCommand().equals("edit")) {
				currentWord = words.getSelectedValue();
				currentLevel = levels.getSelectedValue();
				currentSpelling = currentWord.getWord();
				wordField.setText(currentSpelling);
				levelField.setText(currentLevel.toString());
			}
			else {
				wordField.setText("");
				levelField.setText("");
				previewSound.setEnabled(false);
				previewSentence.setEnabled(false);
				currentSpelling = null;
			}
			recordLengthField.setText("3");
			frame.setContentPane(editMenu);
			frame.pack();
			if (e.getActionCommand().equals("add")) {
				frame.setTitle("Add Word");
			} else {
				frame.setTitle("Edit " + currentWord.getWord());
			}
		}
	}
	
	/**
	 * Attached to save and cancel buttons
	 */
	private class CloseEditMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO make sure all information is present before saving, including no duplicate words
			// FUTURE confirm cancel
			// delete temp files
			System.gc();
			File newSound = new File("Recordings/" + wordField.getText() + ".wav");
			File updatedSound = new File("temp_word.wav");
			File newSentence = new File("Sentences/" + wordField.getText() + ".wav");
			File updatedSentence = new File("temp_sentence.wav");
			if (e.getActionCommand().equals("cancel")) {
				updatedSound.delete();
				updatedSentence.delete();
			}
			else {
				newSound.delete();
				updatedSound.renameTo(newSound);
				newSentence.delete();
				updatedSentence.renameTo(newSentence);
			}
			if (e.getActionCommand().equals("save")) {
				// update word list
				String newWord = wordField.getText();
				Integer newLevel = Integer.parseInt(levelField.getText());
				if (!currentSpelling.equals(wordField.getText())
						|| !currentLevel.equals(newLevel)) {
					removeWord(currentSpelling, currentLevel);
					addWord(newWord, newLevel);
				}
				loadWords();
				levels.setModel(levelList);
				words.setModel(new DefaultListModel<>());
			}
			frame.setContentPane(mainMenu);
			frame.pack();
			frame.setTitle("Editor");
			
		}
	}
	
	/**
	 * Attached to preview buttons
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
				if (recordTime > 30000 || recordTime <= 0) {
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