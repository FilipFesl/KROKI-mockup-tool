package kroki.app.gui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import kroki.app.KrokiMockupToolApp;
import kroki.app.jarloader.JarClassLoader;
import kroki.app.utils.ImageResource;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.DatabaseProps;

public class DBJarConnectionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;

	private JLabel lblJarFile;
	private JTextField tfJarFilePath;
	private JButton btnJarChooser;

	private JLabel lblDriver;
	private JTextField tfDriver;

	private JLabel lblConnectionString;
	private JTextField tfConnectionString;

	private JLabel lblDialectString;
	private JTextField tfDialectString;
	
	private JLabel lblUsername;
	private JTextField tfUsername;

	private JLabel lblPassword;
	private JPasswordField pfPassword;

	private JPanel testPane;
	private JLabel lblStatus;
	private JButton btnTest;
	private JButton btnOK;
	private JButton btnCancel;

	private Boolean forKrokiProject;
	
	public DBJarConnectionDialog(BussinesSubsystem project, Boolean forKrokiProject) {
		this.forKrokiProject = forKrokiProject;
		
		setSize(300, 330);
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
		initGUI(project);
	}

	public void initGUI(final BussinesSubsystem project) {
		if(forKrokiProject){
			lblTitle = new JLabel("Database JAR connection");			
		} else {
			lblTitle = new JLabel("Database for import connection parameters");
		}
		lblTitle.setFont(new Font("sansserif", Font.PLAIN, 16));

		lblJarFile = new JLabel("Jar file");
		tfJarFilePath = new JTextField(30);
		tfJarFilePath.setEnabled(false);
		tfJarFilePath.setText("C:\\mysql-connector.jar");
		btnJarChooser = new JButton("Choose JAR");
		btnJarChooser.setToolTipText("Choose JAR file from file system");

		btnJarChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBJarConnectionDialog.this.setVisible(false);
				JFileChooser jfc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("JAR files", "jar");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(filter);
				int retValue = jfc.showOpenDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					tfJarFilePath.setText(file.getAbsolutePath());
				}
				DBJarConnectionDialog.this.setVisible(true);
			}
		});

		lblDriver = new JLabel("Driver class");
		tfDriver = new JTextField(30);
		tfDriver.setText("com.mysql.jdbc.Driver");

		lblConnectionString = new JLabel("Connection URL");
		tfConnectionString = new JTextField(50);
		tfConnectionString.setText("jdbc:mysql://localhost:3306/filip_kroki");

		lblDialectString = new JLabel("Dialect");
		tfDialectString = new JTextField(50);
		tfDialectString.setText("org.hibernate.dialect.MySQLDialect");
		
		lblUsername = new JLabel("Username");
		tfUsername = new JTextField(20);
		tfUsername.setText("root");

		lblPassword = new JLabel("Password");
		pfPassword = new JPasswordField(20);
		pfPassword.setText("root");

		testPane = new JPanel();
		lblStatus = new JLabel();
		btnTest = new JButton("Test");

		btnTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				testConnection(false);
			}
		});

		testPane.setLayout(new MigLayout(
				"",
				"[left, grow][right]",
				""));
		testPane.add(lblStatus);
		testPane.add(btnTest, "dock east");

		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String stat = checkRequiredFields();
				if(!stat.equals("OK")) {
					displayMessage(stat, true);
				}else {
					if(forKrokiProject && project!=null) {
						assignSettingsToProject(project);
					} 
					dispose();
				}
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBJarConnectionDialog.this.dispose();
				DBJarConnectionDialog.this.setVisible(false);
			}
		});


		Dimension separatorDim = new Dimension(320, 5);
		JSeparator topSep = new JSeparator(JSeparator.HORIZONTAL);
		topSep.setPreferredSize(separatorDim);
		JSeparator middleSep = new JSeparator(JSeparator.HORIZONTAL);
		middleSep.setPreferredSize(separatorDim);
		JSeparator bottomSep = new JSeparator(JSeparator.HORIZONTAL);
		bottomSep.setPreferredSize(separatorDim);

		add(lblTitle, "span 2, wrap, center, gaptop10, gapbottom 10");

		add(topSep, "span 2, wrap, gapbottom 5, gaptop 5, growx");

		add(lblJarFile);
		add(tfJarFilePath, "split 2");
		add(btnJarChooser, "wrap");

		add(lblDriver);
		add(tfDriver, "wrap, growx");

		add(lblConnectionString);
		add(tfConnectionString, "wrap, growx");

		add(lblDialectString);
		add(tfDialectString, "wrap, growx");
		
		add(lblUsername);
		add(tfUsername, "wrap");

		add(lblPassword);
		add(pfPassword, "wrap");

		add(middleSep, "span 2, wrap, gaptop 5, growx");
		add(testPane, "wrap, span 2, grow");
		add(bottomSep, "span 2, wrap, gaptop 5, growx");
		add(new JLabel());
		add(btnOK, "split 2, gaptop 5");
		add(btnCancel);

		pack();
	}


	public void assignSettingsToProject(BussinesSubsystem project) {
		Connection conn = testConnection(true);
		if(conn!=null){
			try{
				String driver = tfDriver.getText();
				String jdbcURL = tfConnectionString.getText();
				String username = tfUsername.getText();
				String password = new String(pfPassword.getPassword());
				String dialect = tfDialectString.getText();
				String jarLocation = tfJarFilePath.getText();
				
				if(dialect.startsWith("org.hibernate.dialect.")) {
					dialect = dialect.substring("org.hibernate.dialect.".length());
				}
				
				DatabaseProps props = new DatabaseProps(driver,jdbcURL,username,password,dialect,jarLocation);
				project.setDBConnectionProps(props);
	
				conn.close();
				DBJarConnectionDialog.this.dispose();
				DBJarConnectionDialog.this.setVisible(false);
			}catch(SQLException e){}
		} else {
			displayMessage("Error getting connection", true);
		}
	}

	public Connection testConnection(boolean getConn) {
		String jarFileName = tfJarFilePath.getText();
		Class cl = null;
		try {
			JarClassLoader jcl = new JarClassLoader(jarFileName);
			cl = jcl.loadClass(tfDriver.getText(), true);
		}catch(ClassNotFoundException e){
			displayMessage("Driver class not found!", true);
		}catch(IOException f){
			displayMessage("Unable to load jar, check path!", true);
		}
		if(cl != null){
			String url = tfConnectionString.getText();

			String userName = tfUsername.getText();
			String password = new String(pfPassword.getPassword());
			Properties prop = new Properties();
			prop.put("user", userName);
			prop.put("password", password);
			prop.put("dialect", tfDialectString.getText());
			
			Connection conn = null;
			try{

				Driver drv = (Driver) cl.newInstance();
				conn = drv.connect(url, prop);
				if(conn==null){
					displayMessage("Error retriving connection",true);
				}else{
					if(getConn){
						return conn;
					}
					conn.close();
					displayMessage("Your settings seem ok.", false);
				}

			}catch (InstantiationException | IllegalAccessException e) {
				displayMessage("Driver class not found!", true);
			} catch (SQLException e) {
				if(e.getMessage().startsWith("Communication")) {
					displayMessage("Link error! Check host and port settings!", true);
				}else {
					displayMessage(e.getMessage(), true);
				}
			}
			btnTest.setEnabled(true);
			return conn;
		}
		return null;
	}

	public void displayMessage(String message, boolean error) {
		if(error) {
			lblStatus.setForeground(Color.RED);
		}else {
			lblStatus.setForeground(new Color(0, 102, 51));
		}
		lblStatus.setText("<html><p>" + message + "</p></html>");
	}

	public String checkRequiredFields() {
		String status = "OK";
		if("".equals(tfConnectionString)) {
			status = "You must provide connection URL!";
		}
		if("".equals(tfDialectString)) {
			status = "You must provide connection dialect!";
		}
		return status;
	}
}
