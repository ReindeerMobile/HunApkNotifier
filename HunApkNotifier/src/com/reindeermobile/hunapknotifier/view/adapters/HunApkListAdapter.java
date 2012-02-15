package com.reindeermobile.hunapknotifier.view.adapters;

import com.reindeermobile.hunapknotifier.R;
import com.reindeermobile.hunapknotifier.entities.HunApkInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class HunApkListAdapter extends ArrayAdapter<HunApkInfo> {
	public static final String TAG = "HunApkListAdapter";

	private ViewHolder viewHolder;
	private List<HunApkInfo> hunApkInfoList;
	SimpleDateFormat curFormater = new SimpleDateFormat("yy-MM-dd");

	public HunApkListAdapter(Context context, int resource,
			int textViewResourceId, List<HunApkInfo> hunApkInfoList) {
		super(context, resource, textViewResourceId, hunApkInfoList);
		this.hunApkInfoList = hunApkInfoList;
	}

	public HunApkListAdapter(Context context, int textViewResourceId,
			List<HunApkInfo> hunApkInfoList) {
		super(context, textViewResourceId, hunApkInfoList);
		this.hunApkInfoList = hunApkInfoList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HunApkInfo item = this.hunApkInfoList.get(position);
		String name = item.getName();
		String author = item.getAuthor();
		String date = "ismeretlen";
		if (item.getDate() != null) {
			date = curFormater.format(item.getDate());
		}

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.listitem_hunapkinfo,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.nameTextView = (TextView) convertView
					.findViewById(R.id.textViewName);
			viewHolder.authorTextView = (TextView) convertView
					.findViewById(R.id.textViewAuthor);
			viewHolder.dateTextView = (TextView) convertView
					.findViewById(R.id.textViewDate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nameTextView.setText(name);
		viewHolder.authorTextView.setText(author);
		viewHolder.dateTextView.setText(date);

		return convertView;
	}

	static class ViewHolder {
		TextView nameTextView;
		TextView authorTextView;
		TextView dateTextView;
	}

}
