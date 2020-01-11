package com.stl.gestion_contacts;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InternalStorage{

    private static Context context;

    InternalStorage(Context context) {
        this.context = context;
    }

    public static void writeObject(String key, Object object, String TAG) {
        try {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static Object readObject(String key, String TAG)
            throws IOException {
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        }
        catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return new Object();
        }
    }
}