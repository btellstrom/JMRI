package jmri.jmrix.openlcb;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Before;

/**
 * Tests for the jmri.jmrix.openlcb.OlcbThrottleManager class.
 *
 * @author	Bob Jacobsen Copyright 2008, 2010, 2011
 * @author      Paul Bender Copyright (C) 2016
 */
public class OlcbThrottleManagerTest extends jmri.managers.AbstractThrottleManagerTestBase {

    private OlcbConfigurationManagerScaffold ocm;
    private OlcbSystemConnectionMemo m;

    // The minimal setup for log4J
    @Override
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        m = OlcbTestInterface.createForLegacyTests();
        ocm = new OlcbConfigurationManagerScaffold(m); 
        m.configureManagers();
        ocm.configureManagers();
        tm = new OlcbThrottleManager(m,ocm);
    }

    @After
    public  void tearDown() {
        ocm.terminateThreads();
        m.getInterface().terminateThreads();
        JUnitUtil.tearDown();
    }
}
