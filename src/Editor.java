import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

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
	private JLabel wordLabel, soundLabel, recordLabel, sentenceLabel, levelLabel;
	private JTextField wordField, levelField;
	private JTextArea sentenceField;
	private JButton previewSound, recordSound, saveButton, cancelButton;

	public Editor() {
		// create level and word lists
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
					 levelWordList.addElement(new Word(spelling));
				}
			}
			catch (FileNotFoundException ex1) {
				
			}
			wordList.put(levelNum, levelWordList);
		}
		// set up frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainMenu.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// level list
		c.gridwidth = 3;
		levels = new JList<>(levelList);
		levels.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		levels.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Integer key = levelList.get(levels.getSelectedIndex());
					words.setModel(wordList.get(key));
				}
			}
		});
		levelPane = new JScrollPane(levels, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainMenu.add(levelPane, c);
		// word list
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 3;
		words = new JList<>();
		wordPane = new JScrollPane(words, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainMenu.add(wordPane, c);
		// buttons
		c.gridx = 1;
		c.gridheight = 1;
		mainMenu.add(removeWord, c);
		c.gridx = 2;
		mainMenu.add(dontRemove, c);
		dontRemove.setVisible(false);
		c.gridx = 1;
		c.gridy = 2;
		mainMenu.add(editWord, c);
		c.gridy = 3;
		mainMenu.add(addWord, c);
		frame.setContentPane(mainMenu);
		frame.pack();
		frame.setVisible(true);
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
