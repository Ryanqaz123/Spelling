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

	public Editor() {
		// get all sound
		//HashMap<String, Clip> pronunciations = getClips("Recordings");
		//HashMap<String, Clip> sentenceMap = getClips("Sentences");
		// load words and create levels
		File wordFolder = new File("Words");
		File[] levelFiles = wordFolder.listFiles();
		for (File levelFile: levelFiles) {
			String fileName = levelFile.getName();
			Integer levelNum = Integer.valueOf(fileName.substring(0, fileName.indexOf(".")));
			levelList.addElement(levelNum);
			DefaultListModel<Word> levelWordList = new DefaultListModel<>();
			try (Scanner levelScanner = new Scanner(levelFile)){
				while (levelScanner.hasNextLine()) {
					 String spelling = levelScanner.nextLine();
					 //Clip sound = pronunciations.get(spelling);
					 //Clip sentence = sentenceMap.get(spelling);
					 levelWordList.addElement(new Word(spelling));
					 //levelWordList.addElement(new Word(spelling, sound, sentence));
				}
			}
			catch (FileNotFoundException ex1) {
				
			}
			wordList.put(levelNum, levelWordList);
		}
		// ==========set up frame==========
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		levels.setFixedCellWidth(25);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) levels.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		levels.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Integer key = levels.getSelectedValue();
					words.setModel(wordList.get(key));
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
		c.gridx++;
		cancelButton = new JButton("Cancel");
		editMenu.add(cancelButton, c);
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
		c.gridx++;
		recordSound = new JButton("Record");
		editMenu.add(recordSound, c);
		c.gridx = 1; c.gridy = 2;
		previewSentence = new JButton("Preview");
		editMenu.add(previewSentence, c);
		c.gridx++;
		recordSentence = new JButton("Record");
		editMenu.add(recordSentence, c);
		// set main menu
		frame.setContentPane(mainMenu);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static HashMap<String, Clip> getClips(String audioFolder){
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
	
	private class OpenEditMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// load current word if necessary
			// audio clips can be loaded by file
			Word currentWord = null;
			Integer currentLevel = -1;
			if (e.getActionCommand().equals("edit")) {
				currentWord = words.getSelectedValue();
				currentLevel = levels.getSelectedValue();
				wordField.setText(currentWord.getWord());
				levelField.setText(currentLevel.toString());
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

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				Editor editor = new Editor();
			}
		});
	}

}
