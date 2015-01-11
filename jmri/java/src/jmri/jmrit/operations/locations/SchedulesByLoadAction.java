// SchedulesByLoadAction.java

package jmri.jmrit.operations.locations;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Action to load the schedules by car type and load frame.
 * 
 * @author Daniel Boudreau Copyright (C) 2012
 * @version $Revision: 17977 $
 */

public class SchedulesByLoadAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6680843615707655230L;
	private SchedulesByLoadFrame _slf;

	public SchedulesByLoadAction(String actionName) {
		super(actionName);
	}

	public void actionPerformed(ActionEvent e) {
		if (_slf != null)
			_slf.dispose();
		_slf = new SchedulesByLoadFrame();
	}
}
