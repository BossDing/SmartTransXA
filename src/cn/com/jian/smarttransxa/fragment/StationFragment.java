package cn.com.jian.smarttransxa.fragment;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.jian.smarttransxa.activity.R;
import cn.com.jian.smarttransxa.database.BusDatabase;

/**
 * station վ���ѯ
 * 
 * @author Jian
 * 
 */
public class StationFragment extends Fragment {

	// ����station3���ؼ������ݿ����
	private EditText etStation;
	private Button btnStationSearch;
	private ListView lvStation;
	private BusDatabase dbfile;
	private SQLiteDatabase database;

	// ���� Adapter �ͼ���
	private ArrayAdapter<String> adapter;
	private List<String> route;

	public StationFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Log.d("smartTransXA", "StationFragment");
		View v = inflater.inflate(R.layout.fragment_trans_station, container,
				false);
		// ��ȡ���ݿ�
		initDatabase();

		// ��ȡ�ؼ�
		initViews(v);

		// ���ü�����
		setListeners();
		return v;
	}

	private void initDatabase() {
		dbfile = new BusDatabase(getActivity());
		dbfile.openDatabase();
		dbfile.closeDatabase();
		database = SQLiteDatabase.openOrCreateDatabase(BusDatabase.getDbPath()
				+ "/" + BusDatabase.getDbName(), null);
	}

	private void initViews(View v) {
		etStation = (EditText) v.findViewById(R.id.et_station);
		btnStationSearch = (Button) v.findViewById(R.id.btn_station_search);
		lvStation = (ListView) v.findViewById(R.id.lv_station);
	}

	private void setListeners() {
		OnClickListener listener = new InnerOnClickListener();
		btnStationSearch.setOnClickListener(listener);
	}

	private class InnerOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// // ��ȡ�������ַ���
			String busStation = etStation.getText().toString().trim();

			// ��ʾstation ��ListView,�����ͷſռ�
			lvStation.setVisibility(View.VISIBLE);

			// �ж������վ���Ƿ���������
			// -- �ǡ����ͽ���ִ��
			// -- �񡡣�Toast��ʾ "���������빫��վ��"
			if (busStation != null && !"".equals(busStation)
					&& busStation.length() > 1 && !" - ".equals(busStation)) {
				// sql���ģ����ѯ��station�д���busStation �Ĺ�������
				Cursor cursor = database.rawQuery(
						"select * from bus_line where station like '%"
								+ busStation + "%'", null);
				// �α궨λ��һ��
				cursor.moveToFirst();

				// �ж�sql�Ƿ�û�ҵ�,���ж��α��Ƿ�ָ�����һλ
				// -- �� : �͹ر��α�,Toast��ʾ "������˼,������Ĺ���վ����δ��ͨ������·!"
				// -- �� : �ͰѲ鵽���������� ���ݿ��б���line ����Ӧ�Ĺ�����·��ӵ�������
				if (cursor.isAfterLast()) {
					cursor.close();
					// ��ʾstation ��ListView,�����ͷſռ�
					lvStation.setVisibility(View.INVISIBLE);
					Toast.makeText(getActivity(),
							"������˼,������� " + busStation + " ��δ��ͨ������·!",
							Toast.LENGTH_SHORT).show();
				} else {
					route = new ArrayList<String>();

					route.add("   ----- ���� " + busStation
							+ " �Ĺ�����·�� �� -----   ");
					while (!cursor.isAfterLast()) {
						route.add(" <"
								+ cursor.getString(cursor
										.getColumnIndex("line"))
								+ "·> \n����ʱ�� :"
								+ cursor.getString(cursor
										.getColumnIndex("time")));
						// �α��Ƶ���һ��
						cursor.moveToNext();
					}
					adapter = new ArrayAdapter<String>(getActivity(),
							R.layout.station_item, route);
					// ��Station ��ListView ����������
					lvStation.setAdapter(adapter);
				}
				// �ر��α�,�ͷ���Դ
				cursor.close();
			} else {
				// ��ʾroute ��ListView,�����ͷſռ�
				lvStation.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity(), "���������빫��վ��", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
