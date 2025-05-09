import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SimpleNotepad extends JFrame implements ActionListener {
    // Components
    JTextArea textArea;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu;
    JMenuItem newFile, openFile, saveFile, exitApp;
    JMenuItem cut, copy, paste;
    File currentFile;
    boolean isTextChanged = false;  

    public SimpleNotepad() {
        // Frame setup
        setTitle("Simple Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Add window listener for unsaved changes
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (promptToSaveChanges()) {
                    System.exit(0);
                }
            }
        });

        // Text area
        textArea = new JTextArea();
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                isTextChanged = true;
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                isTextChanged = true;
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                isTextChanged = true;
            }
        });
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Menu bar
        menuBar = new JMenuBar();

        // File menu
        fileMenu = new JMenu("File");
        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        exitApp = new JMenuItem("Exit");

        // Add action listeners
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        exitApp.addActionListener(this);

        // Add to file menu
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.addSeparator();
        fileMenu.add(exitApp);

        // Edit menu
        editMenu = new JMenu("Edit");
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");

        // Add action listeners
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);


        // File menu
fileMenu = new JMenu("File");
newFile = new JMenuItem("New");
newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); // Ctrl+N
openFile = new JMenuItem("Open");
openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)); // Ctrl+O
saveFile = new JMenuItem("Save");
saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)); // Ctrl+S
exitApp = new JMenuItem("Exit");
exitApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)); // Ctrl+Q

// Edit menu
editMenu = new JMenu("Edit");
cut = new JMenuItem("Cut");
cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK)); // Ctrl+X
copy = new JMenuItem("Copy");
copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)); // Ctrl+C
paste = new JMenuItem("Paste");
paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK)); // Ctrl+V


        // Add to edit menu
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Set menu bar
        setJMenuBar(menuBar);

        // Make frame visible
        setVisible(true);
    }

    private boolean promptToSaveChanges() {
        if (isTextChanged) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "You have unsaved changes. Do you want to save them?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (option == JOptionPane.YES_OPTION) {
                return saveFile();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
           return true;
    }
    private boolean saveFile() {
        if (currentFile == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                // Add ".txt" extension if not provided
                if (!currentFile.getName().toLowerCase().endsWith(".txt")) {
                    currentFile = new File(currentFile.getAbsolutePath() + ".txt");
                }
            } else {
                return false;
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile))) {
            textArea.write(bw);
            isTextChanged = false;
            setTitle("Simple Notepad - " + currentFile.getName());
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile) {
            if (promptToSaveChanges()) {
                textArea.setText("");
                currentFile = null;
                isTextChanged = false;
                setTitle("Simple Notepad");
            }
        } else if (e.getSource() == openFile) {
            if (promptToSaveChanges()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
                int option = fileChooser.showOpenDialog(this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    currentFile = fileChooser.getSelectedFile();
                    try (BufferedReader br = new BufferedReader(new FileReader(currentFile))) {
                        textArea.read(br, null);
                        isTextChanged = false;
                        setTitle("Simple Notepad - " + currentFile.getName());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage());
                    }
                }
            }
        } else if (e.getSource() == saveFile) {
            saveFile();
        } else if (e.getSource() == exitApp) {
            if (promptToSaveChanges()) {
                System.exit(0);
            }
        } else if (e.getSource() == cut) {
            textArea.cut();
        } else if (e.getSource() == copy) {
            textArea.copy();
        } else if (e.getSource() == paste) {
            textArea.paste();
        }
    }

    public static void main(String[] args) {
        new SimpleNotepad();
    }
}
