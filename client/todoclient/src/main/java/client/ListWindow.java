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
import javax.swing.UIManager;

/**
 * Class represents the dialog to edit todo-lists. Shows todo-list information, as well as entries.
 * @author LeichtMatrosee
 * @version 1.0
 */
public class ListWindow extends JDialog implements ActionListener {

    /**
     * Parent element for the dialog.
     */
    protected JFrame parent;

    // GUI Stuff
    /**
     * Panel on top of the dialog
     */
    protected JPanel topPanel;
    /**
     * Label containing information about last action
     */
    protected JLabel infoLabel;
    
    /**
     * Panel with information about the todo-list itself.
     */
    protected JPanel listInfos;
    /**
     * Label displaying the word "Name".
     */
    protected JTextField nameLabel;
    /**
     * Button that is pressed, whenever the name is supposed to be updated in the API DB
     */
    protected JButton updateName;

    /**
     * Panel for all buttons for editing entries.
     */
    protected JPanel buttonPanel;
    /**
     * Button to load all entries from the API
     */
    protected JButton loadAllEntries;
    /**
     * Button for editing a single entry
     */
    protected JButton editEntry;
    /**
     * Button for deleting a single entry.
     */
    protected JButton deleteEntry;
    /**
     * Button for adding a new entry
     */
    protected JButton addEntry;

    /**
     * Panel containing a list of all entries.
     */
    protected JPanel listPanel;
    /**
     * List containing all entries.
     */
    protected JList<String> entryList;
    /**
     * Model for managing all entries
     */
    protected DefaultListModel<String> dlm;
    /**
     * Scrollbar for the {@link #entryList}
     */
    protected JScrollPane listScroller;

    /**
     * Used for all communication with the API
     */
    protected RestCommunicator rc;

    /**
     * UUID of the todo-list
     */
    private String listGuid;
    /**
     * Name of the todo-list
     */
    private String listName;
    /**
     * Text that is displayed in the info label
     */
    private String info = "";

    /**
     * Contains all background information of the entries like uuid etc.
     */
    private ArrayList<HashMap<String,String>> entries;

    /**
     * Constructor for the ListWindow. Builds all GUI components, retrieves information about the list and it's entries from the api.
     * @param parent Frame from which the window is opened.
     * @param title Title of the Window.
     * @param listGuid UUID of the todo-list to be edited.
     * @param rc RestCommunicator of the parent window.
     */
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

        this.updateName = new JButton("Name speichern");
        this.updateName.addActionListener(this);
        
        this.listInfos.add(nameInfo);
        this.listInfos.add(this.nameLabel);
        this.listInfos.add(this.updateName);


