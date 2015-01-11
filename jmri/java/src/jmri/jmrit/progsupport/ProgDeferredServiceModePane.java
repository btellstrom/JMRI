// ProgDeferredServiceModePane.java

package jmri.jmrit.progsupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import javax.swing.*;
import jmri.*;

/**
 * Provide a JPanel to configure the service mode programmer via a
 * "simple until you need it" pane.  This consists of a label with the
 * current mode, plus a "set" button.
 * <P>
 * A ProgModePane may "share" between one of these and an ops-mode selection,
 * which means that there might be _none_ of these modes selected.  When
 * that happens, the mode of the underlying programmer is left unchanged
 * and no message is propagated.
 * <P>
 * Note that you should call the dispose() method when you're really done, so that
 * a ProgModePane object can disconnect its listeners.
 * <P>
 * The implementation relies on a captive ProgServiceModePane which handles
 * "set" operations by changing the actual service mode programmer state.
 *
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under 
 * the terms of version 2 of the GNU General Public License as published 
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 
 * for more details.
 * <P>
 *
 * @author			Bob Jacobsen   Copyright (C) 2001, 2014
 * @version			$Revision$
 */
public class ProgDeferredServiceModePane extends ProgModeSelector implements java.beans.PropertyChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4795140446582465455L;
	ProgServiceModePane servicePane;
    JFrame setFrame;
    JLabel currentMode = new JLabel();
    protected JButton setButton = new JButton(java.util.ResourceBundle.getBundle("jmri/jmrit/symbolicprog/SymbolicProgBundle").getString("SET..."));

    /**
     * Enable/Disable the "set" button in GUI
     * @param enabled false disables button
     */
    public void setEnabled(boolean enabled) {
        setButton.setEnabled(enabled);
    }

    /**
     * Get the configured programmer
     */
    public Programmer getProgrammer() {
        return servicePane.getProgrammer();
    }

    /**
     *
     * @return true always, as we expect to always be selected
     */
    public boolean isSelected() {
        return true;
    }

    /**
     * Create the object.  There are no parameters, as there's
     * only a single layout now.
     */
    public ProgDeferredServiceModePane() {
        servicePane = new ProgServiceModePane(BoxLayout.Y_AXIS);

        // arrange activation
        setButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // pop the frame
                setFrame.setVisible(true);
            }
        });

        // create the set frame
        setFrame = new JFrame(Bundle.getMessage("TitleSetProgrammingMode"));
        setFrame.getContentPane().add(servicePane);
        setFrame.pack();

        // create the main GUI
        setLayout(new FlowLayout());
        add(currentMode);
        add(setButton);

        log.error("This is missing code to listen to the programmer and update the mode display");
    }

    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (e.getPropertyName() == "Mode") {
            // mode changed in programmer, change GUI here if needed
            // take the mode from the message, not the programmer, to get
            // proper synchronization
            log.error("ProgDeferredServiceModePane isn't handling mode changes yet");
            //ProgrammingMode mode = (ProgrammingMode)e.getNewValue();
            //updateStatus(mode);
        } else log.warn("propertyChange with unexpected propertyName: "+e.getPropertyName());
    }

    /**
     * Update the display to the current status
     */
    private void updateStatus(ProgrammingMode mode) {
        currentMode.setText(mode.toString());
        invalidate();
    }

    // no longer needed, disconnect if still connected
    public void dispose() {
    }

    static Logger log = LoggerFactory.getLogger(ProgDeferredServiceModePane.class.getName());
}
