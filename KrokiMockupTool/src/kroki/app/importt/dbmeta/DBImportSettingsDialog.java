package kroki.app.importt.dbmeta;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.checkboxlist.CheckBoxList;
import kroki.app.importt.dbmeta.model.Table;
import kroki.app.importt.dbmeta.model.Tables;
import kroki.app.utils.ImageResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.subsystem.BussinesSubsystem;

public class DBImportSettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private Connection conn;
	
	private JLabel lblTitle;

	private JButton btnOK;
	private JButton btnCancel;
	private JScrollPane scrollPane;
	private JLabel lblProjectName;
	private JTextField tfProjectName;	
	
	private JCheckBox cbExportRealDb;
	
	private JButton btnSelectAll;
	private JButton btnSelectNone;
	
	private CheckBoxList cbList;
	
	private ArrayList<String> tablesToParse;
	private String projectName;
	private Boolean parseForOriginalDB;
	
	public DBImportSettingsDialog(Connection conn) {
		this.conn = conn;
		
		setSize(350, 500);
		setLocationRelativeTo(null);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Database connection settings");
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
		setIconImage(headerIcon);

		setLayout(new MigLayout(
				"",
				"[][right, grow]",
				""));
		initGUI();
	}
	
	public void initGUI() {

		lblTitle = new JLabel("Configure DB import settings");
		lblTitle.setFont(new Font("sansserif", Font.PLAIN, 16));

		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkInputAndCallParseFromDB();
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBImportSettingsDialog.this.dispose();
			}
		});
		
		cbList = getCheckboxListWithData();
		
		btnSelectAll = new JButton("Select all");
		btnSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cbList.selectAll();
			}
		});
		
		btnSelectNone = new JButton("Select none");
		btnSelectNone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cbList.selectNone();
			}
		});
		
		cbExportRealDb = new JCheckBox("Configure project to export for same DB?");
				
		Dimension separatorDim = new Dimension(350, 5);
		JSeparator topSep = new JSeparator(JSeparator.HORIZONTAL);
		topSep.setPreferredSize(separatorDim);
		JSeparator bottomSep = new JSeparator(JSeparator.HORIZONTAL);
		bottomSep.setPreferredSize(separatorDim);

		add(lblTitle, "span 2, wrap, center, gaptop10, gapbottom 10");
		add(topSep, "span 2, wrap, gapbottom 5, gaptop 5, growx");

		add(btnSelectAll, "split 2, gaptop 5");
		add(btnSelectNone, "wrap");
		
		scrollPane = new JScrollPane(cbList);
		add(scrollPane, "span 2, wrap, gapbottom 5, gaptop 5, growx");
	
		lblProjectName = new JLabel("Project name");
		tfProjectName = new JTextField(20);
		
		add(cbExportRealDb, "wrap");
		
		add(lblProjectName);
		add(tfProjectName, "wrap");

		add(bottomSep, "span 2, wrap, gaptop 5, growx");
		add(new JLabel());
		add(btnOK, "split 2, gaptop 5");
		add(btnCancel);

		pack();
	}
	
	private CheckBoxList getCheckboxListWithData(){
		try {
			MetaDataRepository.getDefault().readFromDB(conn);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Tables tables = MetaDataRepository.getDefault().getTables();
		ArrayList<JCheckBox> tableNames = new ArrayList<JCheckBox>();
		for (Object table : tables.getTablesList()) {
	        if (table instanceof Table) {
	        	Table t = (Table) table;
	        	JCheckBox cb = new JCheckBox(t.getName());
	        	cb.setSelected(true);
	            tableNames.add(cb);
	        }
	    }
		CheckBoxList cbList = new CheckBoxList();
		cbList.setListData(tableNames.toArray(new JCheckBox[tableNames.size()]));
		return cbList;
	}

	private void checkInputAndCallParseFromDB(){
		NamingUtil cc =  new NamingUtil();
        BussinesSubsystem pr = KrokiMockupToolApp.getInstance().findProject(tfProjectName.getText());
        if(pr != null) {
        	JOptionPane.showMessageDialog(DBImportSettingsDialog.this, "Project with specified name allready exists!");
        }else {
        	if(tfProjectName.getText().equals("")) {
        		JOptionPane.showMessageDialog(DBImportSettingsDialog.this, "Project name cannot be empty!");
        	}else if(!cc.checkName(tfProjectName.getText())) {
        		JOptionPane.showMessageDialog(DBImportSettingsDialog.this, "Project name can only start with a letter!");
        	}else {
        		projectName = tfProjectName.getText();
        		tablesToParse = cbList.getSelectedCheckboxNames();
        		parseForOriginalDB = cbExportRealDb.isSelected();
				dispose();
        	}
        }
	}

	public ArrayList<String> getTablesToParse() {
		return tablesToParse;
	}
	
	public String getProjectName(){
		return projectName;
	}
	
	public Boolean isParseForOriginalDb(){
		return parseForOriginalDB;
	}
}
