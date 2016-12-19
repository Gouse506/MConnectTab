package in.vmc.mconnecttab.backgroundservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.activity.Home;
import in.vmc.mconnecttab.activity.MyApplication;


public class BeaconsMonitoringService extends Service implements BootstrapNotifier {
    private static final String TAG = "estimote";
    private BeaconManager beaconManager;
    private RegionBootstrap regionBootstrap;

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onCreate() {
        // Configure BeaconManager.
        Log.d(TAG, "Beacons monitoring service created");
        //  Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Beacons monitoring service destroyed");
        // Toast.makeText(this, "Beacons monitoring service done", Toast.LENGTH_SHORT).show();
        Notification noti = new Notification.Builder(BeaconsMonitoringService.this)
                .setContentTitle("Stopped")
                .setContentText("See you!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Beacons monitoring service Started");
        MyApplication app = (MyApplication) getApplication();

        beaconManager = app.getBeaconManager();
        beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        try {
            beaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        } catch (Exception e) {
        }
        ;
        Region region = new Region("all-beacons-region", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        return START_STICKY;
    }


    @Override
    public void didEnterRegion(Region region) {

        Intent intent1 = new Intent(this, Home.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent1);

        Intent notificationIntent = new Intent(BeaconsMonitoringService.this, Home.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(BeaconsMonitoringService.this, 0,
                notificationIntent, 0);

        Notification noti = new Notification.Builder(BeaconsMonitoringService.this)
                .setContentTitle("Entered")
                .setContentText("You're home!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent)
                .build();


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(2);
        mNotificationManager.notify(1, noti);
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "exited");
        Toast.makeText(BeaconsMonitoringService.this, "Exited", Toast.LENGTH_LONG).show();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);

        Notification noti = new Notification.Builder(BeaconsMonitoringService.this)
                .setContentTitle("Exited")
                .setContentText("See you!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        mNotificationManager.notify(2, noti);
    }


    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        //  Toast.makeText(BeaconsMonitoringService.this, "Not Entered", Toast.LENGTH_LONG).show();
    }
}
