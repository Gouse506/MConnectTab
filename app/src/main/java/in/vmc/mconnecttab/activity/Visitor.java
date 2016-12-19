package in.vmc.mconnecttab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.utils.TAG;
import in.vmc.mconnecttab.utils.Utils;

public class Visitor extends AppCompatActivity implements TAG {

    @InjectView(R.id.tvnewVisitor)
    TextView tvNewVisitor;
    @InjectView(R.id.tvvisitor)
    TextView tvVisitor;
    private Toolbar toolbar;
    private String siteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
        setContentView(R.layout.activity_visitor);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        siteID = Utils.getFromPrefs(Visitor.this, SITEID, "N");
        if (siteID.equals("N")) {
            this.finish();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvNewVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Visitor.this, VisitorForm.class);
                Utils.saveToPrefs(Visitor.this, SITEID, siteID);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                Intent intent = new Intent(Visitor.this, Home.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                Visitor.this.startActivity(intent);
//                overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
                // finish();

                //Utils.GoBack(Visitor.this, Home.class);
                return true;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
