import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
/*
 * Created by JFormDesigner on Mon Nov 19 20:34:21 IST 2018
 */


/**
 * @author Subhash Rawat
 */
public class Main extends JFrame implements ActionListener {
    static JTextPane logsText;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Subhash Rawat
    private JButton start_Server;
    private JButton stop_Server;
    private JScrollPane logsPanel;
    private JLabel label;
    private Server server;
    private DBConnector dbConnector;

    public Main() {
        super("Server");
        initComponents();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Main();
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    static void appendToPane(@NotNull JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Subhash Rawat
        start_Server = new JButton();
        stop_Server = new JButton();
        logsPanel = new JScrollPane();
        logsText = new JTextPane();
        label = new JLabel();
        dbConnector = new DBConnector();

        //======== this ========
        setBackground(SystemColor.windowBorder);
        Container contentPane = getContentPane();

        //setContentPane(new JLabel(new ImageIcon("C:\\Users\\Shubzz\\Pictures\\Screenshots\\a.png")));

        //---- start_Server ----
        start_Server.setText("Start");
        start_Server.setIcon(new ImageIcon("C:\\Users\\Shubzz\\Desktop\\Project\\Bank_Server\\resources\\icon.png"));
        start_Server.setToolTipText("Start Server");

        //---- stop_Server ----
        stop_Server.setText("Stop");
        stop_Server.setToolTipText("Stop Server");

        //======== logsPanel ========
        {
            //---- logsText ----
            logsText.setEditable(true);
            //logsText.setEditable(false);
            logsPanel.setViewportView(logsText);
        }

        //---- label ----
        //label.setText("WELCOME");

        label.setIcon(new ImageIcon("C:\\Users\\Shubzz\\Desktop\\Project\\Bank_Server\\resources\\wel_Anin.gif"));//"C:\\Users\\Shubzz\\Desktop\\Project\\Bank_Server\\resources\\wel_Anin.gif"));
        label.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap(39, Short.MAX_VALUE)
                                .addComponent(logsPanel, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                                .addGap(25, 25, 25))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(start_Server, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                                .addComponent(stop_Server, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .addGap(44, 44, 44))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(55, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(stop_Server, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(start_Server, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(logsPanel, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                .addContainerGap(18, Short.MAX_VALUE))
        );
        start_Server.addActionListener(this);
        stop_Server.addActionListener(this);
        stop_Server.setEnabled(false);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stop_Server) {
            //System.out.println("Stop Server clicked");
            start_Server.setEnabled(true);
            stop_Server.setEnabled(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            server.stopServer();
            appendToPane(logsText, "[+] Server Stopped\n", Color.GREEN);
        }
        if (e.getSource() == start_Server) {
            //System.out.println("Start Server clicked");
            server = new Server();
            if (server.startServer()) {
                start_Server.setEnabled(false);
                stop_Server.setEnabled(true);
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                Boolean check=true;
                if (!dbConnector.check()) {
                    check = dbConnector.createDB();
                    if(!check){
                        JOptionPane.showMessageDialog(this,"Unknown Error Occured");
                    }
                }

            }
            appendToPane(logsText, "[+] Server Started\n", Color.GREEN);
        }
    }
}
