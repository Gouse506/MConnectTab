package in.vmc.mconnecttab.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.utils.ConnectivityReceiver;
import in.vmc.mconnecttab.utils.JSONParser;
import in.vmc.mconnecttab.utils.Utils;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,in.vmc.mconnecttab.utils.TAG {
    public static final String DEAFULT = "n/a";
    private static final String TAG = "Login";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_FORGOT = 1;
    private static Login inst;
    public int widthPixels, heightPixels;
    public float widthDp, heightDp;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
   // @InjectView(R.id.link_signup)
    TextView _signupLink;
    @InjectView(R.id.checkBox)
    CheckBox checkBox;
    @InjectView(R.id.rootLayout)
    CoordinatorLayout mroot;
   // @InjectView(R.id.link_forgot)
    TextView _forgot;
    private float scaleFactor;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        scaleFactor = metrics.density;
        widthDp = widthPixels / scaleFactor;
        heightDp = heightPixels / scaleFactor;
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        load();
        logoAnimation();
        _passwordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        if (event.getRawX() >= (_passwordText.getRight() - _passwordText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here

                            if (_passwordText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                                // show password
                                _passwordText.setInputType(InputType.TYPE_CLASS_TEXT);
                                _passwordText.clearFocus();
                                _passwordText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_show, 0);
                            } else {
                                // hide password
                                _passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                _passwordText.clearFocus();
                                _passwordText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_hide, 0);

                            }
                            return false;
                        }
                    } catch (Exception e) {
                    }

                }
                return false;
            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                login();
            }
        });

//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the Signup activity
////                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
////                startActivityForResult(intent, REQUEST_SIGNUP);
//            }
//        });

//        _forgot.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
////                Intent intent = new Intent(getApplicationContext(), ForgotPasword.class);
////                startActivityForResult(intent, REQUEST_FORGOT);
//                // showFPDialog();
//            }
//        });


    }

    public void logoAnimation() {
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 100f, 0f);
        translation.setStartOffset(500);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());
        findViewById(R.id.logo).startAnimation(translation);


    }
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        } else {

            _loginButton.setEnabled(false);


            StartLogin();
        }
        // TODO: Implement your own authentication logic here.

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(mroot, "Registered Sucessfully Please Login To Continue", Snackbar.LENGTH_SHORT).show();
            }

        }
        if (requestCode == REQUEST_FORGOT) {
            if (resultCode == RESULT_OK) {
                String msg = data.getStringExtra("msg");

                Snackbar.make(mroot, msg, Snackbar.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        // moveTaskToBack(true);
        this.finish();

    }


    public void save() {
        if (checkBox.isChecked()) {
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();
            SharedPreferences pref = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("name", email);
            editor.putString("password", password);
            editor.commit();

        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
            prefs.edit().clear().commit();
            String email = _emailText.getText().toString();
            SharedPreferences pref = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("name", email);
            editor.putString("password", "");
            editor.commit();
        }
    }

    public void load() {
        SharedPreferences pref = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String name = pref.getString("name", DEAFULT);
        String password = pref.getString("password", DEAFULT);

        if (!name.equals(DEAFULT) || !password.equals(DEAFULT)) {
            _emailText.setText(name);
            _passwordText.setText(password);
        }
        if (password.equals("")) {
            _passwordText.requestFocus();
            //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        Drawable drawable = ContextCompat.getDrawable(Login.this, R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address", drawable);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters", drawable);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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


    public void StartLogin() {
        if (ConnectivityReceiver.isConnected()) {
            new StartLogin().execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StartLogin();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(Login.this, R.color.primary));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }


    class StartLogin extends AsyncTask<Void, Void, JSONObject> {
        String message = "n";
        String code = "n";
        String authcode = "n", name = "n";

        JSONObject response = null;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        private String image;
        private String eid,bid;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Login.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.login(LOGIN_URL, email, password);
                Log.d("RESPONSE",response.toString());
                if (response.has(CODE))
                    code = response.getString(CODE);
                if (response.has(MESSAGE))
                    message = response.getString(MESSAGE);
                if (response.has(AUTHKEY))
                    authcode = response.getString(AUTHKEY);
                if (response.has(USEREMAIL))
                    email = response.getString(USEREMAIL);
                if (response.has(EID)) {
                    eid = response.getString(EID);
                }  if (response.has(BID)) {
                    bid = response.getString(BID);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data != null) {
                Log.d("LOG", data.toString());
            }


            _loginButton.setEnabled(true);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (code.equals("202")) {

                Snackbar.make(mroot, message, Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StartLogin();

                            }
                        }).
                        setActionTextColor(ContextCompat.getColor(Login.this, R.color.accent)).show();
            }
            if (code.equals("400")) {
                save();
                Utils.saveToPrefs(Login.this, AUTHKEY_CLIENT, authcode);
                Utils.saveToPrefs(Login.this, EMAIL, email);
                Utils.saveToPrefs(Login.this, EID, eid);
                Utils.saveToPrefs(Login.this, BID, bid);
                Intent intent = new Intent(Login.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                Login.this.startActivity(intent);
                overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
            }


            if (code.equals("n")) {
                Snackbar.make(mroot, "No Response From Server", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StartLogin();

                            }
                        }).
                        setActionTextColor(ContextCompat.getColor(Login.this, R.color.accent)).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }



    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(mroot, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

}
