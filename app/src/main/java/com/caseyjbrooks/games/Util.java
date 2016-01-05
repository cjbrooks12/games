package com.caseyjbrooks.games;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import io.realm.Realm;

public class Util {
    public static String randomString(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < length; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static int getThemeColor(Activity activity, int theme_color) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = activity.getTheme();
        theme.resolveAttribute(theme_color, typedValue, true);
        return typedValue.data;
    }

    public static void exportDatabase(Activity activity) {

        // init realm
        Realm realm = Realm.getInstance(activity);

        File exportRealmFile = null;
        try {
            // get or create an "export.realm" file
            exportRealmFile = new File(activity.getExternalCacheDir(), "export.realm");

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        realm.close();

        // init email intent and add export.realm as attachment
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "cjbrooks12@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Realm db");
        intent.putExtra(Intent.EXTRA_TEXT, "Attached is the apps current realm db file");
        Uri u = Uri.fromFile(exportRealmFile);
        intent.putExtra(Intent.EXTRA_STREAM, u);

        // start email intent
        activity.startActivity(Intent.createChooser(intent, "Send Realm File"));
    }

    public static class DismissDialogCallback implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }
}
