package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class represents the menubar of the application, implementing very option.
 * @author LeichtMatrosee
 * @version 1.0
 */
public class Menubar extends JMenuBar implements ActionListener {
    /**
     * Menu containing all settings. Contains {@link #apiSettings}
     */
    private JMenu settings;
    /**
     * MenuItem managing api settings like port and host. Embedded in {@link #settings}
     */
    private JMenuItem apiSettings;

    /**
     * Host of the RestCommunicator instance of the TodoFrame implementing this class
     */
    private String host;
    /**
     * Port of the RestCommunicator instance of the TodoFrame implementing this class
     */
    private int port;

    /**
     * Parent of the instance of this class. Probably deprecated, not sure yet
     */
    private JFrame parentFrame;

    /**
     * Default constructor for this class. Builds the entire JMenuBar structure.
     * @param parentFrame Frame that implements this menu
     * @param host Current host for the RestCommunicator of the parent frame
     * @param port Current port for the RestCommunicator of the parent frame
     */
    public Menubar(JFrame parentFrame, String host, int port) {
        super();
        
        // Build general menu for settings
        this.settings = new JMenu("Einstellungen");
     
        // Build the API Settings Menu
        this.apiSettings = new JMenuItem("API Einstellungen");
        this.apiSettings.addActionListener(this);
        this.settings.add(this.apiSettings);
        this.add(this.settings);

        this.host = host;
        this.port = port;

        this.parentFrame = parentFrame;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.apiSettings) {
            this.showApiSettingWindow();
        }
    }

    /**
     * Shows a new Dialog in which the user can input host and port of the API.
     * Triggers an ActionEvent, when the window is disposed
     */
    private void showApiSettingWindow() {
        JDialog diag = new JDialog(this.parentFrame, "API Einstellungen", true);
        diag.setLayout(new BorderLayout());
        diag.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JLabel host = new JLabel("Host");
        JLabel port = new JLabel("Port");

        JTextField hostField = new JTextField();
        hostField.setText(this.host);
        JTextField portField = new JTextField();
        portField.setText("" + this.port);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));

        panel.add(host);
        panel.add(hostField);
        panel.add(port);
        panel.add(portField);
        
        diag.add(panel);
        diag.pack();
        diag.setVisible(true);

        this.host = hostField.getText();
        this.port = Integer.parseInt(portField.getText());

        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "editedSettings");
        ((TodoFrame) (this.getParent().getParent().getParent())).actionPerformed(e);
    }

    /**
     * Retrieves the host of the API that was passed in the constructor.
     * @return Host of the API.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Sets the host of the API.
     * @param host Host of the API
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Retrieves the port of the API that was passed in the constructor.
     * @return Port of the API.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets the port of the API.
     * @param port Port of the API.
     */
    public void setPort(int port) {
        this.port = port;
    }

}
