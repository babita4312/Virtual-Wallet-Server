import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Fri Nov 30 22:58:26 IST 2018
 */


/**
 * @author Subhash Rawat
 */
public class MainGUI extends JFrame {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Subhash Rawat
    private JButton start_Server;
    private JButton stop_Server;
    private JScrollPane logsPanel;
    private JTextArea logsText;
    private JLabel label;
    public MainGUI() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Subhash Rawat
        start_Server = new JButton();
        stop_Server = new JButton();
        logsPanel = new JScrollPane();
        logsText = new JTextArea();
        label = new JLabel();

        //======== this ========
        setBackground(SystemColor.windowBorder);
        Container contentPane = getContentPane();

        //---- start_Server ----
        start_Server.setText("Start");
        start_Server.setIcon(new ImageIcon("C:\\Users\\Shubzz\\Desktop\\Project\\Bank_Server\\resources\\icon.png"));

        //---- stop_Server ----
        stop_Server.setText("Stop");

        //======== logsPanel ========
        {

            //---- logsText ----
            logsText.setEditable(false);
            logsPanel.setViewportView(logsText);
        }

        //---- label ----
        label.setText("WELCOME");
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
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
