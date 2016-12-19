package in.vmc.mconnecttab.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.model.Model;


/**
 * Created by mukesh on 28/12/15.
 */
public class FirstVisitDailog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    static String DialogboxTitle;
    EditText msg;
    String selection = "";
    Button btnDone, btnCancel;
    private RadioGroup radioGroup;
    private Model model;
    private View view;

    public void setOptions(Model model) {
        this.model = model;
    }


    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        view = inflater.inflate(
                R.layout.first_visit_dialog, container);

        //---get the EditText and Button views
        btnDone = (Button) view.findViewById(R.id.submit);
        radioGroup = (RadioGroup) view.findViewById(R.id.myRadioGroup);

        msg = (EditText) view.findViewById(R.id.msg);
        addRadioButtons(view, model);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) view.findViewById(checkedRadioButtonId);
                selection = radioBtn.getText().toString();
                // Log.d("checked",radioGroup.getCheckedRadioButtonId()+""+selection);

            }
        });

        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirstVisitListener activity = (FirstVisitListener) getActivity();
                String message = "" + msg.getText().toString();
                activity.onFinishFirstVisit(message, selection);

                dismiss();
            }
        });

        getDialog().setTitle("Are you intrested in ?");

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    public void addRadioButtons(View view, Model model) {

        for (int row = 0; row < 1; row++) {
            LinearLayout ll = new LinearLayout(getDialog().getContext());
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < model.getOptionsData().size(); i++) {
                RadioButton rdbtn = new RadioButton(getDialog().getContext());
                rdbtn.setId(i);
                rdbtn.setText(model.getOptionsData().get(i).getOptionName());
                rdbtn.setOnCheckedChangeListener(this);
                radioGroup.addView(rdbtn);
                // ll.addView(rdbtn);
            }
            //  ((ViewGroup) view.findViewById(R.id.myRadioGroup)).addView(ll);
        }

    }

    public interface FirstVisitListener {
        void onFinishFirstVisit(String message, String interest);
    }
}
