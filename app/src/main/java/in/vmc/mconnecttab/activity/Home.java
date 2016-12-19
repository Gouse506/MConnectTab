package in.vmc.mconnecttab.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.adapter.VisitAdapter;
import in.vmc.mconnecttab.fragment.ContentHome;
import in.vmc.mconnecttab.model.VisitData;
import in.vmc.mconnecttab.parser.Parser;
import in.vmc.mconnecttab.parser.Requestor;
import in.vmc.mconnecttab.utils.EndlessScrollListener;
import in.vmc.mconnecttab.utils.JSONParser;
import in.vmc.mconnecttab.utils.OTPDialogFragment;
import in.vmc.mconnecttab.utils.ReferDialogFragment;
import in.vmc.mconnecttab.utils.SingleTon;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

public class Home extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, VisitAdapter.ViewClickedListner, TAG, ReferDialogFragment.ReferDialogListener {

    public float scaleFactor, widthDp, heightDp;
    public Snackbar snack;
    ArrayList<VisitData> VisitData = new ArrayList<>();
    private Toolbar toolbar;
    private int MIN = 0, MAX = 4;
    private boolean loading = false;
    private VisitAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ReferDialogFragment referDialogFragment = new ReferDialogFragment();
    private RelativeLayout mroot;
    private LinearLayout pdloadmore;
    private int widthPixels, heightPixels;
    private String authkey;
    private String email;
    private String eid, bid;
    private String STATE_VISITALLDATA = "1";
    private String TAG = "RESPONSE";
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            getAllPermision();
        }
        showContentHome();


