package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class TodoFrame implements AdjustmentListener, ActionListener {
    // GUI Components
    protected JFrame frame;
    protected JPanel topPanel;
    protected JPanel infoPanel;
    protected JPanel buttonPanel;
    protected JPanel listPanel;
    protected JPanel list;
    protected JList<String> entries;
    protected DefaultListModel<String> dlm;
    protected JScrollPane listScroller;

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
        this.frame = new JFrame();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.frame.setTitle(title);
        this.frame.setSize(600, 600);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(true);
        this.frame.setLayout(new BorderLayout());

        // Configure Panel for Buttons
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

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
        this.list.add(this.entries);
        this.entries.setLayoutOrientation(JList.VERTICAL);

        // Configure Scrollbar
        this.listScroller = new JScrollPane();
        this.listScroller.setViewportView(entries);
        this.list.add(this.listScroller, BorderLayout.CENTER);

        // Add entries to listPanel
        this.listPanel.add(this.list);

        this.infoPanel = new JPanel();
        this.infoPanel.setLayout(new FlowLayout());

        this.topPanel = new JPanel();
        this.topPanel.setLayout(new BorderLayout());

        this.topPanel.add(this.infoPanel, BorderLayout.NORTH);
        this.topPanel.add(this.buttonPanel, BorderLayout.SOUTH);
    
        this.frame.add(this.topPanel, BorderLayout.NORTH);
        this.frame.add(this.listPanel);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent arg0) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public String getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

}
