package ru.middle1.phartools.Managers;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;
import ru.middle1.phartools.MainActivity;
import ru.middle1.phartools.Tools.LogUtils;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.app.Activity;
import android.os.Bundle;
import org.apache.commons.io.FilenameUtils;

public class FilesManager {
	Context context;

	public FilesManager(Context context) {
		this.context = context;
	}

	public boolean copyAssetFile(String srcName, String dstName) {
		try {
			InputStream in = context.getAssets().open(srcName);
			copyFileToSdCard(in, dstName);
			in.close();
			return true;
		} catch (IOException e) {
			Toast.makeText(context, "error with file " + srcName, Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public void copyFileToSdCard(InputStream inputStream, String fileName) {
		File sdCard = context.getExternalFilesDir(null);
		File directory = new File(sdCard.getAbsolutePath()); // Путь к папке /sdcard
		directory.mkdirs(); // Убедитесь, что папка существует

		File outFile = new File(directory, fileName);
		if (outFile.exists()) {
			System.out.println(String.format("File %s exists!", fileName));
			return;
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(outFile);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}

			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void CreateTmpFolder() {
		String TmpPath = context.getExternalFilesDir(null).getAbsolutePath() + "/tmp";
		File TmpPathFile = new File(TmpPath);
		if (!TmpPathFile.exists()) {
			boolean status = TmpPathFile.mkdir();
			if (!status) {
				Toast.makeText(context, "Error mkdir temp folder", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		}
	}

	public String getFileName(String path) {
		File file = new File(path);
		String FileName = file.getName().toString();
		return FileName;
	}

	public static String getFileNameWithoutExtension(String filename) {
		String fileNameWithoutExtension = FilenameUtils.removeExtension(filename);
		return fileNameWithoutExtension;
	}

	public String DetectExtension(String file) {
		if (file.isEmpty()) {
			return "";
		}
		File isDirectory = new File(file);
		if (isDirectory.isDirectory()) {
			return "dir";
		}
		if (file.lastIndexOf(".") != -1 && file.lastIndexOf(".") != 0) {
			return file.substring(file.lastIndexOf(".") + 1);
		}
		return "";
	}

	public Process UnpackPhar(String pathPhar, String outputDir) {
		File pathToData = context.getExternalFilesDir(null);
		String LibPath = context.getApplicationContext().getApplicationInfo().nativeLibraryDir + "/libphp.so";
		ProcessBuilder builder1 = new ProcessBuilder(new String[] { LibPath, "-c" + pathToData + "/php.ini",
				pathToData + "/unPhar.php", pathPhar, outputDir });
		Map map = builder1.environment();
		map.put("TMPDIR", pathToData + "/tmp");
		try {
			Process process = builder1.start();
			return process;
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
		}
		return null;
	}

	public Process CreatePhar(String pathFolder, String outputDir) {
		File pathToDataApp = context.getExternalFilesDir(null);
		String LibPath = context.getApplicationContext().getApplicationInfo().nativeLibraryDir + "/libphp.so";
		ProcessBuilder builder1 = new ProcessBuilder(new String[] { LibPath, "-c" + pathToDataApp + "/php.ini",
				pathToDataApp + "/toPhar.php", pathFolder, outputDir });
		Map map = builder1.environment();
		map.put("TMPDIR", pathToDataApp + "/tmp");
		try {
			Process process = builder1.start();
			return process;
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
}