package in.vmc.mconnecttab.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.activity.Home;
import in.vmc.mconnecttab.activity.VisitorForm;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Visitor extends Fragment implements TAG{

    @InjectView(R.id.tvnewVisitor)
    TextView tvNewVisitor;
    @InjectView(R.id.tvvisitor)
    TextView tvVisitor;
    private Toolbar toolbar;
    private String siteID;

    public Visitor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_visitor, container, false);
        ButterKnife.inject(this,view);
       // toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((Home)getActivity()).getSupportActionBar().setTitle("Visitor");
        ((Home)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Home)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        siteID = Utils.getFromPrefs(getActivity(), SITEID, "N");
        if (siteID.equals("N")) {
            getActivity().finish();
        }
        tvNewVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewVisitorFragment();

            }
        });
        return view;
    }


    public void showNewVisitorFragment() {
        Fragment fr = new NewVisitor();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
       // transaction.setCustomAnimations(R.anim.entryanim, R.anim.exitanim, 0, 0);
        transaction.setCustomAnimations(R.anim.fragment_enter_bottom, R.anim.fragment_exit_bottom,
                R.anim.fragment_enter_top, R.anim.fragment_exit_top);
        Bundle bundle = new Bundle();
        if (bundle != null) {
            // bundle.putString("EMAIL", emailId);

        }
        fr.setArguments(bundle);
        transaction.replace(R.id.frame_layout_id, fr).addToBackStack("NewVisitor").commit();
    }
}
