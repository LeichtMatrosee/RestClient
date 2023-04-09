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
import javax.swing.JScrollBar;
import javax.swing.UIManager;

public class TodoFrame implements AdjustmentListener, ActionListener {
    // GUI Components
    protected JFrame frame;
    protected JPanel buttonPanel;
    protected JPanel listPanel;
    protected JPanel list;
    protected JList<String> entries;
    protected DefaultListModel<String> dlm;
    protected JScrollBar listScroller;

    // Configuration
    private String bgColor = "green";

    // Todo List Information
    private String[] listValues;

    // Api Komponenten
    RestCommunicator rc;

    public TodoFrame(String title) {
        try {
            this.rc = new RestCommunicator();
        } catch (Exception e) {
            return;
        } 
        
        this.listValues = new String[0];

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
        this.frame.setResizable(false);
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

        // Configure Scrollbar
        this.listScroller = new JScrollBar(JScrollBar.VERTICAL);
        this.listScroller.setMinimum(0);
        this.listScroller.setMaximum(this.listValues.length);
        this.listScroller.addAdjustmentListener(this);
        
        this.listPanel.add(this.listScroller, BorderLayout.EAST);


        // Add entries to listPanel
        this.listPanel.add(this.list);
    
        this.frame.add(this.listPanel);
        this.frame.add(this.buttonPanel, BorderLayout.NORTH);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent arg0) {
        if (arg0.getSource() == this.listScroller) {
            System.out.println(this.listScroller.getValue());
        }
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

    public String[] getListValues() {
        return this.listValues;
    }

    public void setListValues(String[] listValues) {
        this.listValues = listValues;
    }

}
