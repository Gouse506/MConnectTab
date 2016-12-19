package in.vmc.mconnecttab.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONObject;

import java.util.ArrayList;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.model.Model;
import in.vmc.mconnecttab.utils.JSONParser;
import in.vmc.mconnecttab.utils.ReferDialogFragment;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

public class LocationDetail extends AppCompatActivity implements TAG, YouTubePlayer.OnInitializedListener, View.OnClickListener,
        ReferDialogFragment.ReferDialogListener {
    Model mmodel = null;
    private ImageView sensorCall, sensorLike, sensorPeople;
    private ImageView sensorLogo;
    private TextView sensorDesctext, sensorName, sensorDesc;
    private EditText sensorMessage;
    private Dialog dialog;
    private RelativeLayout mroot;
    private String beaconid;
    private LinearLayout playerView;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer videoPlayer;
    private boolean fullScreen;
    private LinearLayout layout;
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
    private String VIDEO_ID;
    private ReferDialogFragment referDialogFragment;
    private ProgressDialog pd;
    private Toolbar toolbar;
    private String siteid, bid;
    private Button sensorsend;
    private String authkeyVisitor, authkeyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mroot = (RelativeLayout) findViewById(R.id.root);
        sensorLogo = (ImageView) findViewById(R.id.logo);
        sensorName = (TextView) findViewById(R.id.compnay_name);
        sensorDesc = (TextView) findViewById(R.id.company_desc);
        sensorCall = (ImageView) findViewById(R.id.sensorcall);
        sensorLike = (ImageView) findViewById(R.id.sensorlike);
        sensorPeople = (ImageView) findViewById(R.id.sensorpeople);
        sensorMessage = (EditText) findViewById(R.id.message);


        sensorMessage.clearFocus();
        bid = getIntent().getExtras().getString(BID);
        authkeyClient = Utils.getFromPrefs(LocationDetail.this, AUTHKEY_CLIENT, "N");
        authkeyVisitor = Utils.getFromPrefs(LocationDetail.this, AUTHKEY_VISITOR, "N");

        if (authkeyClient.equals("N") && authkeyVisitor.equals("N")) {
            this.finish();
        }
        Log.d("CHECK", " " + authkeyClient + " " + authkeyVisitor);
        beaconid = getIntent().getExtras().getString(BEACONID);
        layout = (LinearLayout) findViewById(R.id.linear);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().
                findFragmentById(R.id.youtube_fragment);
        playerView = (LinearLayout) findViewById(R.id.playerlayout);

        sensorPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmodel != null)
                    showReferDailogDialog(mmodel.getSiteId());
            }
        });
        sensorCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mmodel != null) {
                    Utils.makeAcall(mmodel.getPhone(), LocationDetail.this);
                }
            }
        });
        sensorDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(LocationDetail.this);
                dialog.setContentView(R.layout.full_textdesc);
                dialog.setTitle("Site Description");
                sensorDesctext = (TextView) dialog.findViewById(R.id.textdesc);
                if (mmodel != null) {
                    sensorDesctext.setText(mmodel.getDescription());
                }
                dialog.show();
            }
        });

        getLoactionDetail(beaconid, bid);

        sensorLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLikeUnlike(sensorLike, siteid, bid);
            }
        });

        sensorsend = (Button) findViewById(R.id.send);


        sensorsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siteid != null && sensorMessage.getText().toString().length()>0) {
                    onSubmitQuery("", "" + sensorMessage.getText().toString(), siteid, mmodel.getBeacinId());
                    Toast.makeText(LocationDetail.this, "Message send sucessfully", Toast.LENGTH_SHORT).show();
                    sensorMessage.setText("");
                }else{
                    Toast.makeText(LocationDetail.this, "Enter message to send", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //  new GetLoactionDetail(beaconid, bid).execute();

    }

    public void onShowPopup(Model model) {
        if (model != null) {

            siteid = model.getSiteId();
            bid = model.getBid();
            sensorMessage.setText("");
            mmodel = model;
            if (model.getVedioUrl() != null) {
                VIDEO_ID = model.getVedioUrl();
                youTubePlayerFragment.initialize(API_KEY, this);
                if (playerView.getVisibility() == View.GONE) {
                    playerView.setVisibility(View.VISIBLE);
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
                sensorLike.setBackgroundResource(R.drawable.ic_liked);
            } else {
                sensorLike.setBackgroundResource(R.drawable.ic_like);
            }


            toolbar.setTitle(model.getName());
            sensorDesc.setText(model.getDescription());
            new GetImageFromUrl(model.getLogo(), sensorLogo, true).execute();

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

    public void getLoactionDetail(final String BeaconId, final String Bid) {


        if (Utils.onlineStatus2(LocationDetail.this)) {
            new GetLoactionDetail(BeaconId, Bid).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getLoactionDetail(BeaconId, Bid);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(LocationDetail.this, R.color.accent));
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
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (mmodel != null) {
            Intent intent = new Intent(LocationDetail.this, DatailImgeView.class);
            intent.putExtra("mylist", mmodel.getImages());
            startActivity(intent);
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


    @Override
    public void onFinishRefralInputDialog(String name, String email, String msg, String phn, String Siteid) {
        Log.d("REFER", name + " " + email + " " + msg + " " + phn + " " + Siteid);

        onReferSumbit(authkeyVisitor, Siteid, name, msg, phn, email);

    }

    public void GetLikeUnlike(final ImageView imageView, final String siteid, final String bid) {

        if (Utils.onlineStatus2(LocationDetail.this)) {
            new SetLikeUnlike(imageView, siteid, bid).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetLikeUnlike(imageView, siteid, bid);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(LocationDetail.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void onSubmitQuery(final String interest, final String query, final String siteid, final String beaconId) {

        if (Utils.onlineStatus2(LocationDetail.this)) {
            new SubmitQuery(authkeyVisitor, interest, query, siteid, beaconId).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSubmitQuery(interest, query, siteid, beaconId);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(LocationDetail.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void onReferSumbit(final String authkey, final String siteid, final String name, final String messagee, final String phone, final String email) {

        if (Utils.onlineStatus2(LocationDetail.this)) {
            new ReferQuery(authkey, siteid, name, messagee, phone, email).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onReferSumbit(authkey, siteid, name, messagee, phone, email);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(LocationDetail.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    class GetLoactionDetail extends AsyncTask<Void, Void, Model> {
        String message = "n";
        String code = "n";
        String name = "n";
        JSONObject response = null;
        Model model = null;

        String BeaconId, Bid;
        JSONObject data = null;

        private JSONObject mediaArray = null;

        public GetLoactionDetail(String BeaconId, String Bid) {
            this.BeaconId = BeaconId;
            this.Bid = Bid;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LocationDetail.this);
            pd.setMessage("Loading...");
            pd.show();
            super.onPreExecute();
        }


        @Override
        protected Model doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.GetlocationDetail(VISIT_OFFLINE, Bid, BeaconId,authkeyVisitor);
                Log.d("RESPONSE", response.toString());
                model = new Model();

                if (response.has(CODE)) {
                    code = response.getString(CODE);
                    model.setCode(code);
                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                    model.setMessage(message);
                }
                if (response.has(DATA)) {
                    JSONObject data = response.getJSONObject(DATA);
                    if (data.has("lname")) {
                        name = data.getString("lname");
                        model.setName(name);
                    }
                    if (data.has(BEACONID)) {
                        model.setBeacinId(data.getString(BEACONID));
                    }
                    if (data.has(SITEID)) {
                        model.setSiteId(data.getString(SITEID));
                    }
                    if (data.has(LOGO)) {
                        model.setLogo(data.getString(LOGO));
                    }
                    if (data.has(NUMBER)) {
                        model.setPhone(data.getString(NUMBER));
                    }

                    if (data.has(BID)) {
                        model.setBid(data.getString(BID));
                    }


                    if (data.has(DESC)) {
                        model.setDescription(data.getString(DESC));
                    }

                    if (data.has(LIKES)) {
                        String Like = data.getString(LIKES);
                        if (Like.equals("1")) {
                            model.setLike(true);
                        } else {
                            model.setLike(false);
                        }
                    }
                    if (data.has(MEDIA)) {

                        try {
                            mediaArray = data.getJSONObject(MEDIA);
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
                    }
                }
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
            return model;
        }

        @Override
        protected void onPostExecute(Model data) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (data != null) {
                Log.d("LOG", data.toString());
            }

            if (code.equals("202")) {

                ;
            }

            if (code.equals("200") || code.equals("201")) {
                Log.d("RESPONSE", "msg");
            }
            if (code.equals("400")) {
                mmodel = data;
                onShowPopup(data);

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

    class SetLikeUnlike extends AsyncTask<Void, Void, String> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        ImageView imageView;
        String siteid, bid;

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

    class ReferQuery extends AsyncTask<Void, Void, String> {
        String message = "n";
        String code = "n";
        JSONObject response = null;
        String authkey, siteid, name, messagee, phone, email;

        public ReferQuery(String authkey, String siteid, String name, String messagee, String phone, String email) {
            this.siteid = siteid;
            this.authkey = authkey;
            this.messagee = messagee;
            this.name = name;
            this.phone = phone;
            this.email = email;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.ReferQuery(GET_REFER, authkey, siteid, name, messagee, phone, email);
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

                if (code.equals("400")) {
                    Log.d("tag", messagee);
                }
            }


        }


    }

}
