package client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ClientGui{

    // Gui Components
    // Frame, menubar
    private JFrame frame;
    private JMenuBar mBar;

    // Menu Components
    private JMenu config;

    // Rest Communication fields
    private RestCommunicator rc;
    private String host = "localhost";
    private int port = 5000;

    public ClientGui() {
        // this.rc = new RestCommunicator(this.host, this.port);        
    }

    public void process() {
        this.buildGui();
        this.frame.setVisible(true);
    }

    private void buildGui() {
        // Build the Window
        this.frame = new JFrame("Todo List Manager");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(800, 800);

        this.buildMenu();
    }

    private void buildMenu() {
        this.mBar = new JMenuBar();
        Border mBorder = new LineBorder(Color.GRAY);
        this.mBar.setBorder(mBorder);

        this.config = new JMenu("Einstellungen");
        this.config.add(new JMenuItem("Rest Einstellungen"));
        
        this.mBar.add(this.config);
        
        // this.frame.setMenuBar(this.mBar);
    }
}
