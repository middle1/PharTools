package ru.middle1.phartools.Tools;

import android.content.Context;
import android.widget.TextView;
import ru.middle1.phartools.MainActivity;

public class LogUtils{

	private Context context;
	private MainActivity activity;

	public LogUtils(Context context, MainActivity activity) {
		this.context = context;
		this.activity = activity;
	}

	public static void WriteLogInLabel(TextView label, StringBuilder content) {
		if (content.length() <= 0) {
			return;
		}
		label.post(new Runnable() {
			public void run() {
				label.append("---------------------------\n");
				label.append(content.toString());
				label.append("\n---------------------------\n");
			}
		});

	}
}