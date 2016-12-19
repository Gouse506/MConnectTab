package in.vmc.mconnecttab.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.activity.Login;


/**
 * Created by mukesh on 6/7/15.
 */
public class Utils implements TAG {

    public static ArrayList<String> sortArray(ArrayList<String> al) {
        Comparator<String> nameComparator = new Comparator<String>() {
            @Override
            public int compare(String value1, String value2) {
                if (Character.isDigit(value1.charAt(0)) && !Character.isDigit(value2.charAt(0)))
                    return 1;
                if (Character.isDigit(value2.charAt(0)) && !Character.isDigit(value1.charAt(0)))
                    return -1;
                return value1.compareTo(value2);
            }
        };

        Collections.sort(al, nameComparator);

        System.out.println(al);
        return al;
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }


    public static double tabletSize(Context context) {

        double size = 0;
        try {

            // Compute screen size

            DisplayMetrics dm = context.getResources().getDisplayMetrics();

            float screenWidth = dm.widthPixels / dm.xdpi;

            float screenHeight = dm.heightPixels / dm.ydpi;

            size = Math.sqrt(Math.pow(screenWidth, 2) +

                    Math.pow(screenHeight, 2));

        } catch (Throwable t) {

        }

        return size;

    }


    public static boolean onlineStatus1(Context activityContext) {
        ConnectivityManager cm = (ConnectivityManager)
                activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] activeNetInfo = cm.getAllNetworkInfo();
        boolean isConnected = false;
        for (NetworkInfo i : activeNetInfo) {
            if (i.getState() == NetworkInfo.State.CONNECTED) {
                isConnected = true;
                break;
            }
        }


        return isConnected;
    }


    public static boolean onlineStatus2(Context activityContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {

                            return true;
                        }
                    }
                }
            }
        }
        //  Toast.makeText(mContext,mContext.getString(R.string.please_connect_to_internet),Toast.LENGTH_SHORT).show();
        return false;
    }


    public static int getAPILevel() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public static int memoryRemaining(Context activityContext) {
        int memClass = ((ActivityManager) activityContext.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        return memClass;
    }


    public static int getBatteryLevel(Context context) {
        int batteryPercentage = 0;
        try {
            IntentFilter ifilter = new IntentFilter(
                    Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,
                    -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,
                    -1);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS,
                    -1);

            float batteryPct = level / (float) scale;
            batteryPercentage = (int) (batteryPct * 100);
            if (batteryPercentage < 0) {
                batteryPercentage = 0;
            }
            // McubeUtils.infoLog("Battery level remaining : " + batteryPercentage + "%");
            String strStatus = "";
            switch (status) {
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    strStatus = "Unknown Charged";
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    strStatus = "Charged Plugged";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    strStatus = "Charged Unplugged";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    strStatus = "Not Charging";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    strStatus = "Charged Completed";
                    break;
            }
            // McubeUtils.infoLog("Battery status  " + strStatus);
        } catch (Exception e) {
            // McubeUtils.errorLog(e.toString());
        }

        return batteryPercentage;
    }

    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveToPrefs(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void isLogout(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();
        Intent intent = new Intent(context, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);



    }

    public static void GoBack(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


    }


    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Boolean isLogin(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String authKey = sharedPrefs.getString(AUTHKEY, "n");
        String empName = sharedPrefs.getString(NAME, "n");
        String empEmail = sharedPrefs.getString(EMAIL, "n");


        if (authKey.equals("n") && empName.equals("n") && empEmail.equals("n")) {
            return false;
        } else {
            return true;
        }

    }

    public static boolean isEmpty(String msg) {
        return msg.trim().equals("")
                || msg == null
                || msg.isEmpty();
    }


    public static Boolean getFromPrefsBoolean(Context context, String key, Boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Calendar getCurrentDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) Math.ceil(px / (metrics.densityDpi / 160f));
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToSp(float px, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static void makeAcall(String number, final Activity mActivity) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            showMessageOKCancel("You need to allow access to Calls",
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
                                    MY_PERMISSIONS_CALL);
                        }
                    }, mActivity);
            return;

        }
        mActivity.startActivity(callIntent);

    }

    public static void sendSms(String number, Activity mActivity) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", " ");
        sendIntent.putExtra("address", number);
        sendIntent.setType("vnd.android-dir/mms-sms");
        mActivity.startActivity(sendIntent);

    }

    private static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, Context mActivity) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static void sendAnEmail(String recipient, Activity mActivity) {
        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            if (recipient != null)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
//	        if (subject != null) {   emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);}
//	        if (message != null)    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            mActivity.startActivity(emailIntent);

        } catch (ActivityNotFoundException e) {
            // cannot send email for some reason
        }

    }




    public  static void showLogoutAlert(final Context context) {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        //alertDialog.setTitle("MConnect");
        alertDialog.setTitle(Html.fromHtml("<font  size='30' color='#FF7F27'>MConnect</font>"));
        alertDialog.setIcon(R.mipmap.ic_launcher);
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setMessage(Html.fromHtml("<h4><font size='20' color='#63c3ef'>Are you sure you want to logout?</font></h4>"));

        // On pressing Settings button
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Utils.isLogout(context);


            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
