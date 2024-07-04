package ru.middle1.phartools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.NativeActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.telecom.InCallService;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.microedition.khronos.egl.EGL10;
import ru.middle1.phartools.Managers.*;
import ru.middle1.phartools.SettingsWindowActivity;
import ru.middle1.phartools.Tools.LogUtils;
import ru.middle1.phartools.Tools.ReadUriPaths;

public class MainActivity extends Activity {
	private static MainActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FilesManager filesManager = new FilesManager(getContext());
		filesManager.copyAssetFile("unPhar.php", "unPhar.php");
		filesManager.copyAssetFile("php.ini", "php.ini");
		filesManager.copyAssetFile("toPhar.php", "toPhar.php");
		instance = this;
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

	public static MainActivity getInstance() {
		return instance;
	}

	private void Starter() {
		Manager manager = new Manager(this, this);
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			manager.setSharedPreferences("version", version);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		String stub = manager.getSharedPreferences("stub");
		if (stub.isEmpty()) {
			manager.setSharedPreferences("stub", "__HALT_COMPILER();");
		}
		Context con = getContext();
		FilesManager filesManager = new FilesManager(con);
		Button LoadFileButton = (Button) findViewById(R.id.load);
		Button activeButton = (Button) findViewById(R.id.buttonToZip);
		Button ShowLogButton = (Button) findViewById(R.id.ShowLogButton);
		Button CopyLogButton = (Button) findViewById(R.id.LogCopyButton);
		Button ChooseFolderButton = (Button) findViewById(R.id.OutputDirButton);
		ButtonListeners listener = new ButtonListeners(getContext(), this);
		listener.ChoosePharButton(LoadFileButton);
		listener.Active(activeButton);
		listener.ShowLogButton(ShowLogButton);
		listener.CopyLogButton(CopyLogButton);
		listener.OutPutDirButton(ChooseFolderButton);
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Выход");
		builder.setMessage("Вы уверены, что хотите выйти из приложения?");
		builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
}