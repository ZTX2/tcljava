/*
 * ExprValue.java
 *
 * Copyright (c) 1997 Cornell University.
 * Copyright (c) 1997 Sun Microsystems, Inc.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 * 
 * RCS: @(#) $Id: ExprValue.java,v 1.5 2006/02/10 02:20:12 mdejong Exp $
 *
 */

package tcl.lang;

/**
 * Describes an expression value, which can be either an integer (the
 * usual case), a double-precision floating-point value, or a string.
 * A number type will typically have a string value that is the number
 * string before it was parsed into a number. If the number has no
 * string value then one will be generated by getStringValue().
 */

public final class ExprValue {
    static final int INT    = 0;
    static final int DOUBLE = 1;
    static final int STRING = 2;

    /**
     * Integer value, if any.
     */
    private int intValue;

    /**
     * Floating-point value, if any.
     */
    private double doubleValue;

    /**
     * Used to hold a string value, if any.
     */
    private String stringValue;

    /**
     * Type of value: INT, DOUBLE, or STRING.
     */
    private int type;

    public
    ExprValue(int i, String s) {
        setIntValue(i, s);
    }

    public
    ExprValue(double d, String s) {
        setDoubleValue(d, s);
    }

    public
    ExprValue(String s) {
        setStringValue(s);
    }

    public
    final
    int getType() {
        return type;
    }

    public
    final
    boolean isIntType() {
        return type == INT;
    }

    public
    final
    boolean isDoubleType() {
        return type == DOUBLE;
    }

    public
    final
    boolean isStringType() {
        return type == STRING;
    }

    public
    final
    boolean isIntOrDoubleType() {
        return (type == INT) || (type == DOUBLE);
    }

    public
    final
    int getIntValue() {
        if (type != INT) {
            throw new TclRuntimeError("called getIntValue() on non-INT type");
        }
        return intValue;
    }

    public
    final
    double getDoubleValue() {
        if (type != DOUBLE) {
            throw new TclRuntimeError("called getDoubleValue() on non-DOUBLE type");
        }
        return doubleValue;
    }

    public
    final
    String getStringValue() {
        if (type == STRING) {
            // No-op
        } else if (type == INT) {
            if (stringValue == null) {
                stringValue = Integer.toString(intValue);
            }
        } else if (type == DOUBLE) {
            if (stringValue == null) {
                // Generate Tcl string rep for the double.
                stringValue = Util.printDouble(doubleValue);
            }
        }
        return stringValue;
    }

    public
    final
    boolean getBooleanValue(Interp interp)
        throws TclException    // Raise TclException is string is not a boolean
    {
        switch (type) {
            case ExprValue.INT:
                return (intValue != 0);
            case ExprValue.DOUBLE:
                return (doubleValue != 0.0);
            case ExprValue.STRING:
                return Util.getBoolean(interp, stringValue);
            default:
                throw new TclRuntimeError("internal error: expression, unknown");
	}
    }

    public
    final
    void setIntValue(int value) {
        stringValue = null;
        intValue = value;
        type = INT;
    }

    public
    final
    void setIntValue(int value, String s) {
        stringValue = s;
        intValue = value;
        type = INT;
    }

    public
    final
    void setIntValue(boolean b) {
        stringValue = null;
        intValue = (b ? 1 : 0);
        type = INT;
    }

    public
    final
    void setDoubleValue(double value) {
        stringValue = null;
        doubleValue = value;
        type = DOUBLE;
    }

    public
    final
    void setDoubleValue(double value, String s) {
        stringValue = s;
        doubleValue = value;
        type = DOUBLE;
    }

    public
    final
    void setStringValue(String s) {
        if (s == null) {
            throw new NullPointerException();
        }
	stringValue = s;
	type = STRING;
    }

    final
    void toStringType() {
        if (type == STRING) {
            throw new TclRuntimeError("called toStringType() on STRING type");
        }
        if ( stringValue == null ) {
            getStringValue();
        }
        type = STRING;
    }

    // This method is used only for debugging, it prints a description
    // of the internal state of a ExprValue object.

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        if (type == STRING) {
            sb.append("STRING \"" + stringValue + "\"");
        } else if (type == INT) {
            sb.append("INT \"" + intValue + "\"");
            if (stringValue != null) {
                String intString = Integer.toString(intValue);
                if (intString.compareTo(stringValue) != 0) {
                    sb.append(" parsed from \"");
                    sb.append(stringValue);
                    sb.append("\"");
                }
            }
        } else if (type == DOUBLE) {
            sb.append("DOUBLE \"" + doubleValue + "\"");
            if (stringValue != null) {
                String doubleString = Util.printDouble(doubleValue);
                if (doubleString.compareTo(stringValue) != 0) {
                    sb.append(" parsed from \"");
                    sb.append(stringValue);
                    sb.append("\"");
                }
            }
        }
        return sb.toString();
    }

}

