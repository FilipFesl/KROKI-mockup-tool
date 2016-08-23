package kroki.app.action;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.DBJarConnectionDialog;
import kroki.app.importt.ImportDBMetaToProject;
import kroki.app.importt.dbmeta.DBImportSettingsDialog;
import kroki.app.importt.dbmeta.MetaDataRepository;
import kroki.app.utils.StringResource;

public class ImportFromDatabaseAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public ImportFromDatabaseAction() {
		putValue(NAME, StringResource.getStringResource("action.db.import.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.db.import.description"));
	}

	public void actionPerformed(ActionEvent e) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				DBJarConnectionDialog settingsDialog = new DBJarConnectionDialog(null, false);
				settingsDialog.setVisible(true);
				Connection conn = settingsDialog.testConnection(true);
				if(conn != null)
				{
					DBImportSettingsDialog dbImportSettingsDialog = new DBImportSettingsDialog(conn);
					dbImportSettingsDialog.setVisible(true);
					
					ArrayList<String> selectedTables = dbImportSettingsDialog.getTablesToParse();
					String projectName = dbImportSettingsDialog.getProjectName();
					Boolean parseForOriginalDb = dbImportSettingsDialog.isParseForOriginalDb();
					if(projectName != null && selectedTables != null && selectedTables.size() > 0)
					{
						try{
							new ImportDBMetaToProject(MetaDataRepository.getDefault().getTables(), projectName, selectedTables, parseForOriginalDb);
						}catch(Exception e){
							e.printStackTrace(); 
						}
					}else
					{
						JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Importing DB meta aborted.");
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Importing DB meta aborted.");
				}
			}
		});
		thread.start();
	}
}
