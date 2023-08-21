package ru.middle1.phartools;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ru.middle1.phartools.Managers.Manager;

public class ButtonListeners {
	public final int PICK_PHAR_OUTPUT_DIR = 2;

	private Context context;
	public MainActivity mainActivity;

	public ButtonListeners(Context context, MainActivity mainActivity) {
		this.context = context;
		this.mainActivity = mainActivity;
	}

	public void ChoosePharButton(Button button) {
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogWindowChooser windowChooser = new DialogWindowChooser();
				windowChooser.onCreateDialog(context, mainActivity);
			}
		});
	}

	public void OutPutDirButton(Button button) {
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mainActivity.CreateActivity(PICK_PHAR_OUTPUT_DIR);
			}
		});
	}

	public void ShowLogButton(Button button) {
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity activity = new MainActivity();
				TextView logText = mainActivity.findViewById(R.id.LogText);
				Button CopyLogButton = mainActivity.findViewById(R.id.LogCopyButton);
				int visibility = logText.getVisibility();
				if (visibility == View.VISIBLE) {
					logText.setVisibility(View.GONE);
					CopyLogButton.setVisibility(View.GONE);
				} else {
					logText.setVisibility(View.VISIBLE);
					CopyLogButton.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public void CopyLogButton(Button button) {
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TextView logText = mainActivity.findViewById(R.id.LogText);
				String LogTextString = logText.getText().toString();
				if (LogTextString.isEmpty()) {
					Toast.makeText(context, "Копировать нечего  ¯⁠\\⁠_⁠(⁠ツ⁠)⁠_⁠/⁠¯", Toast.LENGTH_SHORT).show();
				} else {
					int sdk = android.os.Build.VERSION.SDK_INT;
					if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
						android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mainActivity.getSystemService(
								Context.CLIPBOARD_SERVICE);
						clipboard.setText(LogTextString);
					} else {
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mainActivity.getSystemService(
								Context.CLIPBOARD_SERVICE);
						android.content.ClipData clip = android.content.ClipData.newPlainText("PharConverter", LogTextString);
						clipboard.setPrimaryClip(clip);
						Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	public void Active(Button button){
		button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				EditText inputDir = mainActivity.findViewById(R.id.PharInput);
				EditText ouputDir = mainActivity.findViewById(R.id.OutputInput);
				if (inputDir.getText().toString().isEmpty()){
					inputDir.setError("Заполните это поле");
					return;
				}
				inputDir.setError(null);
				if (ouputDir.getText().toString().isEmpty()){
					ouputDir.setError("Это поле тоже надо заполнить!");
					return;
				}
				ouputDir.setError(null);
				Manager manager = new Manager(context, mainActivity);
				manager.command(inputDir.getText().toString(), ouputDir.getText().toString());
			}
		});
	}
}