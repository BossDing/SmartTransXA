package cn.com.jian.smarttransxa.fragment;

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
 * route ��·��ѯ
 * 
 * @author Jian
 * 
 */
public class RouteFragment extends Fragment {

	// ����route 4���ؼ������ݿ����
	private EditText etRoute;
	private Button btnRouteSearch;
	private Button btnRouteSearchOppo;
	private ListView lvRoute;
	private BusDatabase dbfile;
	private SQLiteDatabase database;

	// ���� Adapter ������
	private ArrayAdapter<String> adapter;
	private String[] stations;

	public RouteFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_trans_route, container,
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
		etRoute = (EditText) v.findViewById(R.id.et_route);
		btnRouteSearch = (Button) v.findViewById(R.id.btn_route_search);
		btnRouteSearchOppo = (Button) v
				.findViewById(R.id.btn_route_search_opposite);
		lvRoute = (ListView) v.findViewById(R.id.lv_route);
	}

	private void setListeners() {
		OnClickListener listener = new InnerOnClickListener();
		btnRouteSearch.setOnClickListener(listener);
		btnRouteSearchOppo.setOnClickListener(listener);
	}

	private class InnerOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_route_search:
				showRoute();
				break;
			case R.id.btn_route_search_opposite:
				showOppoRoute();
				break;
			}
		}
	}

	public void showRoute() {

		// ��ʾroute ��ListView
		lvRoute.setVisibility(View.VISIBLE);
		// // ��ȡ�������ַ���
		String busRoute = etRoute.getText().toString().trim();

		// �ж��������·�Ƿ���������
		// -- �ǡ����ͽ���ִ��
		// -- �񡡣�Toast��ʾ "���������빫����·"
		if (busRoute != null && !"".equals(busRoute)) {
			// sql����ѯ����busRoute�Ĺ�������
			Cursor cursor = database.rawQuery(
					"select * from bus_line where line  = '" + busRoute + "'",
					null);
			// �α궨λ��һ��
			cursor.moveToFirst();

			// �ж�sql�Ƿ�û�ҵ�,���ж��α��Ƿ�ָ�����һλ
			// -- �� : �͹ر��α�,Toast��ʾ "������Ĺ�����·������!"
			// -- �� : �ͰѲ鵽���������� ���ݿ��б���station ����Ӧ�Ĺ�����·��split()�����ָ������
			if (cursor.isAfterLast()) {
				cursor.close();
				// ��ʾroute ��ListView, �����ͷſռ�
				lvRoute.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity(), "������Ĺ�����·������!",
						Toast.LENGTH_SHORT).show();
			} else {
				stations = cursor.getString(cursor.getColumnIndex("station"))
						.split(" - ");
				adapter = new ArrayAdapter<String>(getActivity(),
						R.layout.route_item, stations);
				// ��route ��ListView ����������
				lvRoute.setAdapter(adapter);
			}
			// �ر��α�,�ͷ���Դ
			cursor.close();

		} else {
			// ��ʾroute ��ListView,�����ͷſռ�
			lvRoute.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(), "���������빫����·", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void showOppoRoute() {
		// ��ʾroute ��ListView
		lvRoute.setVisibility(View.VISIBLE);
		// // ��ȡ�������ַ���
		String busRouteOppo = etRoute.getText().toString().trim();

		// �ж��������·�Ƿ���������
		// -- �ǡ����ͽ���ִ��
		// -- �񡡣�Toast��ʾ "���������빫����·"
		if (busRouteOppo != null && !"".equals(busRouteOppo)) {
			// sql����ѯ����busRoute�Ĺ�������
			Cursor cursor = database.rawQuery(
					"select * from bus_line where line  = '" + busRouteOppo
							+ "'", null);
			// �α궨λ��һ��
			cursor.moveToFirst();

			// �ж�sql�Ƿ�û�ҵ�,���ж��α��Ƿ�ָ�����һλ
			// -- �� : �͹ر��α�,Toast��ʾ "������Ĺ�����·������!"
			// -- �� : �ͰѲ鵽���������� ���ݿ��б���opposite ����Ӧ�Ĺ�����·��split()�����ָ������
			if (cursor.isAfterLast()) {
				cursor.close();
				// ��ʾroute ��ListView, �����ͷſռ�
				lvRoute.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity(), "������Ĺ�����·������!",
						Toast.LENGTH_SHORT).show();
			} else {
				stations = cursor.getString(cursor.getColumnIndex("opposite"))
						.split(" - ");
				adapter = new ArrayAdapter<String>(getActivity(),
						R.layout.route_item, stations);

				// ��route ��ListView ����������
				lvRoute.setAdapter(adapter);
			}
			// �ر��α�,�ͷ���Դ
			cursor.close();

		} else {
			// ��ʾroute ��ListView,�����ͷſռ�
			lvRoute.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(), "���������빫����·", Toast.LENGTH_SHORT)
					.show();
		}
	}

}