package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.DBJarConnectionDialog;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

public class DBJarConnectionDialogAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public DBJarConnectionDialogAction() {
		putValue(NAME, StringResource.getStringResource("action.dbsetting.jdbc.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.dbsetting.jdbc.description"));
	}

	public void actionPerformed(ActionEvent e) {

		BussinesSubsystem proj = null;
		try {
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}
		} catch (NullPointerException e2) {
		}


		if(proj!= null) {
			DBJarConnectionDialog settingsDialog = new DBJarConnectionDialog(proj, true);
			settingsDialog.setVisible(true);
		}else {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}

}
