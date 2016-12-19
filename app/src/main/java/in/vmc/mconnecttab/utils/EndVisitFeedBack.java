package in.vmc.mconnecttab.utils;

import android.content.Intent;
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
import in.vmc.mconnecttab.activity.Home;


/**
 * Created by mukesh on 2/12/15.
 */
public class EndVisitFeedBack extends DialogFragment {
    static String DialogboxTitle;
    EditText txtname;
    Button btnsubmit, btnCancel;
    String siteID;


    //---empty constructor required
    public EndVisitFeedBack() {

    }

    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.endvisit, container);

        //---get the EditText and Button views
        txtname = (EditText) view.findViewById(R.id.sharefeedback);

        btnCancel = (Button) view.findViewById(R.id.Cancel);
        btnsubmit = (Button) view.findViewById(R.id.refer);

        //---event handler for the button
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (validate()) {
                    //---gets the calling activity
                    EndVisitFeedBackDialogListener activity = (EndVisitFeedBackDialogListener) getActivity();
                    activity.onFinishEndVisitFeedBackDialog(txtname.getText().toString());

                    //---dismiss the alert
                    dismiss();
                    Intent i=new Intent(getActivity(),Home.class);
                    //i.putExtra("ENDVISIT",true);
                    startActivity(i);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //---dismiss the alert
                dismiss();
                EndVisitFeedBackDialogListener activity = (EndVisitFeedBackDialogListener) getActivity();
                activity.onFinishEndVisitFeedBackDialog("N/A");
                Intent i=new Intent(getActivity(),Home.class);
               // i.putExtra("ENDVISIT",true);
                startActivity(i);

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
        String name;
        name = txtname.getText().toString();

        Drawable drawable = ContextCompat.getDrawable(getDialog().getContext().getApplicationContext(), R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
        if (name.isEmpty() || name.length() < 1) {
            txtname.setError("Feedback must be more than 10 characters", drawable);
            valid = false;
        } else {
            txtname.setError(null);
        }


        return valid;
    }


    public interface EndVisitFeedBackDialogListener {
        void onFinishEndVisitFeedBackDialog(String feedback);
    }
}
