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

public class AddWindow extends JDialog implements ActionListener {
    
    // Gui Components
    protected JPanel buttonPanel;
    protected JPanel editPanel;
    protected JPanel titlePanel;

    protected JButton save;
    protected JButton cancel;
 
    protected JLabel titleLabel;
    protected JLabel description;
    protected JLabel name;

    protected JTextField nameField;
    protected JTextArea descriptionField;

    private boolean saved = false;
    private boolean supposedToBeClosed = false;

    public AddWindow(JFrame parent, String title) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
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
        
        this.nameField = new JTextField("Namen eintragen");
        this.descriptionField = new JTextArea("Beschreibung eintragen");

        this.name = new JLabel("Name");
        this.editPanel.add(this.name);
        this.editPanel.add(this.nameField);

        this.description = new JLabel("Beschreibung");
        this.editPanel.add(this.description);
        this.editPanel.add(this.descriptionField);

        this.titlePanel = new JPanel();
        this.titlePanel.setLayout(new FlowLayout());

        this.titleLabel = new JLabel(title);
        this.titlePanel.add(this.titleLabel);

        this.add(this.titlePanel, BorderLayout.NORTH);
        this.add(this.buttonPanel, BorderLayout.SOUTH);
        this.add(this.editPanel);

        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.save) {
            this.saved = true;
            this.supposedToBeClosed = true;
            this.dispose();
        } else if (e.getSource() == this.cancel) {
            this.saved = false;
            this.supposedToBeClosed = true;
            this.dispose();
        } 
    }

    public String getName() {
        if (this.saved) return this.nameField.getText();
        else return "";
    }

    public String getDescription() {
        if (this.saved) return this.descriptionField.getText();
        else return "";
    }

    public void closeWindow() {
        this.dispose();
    }

    public boolean getSupposedToBeClosed() {return this.supposedToBeClosed;}

}
