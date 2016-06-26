package cn.com.jian.smarttransxa.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * ���ݿⵥԪ������
 * 
 * @author Jian
 * 
 */
public class DatabaseTest extends AndroidTestCase {
	private MyDatabaseHelper mdbp;
	private SQLiteDatabase db;

	public void test() {
		// getContext():��ȡһ�������������
		mdbp = new MyDatabaseHelper(getContext(), "xian.db", null, 1);
		// ������ݿⲻ���ڣ��ȴ������ݿ⣬�ٻ�ȡ�ɶ���д�����ݿ����������ݿ���ڣ���ֱ�Ӵ�
		// SQLiteDatabase db = mdbp.getWritableDatabase();
		// ����洢�ռ����ˣ���ô����ֻ�����ݿ����
		// SQLiteDatabase db = mdbp.getReadableDatabase();

	}

	// ���Կ�ܳ�ʼ�����֮���ڲ��Է���ִ��֮ǰ���˷�������
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mdbp = new MyDatabaseHelper(getContext(), "xian.db", null, 1);
		db = mdbp.getWritableDatabase();
	}

	// ���Է���ִ�����֮�󣬴˷�������
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		db.close();
	}

	public void insertApi() {
		// ��Ҫ���������ȫ����װ��ContentValues����
		ContentValues values = new ContentValues();
		values.put("line", "����ר��");
		values.put("time", " �Ͷ���· 6��30-20��00 �Ҹ���· 6��30-20��00");
		values.put(
				"station",
				"�Ͷ���· - ������ - �߼Ҵ� - ̫��·���� - ��б��· - �����幫˾ - ��б��· - ɳ���� - �����̳� - ��ޱ��԰ - ���Ӷ�· - ʡ����ҽԺ - ����· - ����ׯ - ʡ����ҽԺ - ҽѧԺ - γ���� - Сկ - �������� - ʡ������ - �ݳ��� - ������ - ����· - ��ҤС�� - ��Ҵ� - �վ�ҽԺ - ̫��· - �����ֶ��� - ���ϳǽ� - �Ϲ��� - ���칫԰ - ������Գ� - �����̳� - ����· - ����· - ����·���� - ��԰��·���� - ��ɭկ - �Ҹ���·(��ۡ����С��) - �Ҹ���·");
		values.put(
				"opposite",
				"�Ҹ���· - �Ҹ���·(��ۡ����С��) - ��ɭկ - ��԰��·���� - ����·���� - ����· - ����· - �����̳� - ������Գ� - ���칫԰ - �Ϲ��� - ���ϳǽ� - �����ֶ��� - ̫��· - �վ�ҽԺ - ��ҤС�� - ����· - ������ - �ݳ��� - ʡ������ - �������� - Сկ - γ���� - ҽѧԺ - ʡ����ҽԺ - ����ׯ - ����· - ʡ����ҽԺ - ���Ӷ�· - ��ޱ��԰ - �����̳� - ɳ���� - ��б��· - �����幫˾ - ��б��· - ̫��·���� - �߼Ҵ� - ������ - �Ͷ���·");
		// �����һ������
		db.insert("bus_line", null, values);
		Log.i("Database", "������ӳɹ�");
	}

	public void deleteApi() {
		// ɾ���ո���ӵĽ���ר������
		int i = db.delete("bus_line", "line = ? and _id = ?", new String[] {
				"����ר��", "1" });
		Log.i("Database", "ɾ������ i = " + i);
	}

	public void updateApi() {
		// �ѽ���ר�ߵ�time �ĳ� �Ͷ���· 6��00-23��00 �Ҹ���· 6��00-23��00
		ContentValues values = new ContentValues();
		values.put("time", " �Ͷ���· 6��00-23��00 �Ҹ���· 6��00-23��00");
		int i = db.update("bus_line", values, "line = ?",
				new String[] { "����ר��" });
		Log.i("Database", "�����޸� i = " + i);
	}

	public void selectApi() {
		// ��ѯ�ո���ӵ� ����ר�� ����
		Cursor cursor = db.query("bus_line", null, null, null, null, null,
				null, null);
		while (cursor.moveToNext()) {
			String line = cursor.getString(cursor.getColumnIndex("line"));
			String time = cursor.getString(cursor.getColumnIndex("time"));
			String station = cursor.getString(cursor.getColumnIndex("station"));
			String opposite = cursor.getString(cursor
					.getColumnIndex("opposite"));
			Log.i("Database", "���ݲ�ѯ:  " + line + ";/n" + time + ";/n" + station
					+ ";/n" + opposite);
		}
	}
}
