package cn.com.jian.smarttransxa.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import cn.com.jian.smarttransxa.activity.R;

/**
 * ���ݿ⼯�ɴ��
 * 
 * @author Jian
 * 
 */

public class BusDatabase {

	// ���ݿ���
	private static final String DB_NAME = "xian.db";
	// ����
	private static final String PACKAGE_NAME = "cn.com.jian.smarttransxa.activity";
	// ���ֻ��������ݿ��λ��
	private static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;

	private SQLiteDatabase database;
	private Context context;

	public BusDatabase(Context context) {
		this.context = context;
	}

	public static String getDbName() {
		return DB_NAME;
	}

	public static String getDbPath() {
		return DB_PATH;
	}

	public void openDatabase() {
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			// �ж����ݿ�.db�ļ��Ƿ���ڣ���������ִ�е���,��IO�������ļ�
			if (!(new File(dbfile).exists())) {
				InputStream is = BusDatabase.this.context.getResources()
						.openRawResource(R.raw.xian);
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase() {
		this.database.close();
	}

}
