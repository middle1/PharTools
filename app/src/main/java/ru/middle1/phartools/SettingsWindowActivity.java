package ru.middle1.phartools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ru.middle1.phartools.Managers.Manager;
import ru.middle1.phartools.R;

public class SettingsWindowActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		MainActivity activity = MainActivity.getInstance();
		Manager manager = new Manager(this, activity);
		String appVersion = manager.getSharedPreferences("version");
		SaveSettings(manager);
		TextView VersionLabel = (TextView) findViewById(R.id.VersionAppText);
		VersionLabel.setText("Версия приложения: " + appVersion);
		EditText StubLabel = findViewById(R.id.stubLabel);
		String stub = manager.getSharedPreferences("stub");
		StubLabel.setText(stub);
		if (StubLabel.getText().toString().isEmpty()) {
			StubLabel.setText("__HALT_COMPILER();");
		}
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
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void SaveSettings(Manager manager) {
		Button button = (Button) findViewById(R.id.SaveSettings);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText StubLabel = (EditText) findViewById(R.id.stubLabel);
				String StubString = StubLabel.getText().toString();
				if (StubString.isEmpty()) {
					StubLabel.setError("Заполните это поле");
					return;
				}
				StubLabel.setError(null);
				manager.setSharedPreferences("stub", StubString);
				Toast.makeText(SettingsWindowActivity.this, "Сохранено!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	String getCurrentVersionApp() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = packageInfo.versionName;
			return version;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}