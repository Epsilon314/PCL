/**
 * Created by Yiqing Zhu
 * 2018/11
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.Utils;

import android.content.Context;
import android.provider.Settings;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;


/** will be called from app thread, so don't use anything from Xposed here
 *  not used
 *
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final Integer Version = 1;
    public static final String DB_NAME = "PCL.db";
    public static String key;
    public static String secret = "DiwBDCWO19C1dniq10N9dO2";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, Version);
        SQLiteDatabase.loadLibs(context);
        setKey(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void setKey(Context context) {

        //note that Secure.ANDROID_ID WILL CHANGE after reinstalled
        String uniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        uniqueIdentifier += secret;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(uniqueIdentifier.getBytes(Charset.forName("US-ASCII")), 0, uniqueIdentifier.length());
            byte[] m = md.digest();
            BigInteger bi = new BigInteger(1, m);
            key = String.format("%0" + (m.length << 1) + "x", bi);
            return;
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        key = uniqueIdentifier;
        return;
    }
}
