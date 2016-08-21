package kroki.app.gui.checkboxlist;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class CheckBoxList extends JList<JCheckBox> {
	
	private static final long serialVersionUID = 1L;

   @SuppressWarnings("unchecked")
   public CheckBoxList() {
	   setCellRenderer(new CellRenderer());
	   addMouseListener(new MouseAdapter() {
		   public void mousePressed(MouseEvent e) {
			   int index = locationToIndex(e.getPoint());
               if (index != -1) {
                  JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                  checkbox.setSelected(!checkbox.isSelected());
                  repaint();
               }
            }
         }
      );
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }
   
   public ArrayList<String> getSelectedCheckboxNames(){
	   ArrayList<String> selectedNames = new ArrayList<String>();
	   for(int i=0; i<getModel().getSize(); i++){
		   JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
		   if(checkbox.isSelected()){
			   selectedNames.add(checkbox.getText());
		   }
	   }
	   return selectedNames;
   }

   protected class CellRenderer implements ListCellRenderer<JCheckBox> {
	   @Override
	   public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox checkbox, int index, boolean isSelected, boolean hasFocus) {
		   checkbox.setBackground(isSelected ? getSelectionBackground() : getBackground());
	       checkbox.setForeground(isSelected ? getSelectionForeground() : getForeground());
	       checkbox.setEnabled(isEnabled());
	       checkbox.setFont(getFont());
	       checkbox.setFocusPainted(false);
	       checkbox.setBorderPainted(true);
	       checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : new EmptyBorder(1, 1, 1, 1));
	       return checkbox;
	   }
   }
   
   public void selectAll(){
	   for(int i=0; i<getModel().getSize(); i++){
		   JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
		   checkbox.setSelected(true);
	   }
	   repaint();
   }
   
   public void selectNone(){
	   for(int i=0; i<getModel().getSize(); i++){
		   JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
		   checkbox.setSelected(false);
	   }
	   repaint();
   }
}