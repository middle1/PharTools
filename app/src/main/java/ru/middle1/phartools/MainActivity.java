package ru.middle1.phartools;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telecom.InCallService;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.documentfile.provider.DocumentFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Runtime;
import java.lang.StringBuilder;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import ru.middle1.phartools.Managers.*;
import ru.middle1.phartools.SettingsWindowActivity;
import ru.middle1.phartools.Tools.ReadUriPaths;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FilesManager filesManager = new FilesManager(getContext());
		//	checkRoot();
		filesManager.copyAssetFile("unPhar.php", "unPhar.php");
		filesManager.copyAssetFile("php.ini", "php.ini");
		filesManager.copyAssetFile("toPhar.php", "toPhar.php");
		Starter();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			if (data != null && data.getData() != null) {
				Uri uri = data.getData();
				String path = ReadUriPaths.getPathFromURI(getContext(), uri);
				EditText input = (EditText) findViewById(R.id.PharInput);
				String CurrentText = input.getText().toString();
				input.setText(path);
				return;
			}
		}
		if (requestCode == 2 && resultCode == RESULT_OK) {
			if (data != null && data.getData() != null) {
				Uri uri = data.getData();
				DocumentFile documentFile = DocumentFile.fromTreeUri(getContext(), uri);
				String pathToFolder = ReadUriPaths.getPathFromURI(getContext(), documentFile.getUri());
				TextView output = findViewById(R.id.OutputInput);
				output.setText(pathToFolder);
				return;
			}
		}
		if (requestCode == 3 && resultCode == RESULT_OK) {
			if (data != null && data.getData() != null) {
				Uri uri = data.getData();
				DocumentFile documentFile = DocumentFile.fromTreeUri(getContext(), uri);
				String pathToFolder = ReadUriPaths.getPathFromURI(getContext(), documentFile.getUri());
				TextView input = findViewById(R.id.PharInput);
				input.setText(pathToFolder);
				return;
			}
		}
	}

	public Context getContext() {
		Context context = MainActivity.this;
		return context;
	}

	public MainActivity getActivity() {
		return MainActivity.this;
	}

	private void Starter() {
		Context con = getContext();
		FilesManager filesManager = new FilesManager(con);
		Button LoadFileButton = (Button)findViewById(R.id.load);
		Button activeButton = (Button)findViewById(R.id.buttonToZip);
		Button ShowLogButton = (Button)findViewById(R.id.ShowLogButton);
		Button CopyLogButton = (Button)findViewById(R.id.LogCopyButton);
		Button ChooseFolderButton = (Button)findViewById(R.id.OutputDirButton);
		ButtonListeners listener = new ButtonListeners(getContext(), this);
		listener.ChoosePharButton(LoadFileButton);
		listener.Active(activeButton);
		listener.ShowLogButton(ShowLogButton);
		listener.CopyLogButton(CopyLogButton);
		listener.OutPutDirButton(ChooseFolderButton);
	}
	
	void checkRoot() {
		try {
			Runtime.getRuntime().exec("su");
			} catch (IOException e) {
			Toast.makeText(getContext(), "Руут не был найден", Toast.LENGTH_LONG)
			.show();
		}
		Toast.makeText(getContext(), "Руут был найден", Toast.LENGTH_LONG).show();
	}
	
	public void CreateActivity(int code) {
		Intent intent = new Intent();
		switch (code) {
			case 1:
			intent.setType("*/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select file"), code);
			break;
			case 2:
			intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
			startActivityForResult(intent, 2);
			break;
			case 3:
			intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
			startActivityForResult(intent, 3);
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Intent intent = new Intent(MainActivity.this, SettingsWindowActivity.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}