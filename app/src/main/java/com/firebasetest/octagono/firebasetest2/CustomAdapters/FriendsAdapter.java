package com.firebasetest.octagono.firebasetest2.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.firebasetest.octagono.firebasetest2.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by OCTAGONO on 6/23/2017.
 */

public class FriendsAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listHeader;
    private HashMap<String, List<String>> _listChild;

    public FriendsAdapter(Context context, List<String> listHeader, HashMap<String, List<String>> listChild){
        this._context = context;
        this._listHeader = listHeader;
        this._listChild = listChild;
    }



    @Override
    public int getGroupCount() {
        return this._listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listChild.get(this._listHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChild.get(this._listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listviewfriendsheader, null);
        }
        TextView txtHeader = (TextView) convertView.findViewById(R.id.textviewheader);
        txtHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater
                    .inflate(R.layout.listviewfriendschild, null);
        }
            TextView txtChild = (TextView) convertView
                    .findViewById(R.id.textviewchild);

            txtChild.setText(childText);
            return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
