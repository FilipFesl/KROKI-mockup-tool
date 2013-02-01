/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.common.copy.DeepCopy;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class OpenProjectAction extends AbstractAction {

    public OpenProjectAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.openProject.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.openProject.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.openProject.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.openProject.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        int retValue = jfc.showOpenDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
        if (retValue == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            System.out.println("opening from file: " + file.getAbsolutePath());
            BussinesSubsystem bussinesSubsystem = null;
            try {
                bussinesSubsystem = (BussinesSubsystem) DeepCopy.open(file);
                //KrokiMockupToolApp.getInstance().getWorkspace().addBussinesSubsystem(bussinesSubsystem);
                KrokiMockupToolApp.getInstance().getWorkspace().addPackage(bussinesSubsystem);
                KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Opening failed.");
            }

        } else {
            System.out.println("opening canceled: ");
        }
    }
}
