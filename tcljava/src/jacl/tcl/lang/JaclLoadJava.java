package tcl.lang;

/**
 * This class implements a small helper function that is used to
 * load the Java package into Jacl.
 */

class JaclLoadJavaCmd implements Command {

public void 
cmdProc(
    Interp interp,   			// Current interpreter. 
    TclObject argv[])			// Arguments to "jaclloadjava" statement.
throws TclException
{
    // This method takes no arguments
    if (argv.length != 1) {
	throw new TclNumArgsException(interp, 1, argv, "");
    }

    try {
	(new BlendExtension()).init(interp);
    } catch (TclException e) {
	System.out.println(interp.getResult());
	e.printStackTrace();
	throw new TclRuntimeError("unexpected TclException: " + e);
    }


    // Now that we have loaded the Java package we can delete this command
    // from the interp.

    interp.deleteCommand(argv[0].toString());

}

}
