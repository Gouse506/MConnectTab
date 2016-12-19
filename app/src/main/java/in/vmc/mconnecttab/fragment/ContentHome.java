package in.vmc.mconnecttab.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.activity.Home;
import in.vmc.mconnecttab.activity.Login;
import in.vmc.mconnecttab.activity.MyApplication;
import in.vmc.mconnecttab.adapter.VisitAdapter;
import in.vmc.mconnecttab.model.VisitData;
import in.vmc.mconnecttab.parser.Parser;
import in.vmc.mconnecttab.parser.Requestor;
import in.vmc.mconnecttab.utils.EndlessScrollListener;
import in.vmc.mconnecttab.utils.JSONParser;
import in.vmc.mconnecttab.utils.ReferDialogFragment;
import in.vmc.mconnecttab.utils.SingleTon;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentHome extends Fragment implements in.vmc.mconnecttab.utils.TAG,SwipeRefreshLayout.OnRefreshListener,
        ReferDialogFragment.ReferDialogListener{
    public float scaleFactor, widthDp, heightDp;
    public Snackbar snack;
    ArrayList<in.vmc.mconnecttab.model.VisitData> VisitData = new ArrayList<>();
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

    public ContentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.content_home, container, false);
        ((Home)getActivity()).getSupportActionBar().setTitle("Home");
        ((Home)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((Home)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipefollowUp);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mroot = (RelativeLayout) view.findViewById(R.id.root);
       // snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new VisitAdapter(getActivity(), VisitData, mroot);
        pdloadmore = (LinearLayout) view.findViewById(R.id.loadmorepd1);
        recyclerView.setAdapter(adapter);
        email = Utils.getFromPrefs(getActivity(), EMAIL, "n");
        authkey = Utils.getFromPrefs(getActivity(), AUTHKEY_CLIENT, "n");
        eid = Utils.getFromPrefs(getActivity(), EID, "n");
        bid = Utils.getFromPrefs(getActivity(), BID, "n");
        if (authkey.equals("n") || eid.equals("n") || email.equals("n") || bid.equals("n")) {
            Intent intent = new Intent(getActivity(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
           getActivity().startActivity(intent);
        }
        recyclerView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                if (pdloadmore.getVisibility() == View.GONE) {
                    pdloadmore.setVisibility(View.VISIBLE);
                }
                if (!loading) {
                    GetMoreData();
                }

            }

            @Override
            public void onLoadUp() {

                // if (VisitData != null && VisitData.size() >= MAX) {
                if (pdloadmore.getVisibility() == View.VISIBLE) {
                    pdloadmore.setVisibility(View.GONE);


                }

            }
        });
        if (savedInstanceState != null) {
            VisitData = savedInstanceState.getParcelableArrayList(STATE_VISITALLDATA);
            if (VisitData != null) {
                adapter.setData(VisitData);
                Log.d("RESPONSE", "ALL LODED SCREEN ORIENTATION");
            }

        } else {
            VisitData = MyApplication.getWritableDatabase().getAllSites();
            if (VisitData != null && VisitData.size() > 0) {
                adapter.setData(VisitData);
            } else {
                GetVisits();
            }
        }
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);


        return view;
    }



    public synchronized void GetVisits() {

        if (Utils.onlineStatus2(getActivity())) {
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
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
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

        if (Utils.onlineStatus2(getActivity())) {
            new ReferQuery(authkey, siteid, name, messagee, phone, email).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onReferSumbit(authkey, siteid, name, messagee, phone, email);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }



    public void GetMoreData() {

        if (Utils.onlineStatus2(getActivity())) {
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
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

    }

    public void showReferDailogDialog(String siteid) {
        FragmentManager fragmentManager = getFragmentManager();
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
                adapter = new VisitAdapter(getActivity(), VisitData, mroot);
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
