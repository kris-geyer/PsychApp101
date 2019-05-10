package geyer.sensorlab.psychapp101;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

class InformParticipant extends Message {

    private final Resources resources;
    private final SharedPreferences sharedPreferences;

    InformParticipant(Context context, Resources resources, SharedPreferences sharedPreferences) {
        super(context);
        this.resources = resources;
        this.sharedPreferences = sharedPreferences;
    }

    void sendMessage(){
        switch (sharedPreferences.getInt("messagesSeen",1)){
            case 1:
                updatePreferences();
                generateMessage(resources.getString(R.string.t1), resources.getString(R.string.c1));
                break;
            case 2:
                updatePreferences();
                generateMessage(resources.getString(R.string.t2), resources.getString(R.string.c2));
                break;
            case 3:
                updatePreferences();
                generateMessage(resources.getString(R.string.t3), resources.getString(R.string.c3));
                break;
            case 4:
                updatePreferences();
                generateMessage(resources.getString(R.string.t4), resources.getString(R.string.c4));
                break;
            case 5:
                messagesDisplayed();
                break;
        }
    }

    private void updatePreferences(){
        sharedPreferences.edit().putInt("messagesSeen", 1 + sharedPreferences.getInt("messagesSeen", 1)).apply();
    }
}
