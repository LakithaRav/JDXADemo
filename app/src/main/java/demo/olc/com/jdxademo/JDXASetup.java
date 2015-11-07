package demo.olc.com.jdxademo;

import android.content.ContextWrapper;

import com.softwaretree.jdxandroid.BaseAppSpecificJDXSetup;
import com.softwaretree.jdxandroid.JDXSetup;

/**
 * This class is used to setup JDXA ORM with an object relational mapping specification
 * and to initialize the database per the mapping specification.
 */
public class JDXASetup extends BaseAppSpecificJDXSetup {

    // Resource id for the object relational mapping (.jdx) file
    private static int ormId = R.raw.jdxspec;

    public static void initialize() {
        setORMFileResourceId(ormId);
        setJdxForAndroidLicenseKey(ormId, "VPP01.0cCdNpNaU0ulc85uzulszsupzjdxRdNpuJDXi7LYI3ObBZ5nGI7438");
        setForceCreateSchema(ormId, false);
        //un-comment thie below line to enable debug mode, mode vary from 1 - 5
        //setDebugLevel(ormId, 3);
    }

    public static JDXSetup getInstance(ContextWrapper contextWrapper) throws Exception {
        return getInstance(ormId, contextWrapper);
    }

    public static void cleanup() {
        cleanup(ormId);
    }
}
