package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;

public class ListWindow extends JDialog implements ActionListener {

    JFrame parent;

    // GUI Stuff
    protected JPanel topPanel;
    
    protected JPanel listInfos;
    protected JTextField nameLabel;
    protected JTextPane descriptionPane;
    protected JButton updateName;

    protected JPanel buttonPanel;
    protected JButton loadAllEntries;
    protected JButton editEntry;
    protected JButton deleteEntry;
    protected JButton addEntry;

    protected JPanel listPanel;
    protected JList<String> entryList;
    protected DefaultListModel<String> dlm;
    protected JScrollPane listScroller;

    protected RestCommunicator rc;

    private String listGuid;
    private String listName;
    private String listDesc;

    private ArrayList<HashMap<String,String>> entries;



    public ListWindow (JFrame parent, String title, String listGuid, RestCommunicator rc) {
        super(parent, false);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.rc = rc;
        this.listGuid = listGuid;
        this.listName = title;

        // Dialog config
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setTitle(title);
        this.setSize(600,600);

        // Button Panel Config
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        this.loadAllEntries = new JButton("Alle Einträge laden");
        this.loadAllEntries.addActionListener(this);

        this.editEntry = new JButton("Eintrag bearbeiten");
        this.editEntry.addActionListener(this);

        this.deleteEntry = new JButton("Eintrag löschen");
        this.editEntry.addActionListener(this);

        this.addEntry = new JButton("Neuer Eintrag");
        this.addEntry.addActionListener(this);

        this.buttonPanel.add(this.loadAllEntries);
        this.buttonPanel.add(this.addEntry);
        this.buttonPanel.add(this.editEntry);
        this.buttonPanel.add(this.deleteEntry);

        // Configure list info panel
        this.listInfos = new JPanel();
        this.listInfos.setLayout(new GridLayout(3,2,10,10));

        JLabel nameInfo = new JLabel("Name");
        this.nameLabel = new JTextField();

        JLabel descInfo = new JLabel("Beschreibung");
        this.descriptionPane = new JTextPane();

        this.updateName = new JButton("Name / Beschreibung speichern");
        this.updateName.addActionListener(this);
        
        this.listInfos.add(nameInfo);
        this.listInfos.add(this.nameLabel);
        this.listInfos.add(descInfo);
        this.listInfos.add(this.descriptionPane);
        this.listInfos.add(this.updateName);


        // Top Panel Config
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridLayout(2,1,10,10));
        this.topPanel.add(this.listInfos);
        this.topPanel.add(this.buttonPanel);

        ResponseData rd;
        // Load information about list
        try {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("type", "entry");
            map.put("listId", listGuid);
            rd = this.rc.getEntriesFromList(new PostData(map));
        } catch (Exception e) {
            return;
        }

        this.listPanel = new JPanel();
        this.listPanel.setLayout(new FlowLayout());

        // Configure entries
        this.dlm = new DefaultListModel<String>();
        this.entryList = new JList<String>();
        this.entryList.setModel(dlm);
        this.listPanel.add(this.entryList);
        this.entryList.setLayoutOrientation(JList.VERTICAL);

        // Configure Scrollbar
        this.listScroller = new JScrollPane();
        this.listScroller.setViewportView(entryList);
        this.listPanel.add(this.listScroller, BorderLayout.CENTER);

        for (int i = 0; i < rd.getEntries().length; i++) {
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("id", rd.getEntries()[i].getId());
            newEntry.put("name", rd.getEntries()[i].getName());
            newEntry.put("description", rd.getEntries()[i].getDescription());

            this.dlm.addElement(rd.getEntries()[i].getName());
        }

        // Load information about list
        try {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("type", "list");
            map.put("listId", listGuid);
            rd = this.rc.getCertainList(new PostData(map));
        } catch (Exception e) {
            return;
        }

        this.nameLabel.setText(rd.getEntries()[0].getName());
        this.descriptionPane.setText(rd.getEntries()[0].getDescription());

        this.entries = new ArrayList<HashMap<String,String>>();

