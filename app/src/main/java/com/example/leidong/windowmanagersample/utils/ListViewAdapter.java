package com.example.leidong.windowmanagersample.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leidong.windowmanagersample.MyApplication;
import com.example.leidong.windowmanagersample.R;

import java.util.List;

/**
 * Created by leidong on 2017/4/19.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<NotificationInfos> list;

    /**
     * 构造器
     * @param context context
     * @param list list
     */
    public ListViewAdapter(Context context, List<NotificationInfos> list){
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(MyApplication.getContext());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.notification_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NotificationInfos info = list.get(position);
        if(viewHolder.title != null && viewHolder.text != null) {
            viewHolder.title.setText(info.title);
            viewHolder.text.setText(info.text);
        }
        return convertView;
    }

    /**
     * 内部类
     */
    private class ViewHolder{
        ImageView icon;
        TextView title;
        TextView text;
    }
}
