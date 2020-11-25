import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Editor {
	
	private JFrame frame = new JFrame("Editor");
	private JPanel mainMenu = new JPanel(), editMenu = new JPanel();
	private JList<Integer> levels;
	private JList<Word> words;
	private DefaultListModel<Integer> levelList;
	private HashMap<Integer, DefaultListModel<Word>> wordList;
	private JScrollPane levelPane, wordPane;
	private JButton addWord = new JButton("Add"), editWord = new JButton("Edit"),
			removeWord = new JButton("Remove"), dontRemove = new JButton("Cancel");
	private JLabel wordLabel, soundLabel, sentenceLabel, levelLabel;
	private JTextField wordField, levelField;
	private JTextArea sentenceField;
	private JButton previewSound, recordSound, saveButton, cancelButton;

	public Editor() {
		// TODO create level and word lists
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainMenu.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// level list
		c.gridwidth = 3;
		levelList = new DefaultListModel<>();
		levels = new JList<>(levelList);
		levelPane = new JScrollPane(levels, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainMenu.add(levelPane, c);
		// word list
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 3;
		wordList = new HashMap<>();
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
