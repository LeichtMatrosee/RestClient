package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * Represents the base frame of the application. 
 * In this frame, all todo-lists are managed.
 * @version 1.0
 * @author LeichtMatrosee
 */
public class TodoFrame extends JFrame implements ActionListener {
    // GUI Components

    /**
     * Panel at the top of the frame. Holds {@link #infoPanel} and {@link #buttonPanel}.
     */
    private JPanel topPanel;
    /**
     * Panel in the {@link #topPanel}. Holds {@link #apiInfo} and {@link #errorInfo}. 
     */
    private JPanel infoPanel;
    /**
     * Panel in the {@link #topPanel}. Holds {@link #loadAllLists}, {@link #deleteList}, {@link #editList}, {@link #testApi} and {@link #addList}.
     */
    private JPanel buttonPanel;
    /**
     * Panel in the center of the frame. Holds a list of all todo-lists. Holds {@link #list}.
     */
    private JPanel listPanel;
    /**
     * Panel embedded in the {@link #listPanel}. Holds {@link #entries}, which in turn shows all todo-lists. Also holds the scrollbar {@link #listScroller}.
     */
    private JPanel list;
    /**
     * Manages {@link #dlm}, which has a list of all todo-lists.
     */
    private JList<String> entries;
    /**
     * Manages all todo-lists. Embedded in {@link #entries}.
     */
    private DefaultListModel<String> dlm;
    /**
     * Scrollbar for when the list of todo-lists gets longer than the viewport of {@link #entries}. Embedded in {@link #list}.
     */
    private JScrollPane listScroller;

    // Buttons for main Window
    /**
     * Embedded in {@link #buttonPanel}. Calls {@link #getAllLists()} on click.
     */
    private JButton loadAllLists;
    /**
     * Embedded in {@link #buttonPanel}. Calls {@link #editLists()} on click.
     */
    private JButton editList;
    /**
     * Embedded in {@link #buttonPanel}. Calls {@link #deleteLists()} on click.
     */
    private JButton deleteList;
    /**
     * Embedded in {@link #buttonPanel}. Calls {@link #checkApi()} on click.
     */
    private JButton testApi;
    /**
     * Embedded in {@link #buttonPanel}. Calls {@link #addLists()} on click.
     */
    private JButton addList;
 
    /**
     * Label that displays information about wether the API is reachable. Embedded in {@link #infoPanel}.
     */
    private JLabel apiInfo;
    /**
     * Label that displays information about the last error that occured. Embedded in {@link #infoPanel}.
     */
    private JLabel errorInfo;

    /**
     * @deprecated
     * Might be used again in the future but right now deprecated.
     */
    private Menubar mb;
 
    /**
     * Backend information about all todo-lists. Every element of the ArrayList represents a single todo-list,
     * including name and UUID.
     * {@link #dlm} gets it's information from this structure. This one is always updated first.
     */
    private ArrayList<HashMap<String, String>> todoLists;

    /**
     * In charge of all communication with the API.
     * @see client.RestCommunicator for more information.
     */
    private RestCommunicator rc;

    /**
     * List of all open subwindows.
     */
    private ArrayList<ListWindow> windows;

    /**
     * Standard constructor for the TodoFrame. Builds the entire GUI and instanciates a RestCommunicator
     * for communication with the REST API.
     * @param title Title on top of the frame.
     */
    public TodoFrame(String title) {
        try {
            this.rc = new RestCommunicator();
        } catch (Exception e) {
            return;
        }

        // Configure Frame
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setTitle(title);
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLayout(new BorderLayout());

        // Configure Panel for lists
        this.listPanel = new JPanel();
        this.listPanel.setLayout(new BorderLayout());

        // Configure Panel for entries
        this.list = new JPanel();
        this.list.setLayout(new BorderLayout());

        // Configure entries
        this.dlm = new DefaultListModel<String>();
        this.entries = new JList<String>();
        this.entries.setModel(dlm);
        this.entries.setLayoutOrientation(JList.VERTICAL);
        this.list.add(this.entries);
        
        // Configure Scrollbar
        this.listScroller = new JScrollPane();
        this.listScroller.setViewportView(entries);
        this.list.add(this.listScroller, BorderLayout.CENTER);

        // Add entries to listPanel
        this.listPanel.add(this.list);

        // Panel with general infos
        this.infoPanel = new JPanel();
        this.infoPanel.setLayout(new FlowLayout());

        // Panel at the top of frame
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new BorderLayout());

        this.todoLists = new ArrayList<HashMap<String,String>>();

        // Configure Panel for Buttons
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Button to load all lists
        this.loadAllLists = new JButton("Alle listen laden");
        this.loadAllLists.addActionListener(this);
        
