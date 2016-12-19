package in.vmc.mconnecttab.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.activity.Home;
import in.vmc.mconnecttab.backgroundservice.IncomingSms;


/**
 * Created by mukesh on 17/11/15.
 */
public class OTPDialogFragment extends DialogFragment {
    static String DialogboxTitle;
    EditText txtname;
    Button btnDone, btnCancel;
    OTPDialogListener callback;
    private Context context;

    //---empty constructor required
    public OTPDialogFragment() {

    }

    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public void setOPT(String otp) {
        if (txtname != null) {
            txtname.setText(otp);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OTPDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.otp_dialog, container);

        //---get the EditText and Button views
        txtname = (EditText) view.findViewById(R.id.txtOTP);
        txtname.setInputType(InputType.TYPE_CLASS_NUMBER);
        btnDone = (Button) view.findViewById(R.id.btnDone);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);


        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                callback.onFinishInputDialog(txtname.getText().toString());
                //---gets the calling activity
//                OTPDialogListener activity = (OTPDialogListener)((Home) getActivity());
//                activity.onFinishInputDialog(txtname.getText().toString());
            //    frag= (OTPDialogListener) getTargetFragment();
              //  frag.onFinishInputDialog(txtname.getText().toString()!=null? txtname.getText().toString() :"");
                //---dismiss the alert
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //---gets the calling activity


                //---dismiss the alert
                dismiss();
            }
        });

        //---show the keyboard automatically
        //txtname.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
       // getDialog().setTitle(DialogboxTitle);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
       /* txtname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnDone.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 4) {
                    txtname.setText(s.toString());
                    btnDone.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });*/
//        IncomingSms.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text",messageText);
//                txtname.setText(messageText.substring(39));
//                //Toast.makeText(getActivity(),"Message: "+messageText,Toast.LENGTH_LONG).show();
//            }
//        });
        return view;
    }

    public interface OTPDialogListener {
        void onFinishInputDialog(String inputText);
    }
}