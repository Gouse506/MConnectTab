package in.vmc.mconnecttab.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import in.vmc.mconnecttab.R;


/**
 * Created by mukesh on 2/12/15.
 */
public class ReferDialogFragment extends DialogFragment {
    static String DialogboxTitle;
    EditText txtname, txtemail, txtmessage, txtnum;
    Button btnsubmit, btnCancel;
    String siteID;

    //---empty constructor required
    public ReferDialogFragment() {

    }

    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.referal, container);

        //---get the EditText and Button views
        txtname = (EditText) view.findViewById(R.id.sharename);
        txtmessage = (EditText) view.findViewById(R.id.sharemsg);
        txtemail = (EditText) view.findViewById(R.id.shareemail);
        txtnum = (EditText) view.findViewById(R.id.sharenum);

        btnCancel = (Button) view.findViewById(R.id.Cancel);
        btnsubmit = (Button) view.findViewById(R.id.refer);

        //---event handler for the button
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (validate()) {
                    //---gets the calling activity
                    ReferDialogListener activity = (ReferDialogListener) getActivity();
                    activity.onFinishRefralInputDialog(txtname.getText().toString(), txtemail.getText().toString(), txtmessage.getText().toString(), txtnum.getText().toString(), siteID);

                    //---dismiss the alert
                    dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //---dismiss the alert
                dismiss();
            }
        });

        //---show the keyboard automatically
        txtname.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

        return view;
    }

    public boolean validate() {
        boolean valid = true;
        String name, email, message, phone;
        name = txtname.getText().toString();
        message = txtmessage.getText().toString();
        email = txtemail.getText().toString();
        phone = txtnum.getText().toString();

        Drawable drawable = ContextCompat.getDrawable(getDialog().getContext().getApplicationContext(), R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
        if (name.isEmpty() || name.length() < 4) {
            txtname.setError("at least 4 characters", drawable);
            valid = false;
        } else {
            txtname.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            txtnum.setError("at least 10 digit", drawable);
            valid = false;
        } else {
            txtnum.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtemail.setError("enter a valid email address", drawable);
            valid = false;
        } else {
            txtemail.setError(null);
        }
        if (message.isEmpty() || message.length() < 10) {
            txtmessage.setError("Message must be more than 10 Character", drawable);
            valid = false;
        } else {
            txtmessage.setError(null);
        }


        return valid;
    }


    public interface ReferDialogListener {
        void onFinishRefralInputDialog(String name, String email, String msg, String num, String SiteId);
    }
}