        // Button to edit lists
        this.editList = new JButton("Liste bearbeiten");
        this.editList.addActionListener(this);

        // Button to delete lists
        this.deleteList = new JButton("Liste löschen");
        this.deleteList.addActionListener(this);

        // Button to test, whether the API is reachable
        this.testApi = new JButton("Teste API Verbindung");
        this.testApi.addActionListener(this);

        // Button to add a new list
        this.addList = new JButton("Liste hinzufügen");
        this.addList.addActionListener(this);

        // Add all buttons to buttonpanel
        this.buttonPanel.add(this.loadAllLists);
        this.buttonPanel.add(this.addList);
        this.buttonPanel.add(this.editList);
        this.buttonPanel.add(this.deleteList);
        this.buttonPanel.add(this.testApi);

        // Labels for general information
        this.apiInfo = new JLabel();
        this.errorInfo = new JLabel();
        this.infoPanel.add(this.apiInfo);
        this.infoPanel.add(this.errorInfo);

        // Add both button 
        this.topPanel.add(this.infoPanel, BorderLayout.NORTH);
        this.topPanel.add(this.buttonPanel, BorderLayout.SOUTH);
    
        this.add(this.topPanel, BorderLayout.NORTH);
        this.add(this.listPanel);
        // this.mb = new Menubar(this, this.rc.getHost(), this.rc.getPort());
        // this.setJMenuBar(this.mb);

        this.windows = new ArrayList<ListWindow>();

