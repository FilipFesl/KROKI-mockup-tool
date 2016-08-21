package kroki.app.importt;

import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.importt.dbmeta.model.Table;
import kroki.app.importt.dbmeta.model.Tables;
import kroki.app.utils.uml.ProgressWorker;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.panel.StandardPanel;
import kroki.profil.persistent.PersistentClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.StandardPanelUtil;

public class ImportDBMetaToProject extends ProgressWorker {

	private NamingUtil namingUtil;
	
	private Tables tables;
	private String projectName;
	private ArrayList<String> tablesToParse;
	
	BussinesSubsystem project=null;
	
	public ImportDBMetaToProject(Tables tables, String projectName, ArrayList<String> tablesToParse){
		super();
		this.tables = tables;
		this.tablesToParse = tablesToParse;
		this.projectName = projectName;
		namingUtil = new NamingUtil();
		execute();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		publishText("Starting up! ");
		createKroki();
		if(!isCancelled())
		{
			if(project!=null)
			{
				//project.setFile(file);
				KrokiMockupToolApp.getInstance().getWorkspace().addPackage(project);
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
				publishText("Project for Eclipse UML model created successfully");
				publishText("Importing Eclipse UML diagram finished successfully.");
			}else{
				publishText("Error occurred while creating project from Eclipse UML model.");
				throw new Exception("Error while creating project from Eclipse UML model.");
			}
		}
		return null;
	}
	
	private void createKroki(){
		project=new BussinesSubsystem(projectName, true, ComponentType.MENU, null);
		addIndentation();
		publishText("Project with name "+project.getLabel()+" created");
		addIndentation();
		publishText("Extracting project contents");

		for(String tableName : tablesToParse) {
			Table dbTable = tables.getTable(tableName);
			String name = dbTable.getName();
			
			StandardPanel panel = new StandardPanel();
			StandardPanelUtil.defaultGuiSettings(panel);
			panel.setLabel(name);

			PersistentClass persistent=panel.getPersistentClass();
			panel.getComponent().setName(name);
			persistent.setName(firstUpper(name));
			persistent.setTableName(name);
			persistent.setLabelToCode(false);
					
			project.addOwnedType(panel);
			panel.setUmlPackage(project);
			panel.update();
			publishText("Created "+StandardPanel.class.getSimpleName()+" "+panel.getLabel());
		}
	}
	
	protected String firstUpper(String name){
		String newName=name;
		if(!name.isEmpty())
			newName=newName.substring(0, 1).toUpperCase()+newName.substring(1);
		return newName;
	}
	
	
	
	@Override
	public void done(){
		try {
			get();
			JOptionPane.showMessageDialog(getFrame(), "Importing Database metadata finished successfully.");
		} catch (InterruptedException | ExecutionException e) {
			showErrorMessage(e);
		} catch(CancellationException e){
			//When called cancel method
			//e.printStackTrace();
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
					"Importing Database metadata has been canceled");
			publishErrorText("Import has been canceled");
		} catch (IllegalArgumentException e) {
			publishErrorText("Internal error ocured contact developer");
		} catch(NullPointerException e) {
			publishErrorText("Internal error ocured contact developer");
		} catch (Exception e) {
			showErrorMessage(e);
		}
		super.done();
	}
	
	/**
	 * Shows an error message by calling the
	 * methods {@link #publishText} and
	 * {@link #showError}.
	 * @param e  Exception that contains the error
	 * message
	 */
	private void showErrorMessage(Exception e){
		//Correct the error then try again
		publishErrorText("Error happened while importing");
		publishErrorText(exceptionMessage(e));
		showError(exceptionMessage(e), "Error while importing");
		
		e.printStackTrace();
	}
}
