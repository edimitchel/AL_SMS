package com.cnam.al_sms.modeles;

import android.net.Uri;

public class NavDrawerItem {

	private String title;
	private int icon = 0;
	private Uri iconUri;

	private boolean isImageUri = false;
	private String count = "0";

	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem(String title, Uri imageUri, boolean isCounterVisible,
			String count) {
		this.title = title;
		this.isImageUri = true;
		this.iconUri = imageUri;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
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
	
	public Uri getIconUri() {
		return iconUri;
	}

	public boolean isImageUri() {
		return isImageUri;
	}
}
