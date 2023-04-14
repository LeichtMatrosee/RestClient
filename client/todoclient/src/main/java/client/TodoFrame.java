package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class TodoFrame extends JFrame implements AdjustmentListener, ActionListener {
    // GUI Components
    protected JPanel topPanel;
    protected JPanel infoPanel;
    protected JPanel buttonPanel;
    protected JPanel listPanel;
    protected JPanel list;
    protected JList<String> entries;
    protected DefaultListModel<String> dlm;
    protected JScrollPane listScroller;

    // Buttons for main Window
    protected JButton loadAllLists;
    protected JButton editList;
    protected JButton deleteList;
    protected JButton testApi;
    protected JButton addList;
 
    protected JLabel apiInfo;
    protected JLabel errorInfo;
 
    // Config
    protected ArrayList<HashMap<String, String>> todoLists;

    // Configuration
    private String bgColor = "green";

    // Api Komponenten
    RestCommunicator rc;

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

        this.setVisible(true);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent arg0) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
        }
    }

    public String getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    private void editLists() {
        int index = this.entries.getSelectedIndex();
        String name, guid;

        name = this.todoLists.get(index).get("name");
        guid = this.todoLists.get(index).get("id");

        new ListWindow(this, name, guid, this.rc);
    }

    private void checkApi() {
        boolean isWorking = this.rc.checkApiConnectivity();

        if (isWorking) {
            this.apiInfo.setText("API verfügbar");
        } else {
            this.apiInfo.setText("API nicht verfügbar");
        }

        return;
    }

    private void updateErrorInfo(String message) {
        this.errorInfo.setText(message);
    }

    private void getAllLists() {
        ResponseData rd;
        this.dlm.removeAllElements();

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

        this.updateErrorInfo("");

        HashMap<String,String> newEntry = new HashMap<String, String>();
        newEntry.put("name", rd.getEntries()[0].getName());
        newEntry.put("id", rd.getEntries()[0].getId());
        newEntry.put("description", rd.getEntries()[0].getDescription());
        this.todoLists.add(newEntry);

        this.updateDlm();
    }

    private void deleteLists() {
        int index = this.entries.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.todoLists.get(index).get("id");
        try {
            // this.rc.deleteList(new PostData("list", "", "", idToDelete));
            HashMap<String, String> listToDelete = new HashMap<String, String>();
            listToDelete.put("type", "list");
            listToDelete.put("listId", idToDelete);
            listToDelete.put("id", idToDelete);
            this.rc.sendHttpRequest(new PostData(listToDelete), "deleteList");
        } catch (Exception e) {
            if (!e.getMessage().equals("") || e.getMessage() != null) {
                this.updateErrorInfo(e.getMessage());
            }
            return;
        }

        try {
            HashMap<String, String> entryGetter = new HashMap<String, String>();
            entryGetter.put("type", "entry");
            entryGetter.put("listId", idToDelete);
            ResponseData rd = this.rc.sendHttpRequest(new PostData(entryGetter), "getEntries");
            // ResponseData rd = this.rc.getEntriesFromList(new PostData(entryGetter));

            int deleted = 0;
            for (int i = 0; i < rd.getEntries().length; i++) {
                HashMap<String, String> entryToDelete = new HashMap<String, String>();
                entryToDelete.put("type", "entry");
                entryToDelete.put("listId", idToDelete);
                entryToDelete.put("entryId", rd.getEntries()[i].getId());
                rd = this.rc.sendHttpRequest(new PostData(entryToDelete), "deleteEntry");

                deleted += rd.getDeleted();
            }

            System.out.println("Deleted: " + deleted);
        } catch (Exception e) {
            if (!e.getMessage().equals("") || e.getMessage() != null) {
                this.updateErrorInfo(e.getMessage());
            }
            return;
        }

        this.todoLists.remove(index);
        this.updateErrorInfo("");
        this.updateDlm();
    }

    private void updateDlm() {
        this.dlm.removeAllElements();

        for (int i = 0; i < this.todoLists.size(); i++) {
            this.dlm.addElement(this.todoLists.get(i).get("name"));
        }
    }

}
