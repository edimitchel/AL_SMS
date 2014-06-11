package com.cnam.al_sms.modeles;

import android.graphics.Bitmap;
import android.net.Uri;

public class NavDrawerItem {

	private String title;
	private int icon = 0;
	private Bitmap iconBitmap;

	private boolean isImageBitmap = false;
	private String count = "0";
	private String countUnread = "0";

	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem(String title, Bitmap bitmap, boolean isCounterVisible,
			String count, String countUnRead) {
		this.title = title;
		this.isImageBitmap = true;
		this.iconBitmap = bitmap;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.countUnread = countUnRead;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public String getCount() {
		return this.count;
	}

	public String getCountUnread() {
		return countUnread;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

	public Bitmap getBitmap() {
		return iconBitmap;
	}

	public boolean isImageBitmap() {
		return isImageBitmap;
	}
}
