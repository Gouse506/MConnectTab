package in.vmc.mconnecttab.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import in.vmc.mconnecttab.backgroundservice.BeaconsMonitoringService;
import in.vmc.mconnecttab.database.SiteDb;
import in.vmc.mconnecttab.utils.ConnectivityReceiver;


/**
 * Created by mukesh on 10/11/15.
 */
public class MyApplication extends Application implements BootstrapNotifier {
    private static final String TAG = "Beacon";
    public static MyApplication sInstance;
    private static SiteDb mDatabase;
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;
    private Region region;

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAplicationContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static SiteDb getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new SiteDb(getAplicationContext());
        }
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "App started up");
        sInstance = this;
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        region = new Region("all-beacons-region", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
        startService(new Intent(getApplicationContext(), BeaconsMonitoringService.class));
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "Got a didEnterRegion call");
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    public BeaconManager getBeaconManager() {
        if (beaconManager == null) {
            beaconManager = BeaconManager.getInstanceForApplication(this);
        }
        return beaconManager;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
