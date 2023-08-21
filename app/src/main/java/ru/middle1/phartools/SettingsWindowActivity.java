package ru.middle1.phartools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import ru.middle1.phartools.R;

public class SettingsWindowActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		TextView versionText = findViewById(R.id.VersionAppText);
		versionText.append(getCurrentVersionApp());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.MenuInflaterSettingsWindow, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.returnMainWindow:
			Intent intent = new Intent(SettingsWindowActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	String getCurrentVersionApp() {
		try{
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = packageInfo.versionName;
			return version;
			} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}