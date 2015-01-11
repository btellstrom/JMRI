// DecVariableValue.java

package jmri.jmrit.symbolicprog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.text.Document;

import java.util.ArrayList;

/**
 * Decimal representation of a value.
 *
 * @author		Bob Jacobsen   Copyright (C) 2001
 * @version             $Revision$
 *
 */
public class DecVariableValue extends VariableValue
    implements ActionListener, PropertyChangeListener, FocusListener {

    public DecVariableValue(String name, String comment, String cvName,
                            boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly,
                            String cvNum, String mask, int minVal, int maxVal,
                            HashMap<String, CvValue> v, JLabel status, String stdname) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JTextField("0",fieldLength());
        _defaultColor = _value.getBackground();
        _value.setBackground(COLOR_UNKNOWN);
        // connect to the JTextField value, cv
        _value.addActionListener(this);
        _value.addFocusListener(this);
        CvValue cv = _cvMap.get(getCvNum());
        cv.addPropertyChangeListener(this);
        cv.setState(CvValue.FROMFILE);
    }

    public void setToolTipText(String t) {
        super.setToolTipText(t);   // do default stuff
        _value.setToolTipText(t);  // set our value
    }

    int _maxVal;
    int _minVal;

    int fieldLength() {
        if (_maxVal <= 255) return 3;
        return (int) Math.ceil(Math.log10(_maxVal))+1;
    }
    
    public CvValue[] usesCVs() {
        return new CvValue[]{_cvMap.get(getCvNum())};
    }

    public Object rangeVal() {
        return "Decimal: "+_minVal+" - "+_maxVal;
    }

    String oldContents = "";

    void enterField() {
        oldContents = _value.getText();
    }
    void exitField() {
        if (_value == null) {
            // There's no value Object yet, so just ignore & exit
            return;
        }
        // what to do for the case where _value == null?
        if(!_value.getText().equals("")){
            // there may be a lost focus event left in the queue when disposed so protect
            if (!oldContents.equals(_value.getText())) {
                int newVal = Integer.parseInt(_value.getText());
                int oldVal = Integer.parseInt(oldContents);
                updatedTextField();
                prop.firePropertyChange("Value", Integer.valueOf(oldVal), Integer.valueOf(newVal));
            }
        } else {
            //As the user has left the contents blank, we shall re-instate the old
            // value as, when a write to decoder is performed, the cv remains the same value.
            _value.setText(oldContents);
        }
    }

    /**
     * Invoked when a permanent change to the JTextField has been
     * made.  Note that this does _not_ notify property listeners;
     * that should be done by the invoker, who may or may not
     * know what the old value was. Can be overwridden in subclasses
     * that want to display the value differently.
     */
    void updatedTextField() {
        if (log.isDebugEnabled()) log.debug("updatedTextField");
        // called for new values - set the CV as needed
        CvValue cv = _cvMap.get(getCvNum());
        // compute new cv value by combining old and request
        int oldCv = cv.getValue();
        int newVal;
        try {
            newVal = Integer.parseInt(_value.getText());
        }
        catch (java.lang.NumberFormatException ex) { newVal = 0; }
        int newCv = newValue(oldCv, newVal, getMask());
                if (oldCv != newCv)
                    cv.setValue(newCv);
    }

    /** ActionListener implementations */
    public void actionPerformed(ActionEvent e) {
        if (log.isDebugEnabled()) log.debug("actionPerformed");
        int newVal = Integer.parseInt(_value.getText());
        updatedTextField();
        prop.firePropertyChange("Value", null, Integer.valueOf(newVal));
    }

    /** FocusListener implementations */
    public void focusGained(FocusEvent e) {
        if (log.isDebugEnabled()) log.debug("focusGained");
        enterField();
    }

    public void focusLost(FocusEvent e) {
        if (log.isDebugEnabled()) log.debug("focusLost");
        exitField();
    }

    // to complete this class, fill in the routines to handle "Value" parameter
    // and to read/write/hear parameter changes.
    public String getValueString() {
        return _value.getText();
    }

    public void setIntValue(int i) {
        setValue(i);
    }

    public int getIntValue() {
        return Integer.parseInt(_value.getText());
    }
    
    public Object getValueObject() {
        return Integer.valueOf(_value.getText());
    }

    public Component getCommonRep()  {
        if (getReadOnly())  {
            JLabel r = new JLabel(_value.getText());
            reps.add(r);
            updateRepresentation(r);
            return r;
        } else
            return _value;
    }

    public void setAvailable(boolean a) {
        _value.setVisible(a);
        for (Component c : reps) c.setVisible(a);
        super.setAvailable(a);
    }

    java.util.List<Component> reps = new java.util.ArrayList<Component>();

    public Component getNewRep(String format)  {
        if (format.equals("vslider")) {
            DecVarSlider b = new DecVarSlider(this, _minVal, _maxVal);
            b.setOrientation(JSlider.VERTICAL);
            sliders.add(b);
            reps.add(b);
            updateRepresentation(b);
            return b;
        }
        else if (format.equals("hslider")) {
            DecVarSlider b = new DecVarSlider(this, _minVal, _maxVal);
            b.setOrientation(JSlider.HORIZONTAL);
            sliders.add(b);
            reps.add(b);
            updateRepresentation(b);
            return b;
        }
        else if (format.equals("hslider-percent")) {
            DecVarSlider b = new DecVarSlider(this, _minVal, _maxVal);
            b.setOrientation(JSlider.HORIZONTAL);
            if (_maxVal > 20) {
                b.setMajorTickSpacing(_maxVal/2);
                b.setMinorTickSpacing((_maxVal+1)/8);
            } else {
                b.setMajorTickSpacing(5);
                b.setMinorTickSpacing(1); // because JSlider does not SnapToValue
                b.setSnapToTicks(true);   // like it should, we fake it here
            }
            b.setSize(b.getWidth(),28);
            Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
            labelTable.put( Integer.valueOf( 0 ), new JLabel("0%") );
            if ( _maxVal == 63 ) {   // this if for the QSI mute level, not very universal, needs work
                labelTable.put( Integer.valueOf( _maxVal/2 ), new JLabel("25%") );
                labelTable.put( Integer.valueOf( _maxVal ), new JLabel("50%") );
            } else {
                labelTable.put( Integer.valueOf( _maxVal/2 ), new JLabel("50%") );
                labelTable.put( Integer.valueOf( _maxVal ), new JLabel("100%") );
            }
            b.setLabelTable( labelTable );
            b.setPaintTicks(true);
            b.setPaintLabels(true);
            sliders.add(b);
            updateRepresentation(b);
            if (!getAvailable()) b.setVisible(false);
            return b;
        }
        else {
            JTextField value = new VarTextField(_value.getDocument(),_value.getText(), fieldLength(), this);
            if (getReadOnly() || getInfoOnly()) {
                value.setEditable(false);
            }
            reps.add(value);
            updateRepresentation(value);
            return value;
        }
    }

    ArrayList<DecVarSlider> sliders = new ArrayList<DecVarSlider>();

    /**
     * Set a new value, including notification as needed.  This does the
     * conversion from string to int, so if the place where formatting
     * needs to be applied
     */
    public void setValue(int value) {
        int oldVal;
        try {
            oldVal = Integer.parseInt(_value.getText());
        } catch (java.lang.NumberFormatException ex) { oldVal = -999; }
        if (log.isDebugEnabled()) log.debug("setValue with new value "+value+" old value "+oldVal);
        if (oldVal != value) {
            _value.setText(""+value);
            updatedTextField();
            prop.firePropertyChange("Value", Integer.valueOf(oldVal), Integer.valueOf(value));
        }
    }

    Color _defaultColor;

    // implement an abstract member to set colors
    Color getDefaultColor() { return _defaultColor; }
    Color getColor() { return _value.getBackground(); }
    void setColor(Color c) {
        if (c != null) _value.setBackground(c);
        else _value.setBackground(_defaultColor);
        // prop.firePropertyChange("Value", null, null);
    }

    /**
     * Notify the connected CVs of a state change from above
     * @param state
     */
    public void setCvState(int state) {
        _cvMap.get(getCvNum()).setState(state);
    }

    public boolean isChanged() {
        CvValue cv = _cvMap.get(getCvNum());
        if (log.isDebugEnabled()) log.debug("isChanged for "+getCvNum()+" state "+cv.getState());
        return considerChanged(cv);
    }

    public void readChanges() {
         if (isChanged()) readAll();
    }

    public void writeChanges() {
         if (isChanged()) writeAll();
    }

    public void readAll() {
        setToRead(false);
        setBusy(true);  // will be reset when value changes
        //super.setState(READ);
        _cvMap.get(getCvNum()).read(_status);
    }

    public void writeAll() {
        setToWrite(false);
        if (getReadOnly()) {
            log.error("unexpected write operation when readOnly is set");
        }
        setBusy(true);  // will be reset when value changes
        _cvMap.get(getCvNum()).write(_status);
    }

    // handle incoming parameter notification
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        // notification from CV; check for Value being changed
        if (log.isDebugEnabled()) log.debug("Property changed: "+e.getPropertyName());
        if (e.getPropertyName().equals("Busy")) {
            if (((Boolean)e.getNewValue()).equals(Boolean.FALSE)) {
                setToRead(false);
                setToWrite(false);  // some programming operation just finished
                setBusy(false);
            }
        }
        else if (e.getPropertyName().equals("State")) {
            CvValue cv = _cvMap.get(getCvNum());
            if (cv.getState() == STORED) setToWrite(false);
            if (cv.getState() == READ) setToRead(false);
            setState(cv.getState());
        }
        else if (e.getPropertyName().equals("Value")) {
            // update value of Variable
            CvValue cv = _cvMap.get(getCvNum());
            int newVal = (cv.getValue() & maskVal(getMask())) >>> offsetVal(getMask());
            setValue(newVal);  // check for duplicate done inside setVal
        }
    }

    // stored value, read-only Value
    JTextField _value = null;



    /* Internal class extends a JTextField so that its color is consistent with
     * an underlying variable
     *
     * @author			Bob Jacobsen   Copyright (C) 2001
     * @version
     */
    public class VarTextField extends JTextField {

        /**
		 * 
		 */
		private static final long serialVersionUID = 7844901319795104952L;

		VarTextField(Document doc, String text, int col, DecVariableValue var) {
            super(doc, text, col);
            _var = var;
            // get the original color right
            setBackground(_var._value.getBackground());
            // listen for changes to ourself
            addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        thisActionPerformed(e);
                    }
                });
            addFocusListener(new java.awt.event.FocusListener() {
                    public void focusGained(FocusEvent e) {
                        if (log.isDebugEnabled()) log.debug("focusGained");
                        enterField();
                    }

                    public void focusLost(FocusEvent e) {
                        if (log.isDebugEnabled()) log.debug("focusLost");
                        exitField();
                    }
                });
            // listen for changes to original state
            _var.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    public void propertyChange(java.beans.PropertyChangeEvent e) {
                        originalPropertyChanged(e);
                    }
                });
        }

        DecVariableValue _var;

        void thisActionPerformed(java.awt.event.ActionEvent e) {
            // tell original
            _var.actionPerformed(e);
        }

        void originalPropertyChanged(java.beans.PropertyChangeEvent e) {
            // update this color from original state
            if (e.getPropertyName().equals("State")) {
                setBackground(_var._value.getBackground());
            }
        }

    }

    // clean up connections when done
    public void dispose() {
        if (log.isDebugEnabled()) log.debug("dispose");
        if (_value != null) _value.removeActionListener(this);
        _cvMap.get(getCvNum()).removePropertyChangeListener(this);

        _value = null;
        // do something about the VarTextField
    }

    // initialize logging
    static Logger log = LoggerFactory.getLogger(DecVariableValue.class.getName());

}