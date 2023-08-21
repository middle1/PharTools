package ru.middle1.phartools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import ru.middle1.phartools.MainActivity;
import ru.middle1.phartools.Managers.Manager;

public class DialogWindowChooser extends DialogFragment {
	private static final int PICK_PHAR_FILE = 1;
	private static final int PICK_FOLDER_FOR_CONVERT = 3;

	public Dialog onCreateDialog(Context context, MainActivity activity) {
		String title = "Выбор формата";
		String message = "Выбери что вы хотите конвертировать";
		String button1String = "phar -> folder";
		String button2String = "folder -> phar";

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title); // заголовок
		builder.setMessage(message); // сообщение
		builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				activity.CreateActivity(PICK_PHAR_FILE);
			}
		});
		builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				activity.CreateActivity(PICK_FOLDER_FOR_CONVERT);
			}
		});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}
}