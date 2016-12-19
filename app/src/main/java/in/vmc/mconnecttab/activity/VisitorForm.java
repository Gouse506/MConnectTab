package in.vmc.mconnecttab.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.utils.JSONParser;
import in.vmc.mconnecttab.utils.OTPDialogFragment;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

public class VisitorForm extends AppCompatActivity implements TAG,OTPDialogFragment.OTPDialogListener {

    @InjectView(R.id.sp_state)
    Spinner spState;
    @InjectView(R.id.sp_city)
    Spinner spCity;
    @InjectView(R.id.input_name)
    EditText etName;
    @InjectView(R.id.input_email)
    EditText etEmail;
    @InjectView(R.id.input_phone)
    EditText etPhone;
    @InjectView(R.id.etPincode)
    EditText etPincode;
    @InjectView(R.id.et_address)
    EditText etAddress;
    @InjectView(R.id.btn_signup)
    Button btnSignup;
    @InjectView(R.id.rootLayout)
    LinearLayout mroot;
    private Toolbar toolbar;
    private String[] cities = {"Default", "AndhraPradesh", "ArunachalPradesh", "Assam", "Bihar", "Chandigarh", "Chhatisgarh", "DadraAndNagarHaveli",
            "DamanAndDiu", "Delhi", "Goa", "Gujarat", "Haryana", "HimachalPradesh", "JammuKashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep",
            "MadhyaPradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Orissa", "Puducherry", "Punjab", "Rajasthan", "Sikkim",
            "TamilNadu", "Telangana", "Tripura", "UttarPradesh", "Uttarakhand", "WestBengal"};
    private ArrayList<String> regions;
    private String[] regionsArray;
    private String ResOtp="";
    private ProgressDialog progressDialog;
    private OTPDialogFragment otpDialogFragment;
    private String name, email, pincode, phone, state, city, Address;
    private String siteID, eid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
        setContentView(R.layout.activity_visitor_form);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        siteID = Utils.getFromPrefs(VisitorForm.this, SITEID, "N");
        eid = Utils.getFromPrefs(VisitorForm.this, EID, "N");
        if (siteID.equals("N") || eid.equals("N")) {
            this.finish();
        }
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int arryid = getResources().getIdentifier(cities[position], "array", getPackageName());
                regionsArray = getResources().getStringArray(arryid);
                regions = new ArrayList<String>(Arrays.asList(regionsArray));
                ArrayAdapter<String> a = new ArrayAdapter<String>(VisitorForm.this, android.R.layout.simple_spinner_item, regions);
                a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCity.setAdapter(a);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                if (validate(v)) {
                    name = etName.getText().toString();
                    email = etEmail.getText().toString();
                    pincode = etPincode.getText().toString();
                    phone = etPhone.getText().toString();
                    state = spState.getSelectedItem().toString();
                    city = spCity.getSelectedItem().toString();
                    Address = etAddress.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(VisitorForm.this);
                    builder.setMessage("A one time password will be sent to your number +91" + phone + " go back to change your mobile number or continue to activate .")
                            .setCancelable(false)
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    GetOtp(phone);

                                }
                            })
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    btnSignup.setEnabled(true);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    TextView textView = (TextView) alert.findViewById(android.R.id.message);
                    textView.setTextSize(30);

                }


            }
        });

    }

    public void GetOtp(final String phone) {
        if (Utils.onlineStatus2(VisitorForm.this)) {
            new GetOtp(phone).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetOtp(phone);

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(VisitorForm.this, R.color.primary));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return true;
    }

    @Override
    public void onFinishInputDialog(String inputText) {

        if (ResOtp.equals(inputText)) {
            Register(name, email, pincode, phone, city, state, Address);
            hideKeyboard();

        } else {
            Toast.makeText(VisitorForm.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOTPDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        otpDialogFragment = new OTPDialogFragment();
        otpDialogFragment.setCancelable(false);
        otpDialogFragment.setDialogTitle("Enter OTP");
        otpDialogFragment.show(fragmentManager, "Input Dialog");
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

    public boolean validate(View v) {
        boolean valid = true;

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String pincode = etPincode.getText().toString();
        String phone = etPhone.getText().toString();
        String Address = etAddress.getText().toString();
        Drawable drawable = ContextCompat.getDrawable(VisitorForm.this, R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
        if (name.isEmpty() || name.length() < 4) {
            etName.setError("at least 4 characters", drawable);
            valid = false;
        } else {
            etName.setError(null);
        }
        if (Address.isEmpty() || Address.length() < 10) {
            etAddress.setError("at least 10 characters", drawable);
            valid = false;
        } else {
            etAddress.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            etPhone.setError("at least 10 digit", drawable);
            valid = false;
        } else {
            etPhone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address", drawable);
            valid = false;
        } else {
            etEmail.setError(null);
        }
        if (pincode.isEmpty() || pincode.length() < 6) {
            etPincode.setError("must be 6 numeric characters", drawable);
            valid = false;
        } else {
            etPincode.setError(null);
        }

        if (spState.getSelectedItemPosition() == 0) {
            Snackbar snack = Snackbar.make(v, "Select a valid State ", Snackbar.LENGTH_SHORT);
            snack.show();
            valid = false;
        } else {

        }
        return valid;
    }

    public void Register(final String name, final String email, final String pincode, final String phone, final String state, final String city, final String address) {
        if (Utils.onlineStatus2(VisitorForm.this)) {
            new Register(name, email, pincode, phone, state, city, address).execute();

        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Register(name, email, pincode, phone, state, city, address);
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }


    class GetOtp extends AsyncTask<Void, Void, JSONObject> {
        String message = "No Response from server";
        String code = "N";
        String phone = "n";

        String msg;
        JSONObject response = null;


        public GetOtp(String phone) {
            this.phone = phone;
        }

        @Override
        protected void onPreExecute() {
            btnSignup.setEnabled(false);
            // showProgress("Login Please Wait.."); progressDialog.setIndeterminate(true);
            progressDialog = new ProgressDialog(VisitorForm.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub


            try {
                response = JSONParser.getOTP(GENERATE_OTP_URL, phone);
                Log.d("RESPONSE", response.toString());
                if (response != null) {
                    if (response.has(CODE)) {
                        code = response.getString(CODE);
                    }
                    if (response.has(OTP)) {
                        ResOtp = response.getString(OTP);
                    }
                    if (response.has(MESSAGE)) {
                        msg = response.getString(MESSAGE);
                    }
                }

                //  if(response.c)

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            btnSignup.setEnabled(true);
            if (data != null) {
                Log.d("TEST", data.toString());
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (code.equals("202")) {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            } else if(data!=null) {
                showOTPDialog();
            }
            else{
                Toast.makeText(getBaseContext(), "No response from server", Toast.LENGTH_LONG).show();
            }

        }

    }

    class Register extends AsyncTask<Void, Void, JSONObject> {
        String code = "N";
        JSONObject response = null;
        String name;
        String email;
        String pincode;
        String phone;
        String state;
        String city;
        String address;
        private String msg;
        private String authkey;


        public Register(String name, String email, String pincode, String phone, String state, String city, String address) {
            this.name = name;
            this.email = email;
            this.pincode = pincode;
            this.phone = phone;
            this.state = state;
            this.city = city;
            this.address = address;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(VisitorForm.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Verfying OTP");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.Register(REGISTER_URL, name, email, phone, address, city, state, pincode,eid);

                Log.d("RESPONSE", response.toString());
                Log.d("RESPONSE URL",REGISTER_URL);
                if (response.has(CODE)) {
                    code = response.getString(CODE);
                }
                if (response.has(MESSAGE)) {
                    msg = response.getString(MESSAGE);
                }
                if (response.has(AUTHKEY_VISITOR)) {
                    authkey = response.getString(AUTHKEY_VISITOR);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data != null) {
                Log.d("TEST", data.toString());
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (code.equals("n")) {
                Snackbar.make(mroot, "No Response From Server", Snackbar.LENGTH_SHORT).show();
            }
            if (code.equals("202")) {
                Snackbar.make(mroot, msg, Snackbar.LENGTH_SHORT).show();
            } else if (code.equals("400")) {
                Snackbar.make(mroot, msg, Snackbar.LENGTH_SHORT).show();

                Intent i = new Intent(VisitorForm.this, CurrentSite.class);
                Utils.saveToPrefs(VisitorForm.this, SITEID, siteID);
                Utils.saveToPrefs(VisitorForm.this, AUTHKEY_VISITOR, authkey);
                VisitorForm.this.startActivity(i);
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);


            } else {
                btnSignup.setEnabled(true);
            }
        }

    }
}
