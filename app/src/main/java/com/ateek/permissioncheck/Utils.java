package com.ateek.permissioncheck;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ateek on 10/30/2015.
 */
public class Utils {


    public static List<String> readContacts(Context context) {
        ArrayList<String> names = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        System.out.println("phone" + phone);
                        names.add(name);
                    }
                    pCur.close();


                }
            }
        }

        return names;
    }


    public static Boolean checkPermission(Context context, String permission) {
        // check os version
        if (Build.VERSION.SDK_INT < 23) {
            // there no need to check the permission
            return true;
        } else {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // We will need to request the permission
                return false;
            } else
                // The permission is granted, we can perform the action
                return true;
        }
    }


}