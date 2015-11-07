package demo.olc.com.jdxademo;

import android.content.ContextWrapper;

import com.softwaretree.jdxandroid.BaseAppSpecificJDXSetup;
import com.softwaretree.jdxandroid.JDXSetup;

public class JDXASetup extends BaseAppSpecificJDXSetup {

    private static int ormId = R.raw.jdxspec;

    public static void initialize() {
        setORMFileResourceId(ormId);
//        setJdxForAndroidLicenseKey(ormId, "aPE01.0cCJDXEdU6E6885Fu5i7d5uTdzevRloR9Tu7znRskRgNJDXi7LY122");
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
