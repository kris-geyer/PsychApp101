package geyer.sensorlab.psychapp101;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import at.favre.lib.armadillo.Armadillo;

class DataStorage {
    private final SharedPreferences sharedPreferences;
    private static final String TAG = "DataStorage";
    DataStorage(Context context){
        this.sharedPreferences = Armadillo
                .create(context, "dataStorage")
                .encryptionFingerprint(context)
                .enableKitKatSupport(true)
                .build();
    }

    void screenOff() {
        Log.i(TAG, "screen off");
        Long startedUsing = sharedPreferences.getLong("startSession", System.currentTimeMillis());
        Log.i(TAG, "started using: " + startedUsing);
        Long durationUsingPhone = System.currentTimeMillis() - startedUsing;
        Log.i(TAG, "duration: " + durationUsingPhone);
        sharedPreferences.edit().putLong("timeUsedPhone", sharedPreferences.getLong("timeUsedPhone", 0) + durationUsingPhone).apply();
    }

    void screenOn() {
        Log.i(TAG, "screen on");
        sharedPreferences.edit().putLong("startSession", System.currentTimeMillis()).apply();
    }

    Long timeUsingPhone(){
        Log.i(TAG, "time using phone");
        return sharedPreferences.getLong("timeUsedPhone", 0);
    }
}