//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        widthPixels = metrics.widthPixels;
//        heightPixels = metrics.heightPixels;
//        scaleFactor = metrics.density;
//        widthDp = widthPixels / scaleFactor;
//        heightDp = heightPixels / scaleFactor;
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipefollowUp);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mroot = (RelativeLayout) findViewById(R.id.root);
//        snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
//        swipeRefreshLayout.setOnRefreshListener(this);
//        adapter = new VisitAdapter(Home.this, VisitData, mroot);
//        pdloadmore = (LinearLayout) findViewById(R.id.loadmorepd1);
//        recyclerView.setAdapter(adapter);
//        email = Utils.getFromPrefs(Home.this, EMAIL, "n");
//        authkey = Utils.getFromPrefs(Home.this, AUTHKEY_CLIENT, "n");
//        eid = Utils.getFromPrefs(Home.this, EID, "n");
//        bid = Utils.getFromPrefs(Home.this, BID, "n");
//        if (authkey.equals("n") || eid.equals("n") || email.equals("n") || bid.equals("n")) {
//            Intent intent = new Intent(Home.this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//            Home.this.startActivity(intent);
//        }
//        recyclerView.addOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore() {
//                if (pdloadmore.getVisibility() == View.GONE) {
//                    pdloadmore.setVisibility(View.VISIBLE);
//                }
//                if (!loading) {
//                    GetMoreData();
//                }
//
//            }
//
//            @Override
//            public void onLoadUp() {
//
//                // if (VisitData != null && VisitData.size() >= MAX) {
//                if (pdloadmore.getVisibility() == View.VISIBLE) {
//                    pdloadmore.setVisibility(View.GONE);
//
//
//                }
//
//            }
//        });
//        if (savedInstanceState != null) {
//            VisitData = savedInstanceState.getParcelableArrayList(STATE_VISITALLDATA);
//            if (VisitData != null) {
//                adapter.setData(VisitData);
//                Log.d("RESPONSE", "ALL LODED SCREEN ORIENTATION");
//            }
//
//        } else {
//            VisitData = MyApplication.getWritableDatabase().getAllSites();
//            if (VisitData != null && VisitData.size() > 0) {
//                adapter.setData(VisitData);
//            } else {
//                GetVisits();
//            }
//        }
//        swipeRefreshLayout.setColorSchemeResources(
//                R.color.refresh_progress_1,
//                R.color.refresh_progress_2,
//                R.color.refresh_progress_3);
    }
    public void showContentHome() {

        Fragment fr = new ContentHome();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        if(bundle!=null){
          //  bundle.putString("EMAIL", emailId);

        }
        fr.setArguments(bundle);
        transaction.replace(R.id.frame_layout_id, fr).commit();




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    Log.d("COUNT",getSupportFragmentManager().getBackStackEntryCount()+"");
                     getSupportFragmentManager().popBackStack();

                   //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }else {
                    showContentHome();

                }
                return true;
            case R.id.logout:
                Utils.showLogoutAlert(this);
                break;
        }

        return true;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            View view = this.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            // writeToLog("Software Keyboard was not shown");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);



    }

    public synchronized void GetVisits() {

        if (Utils.onlineStatus2(Home.this)) {
            MIN = 0;
            VisitData = new ArrayList<>();
            new GetVistHistory().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetVisits();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(Home.this, R.color.primary));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        GetVisits();
    }

    @Override
    public void onFinishRefralInputDialog(String name, String email, String msg, String num, String SiteId) {
        onReferSumbit(authkey, SiteId, name, msg, num, email);
    }

    public void onReferSumbit(final String authkey, final String siteid, final String name, final String messagee, final String phone, final String email) {

        if (Utils.onlineStatus2(Home.this)) {
            new ReferQuery(authkey, siteid, name, messagee, phone, email).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onReferSumbit(authkey, siteid, name, messagee, phone, email);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    @Override
    public void OnItemClick(String siteID) {
//        Intent i = new Intent(Home.this, Visitor.class);
//        // i.putExtra(SITEID, siteID);
        Utils.saveToPrefs(Home.this, SITEID, siteID);
//        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(i);
//        overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
        showVisitorFragment();

    }

    public void showVisitorFragment() {
        Fragment fr = new in.vmc.mconnecttab.fragment.Visitor();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       // transaction.setCustomAnimations(R.anim.entryanim, R.anim.exitanim, 0, 0);

        Bundle bundle = new Bundle();
        if (bundle != null) {
           // bundle.putString("EMAIL", emailId);

        }
        fr.setArguments(bundle);
        transaction.replace(R.id.frame_layout_id, fr).addToBackStack("Visitor").commit();
    }

    public void GetMoreData() {

        if (Utils.onlineStatus2(Home.this)) {
            MIN = VisitData.size();

            new GetMoreData().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetMoreData();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the movie list to a parcelable prior to rotation or configuration change
        outState.putParcelableArrayList(STATE_VISITALLDATA, VisitData);


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
                Log.d("REFER", response.toString());

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }
                if (response.has(MESSAGE)) {
                    message = response.getString(MESSAGE);
                }


            } catch (Exception e) {
                Log.d("REFER", e.getMessage());
            }
            return code;
        }

        @Override
        protected void onPostExecute(String data) {

            if (data != null) {

                if (code.equals("400")) {
                    Log.d("REFER", messagee);
                }
            }


        }


    }

    class GetVistHistory extends AsyncTask<Void, Void, ArrayList<VisitData>> {
        private final RequestQueue requestQueue;
        private final SingleTon volleySingleton;

        public GetVistHistory() {
            volleySingleton = SingleTon.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
        }


        @Override
        protected ArrayList<VisitData> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<VisitData> data = null;
            try {

                VisitData = Parser.ParseResponse(Requestor.requestAllSites(requestQueue, GET_ALLSITES, bid, MIN + "", MAX + ""));
                Log.d("RESPONSE", VisitData.toString());


            } catch (Exception e) {

            }
            return VisitData;
        }

        @Override
        protected void onPostExecute(ArrayList<VisitData> data) {

            loading = false;
            if (data != null) {
                VisitData = data;
                MyApplication.getWritableDatabase().insertAllSites(VisitData, true);
                adapter = new VisitAdapter(Home.this, VisitData, mroot);
                recyclerView.setAdapter(adapter);
            }


        }


    }

    class GetMoreData extends AsyncTask<Void, Void, ArrayList<VisitData>> {
        private final RequestQueue requestQueue;
        private final SingleTon volleySingleton;

        public GetMoreData() {
            volleySingleton = SingleTon.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            if (pdloadmore.getVisibility() == View.GONE) {
                pdloadmore.setVisibility(View.VISIBLE);
            }
        }


        @Override
        protected ArrayList<VisitData> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<VisitData> data = null;
            try {
                data = new ArrayList<VisitData>();
                data = Parser.ParseResponse(Requestor.requestAllSites(requestQueue, GET_ALLSITES, bid, MIN + "", MAX + ""));
                Log.d("RESPONSE", data.toString());


            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<VisitData> data) {
            if (pdloadmore.getVisibility() == View.VISIBLE) {
                pdloadmore.setVisibility(View.GONE);
            }
            loading = false;


            if (data != null && data.size() > 0 && data.get(0).getCode().equals("202")) {
                Snackbar.make(mroot, "No more records availabe", Snackbar.LENGTH_SHORT).show();

            } else if (data != null && data.size() > 0) {
                MyApplication.getWritableDatabase().insertAllSites(data, false);
                VisitData = MyApplication.getWritableDatabase().getAllSites();
                adapter.setData(VisitData);
                //recyclerView.setAdapter(adapter);
            }


        }


    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @TargetApi(Build.VERSION_CODES.M)
    private void getAllPermision() {

        requestPermissions(new String[]{
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        return;
    }



}
