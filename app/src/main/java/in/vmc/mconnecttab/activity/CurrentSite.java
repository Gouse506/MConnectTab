package in.vmc.mconnecttab.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.adapter.Sitedetailadapter;
import in.vmc.mconnecttab.model.Model;
import in.vmc.mconnecttab.model.OptionsData;
import in.vmc.mconnecttab.model.SiteData;
import in.vmc.mconnecttab.utils.EndVisitFeedBack;
import in.vmc.mconnecttab.utils.FirstVisitDailog;
import in.vmc.mconnecttab.utils.JSONParser;
import in.vmc.mconnecttab.utils.ReferDialogFragment;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

public class CurrentSite extends AppCompatActivity implements TAG, YouTubePlayer.OnInitializedListener, RangeNotifier, BeaconConsumer, FirstVisitDailog.FirstVisitListener, View.OnClickListener
        , EndVisitFeedBack.EndVisitFeedBackDialogListener {

    public Model mModel, mFirstVisit;
    @InjectView(R.id.listView2)
    GridView listView;
    @InjectView(R.id.root)
    CoordinatorLayout mroot;
    @InjectView(R.id.defaultview)
    View contentPanel;
    @InjectView(R.id.desc)
    TextView tvdesc;
    ArrayList<SiteData> SiteData = new ArrayList<>();
    private String siteID;
    private Toolbar toolbar;
    private ProgressDialog pd;
    private ImageView sensorCall, sensorLike, sensorPeople;
    private Sitedetailadapter adapter;
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
    private LinearLayout playerView;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private FirstVisitDailog firstVisitDailog = new FirstVisitDailog();
    private YouTubePlayer videoPlayer;
    private String VIDEO_ID;
    private boolean fullScreen;
    private ImageView sensorLogo;
    private TextView sensorName, sensorDesc;
    private Button sensorsend;
    private EditText sensorMessage;
    private boolean processing;
    private LinearLayout layout;
    private ArrayList<String> mvisitedBeacon = new ArrayList<String>();
    private LinearLayout sensor;
    private BeaconManager mBeaconManager;
    private ReferDialogFragment referDialogFragment;
    private EndVisitFeedBack endVisitFeedBack;
    private String siteid, bid;
    private boolean enable;
    private YouTubePlayerSupportFragment youTubePlayerFragment1;
    private LinearLayout playerView1;
    private Snackbar snack;
    private String authkeyVisitor, authkeyClient;
    private boolean SCANN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_site);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        enableBluetooth();
        siteID = Utils.getFromPrefs(CurrentSite.this, SITEID, "N");
        authkeyClient = Utils.getFromPrefs(CurrentSite.this, AUTHKEY_CLIENT, "N");
        authkeyVisitor = Utils.getFromPrefs(CurrentSite.this, AUTHKEY_VISITOR, "N");

        if (siteID.equals("N") && authkeyClient.equals("N") && authkeyVisitor.equals("N")) {
            this.finish();
        }
        Log.d("CHECK", siteID + " " + authkeyClient + " " + authkeyVisitor);
        snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        playerView = (LinearLayout) findViewById(R.id.playerlayout);
        playerView1 = (LinearLayout) findViewById(R.id.playerlayout1);
        sensor = (LinearLayout) findViewById(R.id.sensor);
        sensor.setVisibility(View.GONE);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout = (LinearLayout) findViewById(R.id.linear);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().
                findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment1 = (YouTubePlayerSupportFragment) getSupportFragmentManager().
                findFragmentById(R.id.youtube_fragment1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CurrentSite.this, LocationDetail.class);
                intent.putExtra(BID, SiteData.get(position).getBid());
                intent.putExtra(BEACONID, SiteData.get(position).getId());
                startActivity(intent);
            }
        });
        if (playerView1.getVisibility() == View.VISIBLE) {
            playerView1.setVisibility(View.GONE);
        }

        GetSites();
        MyApplication.getWritableDatabase().deleteBeacon();

        //Sensor Initiliazation
        sensorLogo = (ImageView) findViewById(R.id.logo);
        sensorName = (TextView) findViewById(R.id.compnay_name);
        sensorDesc = (TextView) findViewById(R.id.company_desc);
        sensorCall = (ImageView) findViewById(R.id.sensorcall);
        sensorLike = (ImageView) findViewById(R.id.sensorlike);
        sensorLike.setVisibility(View.GONE);
        sensorPeople = (ImageView) findViewById(R.id.sensorpeople);
        sensorMessage = (EditText) findViewById(R.id.message);
        sensorsend = (Button) findViewById(R.id.send);


        sensorsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mModel != null && sensorMessage.getText().toString().length() > 0) {
                    onSubmitQuery("", "" + sensorMessage.getText().toString(), mModel.getSiteId(), mModel.getBeacinId());
                    Toast.makeText(CurrentSite.this, "Message send sucessfully", Toast.LENGTH_SHORT).show();
                    sensorMessage.setText("");
                } else {
                    Toast.makeText(CurrentSite.this, "Enter message to send", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sensorCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mModel != null) {
                    Utils.makeAcall(mModel.getPhone(), CurrentSite.this);
                }
            }
        });
        sensorPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mModel != null)
                    showReferDailogDialog(mModel.getSiteId());
            }
        });


        sensorLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLikeUnlike(sensorLike, siteid, bid);
            }
        });


    }

    public void GetLikeUnlike(final ImageView imageView, final String siteid, final String bid) {

        if (Utils.onlineStatus2(CurrentSite.this)) {
            new SetLikeUnlike(imageView, siteid, bid).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetLikeUnlike(imageView, siteid, bid);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(CurrentSite.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void onSubmitQuery(final String interest, final String query, final String siteid, final String beaconId) {

        if (Utils.onlineStatus2(CurrentSite.this)) {
            new SubmitQuery(authkeyVisitor, interest, query, siteid, beaconId).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSubmitQuery(interest, query, siteid, beaconId);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(CurrentSite.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    @Override
    public void onFinishFirstVisit(String message, String interest) {
        if (mFirstVisit != null) {
            onSubmitQuery(interest, message, mFirstVisit.getSiteId(), mFirstVisit.getBeacinId());
            Log.d("RADIO", message + " " + interest);
        }

    }

    @Override
    public void onBackPressed() {
        if (isPopopVisible()) {
            if (fullScreen) {
                videoPlayer.setFullscreen(false);
            } else {
                hidePopup();
            }

        } else {
            if (!toolbar.isShown()) {
                getSupportActionBar().show();
            } else {

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter.isEnabled() && enable) {
                    mBluetoothAdapter.disable();

                }
                mBeaconManager.unbind(this);
                if (mModel != null) {
                    showFeedbacKDialog(mModel.getSiteId());
                } else if (siteID != null) {
                    showFeedbacKDialog(siteID);

                } else {
                    mBeaconManager.unbind(this);
                    super.onBackPressed();
                }
                //showFeedbacKDialog(siteID);

                return;
            }


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.site_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void enableBluetooth() {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable Bluetooth");
            builder.setMessage("MCubeConnect requires a bluetooth to Process")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            mBluetoothAdapter.enable();
                            enable = true;

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            enable = false;

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            enable = false;
        }

    }


    @Override
    public void onClick(View v) {
        if (mModel != null) {
            Intent intent = new Intent(CurrentSite.this, DatailImgeView.class);
            intent.putExtra("mylist", mModel.getImages());

            startActivity(intent);
        }

    }

    public void onShowPopup(Model model) {
        if (model != null) {


            sensorMessage.setText("");
            siteid = model.getSiteId();
            bid = model.getBid();
            mModel = model;
            if (model.getVedioUrl() != null) {
                VIDEO_ID = model.getVedioUrl();
                youTubePlayerFragment.initialize(API_KEY, this);
                if (playerView.getVisibility() == View.GONE) {
                    playerView.setVisibility(View.VISIBLE);
                }
                if (contentPanel.getVisibility() == View.VISIBLE) {
                    contentPanel.setVisibility(View.GONE);
                }

            } else {
                if (playerView.getVisibility() == View.VISIBLE) {
                    playerView.setVisibility(View.GONE);
                }
            }
            if (model.getImages() != null) {
                showImages(model.getImages());
                if (layout.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);
                }
            }
            if (model.isLike()) {
                sensorLike.setBackgroundResource(R.drawable.ic_like);
            } else {
                sensorLike.setBackgroundResource(R.drawable.ic_liked);
            }


            sensorName.setText(model.getName());
            sensorDesc.setText(model.getDescription());
            //  sensorDesctext.setText(model.getDescription());
            new GetImageFromUrl(model.getLogo(), sensorLogo, true).execute();


            if (sensor.getVisibility() == View.GONE) {
                YoYo.with(Techniques.SlideInUp).duration(1000).playOn(sensor);
                sensor.setVisibility(View.VISIBLE);
            }
            if (referDialogFragment != null && referDialogFragment.isVisible()) {
                referDialogFragment.dismiss();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (toolbar.isShown()) {
                        getSupportActionBar().hide();
                    }
                }
            }, 800);


        }
    }

    public void showImages(ArrayList<String> images) {
        layout.removeAllViews();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            new GetImageFromUrl(images.get(i), imageView, false).execute();
            // imageView.setImageBitmap();
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(this);

        }
    }

    public void showReferDailogDialog(String siteid) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        referDialogFragment = new ReferDialogFragment();
        referDialogFragment.setCancelable(false);
        referDialogFragment.setDialogTitle("Refer To Your Contact");
        referDialogFragment.setSiteID(siteid);
        referDialogFragment.show(fragmentManager, "Input Dialog");
    }

    public void showFeedbacKDialog(String siteid) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        endVisitFeedBack = new EndVisitFeedBack();
        endVisitFeedBack.setCancelable(false);
        endVisitFeedBack.setDialogTitle("Share Your Feedback");
        endVisitFeedBack.setSiteID(siteid);
        endVisitFeedBack.show(fragmentManager, "Input Dialog");
    }

    public void showFirstVisitDialog(Model model) {
        // firstVisitDailog = new FirstVisitDailog();
        FragmentManager fragmentManager = getSupportFragmentManager();
        firstVisitDailog.setCancelable(true);
        firstVisitDailog.setDialogTitle("Share Your Interest");
        firstVisitDailog.setOptions(model);
        firstVisitDailog.show(fragmentManager, "Input Dialog");
    }


    @Override
    public void onResume() {
        super.onResume();
        SCANN = true;
        try {
            mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            mBeaconManager.setForegroundScanPeriod(10000l);
            mBeaconManager.setForegroundBetweenScanPeriod(500l);
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mBeaconManager.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SCANN = false;
        mBeaconManager.unbind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            mBeaconManager.setForegroundScanPeriod(10000l);
            mBeaconManager.setForegroundBetweenScanPeriod(500l);
            mBeaconManager.updateScanPeriods();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {

        final ArrayList<Beacon> min = new ArrayList<Beacon>();
        for (Beacon x : beacons) {
            if (min.size() == 0 || x.getDistance() == min.get(0).getDistance())
                min.add(x);
            else if (x.getDistance() < min.get(0).getDistance()) {
                min.clear();
                min.add(x);
            }
        }
        if (!isPopopVisible() && !firstVisitDailog.isVisible() && min.size() > 0 && SCANN) {
            Log.d("CurrentBeacon", min.get(0).getId2().toString());
            CheckVisit(min.get(0));
        } else if (min.size() > 0 && SCANN) {
            if (MyApplication.getWritableDatabase().isBeaconVisited(min.get(0).getId2().toString())) {
                Log.d("TAG", "id exist" + min.get(0).getId2().toString());
            } else {
                Log.d("TAG", "id not exist" + min.get(0).getId2().toString());
                if (!snack.isShown() && !firstVisitDailog.isVisible() && isPopopVisible() && !mvisitedBeacon.contains(min.get(0).toString())) {

                    mvisitedBeacon.add(min.get(0).toString());
                    snack = Snackbar.make(mroot, "New Location Detected", Snackbar.LENGTH_LONG)
                            .setAction("Show", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hidePopup();
                                    CheckVisit(min.get(0));

                                    Log.d("TAG", "New Location Detected");

                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(CurrentSite.this, R.color.accent));
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snack.show();

                }
            }

        }
    }


    public void CheckVisit(final Beacon BeaconId) {

        if (!processing) {
            if (Utils.onlineStatus2(CurrentSite.this)) {
                new CheckVisit(BeaconId).execute();
            } else {
                Snackbar
                        snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckVisit(BeaconId);

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(CurrentSite.this, R.color.accent));
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            }
        }
    }

    public void hidePopup() {

        if (sensor.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.SlideOutDown).duration(1000).playOn(sensor);
            sensor.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (!toolbar.isShown()) {
                        getSupportActionBar().show();
                    }
                }
            }, 300);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (contentPanel.getVisibility() == View.GONE) {
                        contentPanel.setVisibility(View.VISIBLE);

                    }
                }
            }, 800);


        }


    }

    public Boolean isPopopVisible() {

        return sensor.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()> 0){
                    getSupportFragmentManager().popBackStack();
                }
                startActivity(new Intent(CurrentSite.this,Home.class));
                break;
            // Respond to the action bar's Up/Home button
            case R.id.endVisit:
                //NavUtils.navigateUpFromSameTask(this);
                // onBackPressed();
                if (mModel != null) {
                    showFeedbacKDialog(mModel.getSiteId());
                } else {
                    showFeedbacKDialog(siteID);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GetSites() {


        if (Utils.onlineStatus2(CurrentSite.this)) {
            new Getsites().execute();
        } else {
            Snackbar
                    snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetSites();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(CurrentSite.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        player.setFullscreenControlFlags(0);
        player.setShowFullscreenButton(false);
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        videoPlayer = player;
        videoPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

            @Override
            public void onFullscreen(boolean _isFullScreen) {
                fullScreen = _isFullScreen;
            }
        });
        if (!b) {
            player.cueVideo(VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(CurrentSite.this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFinishEndVisitFeedBackDialog(String feedback) {


        // Toast.makeText(CurrentSite.this, feedback, Toast.LENGTH_SHORT).show();
        onSubmitFeedBack(feedback, siteID);


    }

    public void onSubmitFeedBack(final String feedback, final String siteid) {

        if (Utils.onlineStatus2(CurrentSite.this)) {
            new SubmitFeedBack(authkeyVisitor, feedback, siteid).execute();
            if (snack.isShown()) {
                snack.dismiss();
            }
        } else {
            if (snack.isShown()) {
                snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onSubmitFeedBack(feedback, siteid);

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(CurrentSite.this, R.color.accent));
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                snack.show();
            }
        }
    }

    class SubmitFeedBack extends AsyncTask<Void, Void, String> {
        private final String authkey;
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String siteid;
        private String feedback;

        public SubmitFeedBack(String authkey, String feedback, String siteid) {
            this.siteid = siteid;
            this.authkey = authkey;
            this.feedback = feedback;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.SubmitFeedBack(FEEDBACK_URL, authkey, feedback, siteid);
                Log.d("FEEDBACK", response.toString());

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }


            } catch (Exception e) {

            }
            return code;
        }

        @Override
        protected void onPostExecute(String data) {

            CurrentSite.this.finish();

            if (data != null) {


            }


        }


    }

    class SetLikeUnlike extends AsyncTask<Void, Void, String> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        ImageView imageView;
        String siteid;
        String bid;

        public SetLikeUnlike(ImageView imageView, String siteid, String bid) {
            this.imageView = imageView;
            this.siteid = siteid;
            this.bid = bid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.Getlikeunlike(LIKE, authkeyVisitor, siteid, bid);
                Log.d("RESPONSE", response.toString());


                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }


            } catch (Exception e) {

            }
            return code;
        }

        @Override
        protected void onPostExecute(String data) {

            if (data != null) {

                if (code.equals("400")) {
                    Log.d("Check ", "Like Clicked " + data);
                    imageView.setBackgroundResource(R.drawable.ic_liked);
                    YoYo.with(Techniques.Flash).duration(1000).playOn(imageView);
                }
                if (code.equals("200")) {
                    Log.d("Check ", "Like Clicked " + data);
                    imageView.setBackgroundResource(R.drawable.ic_like);
                    YoYo.with(Techniques.Flash).duration(1000).playOn(imageView);
                }
            }


        }


    }

    class CheckVisit extends AsyncTask<Void, Void, Model> {
        String message = "n";
        String code = "n";
        String name = "n", logo, desc;
        JSONObject response = null;
        Model model = null;

        Beacon BeaconId;

        private JSONObject mediaArray = null;
        private JSONObject data;

        public CheckVisit(Beacon BeaconId) {
            this.BeaconId = BeaconId;

        }

        @Override
        protected void onPreExecute() {
            processing = true;

            super.onPreExecute();
        }


        @Override
        protected Model doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.CheckVisitJASON(CHECK_VISIT, authkeyVisitor, BeaconId.getId2().toString());
                // Log.d("RESPONSE", response.toString());
                Log.d("RESPONSE", "Beacon :" + BeaconId.getId2().toString() + " authkey :" + authkeyVisitor + response.toString());
                model = new Model();

                if (response.has(CODE)) {
                    code = response.getString(CODE);
                    model.setCode(code);
                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                    model.setMessage(message);
                }

                if (response.has(DESC)) {
                    desc = response.getString(DESC);
                    model.setDescription(desc);
                }
                if (response.has(NAME)) {
                    name = response.getString(NAME);
                    model.setName(name);
                }

                if (response.has(LOGO)) {
                    logo = response.getString(LOGO);
                    model.setLogo(logo);
                }
                if (response.has(NUMBER)) {
                    model.setPhone(response.getString(NUMBER));
                }
                if (response.has(SITEID)) {
                    model.setSiteId(response.getString(SITEID));
                }
                if (response.has(BID)) {
                    model.setBid(response.getString(BID));
                }
                if (response.has(LIKES)) {
                    String Like = response.getString(LIKES);
                    if (Like.equals("1")) {
                        model.setLike(true);
                    } else {
                        model.setLike(false);
                    }
                }

                if (response.has(BEACONID)) {
                    model.setBeacinId(response.getString(BEACONID));
                }
                if (response.has(MEDIA))

                    try {
                        mediaArray = response.getJSONObject(MEDIA);
                        ArrayList<String> images = new ArrayList<String>();

                        if (mediaArray.has(IMAGE_1)) {
                            images.add(mediaArray.getString(IMAGE_1));
                        }
                        if (mediaArray.has(IMAGE_2)) {
                            images.add(mediaArray.getString(IMAGE_2));
                        }
                        if (mediaArray.has(IMAGE_3)) {
                            images.add(mediaArray.getString(IMAGE_3));
                        }
                        if (mediaArray.has(IMAGE_4)) {
                            images.add(mediaArray.getString(IMAGE_4));
                        }
                        if (mediaArray.has(VIDEO)) {
                            String Vedio = mediaArray.getString(VIDEO);
                            if (Vedio.length() > 2) {
                                model.setVedioUrl(Utils.extractYTId(mediaArray.getString(VIDEO)));
                            }
                        }

                        model.setImages(images);
                    } catch (Exception e) {

                    }

                if (response.has(DATA))

                    try {
                        data = response.getJSONObject(DATA);
                        Iterator keys = data.keys();
                        ArrayList<OptionsData> optionsDatas = new ArrayList<OptionsData>();
                        while (keys.hasNext()) {
                            OptionsData optionsData = new OptionsData();
                            String currentKey = (String) keys.next();
                            optionsData.setOptionName(data.getString(currentKey));
                            optionsDatas.add(optionsData);
                            // do something here with the value...
                        }
                        model.setOptionsData(optionsDatas);


                    } catch (Exception e) {

                    }


            } catch (Exception e) {
                Log.d("error", BeaconId.getId2().toString() + e.getMessage());
            }
            return model;
        }

        @Override
        protected void onPostExecute(Model data) {
            processing = false;
            // Toast.makeText(CurrentSite.this, "Current Beacon :" + BeaconId.getId2(), Toast.LENGTH_SHORT).show();
            if (data != null) {
                Log.d("LOG", data.toString());


                if (code.equals("202")) {

                    if (!isPopopVisible()) {
                        try {
                            mFirstVisit = data;
                            showFirstVisitDialog(data);
                            //  mvisitedBeacon.remove(BeaconId.getId2().toString());
                        } catch (Exception e) {
                        }
                        ;
                    }

                } else if (code.equals("200") || code.equals("201")) {
                    Log.d("RESPONSE", "msg");
                    //  Toast.makeText(CurrentSite.this, "Detail sent :" + BeaconId.getId2(), Toast.LENGTH_SHORT).show();
                } else if (code.equals("400")) {
                    //    Toast.makeText(CurrentSite.this, "Current Processed Beacon :" + BeaconId.getId2(), Toast.LENGTH_SHORT).show();
                    MyApplication.getWritableDatabase().insertBeacon(BeaconId.getId2().toString(), data.getSiteId());
                    if (!isPopopVisible()) {
                        onShowPopup(data);
                    }

                }

            }

        }


    }

    class GetImageFromUrl extends AsyncTask<Void, Void, Bitmap> {
        String url;
        Bitmap bitmap;
        ImageView imageView;
        boolean logo;

        public GetImageFromUrl(String url, ImageView imageView, boolean logo) {
            this.url = url;
            this.imageView = imageView;
            this.logo = logo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                bitmap = JSONParser.getBitmapFromURL(url);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap data) {

            if (data != null) {
                if (logo) {
                    imageView.setImageBitmap(data);
                } else {
                    int WIDTH = data.getWidth();
                    int HEIGHT = data.getHeight();
                    if (WIDTH > HEIGHT) {
                        //Landsscape
                        Bitmap resize = Bitmap.createScaledBitmap(data, 600, 300, false);
                       /* if (mModel.getVedioUrl().length() > 3) {
                            resize = Bitmap.createScaledBitmap(data, 600, 300, false);
                        } else {
                            resize = Bitmap.createScaledBitmap(data, 800, 400, false);
                        }*/
                        imageView.setImageBitmap(resize);

                    } else if (WIDTH < HEIGHT) {
                        //portrait
                        Bitmap resize = Bitmap.createScaledBitmap(data, 100, 200, false);
                        imageView.setImageBitmap(resize);
                    } else {
                        Bitmap resize = Bitmap.createScaledBitmap(data, 400, 400, false);
                        imageView.setImageBitmap(resize);
                    }

                }
            }
        }

    }

    class SubmitQuery extends AsyncTask<Void, Void, String> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String interest, query, authkey;
        ImageView imageView;
        String siteid;
        String beaconId;

        public SubmitQuery(String authkey, String interest, String query, String siteid, String beaconId) {
            this.siteid = siteid;
            this.query = query;
            this.authkey = authkey;
            this.interest = interest;
            this.beaconId = beaconId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.SubmitQuery(SEND_QUERY, authkey, interest, query, siteid, beaconId);
                Log.d("QUERY", response.toString());

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }


            } catch (Exception e) {

            }
            return code;
        }

        @Override
        protected void onPostExecute(String data) {

            if (data != null) {


            }


        }


    }

    public class DistanceCompare implements Comparator<Beacon> {

        @Override
        public int compare(Beacon lhs, Beacon rhs) {
            Double distance = lhs.getDistance();
            Double distance1 = rhs.getDistance();
            if (distance.compareTo(distance1) < 0)
                return -1;
            else if (distance.compareTo(distance1) > 0)
                return 1;
            else
                return 0;
        }
    }

    class Getsites extends AsyncTask<Void, Void, ArrayList<SiteData>> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        SiteData siteData = null;
        JSONArray data = null;
        ArrayList<SiteData> siteDatas;
        private String desc;
        private String media;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CurrentSite.this);
            pd.setMessage("Loading...");
            pd.show();
        }


        @Override
        protected ArrayList<SiteData> doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.GetSiteDetail(GET_SITEDETAIL, authkeyVisitor, siteID);
                Log.d("RESPONSE1", response.toString());


                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }


                if (response.has(SITEDESC)) {
                    desc = response.getString(SITEDESC);

                }
                if (response.has(SITEMEDIA)) {
                    media = response.getString(SITEMEDIA);

                }
                if (response.has(DATA)) {
                    siteDatas = new ArrayList<>();
                    data = response.getJSONArray(DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject currentlocation = data.getJSONObject(i);
                        siteData = new SiteData();
                        if (currentlocation.has(ID)) {
                            siteData.setId(currentlocation.getString(ID));
                        }
                        if (currentlocation.has(BID)) {
                            siteData.setBid(currentlocation.getString(BID));
                        }
                        if (currentlocation.has(NAME)) {
                            siteData.setName(currentlocation.getString(NAME));
                        }
                        siteDatas.add(siteData);

                    }
                }

            } catch (Exception e) {

            }
            return siteDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<SiteData> data) {
            pd.dismiss();
            if (data != null) {
                SiteData = data;
                Log.d("SITE", SiteData.toString());
                listView.setAdapter(new Sitedetailadapter(CurrentSite.this, SiteData));
                tvdesc.setText(desc);

                if (media.length() > 3) {
                    VIDEO_ID = Utils.extractYTId(media);
                    youTubePlayerFragment1.initialize(API_KEY, CurrentSite.this);
                    if (playerView1.getVisibility() == View.GONE) {
                        playerView1.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (playerView1.getVisibility() == View.VISIBLE) {
                        playerView1.setVisibility(View.GONE);
                    }


                }


            }


        }


    }

}
