package com.firebasetest.octagono.firebasetest2.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by OCTAGONO on 7/7/2017.
 */

public class BitmapToBase64 {
    private static BitmapToBase64 instance = null;
    private BitmapToBase64(){}
    public static BitmapToBase64 getInstance(){
        if (instance == null){
            instance = new BitmapToBase64();
        }
        return instance;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