        this.setVisible(true);
    }

    /**
     * Actionlistener for all Buttons. Also calls {@link #checkAllSubWindows()} on every trigger.
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < this.windows.size(); i++) {
            if (e.getSource() == this.windows.get(i).getUpdateName() && e.getActionCommand().equals("updateNowDude")) {
                this.getAllLists();
            }
        }
        if (e.getSource() == this.loadAllLists) {
            this.getAllLists();
        } else if (e.getSource() == this.addList) {
            this.addLists();
        } else if (e.getSource() == this.deleteList) {
            this.deleteLists();
        } else if (e.getSource() == this.testApi) {
            this.checkApi();
        } else if (e.getSource() == this.editList) {
            this.editLists();
        } else if (e.getSource() == this.mb && e.getActionCommand().equals("Edited settings")) {
            String host = this.mb.getHost();
            int port = this.mb.getPort();
            this.changeApiSettings(host, port);
        }

        this.checkAllSubWindows();
    }

    /**
     * @deprecated
     * Used to change host and port on RestCommunicator object. Will be used again, when the Menubar works.
     * @param host Host the Rest API is on.
     * @param port Port the Rest API is on.
     */
    private void changeApiSettings(String host, int port) {
        this.rc.setHost(host);
        this.rc.setPort(port);
        this.rc.buildBaseUrl();
    }

    /**
     * Iterates through {@link #windows} and checks, if any of the windows is not visible anymore.
     * If one isn't, all list information gets loaded from API again. All non-visible Windows will be removed from {@link #windows}.
     */
    private void checkAllSubWindows() {
        Stack<Integer> indicesToDispose = new Stack<Integer>();
        for (int i = 0; i < this.windows.size(); i++) {
            if (!this.windows.get(i).isVisible()) {
                indicesToDispose.push(i);
            }
        }
        if (!indicesToDispose.empty()) {
            this.getAllLists();
        }
        while (!indicesToDispose.empty()) {
            this.windows.remove((int) indicesToDispose.pop());
        }
    }

    /**
     * Callback function for the edit list button. opens a new ListWindow in which the list can be edited.
     */
    private void editLists() {
        int index = this.entries.getSelectedIndex();
        String name, guid;

        name = this.todoLists.get(index).get("name");
        guid = this.todoLists.get(index).get("id");

        ListWindow lw = new ListWindow(this, name, guid, this.rc);
        lw.getUpdateName().addActionListener(this);
        this.windows.add(lw);
    }

    /**
     * Callback function for the button that checks, whether the API is reachable.
     * Edits the private classmember {@link #apiInfo} and displays, whether API is online.
     */
    private void checkApi() {
        boolean isWorking = this.rc.checkApiConnectivity();

        if (isWorking) {
            this.apiInfo.setText("API verfügbar");
        } else {
            this.apiInfo.setText("API nicht verfügbar");
        }

        return;
    }

    /**
     * Updates the private classmember {@link #errorInfo} and sets it's text to be message.
     * @param message Message that is to be shown in {@link #errorInfo}.
     */
    private void updateErrorInfo(String message) {
        this.errorInfo.setText(message);
    }

    /**
     * Callback function for the button that retrieves all existing todolists from the API.
     * Deletes the entire {@link #todoLists} Array, fills it with the new information from the api, 
     * then clears {@link #dlm} and fills it with the entries from {@link #todoLists}.
     */
    private void getAllLists() {
        ResponseData rd;

        try {
            HashMap<String, String> dummy = new HashMap<String, String>();
            dummy.put("type", "list");
            // rd = this.rc.getAllLists();
            rd = this.rc.sendHttpRequest(new PostData(dummy), "getAllLists");
        } catch (Exception e) {
            if (e.getMessage() != "" && e.getMessage() != null) {
                this.updateErrorInfo(e.getMessage());
            }
            return;
        }

        this.updateErrorInfo("");

        if (rd.getStatusCode() != 200) {
            this.updateErrorInfo("Failed to get all lists, statuscode = " + rd.getStatusCode());
            return;
        }
        // TODO see, if this does what I hope it does.
        this.todoLists.clear();
        this.dlm.removeAllElements();

        for (int i = 0; i < rd.getEntries().length; i++) {
            this.todoLists.add(new HashMap<String,String>());
            this.todoLists.get(i).put("name", rd.getEntries()[i].getName());
            this.todoLists.get(i).put("id", rd.getEntries()[i].getId());
            this.todoLists.get(i).put("description", rd.getEntries()[i].getDescription());
        }

        for (int i = 0; i < this.todoLists.size(); i++) {
            this.dlm.addElement(this.todoLists.get(i).get("name"));
        }
        
        this.entries.setModel(this.dlm);
    }

    /**
     * Opens a new AddWindow, in which the user is supposed to enter the information on the new list.
     * If that window is closed with the save button, the name the user input is retrieved from the AddWindow
     * and the {@link #rc} sends a request to the API for a new list.
     * If the call was successful, the information from the API Response (UUID and name
     * of the new list) are put into {@link #todoLists} and the {@link #dlm} is updated.
     */
    private void addLists() {
        AddWindow add = new AddWindow(this, "Neue Liste", "", "", false);

        String name = add.getName();

        ResponseData rd;
        if (!name.equals("")) {
            try {
                HashMap<String, String> newList = new HashMap<String, String>();
                newList.put("type", "list");
                newList.put("name", name);
                // rd = this.rc.addNewList(new PostData(newList));
                rd = this.rc.sendHttpRequest(new PostData(newList), "addList");
            } catch (Exception e) {
                if (!e.getMessage().equals("") || e.getMessage() != null) {
                    this.updateErrorInfo(e.getMessage());
                }
                return;
            }
        } else {
            return;
        }

        if (rd.getStatusCode() != 200) {
            this.updateErrorInfo("Failed to add list, statuscode = " + rd.getStatusCode());
            return;
        } 

        this.updateErrorInfo("");

        HashMap<String,String> newEntry = new HashMap<String, String>();
        newEntry.put("name", rd.getEntries()[0].getName());
        newEntry.put("id", rd.getEntries()[0].getId());
        newEntry.put("description", rd.getEntries()[0].getDescription());
        this.todoLists.add(newEntry);

        this.updateDlm();
    }

    /**
     * Gets the current selection in {@link #entries}. Gets the UUID of the selection from {@link #todoLists}
     * and sends an API call via {@link #rc}, to delete the list with that UUID. Entries of the list are ignored,
     * as the API deletes them itself.
     * If the Response has a 200 Statuscode, the selected entry gets removed from {@link #entries} and the dlm is
     * updated.
     */
    private void deleteLists() {
        int index = this.entries.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.todoLists.get(index).get("id");
        ResponseData rd;
        try {
            // this.rc.deleteList(new PostData("list", "", "", idToDelete));
            HashMap<String, String> listToDelete = new HashMap<String, String>();
            listToDelete.put("type", "list");
            listToDelete.put("listId", idToDelete);
            listToDelete.put("id", idToDelete);
            rd = this.rc.sendHttpRequest(new PostData(listToDelete), "deleteList");
        } catch (Exception e) {
            if (!e.getMessage().equals("") || e.getMessage() != null) {
                this.updateErrorInfo(e.getMessage());
            }
            return;
        }
        
        if (rd.getStatusCode() != 200) {
            this.updateErrorInfo("Failed to delete list, statuscode = " + rd.getStatusCode());
            return;
        } 
        this.todoLists.remove(index);
        this.updateErrorInfo("");
        this.updateDlm();
    }

    /**
     * Updates {@link #dlm} by removing all it's elements, then iterating through {@link #todoLists}
     * and adding every element's name to the {@link #dlm}.
     */
    private void updateDlm() {
        this.dlm.removeAllElements();

        for (int i = 0; i < this.todoLists.size(); i++) {
            this.dlm.addElement(this.todoLists.get(i).get("name"));
        }
    }

}
