package kroki.app.importt;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.swing.JOptionPane;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

import kroki.api.commons.ApiCommons;
import kroki.api.util.Util;
import kroki.api.commons.ApiCommons;
import kroki.app.KrokiMockupToolApp;
import kroki.app.importt.dbmeta.model.Column;
import kroki.app.importt.dbmeta.model.Columns;
import kroki.app.importt.dbmeta.model.Table;
import kroki.app.importt.dbmeta.model.Tables;
import kroki.app.utils.uml.ProgressWorker;
import kroki.app.utils.uml.UMLElementsEnum;
import kroki.app.utils.uml.stereotypes.PropertyStereotype;

import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;

import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.persistent.PersistentClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.StandardPanelUtil;
import kroki.profil.utils.UIPropertyUtil;

public class ImportDBMetaToProject extends ProgressWorker {
	
	private Tables tables;
	private String projectName;
	private ArrayList<String> tablesToParse;
	private Boolean parseForOriginalDb;
	
	private HashMap<String, StandardPanel> panels = new HashMap<String, StandardPanel>();
	
	BussinesSubsystem project=null;
	
	private enum DataType {
		String, Integer, Long, BigDecimal, Date, Boolean
	}
	
	private enum Persistent {
		Char, Varchar, Text, Integer, Number, Float, Decimal, Boolean, Date, DateTime, Timestamp, Bit
	}
	
