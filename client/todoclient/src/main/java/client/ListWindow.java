package client;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class ListWindow extends TodoFrame {

    // GUI Stuff
    JButton loadAllEntries;
    JButton editEntry;
    JButton deleteEntry;
    JButton addEntry;
    

    public ListWindow (String title, String listGuid) {
        super(title);

        this.loadAllEntries = new JButton("Lade alle Einträge");
        this.loadAllEntries.addActionListener(this);

        this.addEntry = new JButton("Eintrag hinzufügen");
        this.addEntry.addActionListener(this);

        this.editEntry = new JButton("Eintrag bearbeiten");
        this.editEntry.addActionListener(this);

        this.deleteEntry = new JButton("Eintrag löschen");
        this.deleteEntry.addActionListener(this);

        this.buttonPanel.add(this.loadAllEntries);
        this.buttonPanel.add(this.addEntry);
        this.buttonPanel.add(this.editEntry);
        this.buttonPanel.add(this.deleteEntry);


        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
