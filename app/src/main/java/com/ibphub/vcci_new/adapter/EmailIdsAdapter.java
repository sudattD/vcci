package com.ibphub.vcci_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ibphub.vcci_new.fragment.ContactFragment;
import com.ibphub.vcci_new.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailIdsAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> myContactDataList = new ArrayList<>();
    private LayoutInflater mInflater;
    private ContactFragment contactFragment;

    public EmailIdsAdapter(@NonNull Context context, ContactFragment contactFragment) {
        super(context, 0);
        this.mContext = context;
        this.contactFragment = contactFragment;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<String> myContactDataList) {
        this.myContactDataList = myContactDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (myContactDataList != null && myContactDataList.size() > 0) {
            return myContactDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_text_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String contact_info = myContactDataList.get(position);
        viewHolder.text_title.setText(contact_info);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.text_title)
        TextView text_title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
