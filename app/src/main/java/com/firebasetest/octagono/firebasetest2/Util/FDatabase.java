package com.firebasetest.octagono.firebasetest2.Util;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by OCTAGONO on 7/10/2017.
 */

public class FDatabase {

    private static FirebaseDatabase database;
    public static FirebaseDatabase getDatabase(){
        if (database == null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
