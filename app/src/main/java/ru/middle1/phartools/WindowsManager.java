package ru.middle1.phartools;

import android.app.ActionBar;
import android.content.Context;
import android.text.Layout;
import android.view.View;

public class WindowsManager {
	private Context context;
	private MainActivity activity;

	public WindowsManager(Context context, MainActivity activity) {
		this.context = context;
		this.activity = activity;
	}
	public void ControllSettingWindow(){
		View settingsLayout = activity.findViewById(R.layout.settings_layout);
		activity.setContentView(settingsLayout);
	}
}