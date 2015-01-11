// NodeConfigAction.java

package jmri.jmrix.maple.nodeconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Swing action to create and register a
 *       			NodeConfigFrame object
 *
 * @author	Bob Jacobsen    Copyright (C) 2001
 * @version	$Revision$
 */
public class NodeConfigAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6952737531002185247L;

	public NodeConfigAction(String s) { super(s);}

    public NodeConfigAction() {
		this("Configure Maple Nodes");
    }

    public void actionPerformed(ActionEvent e) {
        NodeConfigFrame f = new NodeConfigFrame();
        try {
            f.initComponents();
            }
        catch (Exception ex) {
            log.error("Exception: "+ex.toString());
            }
        f.setLocation(100,30);
        f.setVisible(true);
    }
	
	static Logger log = LoggerFactory.getLogger(NodeConfigAction.class.getName());
}


/* @(#)SerialPacketGenAction.java */