package geyer.sensorlab.psychapp101;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    InformParticipant informParticipant;
    EventDetection eventDetection;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        initializeLocalReceiver();
        informParticipant.sendMessage();
    }

    private void initializeUI() {
        result = findViewById(R.id.tvResult);
        Button getResult = findViewById(R.id.btnGetResult);
        getResult.setOnClickListener(this);
    }

    private void initializeLocalReceiver() {
        informParticipant = new InformParticipant(this, getResources(), getSharedPreferences("appInitialization", MODE_PRIVATE));

        BroadcastReceiver localReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null){
                    if(intent.getAction().equals("messageSeen")){
                        informParticipant.sendMessage();
                    }
                    if(intent.getAction().equals("allMessagesSeen")){
                        startDataCollection();
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("messageSeen");
        intentFilter.addAction("allMessagesSeen");
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, intentFilter);
    }

    private void startDataCollection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, EventDetection.class));
        }else{
            startService(new Intent(this, EventDetection.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetResult:
                Log.i("MAIN", "bind");
                bindService(new Intent(this, EventDetection.class), this, Context.BIND_AUTO_CREATE);
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        EventDetection.myBinder binder = (EventDetection.myBinder) service;
        eventDetection = binder.getService();
        Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
        final String finalResult = "Result: " + eventDetection.retrieveData();
        result.setText(finalResult);
        unbindService(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        eventDetection = null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
