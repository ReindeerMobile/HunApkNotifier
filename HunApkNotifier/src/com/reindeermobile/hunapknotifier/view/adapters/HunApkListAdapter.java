package com.reindeermobile.hunapknotifier.view.adapters;

import com.reindeermobile.hunapknotifier.R;
import com.reindeermobile.hunapknotifier.entities.HunApkInfo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class HunApkListAdapter extends ArrayAdapter<HunApkInfo> {
	public static final String TAG = "HunApkListAdapter";

	private ViewHolder viewHolder;
	private List<HunApkInfo> hunApkInfoList;
	SimpleDateFormat curFormater = new SimpleDateFormat("yy-MM-dd");

	private Typeface font;

	public HunApkListAdapter(Context context, int resource,
			int textViewResourceId, List<HunApkInfo> hunApkInfoList) {
		super(context, resource, textViewResourceId, hunApkInfoList);
		this.hunApkInfoList = hunApkInfoList;
		this.font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Light.ttf");
	}

	public HunApkListAdapter(Context context, int textViewResourceId,
			List<HunApkInfo> hunApkInfoList) {
		super(context, textViewResourceId, hunApkInfoList);
		this.hunApkInfoList = hunApkInfoList;
		this.font = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Light.ttf");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HunApkInfo item = this.hunApkInfoList.get(position);
		String name = item.getName();
		String author = item.getAuthor();
		String date = "ismeretlen";
		boolean readed = item.isReaded();

		if (item.getDate() != null) {
			date = curFormater.format(item.getDate());
		}

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.listitem_hunapkinfo,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.layout = (RelativeLayout) convertView;

			viewHolder.nameTextView = (TextView) convertView
					.findViewById(R.id.textViewName);
			viewHolder.nameTextView.setTypeface(font);

			viewHolder.authorTextView = (TextView) convertView
					.findViewById(R.id.textViewAuthor);
			viewHolder.authorTextView.setTypeface(font);
			viewHolder.dateTextView = (TextView) convertView
					.findViewById(R.id.textViewDate);
			viewHolder.dateTextView.setTypeface(font);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nameTextView.setText(name);
		if (!readed) {
			viewHolder.nameTextView.setTextAppearance(getContext(),
					R.style.bold_item_text);
			viewHolder.layout.setBackgroundColor(Color.parseColor("#111111"));
		} else {
			viewHolder.nameTextView.setTextAppearance(getContext(),
					R.style.normal_item_text);
			viewHolder.layout.setBackgroundColor(Color.parseColor("#000000"));
		}
		viewHolder.authorTextView.setText(author);
		viewHolder.dateTextView.setText(date);

		return convertView;
	}

	static class ViewHolder {
		RelativeLayout layout;
		TextView nameTextView;
		TextView authorTextView;
		TextView dateTextView;
	}

}
