package jmri.jmrit.beantable;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import jmri.Route;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

/**
 * Tests for the jmri.jmrit.beantable.RouteTableAction class
 *
 * @author	Bob Jacobsen Copyright 2004, 2007
 */
public class RouteTableActionTest extends AbstractTableActionBase {

    @Test
    public void testCreate() {
        Assert.assertNotNull(a);
    }

    @Override
    public String getTableFrameName() {
        return Bundle.getMessage("TitleRouteTable");
    }

    @Test
    public void testConstants() {
        // check constraints required by implementation,
        // because we assume that the codes are the same as the index
        // in a JComboBox
        Assert.assertEquals("Route.ONACTIVE", 0, Route.ONACTIVE);
        Assert.assertEquals("Route.ONINACTIVE", 1, Route.ONINACTIVE);
        Assert.assertEquals("Route.VETOACTIVE", 2, Route.VETOACTIVE);
        Assert.assertEquals("Route.VETOINACTIVE", 3, Route.VETOINACTIVE);
    }

    @Override
    @Test
    public void testGetClassDescription() {
        Assert.assertEquals("Route Table Action class description", "Route Table", a.getClassDescription());
    }

    /**
     * Check the return value of includeAddButton. The table generated by this
     * action includes an Add Button.
     */
    @Override
    @Test
    public void testIncludeAddButton() {
        Assert.assertTrue("Default include add button", a.includeAddButton());
    }

    @Test
    public void testAddRoute() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        a.actionPerformed(null); // show table
        JFrame f = JFrameOperator.waitJFrame(Bundle.getMessage("TitleRouteTable"), true, true);
        Assert.assertNotNull(f);

        a.addPressed(null);
        JFrameOperator addFrame = new JFrameOperator(Bundle.getMessage("TitleAddRoute"));  // NOI18N
        Assert.assertNotNull("Found Add Route Frame", addFrame);  // NOI18N

        new JTextFieldOperator(addFrame, 0).setText("105");  // NOI18N
        new JTextFieldOperator(addFrame, 1).setText("Route 105");  // NOI18N
        new JButtonOperator(addFrame, Bundle.getMessage("ButtonCreate")).push();  // NOI18N
        new JButtonOperator(addFrame, Bundle.getMessage("ButtonCancel")).push();  // NOI18N


        Route chk105 = jmri.InstanceManager.getDefault(jmri.RouteManager.class).getRoute("Route 105");  // NOI18N
        Assert.assertNotNull("Verify IR105 Added", chk105);  // NOI18N
        Assert.assertEquals("Verify system name prefix", "IR105", chk105.getSystemName());  // NOI18N

        addFrame.dispose();
        JUnitUtil.dispose(f);
    }


    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        jmri.util.JUnitUtil.initDefaultUserMessagePreferences();
        a = new RouteTableAction();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
}
