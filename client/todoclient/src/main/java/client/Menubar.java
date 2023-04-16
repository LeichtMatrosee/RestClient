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

public class Menubar extends JMenuBar implements ActionListener {
    private JMenu settings;
    private JMenuItem apiSettings;

    private String host;
    private int port;

    private JFrame parentFrame;

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
     * @deprecated
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

        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Edited settings");
        this.getParent().getParent().getParent().dispatchEvent(e);
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
