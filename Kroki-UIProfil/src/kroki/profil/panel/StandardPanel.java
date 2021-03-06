package kroki.profil.panel;

import kroki.mockup.model.Composite;
import kroki.mockup.model.components.Button;
import kroki.mockup.model.components.TitledContainer;
import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.std.StdDataSettings;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.persistent.PersistentClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

/**
 * <code>StandardPanel</code> represents a standard panel which is connected to
 * a persistent class (in case of a three-layered application) or a table
 * created when mapping an object model to a relational (in case of a two-layered application).
 * The visual properties and functionalities of the standard panel respects the HCI standards.
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class StandardPanel extends VisibleClass {

	private static final long serialVersionUID = 1L;
	/**Is it permitted or forbidden to enter new data*/  
    protected boolean add = true;
    /**Is it permitted or forbidden modify existing data*/
    protected boolean update = true;
    /**Is it permitted or forbidden to copy data*/
    protected boolean copy = true;
    /**Is it permitted or forbidden to delete data*/ 
    protected boolean delete = true;
    /**Is it permitted or forbidden to perform data search*/
    protected boolean search = true;
    /**Is it permitted or forbidden to change data view (from tabular to one record one screen and vice versa*/
    protected boolean changeMode = true;
    /**Is it permitted or forbidden to perform row navigation (first, previous, next, last)*/ 
    protected boolean dataNavigation = true;
    /**Standard panel setting*/
    protected transient StdPanelSettings stdPanelSettings = new StdPanelSettings();
    /**Setting connected to data which the panel shows*/
    protected transient StdDataSettings stdDataSettings = new StdDataSettings();
    /**Persistent class connected to the standard panel*/
    protected PersistentClass persistentClass;

    /*mockup components if the standard operations*/
    private Button addButton;
    private Button updateButton;
    private Button copyButton;
    private Button deleteButton;
    private Button searchButton;
    private Button changeModeButton;
    private Button firstButton;
    private Button previuosButton;
    private Button nextButton;
    private Button lastButton;
    //panels which form the standard form
    private ElementsGroup toolbarPanel;
    //panel with the components*/
    private ElementsGroup propertiesPanel;
    //panel on the right which contains the operations (transactions, reports...)
    private ElementsGroup operationsPanel;
    
    /*****************/
    /*Constructors   */
    /*****************/
    public StandardPanel() {
        super();
        component = new TitledContainer("StandardForm");
        component.getRelativePosition().setLocation(5, 5);
        component.getAbsolutePosition().setLocation(5, 5);
        component.getDimension().setSize(800, 500);
        component.getElementPainter().update();
        persistentClass = new PersistentClass();
    }

    public StandardPanel(String tableName, String label, boolean visible, ComponentType componentType, boolean modal) {
        super(label, visible, componentType, modal);
        persistentClass = new PersistentClass(tableName);
    }

    public StandardPanel(boolean modal) {
        super(modal);
        persistentClass = new PersistentClass();
    }

    

    @Override
    public void update() {
        component.updateComponent();
        ((Composite) component).layout();
    }

    @Override
    public String toString() {
        return label;
    }
    
    public BussinesSubsystem project(){
    	UmlPackage currentPack = umlPackage;
    	while (currentPack.nestingPackage() != null)
    		currentPack = currentPack.nestingPackage();
    	return (BussinesSubsystem) currentPack;
    }

    /**********************/
    /*GETTERS AND SETTERS*/
    /*********************/
    
    
    public StdDataSettings getStdDataSettings() {
    	if (stdDataSettings == null)
    		stdDataSettings = new StdDataSettings();
        return stdDataSettings;
    }

    public void setStdDataSettings(StdDataSettings stdDataSettings) {
        this.stdDataSettings = stdDataSettings;
    }

    public StdPanelSettings getStdPanelSettings() {
    	if (stdPanelSettings == null)
    		stdPanelSettings = new StdPanelSettings();
        return stdPanelSettings;
    }

    public void setStdPanelSettings(StdPanelSettings stdPanelSettings) {
        this.stdPanelSettings = stdPanelSettings;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
        if (this.add) {
            ((Composite) toolbarPanel.getComponent()).addChild(addButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(addButton);
        }

    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public void setChangeMode(boolean changeMode) {
        this.changeMode = changeMode;
        if (this.changeMode) {
            ((Composite) toolbarPanel.getComponent()).addChild(changeModeButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(changeModeButton);
        }
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
        if (this.copy) {
            ((Composite) toolbarPanel.getComponent()).addChild(copyButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(copyButton);
        }
    }

    public boolean isDataNavigation() {
        return dataNavigation;
    }

    public void setDataNavigation(boolean dataNavigation) {
        this.dataNavigation = dataNavigation;
        if (this.dataNavigation) {
            ((Composite) toolbarPanel.getComponent()).addChild(firstButton);
            ((Composite) toolbarPanel.getComponent()).addChild(previuosButton);
            ((Composite) toolbarPanel.getComponent()).addChild(nextButton);
            ((Composite) toolbarPanel.getComponent()).addChild(lastButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(firstButton);
            ((Composite) toolbarPanel.getComponent()).removeChild(previuosButton);
            ((Composite) toolbarPanel.getComponent()).removeChild(nextButton);
            ((Composite) toolbarPanel.getComponent()).removeChild(lastButton);
        }
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
        if (this.delete) {
            ((Composite) toolbarPanel.getComponent()).addChild(deleteButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(deleteButton);
        }
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
        if (this.search) {
            ((Composite) toolbarPanel.getComponent()).addChild(searchButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(searchButton);
        }
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
        if (this.update) {
            ((Composite) toolbarPanel.getComponent()).addChild(updateButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(updateButton);
        }
    }
    
    public Button getAddButton() {
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    public Button getChangeModeButton() {
        return changeModeButton;
    }

    public void setChangeModeButton(Button changeModeButton) {
        this.changeModeButton = changeModeButton;
    }

    public Button getCopyButton() {
        return copyButton;
    }

    public void setCopyButton(Button copyButton) {
        this.copyButton = copyButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Button getFirstButton() {
        return firstButton;
    }

    public void setFirstButton(Button firstButton) {
        this.firstButton = firstButton;
    }

    public Button getLastButton() {
        return lastButton;
    }

    public void setLastButton(Button lastButton) {
        this.lastButton = lastButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public void setNextButton(Button nextButton) {
        this.nextButton = nextButton;
    }

    public ElementsGroup getOperationsPanel() {
        return operationsPanel;
    }

    public void setOperationsPanel(ElementsGroup operationsPanel) {
        this.operationsPanel = operationsPanel;
    }

    public Button getPreviuosButton() {
        return previuosButton;
    }

    public void setPreviuosButton(Button previuosButton) {
        this.previuosButton = previuosButton;
    }

    public ElementsGroup getPropertiesPanel() {
        return propertiesPanel;
    }

    public void setPropertiesPanel(ElementsGroup propertiesPanel) {
        this.propertiesPanel = propertiesPanel;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public ElementsGroup getToolbarPanel() {
        return toolbarPanel;
    }

    public void setToolbarPanel(ElementsGroup toolbarPanel) {
        this.toolbarPanel = toolbarPanel;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public PersistentClass getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(PersistentClass persistentClass) {
        this.persistentClass = persistentClass;
    }
}
