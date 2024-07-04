package ru.middle1.phartools.Managers;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.transition.Visibility;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import ru.middle1.phartools.MainActivity;
import ru.middle1.phartools.R;
import ru.middle1.phartools.Tools.LogUtils;
import ru.middle1.phartools.Tools.WaitClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Manager {
	public Context context;
	public MainActivity activity;

	public Manager(Context context, MainActivity activity) {
		this.activity = activity;
		this.context = context;
	}

	public void command(String pathPhar, String outputDir) {
		Runtime runtime1 = Runtime.getRuntime();
		FilesManager fm = new FilesManager(context);
		fm.CreateTmpFolder();
		String fileName = fm.getFileName(pathPhar);
		String extension = fm.DetectExtension(pathPhar);
		StringBuilder logText = new StringBuilder();
		TextView text = activity.findViewById(R.id.LogText);
		switch (extension) {
		case "phar":
			setStatusText("В процессе...");
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					Process process = fm.UnpackPhar(pathPhar, outputDir);
					String res = ProcessReader(process, pathPhar);
					logText.append(res);
					LogUtils.WriteLogInLabel(text, logText);
					if (res.isEmpty()) {
						setStatusText("Файлы были успешно распакованы по пути:\n" + outputDir + "/"
								+ FilesManager.getFileNameWithoutExtension(fileName));
						logText.append("Phar успешно распакован!");
						LogUtils.WriteLogInLabel(text, logText);
					} else {
						setStatusText("Во время распаковки произошла ошибка :(\nВозможно, что папка с названием \""
								+ FilesManager.getFileNameWithoutExtension(fileName) + "\" по пути \"" + outputDir
								+ "\" уже существует!");
					}
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();

			break;
		case "dir":
			setStatusText("В процессе...");
			Runnable runnable2 = new Runnable() {
				@Override
				public void run() {
					Process mainProcess = fm.CreatePhar(pathPhar, outputDir);
					String out = ProcessReader(mainProcess, pathPhar);
					logText.append(out);
					LogUtils.WriteLogInLabel(text, logText);
					if (out.isEmpty()) {
						setStatusText("Phar архив был успешно создан по пути " + outputDir);
						logText.append("Phar успешно создан!");
						LogUtils.WriteLogInLabel(text, logText);
					} else {
						setStatusText("Ошибка создания архива, подробнее в логе");
					}
				}
			};
			Thread thread2 = new Thread(runnable2);
			thread2.start();
			break;
		default:
			Toast.makeText(context, "Неизвестное расширение", Toast.LENGTH_SHORT).show();
			StringBuilder ExtensionError = new StringBuilder();
			logText.append(
					"Неизвестное расширение файла. Приложеник поддерживает только папки и phar архивы. Ваше расширение: "
							+ extension);
			break;
		}
	}

	public void setStatusText(String text) {
		TextView statusText = activity.findViewById(R.id.statusText);
		statusText.post(new Runnable() {
			public void run() {
				statusText.setText(text);
			}
		});
	}

	public void ShowAndHideLog() {
		Button CopyLogButton = activity.findViewById(R.id.LogCopyButton);
		TextView LogText = activity.findViewById(R.id.LogText);
		boolean currentStatus = getCurrentStatusLog();
		if (currentStatus == true) {
			CopyLogButton.setVisibility(View.GONE);
			LogText.setVisibility(View.GONE);
			return;
		} else {
			CopyLogButton.setVisibility(View.VISIBLE);
			LogText.setVisibility(View.VISIBLE);
		}
	}

	public void ChangeTextCopyButton(Button CopyButton) {
		boolean currentStatus = getCurrentStatusLog();
		if (currentStatus == true) {
			CopyButton.setText("Открыть лог");
		} else {
			CopyButton.setText("Закрыть лог");
		}
	}

	public boolean getCurrentStatusLog() {
		TextView logText = activity.findViewById(R.id.LogText);
		return logText.getVisibility() == View.VISIBLE;
	}

	public String ProcessReader(Process process, String pathPhar) {
		InputStream stream = process.getInputStream();
		Charset charset = Charset.defaultCharset();
		InputStreamReader InputReader = new InputStreamReader(stream, charset);
		BufferedReader reader = new BufferedReader(InputReader);
		String line;
		StringBuilder output = new StringBuilder();
		try {
			while ((line = reader.readLine()) != null) {
				output.append(line);
			}
			reader.close();
		} catch (IOException e) {
		}
		String res = output.toString();
		return res;
	}

	public String getSharedPreferences(String StringName) {
		SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		String res = preferences.getString(StringName, "");
		return res;
	}

	public void setSharedPreferences(String paramName, String data) {
		SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(paramName,  data);
		editor.apply();
	}
	public void unsetSharedPreferences(String StringName){
		SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(StringName);
		editor.apply();
	}
}