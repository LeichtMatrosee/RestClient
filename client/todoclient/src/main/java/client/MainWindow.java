package client;

import java.awt.event.ActionEvent;

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

    public MainWindow(String title) {
        super(title);
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
            this.addList();
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
            this.dlm.addElement(rd.getEntries()[i].getName());
        }
        this.listScroller.setMaximum(rd.getEntries().length);
    }

    private void addList() {
        AddWindow add = new AddWindow(this.frame, "Neue Liste");

        String name = add.getName();
        String description = add.getDescription();

        ResponseData rd;
        if (!name.equals("")) {
            try {
                rd = this.rc.addNewList(new PostData("list", name, description));
            } catch (Exception e) {
                this.lastError = e.getMessage();
                return;
            }
        } else {
            return;
        }

        this.dlm.addElement(rd.getEntries()[0].getName());
    }
}
