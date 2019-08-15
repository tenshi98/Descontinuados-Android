/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package info.androidhive.loginandregistration.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.loginandregistration.R;

public class View_MyAlarms_Adapter extends ArrayAdapter<View_MyAlarms_Model> {

    private final Context context;
    private final ArrayList<View_MyAlarms_Model> modelsArrayList;

    public View_MyAlarms_Adapter(Context context, ArrayList<View_MyAlarms_Model> modelsArrayList) {

        super(context, R.layout.target_item_alarm, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        if(!modelsArrayList.get(position).isGroupHeader()){
            rowView = inflater.inflate(R.layout.target_item_alarm, parent, false);

            // 3. Get icon,title & counter views from the rowView
            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            TextView titleText = (TextView) rowView.findViewById(R.id.item_text);

            // 4. Set the text for textView
            titleView.setText(modelsArrayList.get(position).getTitle());
            titleText.setText(modelsArrayList.get(position).getText());
        }else{
            rowView = inflater.inflate(R.layout.group_header_item, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.header);
            titleView.setText(modelsArrayList.get(position).getTitle());

        }

        // 5. retrn rowView
        return rowView;
    }
}