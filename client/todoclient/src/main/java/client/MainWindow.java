package client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

public class MainWindow extends TodoFrame {
    // Buttons for main Window
    JButton loadAllLists;
    JButton editList;
    JButton deleteList;
    JButton testApi;
    JButton addList;

    // Config
    String lastError;
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
        }
    }

    private void getAllLists() {
        ResponseData rd;
        this.dlm.clear();

        try {
            rd = super.rc.getAllLists();
        } catch (Exception e) {
            if (e.getMessage() != "" && e.getMessage() != null) {
                this.lastError = e.getMessage();
            }
            return;
        }

        for (int i = 0; i < rd.getEntries().length; i++) {
            this.lists.add(new HashMap<String,String>());
            this.lists.get(i).put("name", rd.getEntries()[i].getName());
            this.lists.get(i).put("id", rd.getEntries()[i].getId());
            this.lists.get(i).put("description", rd.getEntries()[i].getDescription());
        }

        for (int i = 0; i < this.lists.size(); i++) {
            this.dlm.addElement(this.lists.get(i).get("name"));
        }
        this.listScroller.setMaximum(rd.getEntries().length);
        this.entries.setModel(this.dlm);
    }

    private void addLists() {
        AddWindow add = new AddWindow(this.frame, "Neue Liste");

        String name = add.getName();
        String description = add.getDescription();

        ResponseData rd;
        if (!name.equals("")) {
            try {
                rd = super.rc.addNewList(new PostData("list", name, description));
            } catch (Exception e) {
                if (!e.getMessage().equals("") || e.getMessage() != null) {
                    this.lastError = e.getMessage();
                }
                return;
            }
        } else {
            return;
        }

        this.dlm.addElement(rd.getEntries()[0].getName());
        this.entries.setModel(this.dlm);
    }

    private void deleteLists() {
        int index = this.entries.getSelectedIndex();
        if (index == -1) return;

        String idToDelete = this.lists.get(index).get("id");
        try {
            super.rc.deleteList(new PostData("list", "", "", idToDelete));
        } catch (Exception e) {
            if (!e.getMessage().equals("") || e.getMessage() != null) {
                this.lastError = e.getMessage();
            }
            return;
        }

        this.getAllLists();
    }
}
