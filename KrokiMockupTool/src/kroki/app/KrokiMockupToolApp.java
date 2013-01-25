/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import kroki.app.controller.TreeController;
import kroki.app.controller.TabbedPaneController;
import kroki.app.gui.GuiManager;
import kroki.app.model.ProjectHierarchyModel;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.app.utils.KrokiLookAndFeel;
import kroki.app.utils.StringResource;
import kroki.app.utils.TypeComponentMapper;

/**
 *
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 * ovo je sad izmenjeno
 */
public class KrokiMockupToolApp {

    /**
     *
     */
    private static KrokiMockupToolApp krokiMockupApp;
    private KrokiMockupToolFrame krokiMockupToolFrame;
    private TabbedPaneController tabbedPaneController;
    private TreeController projectHierarchyController;
    private ProjectHierarchyModel projectHierarchyModel;
    private Workspace workspace;
    private GuiManager guiManager;
    private static KrokiMockupToolSplashScreen splash;

    public KrokiMockupToolApp() {


        KrokiLookAndFeel.setLookAndFeel();
        guiManager = new GuiManager();
        
        splash  = new KrokiMockupToolSplashScreen();

        krokiMockupToolFrame = new KrokiMockupToolFrame(guiManager);
        tabbedPaneController = new TabbedPaneController(krokiMockupToolFrame.getCanvasTabbedPane());
        workspace = new Workspace();

        JTree projectHierarchy = krokiMockupToolFrame.getTree();

        projectHierarchyController = new TreeController(projectHierarchy, workspace);
        projectHierarchyModel = new ProjectHierarchyModel(projectHierarchy, workspace);

        projectHierarchy.addMouseListener(projectHierarchyController);
        projectHierarchy.addKeyListener(projectHierarchyController);
        projectHierarchy.addTreeSelectionListener(projectHierarchyController);
        projectHierarchyModel.addTreeModelListener(projectHierarchyController);
        projectHierarchy.setModel(projectHierarchyModel);

        //PODESAVANJE IKONICA...
        //TODO:Videti gde se ovo moze izmestiti
        ImageIcon leafIcon = new ImageIcon(ImageResource.getImageResource("tree.leaf.icon"));
        ImageIcon openIcon = new ImageIcon(ImageResource.getImageResource("tree.open.icon"));
        ImageIcon closedIcon = new ImageIcon(ImageResource.getImageResource("tree.closed.icon"));

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();//{
//
//            @Override
//            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
//
//                setIconAndToolTip(node.getUserObject(), tree);
//
//                return this;
//            }
//
//            private void setIconAndToolTip(Object userObject, JTree tree) {
//
//                if(userObject instanceof BussinesSubsystem){
//                    setOpenIcon(openIcon);
//                    setClosedIcon(closedIcon);
//                }
//                if(userObject instanceof VisibleClass){
//                    setIcon(leafIcon);
//                }
//            }
//
//
//        };
        renderer.setLeafIcon(leafIcon);
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        projectHierarchy.setCellRenderer(renderer);
        projectHierarchy.setEditable(true);
    }

    public static KrokiMockupToolApp getInstance() {
        if (krokiMockupApp == null) {
            krokiMockupApp = new KrokiMockupToolApp();
        }
        return krokiMockupApp;
    }

    public void launch() {
    	splash.showSplashAndExit();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                krokiMockupToolFrame.pack();
                GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
                krokiMockupToolFrame.setMaximizedBounds(e.getMaximumWindowBounds());
                krokiMockupToolFrame.setMinimumSize(new Dimension(1200, 800));
                krokiMockupToolFrame.setExtendedState(krokiMockupToolFrame.getExtendedState() | JFrame.NORMAL);
                krokiMockupToolFrame.setLocationRelativeTo(null);
                krokiMockupToolFrame.toFront();
                krokiMockupToolFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                krokiMockupToolFrame.getStatusMessage().setText(StringResource.getStringResource("app.state.select"));
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
                splash.turnOffSplash();
                krokiMockupToolFrame.setVisible(true);
            }
        });

    }

    public void stop() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KrokiMockupToolApp.getInstance().launch();
        TypeComponentMapper tcm = new TypeComponentMapper();
        tcm.getMappings();
    }

    public KrokiMockupToolFrame getKrokiMockupToolFrame() {
        return krokiMockupToolFrame;
    }

    public TabbedPaneController getTabbedPaneController() {
        return tabbedPaneController;
    }

    public void setTabbedPaneController(TabbedPaneController tabbedPaneController) {
        this.tabbedPaneController = tabbedPaneController;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public TreeController getProjectHierarchyController() {
        return projectHierarchyController;
    }

    public void setProjectHierarchyController(TreeController projectHierarchyController) {
        this.projectHierarchyController = projectHierarchyController;
    }

    public ProjectHierarchyModel getProjectHierarchyModel() {
        return projectHierarchyModel;
    }

    public void setProjectHierarchyModel(ProjectHierarchyModel projectHierarchyModel) {
        this.projectHierarchyModel = projectHierarchyModel;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public void setGuiManager(GuiManager guiManager) {
        this.guiManager = guiManager;
    }
}