        // Top Panel Config
        this.infoLabel = new JLabel();
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridLayout(3,1,10,10));
        this.topPanel.add(this.infoLabel);
        this.topPanel.add(this.listInfos);
        this.topPanel.add(this.buttonPanel);

        this.listPanel = new JPanel();
        this.listPanel.setLayout(new BorderLayout());

        // Configure entries
        this.dlm = new DefaultListModel<String>();
        this.entryList = new JList<String>();
        this.entryList.setModel(dlm);
        this.listPanel.add(this.entryList, BorderLayout.CENTER);
        this.entryList.setLayoutOrientation(JList.VERTICAL);

        // Configure Scrollbar
        this.listScroller = new JScrollPane();
        this.listScroller.setViewportView(entryList);
        this.listPanel.add(this.listScroller, BorderLayout.CENTER);

        this.entries = new ArrayList<HashMap<String,String>>();
        ResponseData rd;
        // Load information about list
        try {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("type", "entry");
            map.put("listId", listGuid);
            // rd = this.rc.getEntriesFromList(new PostData(map));
            rd = this.rc.sendHttpRequest(new PostData(map), "getEntries");
        } catch (Exception e) {
            return;
        }

        String stMsg = "";

        if (rd.getStatusCode() == 200) {
            for (int i = 0; i < rd.getEntries().length; i++) {
                HashMap<String, String> newEntry = new HashMap<String, String>();
                newEntry.put("id", rd.getEntries()[i].getId());
                newEntry.put("name", rd.getEntries()[i].getName());
                newEntry.put("description", rd.getEntries()[i].getDescription());
                
                this.entries.add(newEntry);
                this.dlm.addElement(rd.getEntries()[i].getName());
            }
        } else {
            stMsg += "Could not load entries for id " + listGuid + ". ";
        }

        // Load information about list
        try {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("type", "list");
            map.put("listId", listGuid);
            // rd = this.rc.getCertainList(new PostData(map));
            rd = this.rc.sendHttpRequest(new PostData(map), "getCertainList");
        } catch (Exception e) {
            return;
        }

        if (rd.getStatusCode() == 200) {
            this.nameLabel.setText(rd.getEntries()[0].getName());
        } else {
            stMsg += "Could not load list information for id " + listGuid + ".";
        }

        this.infoLabel.setText(stMsg);

        this.add(this.topPanel, BorderLayout.NORTH);
        this.add(this.listPanel);

        this.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Updates the label {@link #infoLabel} in the {@link #topPanel} that displays information about the last action
     * to be the passed String. Sets {@link #info} and then calls {@link #updateInfoMessage()}
     * @param msg String that is to be shown in {@link #infoLabel}
     */
    private void updateInfoMessage(String msg) {
        this.info = msg;
        this.updateInfoMessage();
    }

    /**
     * Sets the text of {@link #infoLabel} to be {@link #info}.
     */
    private void updateInfoMessage() {
        this.infoLabel.setText(this.info);
    }

    /**
     * Callback method for {@link #actionPerformed(ActionEvent)}, when the button {@link #updateName} is pressed.
     * Sends http request to API in order to update name there. 
     * If Exception occurs, {@link #updateInfoMessage(String)} is called to show cause of failure in {@link #infoLabel}.
     */
    private void nameUpdated() {
        String oldName = this.listName;
        String newName = this.nameLabel.getText();
        
        try {
            HashMap<String,String> newValues = new HashMap<String,String>();
            newValues.put("type", "list");
            newValues.put("listId", this.listGuid);
            newValues.put("name", newName);
            // this.rc.updateList(new PostData(newValues));
            this.rc.sendHttpRequest(new PostData(newValues), "updateList");
        } catch (Exception e) {
            this.nameLabel.setText(oldName);
            this.updateInfoMessage("Updating list infos failed with a " + e.getClass().getName() + ", msg: " + e.getMessage());
            return;
        } 

        this.listName = newName;
        this.updateInfoMessage("Listeninformationen aktualisiert");
    }
    /**
     * Callback method for {@link #actionPerformed(ActionEvent)}, when the button {@link #loadAllEntries} is pressed.
     * Fetches all entry of the list specified by {@link #listGuid} from the API.
     * Also updates the entries shown in GUI by updating {@link #entries} and calling {@link #updateDlm()}.
     * If Exception occurs, {@link #updateInfoMessage(String)} is called to show cause of failure in {@link #infoLabel}.
     */
    private void loadEntries() {
        this.dlm.removeAllElements();
        ResponseData rd;
        try {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("listId", this.listGuid);
            map.put("type", "entry");
            // rd = this.rc.getEntriesFromList(new PostData(map), "getEntries");
            rd = this.rc.sendHttpRequest(new PostData(map), "getEntries");
        } catch (Exception e) {
            this.updateInfoMessage("Loading entries failed with a " + e.getClass().getName() + ", msg: " + e.getMessage());
            return;
        }

        if (rd.getStatusCode() == 200) {
            this.entries.clear();
            for (int i = 0; i < rd.getEntries().length; i++) {
                HashMap<String, String> newEntry = new HashMap<String, String>();
                newEntry.put("id", rd.getEntries()[i].getId());
                newEntry.put("name", rd.getEntries()[i].getName());
                newEntry.put("description", rd.getEntries()[i].getDescription());

                this.entries.add(newEntry);
            }

            this.updateDlm();
            this.updateInfoMessage("Einträge geladen");
        } else {
            this.updateInfoMessage("Could not load entries, statuscode=" + rd.getStatusCode());
        }
    }
    /**
     * Callback method for {@link #actionPerformed(ActionEvent)}, when the button {@link #editEntry} is pressed.
     * Opens new AddWindow with information of current entry, in order to make it editable.
     * After window is closed, sends http request to api to update the entry.
     * Also updates the entries shown in GUI by updating {@link #entries} and calling {@link #updateDlm()}.
     * If Exception occurs, {@link #updateInfoMessage(String)} is called to show cause of failure in {@link #infoLabel}.
     */
    private void editCurrentEntry() {
        int index = this.entryList.getSelectedIndex();
        if (index == -1) return;

        String idToEdit = this.entries.get(index).get("id");
        String name = this.entries.get(index).get("name");
        String desc = this.entries.get(index).get("description");
        
        AddWindow add = new AddWindow((JFrame) this.getParent(), "Eintrag bearbeiten", name, desc, true);

        String newName = add.getName();
        String newDesc = add.getDescription();

        try {
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("listId", this.listGuid);
            newEntry.put("entryId", idToEdit);
            newEntry.put("name", newName);
            newEntry.put("description", newDesc);
            // this.rc.updateEntry(new PostData(newEntry));
            this.rc.sendHttpRequest(new PostData(newEntry), "updateEntry");
        } catch (Exception e) {
            this.updateInfoMessage("Editing entry failed with a " + e.getClass().getName() + ", msg: " + e.getMessage());
            return;
        }


        this.entries.get(index).put("name", newName);
        this.entries.get(index).put("description", newDesc);
        this.updateDlm();
        this.updateInfoMessage("Eintrag bearbeitet");
    }

    /**
     * Callback method for {@link #actionPerformed(ActionEvent)}, when the button {@link #deleteEntry} is pressed.
     * Sends http request to api to delete the entry.
     * Also updates the entries shown in GUI by updating {@link #entries} and calling {@link #updateDlm()}.
     * If Exception occurs, {@link #updateInfoMessage(String)} is called to show cause of failure in {@link #infoLabel}.
     */
    private void deleteCurrentEntry() {
        int index = this.entryList.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.entries.get(index).get("id");
        try {
            HashMap<String, String> newEntry = new HashMap<String, String>();
            newEntry.put("type", "entry");
            newEntry.put("listId", this.listGuid);
            newEntry.put("entryId", idToDelete);
            // this.rc.deleteEntry(new PostData(newEntry));
            this.rc.sendHttpRequest(new PostData(newEntry), "deleteEntry");
        } catch (Exception e) {
            this.updateInfoMessage("Deleting entry failed with a " + e.getClass().getName() + ", msg: " + e.getMessage());
            return;
        }

        this.entryList.remove(index);
        this.updateDlm();
        this.updateInfoMessage("Eintrag gelöscht");
    }

    /**
     * Callback method for {@link #actionPerformed(ActionEvent)}, when the button {@link #addEntry} is pressed.
     * Opens new AddWindow to let the user add a new entry.
     * After window is closed, sends http request to api to post the entry.
     * Also updates the entries shown in GUI by updating {@link #entries} and calling {@link #updateDlm()}.
     * If Exception occurs, {@link #updateInfoMessage(String)} is called to show cause of failure in {@link #infoLabel}.
     */
    private void addNewEntry() {
        AddWindow add = new AddWindow((JFrame) super.getParent(), "Neuer Eintrag", "", "", true);

        String name = add.getName();
        if (name.equals("") || name == null) {
            this.updateInfoMessage("No Name given!");
            return;
        }
        String description = add.getDescription();

        ResponseData rd;
        if (!name.equals("") && name != null) {
            try {
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("listId", this.listGuid);
                map.put("type", "entry");
                map.put("name", name);
                if (!description.equals("") && description != null) map.put("description", description);
                
                // rd = this.rc.addEntryToList(new PostData(map));
                rd = this.rc.sendHttpRequest(new PostData(map), "addEntry");
            } catch (Exception e) {
                this.updateInfoMessage("Adding entry failed with a " + e.getClass().getName() + ", msg: " + e.getMessage());
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
        this.updateInfoMessage("Eintrag hinzugefügt");
    }

    /**
     * Updates the {@link #dlm} in order to show all entries of the todo-list.
     * Clears dlm, iterates through {@link #entries} and adds every entry in it to the dlm.
     */
    private void updateDlm() {
        this.dlm.removeAllElements();

        for (int i = 0; i < this.entries.size(); i++) {
            this.dlm.addElement(this.entries.get(i).get("name"));
        }
    }
}
