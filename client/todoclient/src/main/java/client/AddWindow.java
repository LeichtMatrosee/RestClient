package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Class that represents a dialog, that is opened, whenever a new todo-list or entry is created
 * or when an entry is edited.
 * @author Gerrit Koppe
 * @version 1.0
 */
public class AddWindow extends JDialog implements ActionListener {
    
    // GUI Components
    /**
     * Panel for all buttons in the window.
     */
    protected JPanel buttonPanel;
    /**
     * Panel in which the new entry or list is edited. The necessary textfields are implemented here.
     */
    protected JPanel editPanel;
    /**
     * The title of the entry or list is shown here.
     */
    protected JPanel titlePanel;

    /**
     * Button to finish and save editing
     */
    protected JButton save;
    /**
     * Button to cancel editing.
     */
    protected JButton cancel;
 
    /**
     * Label that displays the title that was passed into the constructor
     */
    protected JLabel titleLabel;
    
    /**
     * Label that displays the word "Beschreibung"
     */
    protected JLabel description;
    
    /**
     * Label that displays the word "Name"
     */
    protected JLabel name;

    /**
     * Textfield for the user to enter the name of the new list or entry.
     */
    protected JTextField nameField;

    /**
     * Textfield for the user to enter the description of the new entry.
     */
    protected JTextArea descriptionField;

    /**
     * Indicates, whether the save button or the cancel button was pressed.
     * True: Save button
     * False: Cancel button (default)
     */
    private boolean saved = false;

    /**
     * Constructor for the dialog
     * @param parent Parent element of the dialog, e.g. the frame it was opened from.
     * @param title The title the window is supposed to have.
     * @param name Name of the list or entry. Pass old information, if this window is used for editing something existing,
     * pass empty String otherwise. If empty String is passed, initially the name field will show "Namen eintragen".
     * @param desc Description of the list or entry. Pass old information, if this window is used for editing something existing,
     * pass empty String otherwise. If empty String is passed, initially the description field will show "Beschreibung eintragen".
     * @param withDesc True, if it should be possible, to enter a description, false otherwise. Pass true, if this window
     * is used for entries, false if it's used for lists.
     */
    public AddWindow(JFrame parent, String title, String name, String desc, boolean withDesc) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());

        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new FlowLayout());

        this.save = new JButton("Speichern");
        this.save.addActionListener(this);
        this.buttonPanel.add(this.save);

        this.cancel = new JButton("Abbrechen");
        this.cancel.addActionListener(this);
        this.buttonPanel.add(this.cancel);

        this.editPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        
        this.nameField = new JTextField(name.equals("") ? "Namen eintragen": name);
        this.name = new JLabel("Name");
        this.editPanel.add(this.name);
        this.editPanel.add(this.nameField);
        
        if (withDesc) {
            this.descriptionField = new JTextArea(desc.equals("") ? "Beschreibung eintragen" : desc);
            this.description = new JLabel("Beschreibung");
            this.editPanel.add(this.description);
            this.editPanel.add(this.descriptionField);
        }

        this.titlePanel = new JPanel();
        this.titlePanel.setLayout(new FlowLayout());

        this.titleLabel = new JLabel(title);
        this.titlePanel.add(this.titleLabel);

        this.add(this.titlePanel, BorderLayout.NORTH);
        this.add(this.buttonPanel, BorderLayout.SOUTH);
        this.add(this.editPanel);
        this.pack();

        this.setVisible(true);
    }


    
    /** 
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.save) {
            this.saved = true;
            this.dispose();
        } else if (e.getSource() == this.cancel) {
            this.saved = false;
            this.dispose();
        } 
    }

    
    /** 
     * Returns the name the user has entered, if {@link #saved} is true, an empty string otherwise.
     * @return String that was entered in the {@link #nameField} textfield of the window.
     */
    public String getName() {
        if (this.saved) return this.nameField.getText();
        else return "";
    }

    
    /** 
     * Returns the description the user has entered, if {@link #saved} is true, an empty string otherwise.
     * @return String that was entered in the {@link #descriptionField} textfield of the window.
     */
    public String getDescription() {
        if (this.saved) return this.descriptionField.getText();
        else return "";
    }

    /**
     * Closes the dialog.
     */
    public void closeWindow() {
        this.dispose();
    }

}
