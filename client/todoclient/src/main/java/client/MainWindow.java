package client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;

public class MainWindow extends TodoFrame {
    // Buttons for main Window
    JButton loadAllLists;
    JButton editList;
    JButton deleteList;
    JButton testApi;
    JButton addList;

    JLabel apiInfo;
    JLabel errorInfo;

    // Config
    ArrayList<HashMap<String, String>> lists;


    public MainWindow(String title) {
        super(title);
        this.lists = new ArrayList<HashMap<String,String>>();
        this.loadAllLists = new JButton("Alle listen laden");
        this.loadAllLists.addActionListener(this);
        
        this.editList = new JButton("Liste bearbeiten");
        this.editList.addActionListener(this);

        this.deleteList = new JButton("Liste löschen");
        this.deleteList.addActionListener(this);

        this.testApi = new JButton("Teste API Verbindung");
        this.testApi.addActionListener(this);

        this.addList = new JButton("Liste hinzufügen");
        this.addList.addActionListener(this);

        this.apiInfo = new JLabel();
        this.errorInfo = new JLabel();
        this.infoPanel.add(this.apiInfo);
        this.infoPanel.add(this.errorInfo);

        this.buttonPanel.add(this.loadAllLists);
        this.buttonPanel.add(this.addList);
        this.buttonPanel.add(this.editList);
        this.buttonPanel.add(this.deleteList);
        this.buttonPanel.add(this.testApi);

        this.frame.setVisible(true);
    }

    public MainWindow() {
        super("Todolisten Verwaltung");
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

    private void editLists() {
        int index = this.entries.getSelectedIndex();
        String name, guid;

        name = this.lists.get(index).get("name");
        guid = this.lists.get(index).get("id");

        new ListWindow(this.frame, name, guid, this.rc);
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
            // rd = super.rc.getAllLists();
            rd = super.rc.sendHttpRequest(new PostData(dummy), "getAllLists");
        } catch (Exception e) {
            if (e.getMessage() != "" && e.getMessage() != null) {
                this.updateErrorInfo(e.getMessage());
            }
            return;
        }

        this.updateErrorInfo("");

        for (int i = 0; i < rd.getEntries().length; i++) {
            this.lists.add(new HashMap<String,String>());
            this.lists.get(i).put("name", rd.getEntries()[i].getName());
            this.lists.get(i).put("id", rd.getEntries()[i].getId());
            this.lists.get(i).put("description", rd.getEntries()[i].getDescription());
        }

        for (int i = 0; i < this.lists.size(); i++) {
            this.dlm.addElement(this.lists.get(i).get("name"));
        }
        
        this.entries.setModel(this.dlm);
    }

    private void addLists() {
        AddWindow add = new AddWindow(this.frame, "Neue Liste", "", "", false);

        String name = add.getName();

        ResponseData rd;
        if (!name.equals("")) {
            try {
                HashMap<String, String> newList = new HashMap<String, String>();
                newList.put("type", "list");
                newList.put("name", name);
                // rd = super.rc.addNewList(new PostData(newList));
                rd = super.rc.sendHttpRequest(new PostData(newList), "addList");
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
        this.lists.add(newEntry);

        this.updateDlm();
    }

    private void deleteLists() {
        int index = this.entries.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.lists.get(index).get("id");
        try {
            // super.rc.deleteList(new PostData("list", "", "", idToDelete));
            HashMap<String, String> listToDelete = new HashMap<String, String>();
            listToDelete.put("type", "list");
            listToDelete.put("listId", idToDelete);
            listToDelete.put("id", idToDelete);
            super.rc.sendHttpRequest(new PostData(listToDelete), "deleteList");
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
                rd = super.rc.sendHttpRequest(new PostData(entryToDelete), "deleteEntry");

                deleted += rd.getDeleted();
            }

            System.out.println("Deleted: " + deleted);
        } catch (Exception e) {
            if (!e.getMessage().equals("") || e.getMessage() != null) {
                this.updateErrorInfo(e.getMessage());
            }
            return;
        }

        this.lists.remove(index);
        this.updateErrorInfo("");
        this.updateDlm();
    }

    private void updateDlm() {
        this.dlm.removeAllElements();

        for (int i = 0; i < this.lists.size(); i++) {
            this.dlm.addElement(this.lists.get(i).get("name"));
        }
    }
}