	public ImportDBMetaToProject(Tables tables, String projectName, ArrayList<String> tablesToParse, Boolean parseForOriginalDb){
		super();
		this.tables = tables;
		this.tablesToParse = tablesToParse;
		this.projectName = projectName;
		this.parseForOriginalDb = parseForOriginalDb;

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
				KrokiMockupToolApp.getInstance().getWorkspace().addPackage(project);
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
				publishText("Project for created successfully");
				publishText("Importing database meta finished successfully.");
			}else{
				publishText("Error occurred while creating project from database meta.");
				throw new Exception("Error while creating project from database meta.");
			}
		}
		return null;
	}
	
	private void createKroki(){
		project=new BussinesSubsystem(projectName, true, ComponentType.MENU, null);
		project.setParseForOriginalDB(parseForOriginalDb);
		
		addIndentation();
		publishText("Project with name "+project.getLabel()+" created");
		addIndentation();
		publishText("Extracting project contents");

		for(String tableName : tablesToParse) {
			Table dbTable = tables.getTable(tableName);
			panels.put(tableName, createPanel(dbTable, project)); //create panel from table
		}
		
		for(String tableName : tablesToParse) {
			Table dbTable = tables.getTable(tableName);
			parseZoomForTable(dbTable, panels.get(tableName));
		}
	}

	protected String firstUpper(String name){
		String newName=name;
		if(!name.isEmpty())
			newName=newName.substring(0, 1).toUpperCase()+newName.substring(1);
		return newName;
	}

	protected StandardPanel createPanel(Table dbTable, BussinesSubsystem project){
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
		
		parseTableColumns(dbTable.getColumns(), panel);
		
		return panel;
	}
	
	protected void parseTableColumns(Columns columns, VisibleClass panel){
		Vector<Column> vec = columns.getColumnsVec();
		for(int i=0; i < vec.size(); i++){
			Column column = vec.get(i);
			if(!column.isPartOfFK()){
				createVisiblePropertyFromColumn(column, panel);
			}
		}
	}

	protected void parseZoomForTable(Table table, VisibleClass startingPanel) {
		Vector<Column> vec = table.getColumns().getColumnsVec();
		for(int i=0; i < vec.size(); i++){
			Column column = vec.get(i);
			if(column.isPartOfFK()) {
				if(panels.containsKey(column.getFkTableName())) {
					StandardPanel targetPanel = panels.get(column.getFkTableName());

					ElementsGroup element = (ElementsGroup) startingPanel.getVisibleElementList().get(Util.STANDARD_PANEL_PROPERTIES);

					VisibleProperty property = new VisibleProperty(column.getName(), true,  ComponentType.COMBO_BOX);
					Zoom zoom = new Zoom(property);
					zoom.setActivationPanel(startingPanel);
					zoom.setTargetPanel((VisibleClass) targetPanel);

					ElementsGroupUtil.addVisibleElement(element, zoom);
					UIPropertyUtil.addVisibleElement(startingPanel,zoom);

				} else {
					createVisiblePropertyFromColumn(column, startingPanel);
				}

			}
		}
	}
	
	private void createVisiblePropertyFromColumn(Column column, VisibleClass panel) {
		if(column.isPartOfPK() && !parseForOriginalDb) {
			return; //id is created in template
		}
		
		ComponentType type = ComponentType.TEXT_FIELD;
		VisibleProperty property = ApiCommons.makeVisibleProperty(column.getName(), true, type, panel);		
		setDataTypeForProperty(column.getSQLDataType(), property);
	
		if(property.getDataType() == null && property.getPersistentType() == null) {
			ApiCommons.removeVisibleElement(panel, property);
			return;
		}

		property.setLabelToCode(false);
		property.setLength(column.getLength());
		property.setPrecision(column.getDecimalDigits());
		if(column.isNullable()){
			property.setLower(1);
		} else {
			property.setLower(0);
		}
		if(column.isPartOfPK()) {
			property.setPrimary(true);
		}
		
		if(parseForOriginalDb) {
			property.setColumnLabel(column.getName());
		}
	}
	
	protected void setDataTypeForProperty(int sqlDataType, VisibleProperty property) {
		DataType dataType = null;
		Persistent persistentType = null;
		ComponentType componentType = null;
		switch(sqlDataType){
			//text
			case Types.CHAR: {
				dataType = DataType.String;
				persistentType = Persistent.Char;
			}; break;
			
			case Types.NCHAR : {
				dataType = DataType.String;
				persistentType = Persistent.Char;
			}; break;
			
			case Types.VARCHAR: {
				dataType = DataType.String;
				persistentType = Persistent.Varchar;
			}; break;
			
			case Types.NVARCHAR: {
				dataType = DataType.String;
				persistentType = Persistent.Varchar;
			}; break;
			
			case Types.LONGNVARCHAR: {
				dataType = DataType.String;
				persistentType = Persistent.Text;
			}; break;
			
			case Types.LONGVARCHAR: {
				dataType = DataType.String;
				persistentType = Persistent.Text;
			}; break;
			
			//boolean
			case Types.BOOLEAN: {
				dataType = DataType.Boolean;
				componentType = ComponentType.CHECK_BOX;
				persistentType = Persistent.Bit;
			}; break;
			case Types.BIT: {
				dataType = DataType.Boolean;
				componentType = ComponentType.CHECK_BOX;
				persistentType = Persistent.Bit;
			}; break;
			
			//number
			case Types.BIGINT: {
				dataType = DataType.Long;
				persistentType = Persistent.Number;
			}; break;
			case Types.DECIMAL: {
				dataType = DataType.BigDecimal;
				persistentType = Persistent.Decimal;
			}; break;
			case Types.DOUBLE: {
				dataType = DataType.BigDecimal;
				persistentType = Persistent.Decimal;
			}; break;
			case Types.FLOAT: {
				dataType = DataType.BigDecimal;
				persistentType = Persistent.Float;
			}; break;
			case Types.INTEGER: {
				dataType = DataType.Integer;
				persistentType = Persistent.Integer;
			}; break;
			case Types.NUMERIC: {
				dataType = DataType.BigDecimal;
				persistentType = Persistent.Decimal;
			}; break;
			case Types.REAL: {
				dataType = DataType.BigDecimal;
				persistentType = Persistent.Decimal;
			}; break;			
			case Types.SMALLINT: {
				dataType = DataType.Integer;
				persistentType = Persistent.Integer;
			}; break;
			case Types.TINYINT: {
				dataType = DataType.Integer;
				persistentType = Persistent.Integer;
			}; break;
			
			case Types.DATE: {
				dataType = DataType.Date;
				persistentType = Persistent.Date;
			}; break;
			case Types.TIME: {
				dataType = DataType.Date;
				persistentType = Persistent.DateTime;
			}; break;
			case Types.TIMESTAMP: {
				dataType = DataType.Date;
				persistentType = Persistent.Timestamp;
			}; break;

			//unmapped
//			case Types.ARRAY: {}; break;
//			case Types.CLOB: {}; break;
//			case Types.NCLOB : {}; break;
//			case Types.NULL: {}; break;
//			case Types.OTHER: {}; break;
//			case Types.REF: {}; break;
//			case Types.SQLXML: {}; break;
//			case Types.STRUCT: {}; break;
//			case Types.ROWID: {}; break;
//			case Types.BLOB: {}; break;
//			case Types.BINARY: {}; break;
//			case Types.LONGVARBINARY: {}; break;
//			case Types.DATALINK: {}; break;
//			case Types.DISTINCT: {}; break;
//			case Types.JAVA_OBJECT: {}; break;
//			case Types.VARBINARY: {}; break;
			default : {
				dataType = null;
				persistentType = null;
			}
		}

		property.setPersistentType(persistentType == null ? null : persistentType.toString());
		property.setDataType(dataType == null ? null :dataType.toString());
		
		if(componentType != null)
			property.setComponentType(componentType);
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
