package in.vmc.mconnecttab.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import in.vmc.mconnecttab.utils.SmsListener;

/**
 * Created by mukesh on 26/11/15.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private String sender;
    private static SmsListener mListener;
    public void onReceive(Context context, Intent intent) {

        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                sender = messages[i].getOriginatingAddress();
                if (sender.equals("TD-VMCIND")) {
                    strMessage = messages[i].getMessageBody();
                    Log.d("OTP", strMessage);
                    if(mListener!=null) {
                        mListener.messageReceived(strMessage);
                    }



                    break;
                }

            }


        }
    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}

