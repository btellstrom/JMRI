// TrainSetColorAction.java

package jmri.jmrit.operations.trains;

import java.awt.event.ActionEvent;
import java.awt.Frame;

import javax.swing.AbstractAction;

/**
 * Swing action to create and register a TrainSetColorFrame object.
 * 
 * @author Bob Jacobsen Copyright (C) 2001
 * @author Daniel Boudreau Copyright (C) 2014
 * @version $Revision: 17977 $
 */
public class TrainsTableSetColorAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2895152667316839146L;

	public TrainsTableSetColorAction() {
		super(Bundle.getMessage("MenuItemSetTrainColor"));
	}

	Train _train = null;

	public TrainsTableSetColorAction(String s, Train train) {
		super(s);
		_train = train;
	}

	TrainsTableSetColorFrame f = null;

	public void actionPerformed(ActionEvent e) {
		if (f == null || !f.isVisible()) {
			f = new TrainsTableSetColorFrame(_train);
		}
		f.setExtendedState(Frame.NORMAL);
		f.setVisible(true); // this also brings the frame into focus
	}
}

/* @(#)TrainSetColorAction.java */