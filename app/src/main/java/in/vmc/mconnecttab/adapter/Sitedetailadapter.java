package in.vmc.mconnecttab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.model.SiteData;


/**
 * Created by mukesh on 8/1/16.
 */
public class Sitedetailadapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<SiteData> siteData;


    public Sitedetailadapter(Context context, ArrayList<SiteData> siteData) {
        super();

        this.context = context;
        this.siteData = siteData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return siteData.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = layoutInflater.inflate(R.layout.custom_siteview, null);

        TextView txt = (TextView) convertView.findViewById(R.id.tv);

        txt.setText(siteData.get(position).getName());


        return convertView;
    }
}
