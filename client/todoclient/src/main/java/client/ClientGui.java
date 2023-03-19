package client;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientGui{

    // Gui Components
    // Frame, panels
    private JFrame frame;
    private JPanel panel;
    
    public ClientGui() {
        this.frame = new JFrame();
        this.panel = new JPanel();

        this.frame.add(this.panel);
    }

    public void process() {
        this.frame.setVisible(true);
    }
}
