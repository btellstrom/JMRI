/**
 * 
 */
package jmri.configurexml.turnoutoperations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom2.Element;

//import java.lang.reflect.Constructor;

import jmri.NoFeedbackTurnoutOperation;
//import jmri.SensorTurnoutOperation;
import jmri.TurnoutOperation;
//import jmri.configurexml.turnoutoperations.TurnoutOperationXml;

/**
 * Concrete subclass to save/restore SensorTurnoutOperation object
 * to/from XML. Most of the work is done by CommonTurnoutOperationXml
 * @author John Harper	Copyright 2005
 *
 */
public class SensorTurnoutOperationXml extends CommonTurnoutOperationXml {

	/**
	 * called for a newly-constructed object to load it from an XML element
	 * @param e the XML element of type "turnoutOperation"
	 */
	public TurnoutOperation loadOne(Element e) {
	    try {
            Class<?> myOpClass = Class.forName("jmri.SensorTurnoutOperation");
            return super.loadOne(e, myOpClass.getConstructor(new Class[]{String.class, int.class, int.class}),
                    NoFeedbackTurnoutOperation.getDefaultIntervalStatic(),
                    NoFeedbackTurnoutOperation.getDefaultMaxTriesStatic());
        } catch (ClassNotFoundException e1) {
            log.error("while creating NoFeedbackTurnoutOperation", e1);
            return null;
        } catch (NoSuchMethodException e2) {
            log.error("while creating NoFeedbackTurnoutOperation", e2);
            return null;
        }
	}
	
    static Logger log = LoggerFactory.getLogger(SensorTurnoutOperationXml.class.getName());
}