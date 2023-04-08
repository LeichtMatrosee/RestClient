package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class ClientGui {

    // Gui Komponenten
    private JFrame frame;
    private Container pane;
    private GroupLayout gl;
    private JButton button;
    private JMenuBar mb;


    // Rest Komponenten
    private RestCommunicator rc;

    public ClientGui() throws Error {
        try {
            this.rc = new RestCommunicator();
        } catch (Exception e) {
            throw new Error("Could not construct the RestCommunicator!", e);
        }
    }

    public void process() {
        this.buildGui();
    }

    private void buildGui() {
        this.buildFrame();
        this.buildPane();
        this.buildMenu();
    }

    private void buildFrame() {
        this.frame = new JFrame();
        this.frame.setTitle("Todo Listen Verwaltung");
        this.frame.setSize(800, 800);
        this.frame.setLocale(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        this.frame.setResizable(true);
    }

    private void buildPane() {
        this.pane = this.frame.getContentPane();
        this.gl = new GroupLayout(pane);
        this.pane.setLayout(this.gl);
    }

    private void buildMenu() {
        this.mb = new JMenuBar();
    }

    private void buildButtons() {
        this.button = new JButton("Bekomme alle Todo Listen");
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResponseData rd = getAllLists();
                String entries = "";

                for (int i = 0; i < rd.getEntries().length; i++) {
                    entries += "" + rd.getEntries()[i].getName() + ";";
                }

                System.out.println(entries);
            }
        });
        this.gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(button));
        this.gl.setVerticalGroup(gl.createSequentialGroup().addComponent(button));

        this.gl.setAutoCreateContainerGaps(true);
    }

    private ResponseData getAllLists() {
        try {
            return this.rc.getAllLists();
        } catch (Exception e) {
            return new ResponseData(e);
        }
    }

}
