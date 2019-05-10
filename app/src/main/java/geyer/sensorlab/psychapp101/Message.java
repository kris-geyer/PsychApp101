package geyer.sensorlab.psychapp101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

class Message {
    private final Context mContext;
    private final LocalBroadcastManager lBroadcastManager;

    Message (Context context){
        this.mContext = context;
        this.lBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

     void generateMessage(String title, String content){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lBroadcastManager.sendBroadcast(new Intent("messageSeen"));
                    }
                });

        alertDialog.create().show();
    }

    void messagesDisplayed(){
        lBroadcastManager.sendBroadcast(new Intent("allMessagesSeen"));
    }
}
