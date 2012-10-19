/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action.mockup;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import kroki.app.KrokiMockupToolApp;
import kroki.app.controller.TabbedPaneController;
import kroki.app.state.AddState;
import kroki.app.state.State;
import kroki.app.utils.CursorResource;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddCheckBoxesAction extends AbstractAction {

    ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.addCheckBoxes.smallImage"));
    Image addEnabledIcon = CursorResource.getCursorResource("action.addCheckBoxes.smallImage");
    Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public AddCheckBoxesAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.addCheckBoxes.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.addCheckBoxes.description"));
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        Canvas currentCanvas = tabbedPaneController.getCurrentTabContent();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.ADD_STATE);


        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);

    }
}