        this.add(this.topPanel, BorderLayout.NORTH);
        this.add(this.listPanel);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.loadAllEntries) {
            this.loadEntries();
        } else if (e.getSource() == this.editEntry) {
            this.editCurrentEntry();
        } else if (e.getSource() == this.deleteEntry) {
            this.deleteCurrentEntry();
        } else if (e.getSource() == this.addEntry) {
            this.addNewEntry();
        } else if (e.getSource() == this.updateName) {
            this.nameUpdated();
        }
    }

    private void nameUpdated() {
        String oldName = this.listName;
        String newName = this.nameLabel.getText();
        String oldDesc = this.listDesc;
        String newDesc = this.descriptionPane.getText();
        
        try {
            HashMap<String,String> newValues = new HashMap<String,String>();
            newValues.put("type", "list");
            newValues.put("listId", this.listGuid);
            newValues.put("name", newName);
            newValues.put("description", newDesc);
            this.rc.updateList(new PostData(newValues));
        } catch (Exception e) {
            this.nameLabel.setText(oldName);
            this.descriptionPane.setText(oldDesc);
            return;
        } 

        this.listName = newName;
        this.listDesc = newDesc;
    }

    private void loadEntries() {
        this.dlm.removeAllElements();
        ResponseData rd;
        try {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("listId", this.listGuid);
            map.put("type", "entry");
            rd = this.rc.getEntriesFromList(new PostData(map));
        } catch (Exception e) {
            return;
        }

        for (int i = 0; i < rd.getEntries().length; i++) {
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("id", rd.getEntries()[i].getId());
            newEntry.put("name", rd.getEntries()[i].getName());
            newEntry.put("description", rd.getEntries()[i].getDescription());

            this.entries.add(newEntry);
        }

        this.updateDlm();
    }

    private void editCurrentEntry() {
        int index = this.entryList.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.entries.get(index).get("id");
        String name = this.entries.get(index).get("name");
        String desc = this.entries.get(index).get("description");
        
        AddWindow add = new AddWindow((JFrame) super.getParent(), "Eintrag bearbeiten", name, desc);

        String newName = add.getName();
        String newDesc = add.getDescription();

        try {
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("listId", this.listGuid);
            newEntry.put("entryId", idToDelete);
            newEntry.put("name", newName);
            newEntry.put("description", newDesc);
            this.rc.updateEntry(new PostData(newEntry));
        } catch (Exception e) {
            return;
        }


        this.entries.get(index).put("name", newName);
        this.entries.get(index).put("description", newDesc);
        this.updateDlm();
    }

    private void deleteCurrentEntry() {
        int index = this.entryList.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.entries.get(index).get("id");
        try {
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("listId", this.listGuid);
            newEntry.put("entryId", idToDelete);
            this.rc.deleteEntry(new PostData(newEntry));
        } catch (Exception e) {
            return;
        }

        this.entryList.remove(index);
        this.updateDlm();
    }

    private void addNewEntry() {
        AddWindow add = new AddWindow((JFrame) super.getParent(), "Neuer Eintrag", "", "");

        String name = add.getName();
        String description = add.getDescription();

        ResponseData rd;
        if (!name.equals("") && name != null) {
            try {
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("listId", this.listGuid);
                map.put("type", "entry");
                map.put("name", name);
                if (!description.equals("") && description != null) map.put("description", description);
                
                rd = this.rc.addEntryToList(new PostData(map));
            } catch (Exception e) {
                return;
            }
        } else {
            return;
        }

        HashMap<String,String> newEntry = new HashMap<String, String>();
        newEntry.put("name", rd.getEntries()[0].getName());
        newEntry.put("id", rd.getEntries()[0].getId());
        newEntry.put("description", rd.getEntries()[0].getDescription());
        this.entries.add(newEntry);

        this.updateDlm();
    }

    private void updateDlm() {
        this.dlm.removeAllElements();

        for (int i = 0; i < this.entries.size(); i++) {
            this.dlm.addElement(this.entries.get(i).get("name"));
        }
    }
}
