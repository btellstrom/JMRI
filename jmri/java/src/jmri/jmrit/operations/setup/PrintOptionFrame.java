// PrintOptionFrame.java
package jmri.jmrit.operations.setup;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import jmri.jmrit.operations.OperationsFrame;

/**
 * Frame for user edit of manifest and switch list print options
 *
 * @author Dan Boudreau Copyright (C) 2008, 2010, 2011, 2012, 2013
 * @version $Revision$
 */
public class PrintOptionFrame extends OperationsFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3366352601871116274L;

	public PrintOptionFrame() {
        super(Bundle.getMessage("TitlePrintOptions"), new PrintOptionPanel());
    }

    @Override
    public void initComponents() {
        super.initComponents();

        // build menu
        JMenuBar menuBar = new JMenuBar();
        JMenu toolMenu = new JMenu(Bundle.getMessage("Tools"));
        toolMenu.add(new PrintMoreOptionAction());
        toolMenu.add(new EditManifestHeaderTextAction());
        toolMenu.add(new EditManifestTextAction());
        toolMenu.add(new EditSwitchListTextAction());
        menuBar.add(toolMenu);
        setJMenuBar(menuBar);
        addHelpMenu("package.jmri.jmrit.operations.Operations_PrintOptions", true); // NOI18N
        
        initMinimumSize();
    }

    //private static final Logger log = LoggerFactory.getLogger(PrintOptionFrame.class);
}
