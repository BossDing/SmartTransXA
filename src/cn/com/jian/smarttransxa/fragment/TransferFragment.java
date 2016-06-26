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
import cn.com.jian.smarttransxa.domain.Bus;

/**
 * transfer ���˲�ѯ
 * 
 * @author Jian
 * 
 */
public class TransferFragment extends Fragment {

	// ����station4���ؼ������ݿ����
	private EditText etTransferStart;
	private EditText etTransferEnd;
	private Button btnTransferSearch;
	private ListView lvTransfer;
	private BusDatabase dbfile;
	private SQLiteDatabase database;

	// ���� Adapter �ͼ���
	public ArrayAdapter<String> adapter;
	private List<String> result = new ArrayList<String>();

	public TransferFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Log.d("smartTransXA", "TransferFragment");
		View v = inflater.inflate(R.layout.fragment_trans_transfer, container,
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
		etTransferStart = (EditText) v.findViewById(R.id.et_transfer_start);
		etTransferEnd = (EditText) v.findViewById(R.id.et_transfer_end);
		btnTransferSearch = (Button) v.findViewById(R.id.btn_transfer_search);
		lvTransfer = (ListView) v.findViewById(R.id.lv_transfer);
	}

	private void setListeners() {
		OnClickListener listener = new InnerOnClickListener();
		btnTransferSearch.setOnClickListener(listener);

	}

	private class InnerOnClickListener implements OnClickListener {

		// ����һ���ߵ�����վ��
		private String[] routeAllStationStart;
		private String[] routeAllStationEnd;

		private String resultRouteStation;
		private String stations;

		public void onClick(View v) {
			// ����һ����Ǳ���
			boolean state = true;

			// // ��ȡ�����������ַ���
			String busStationStart = etTransferStart.getText().toString()
					.trim();
			String busStationEnd = etTransferEnd.getText().toString().trim();

			// ��ȡվ������й�������
			Bus busRouteStart = getBusMessage(busStationStart);
			Bus busRouteEnd = getBusMessage(busStationEnd);

			// �ж�bus����Ϊ��,�����ݿ����Ƿ��ҵ���վ��
			if ("".equals(busStationStart) || "".equals(busStationEnd)) {
				lvTransfer.setVisibility(View.INVISIBLE);
				Toast.makeText(getActivity(), "��ʼվ  \\ �յ�վδ����!",
						Toast.LENGTH_SHORT).show();
			} else {
				lvTransfer.setVisibility(View.VISIBLE);
				// �ж�����������Ƿ�Ϊ��
				// -- �� �� Toast��ʾ "��ʼվ \ �յ�վδ����!"
				// -- �� �� ����ִ����һ��
				if (busRouteStart == null || busRouteEnd == null) {
					lvTransfer.setVisibility(View.INVISIBLE);
					Toast.makeText(getActivity(), "��Ǹ, �������վ�㲻����!",
							Toast.LENGTH_SHORT).show();
				} else {

					String[] lineStart = busRouteStart.getLine().split(",");
					String[] lineEnd = busRouteEnd.getLine().split(",");

					// ����2����·����,�ж���ͬһ����·�ľ���
					result.clear();
					for (int i = 0; i < lineStart.length; i++) {
						for (int j = 0; j < lineEnd.length; j++) {
							if (lineStart[i].equals(lineEnd[j])) {
								// Log.i("--- start ---", lineStart[i]);
								result.add(" <" + lineStart[i] + "·> ֱ��");
								state = false;
							}
						}
					}

					// ����ͬһ����·��
					if (state) {
						lvTransfer.setVisibility(View.VISIBLE);
						result.clear();

						// ������һ���վ��ֳ�һ����·������վ��
						String[] stationStartDiff = busRouteStart.getStation()
								.split(",");
						String[] stationEndDiff = busRouteEnd.getStation()
								.split(",");

						// ������һ�����·�ֳ�һ��������·����
						String[] routeStartDiff = busRouteStart.getLine()
								.split(",");
						String[] routeEndDiff = busRouteEnd.getLine()
								.split(",");

						// ��������վ��,�ٲ�ֳ�һ������վ������
						for (int i = 0; i < stationStartDiff.length; i++) {
							// ���ÿ��վ��
							routeAllStationStart = stationStartDiff[i]
									.split(" - ");
							// Log.d("routeAllStationStart",
							// Arrays.toString(routeAllStationStart));
							for (int j = 0; j < stationEndDiff.length; j++) {
								routeAllStationEnd = stationEndDiff[j]
										.split(" - ");
								// Log.d("--- routeAllStationEnd ---",
								// Arrays.toString(routeAllStationEnd));

								stations = "";
								resultRouteStation = "";
								// ������ʼվ���յ�վ������վ��,�ҵ���ͬ��վ��
								for (int x = 0; x < routeAllStationStart.length; x++) {
									for (int y = 0; y < routeAllStationEnd.length; y++) {
										// ��վ����ͬʱ,��ƴ������·��վ�����Ϣ
										if (routeAllStationStart[x]
												.equals(routeAllStationEnd[y])) {
											stations += " "
													+ routeAllStationStart[x]
													+ " ";
											// Log.v("--- starts ---",
											// routeAllStationStart[x]);
											resultRouteStation = "���� <"
													+ routeStartDiff[i]
													+ "·> ��" + stations
													+ "վ���� <" + routeEndDiff[j]
													+ "> ·�ɵ���";
										}
									}
								}
								// վ�㱻��ѯ�����ڲ���ӵ�������
								if (!"".equals(stations)) {
									result.add(resultRouteStation);
								}
							}
						}
					}
				}
				adapter = new ArrayAdapter<String>(getActivity(),
						R.layout.transfer_item, result);
				lvTransfer.setAdapter(adapter);

			}
		}

	}

	/**
	 * 
	 * �����ݿ��л�ȡ������Ϣ����װ��bus������
	 * 
	 * @param busStation
	 *            ����վ��
	 * @return ��װ�õ�bus ����
	 */
	private Bus getBusMessage(String busStation) {
		Bus bus = new Bus();
		StringBuffer lines = new StringBuffer();
		StringBuffer station = new StringBuffer();

		Cursor cursor = database.rawQuery(
				"select * from bus_line where station like '%" + busStation
						+ "%'", null);

		if (cursor.moveToNext()) {
			while (cursor.moveToNext()) {
				bus.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
				lines.append(cursor.getString(1));
				lines.append(",");
				station.append(cursor.getString(3));
				station.append(",");
			}
			bus.setLine(lines.toString());
			bus.setStation(station.toString());
		} else {
			bus = null;
		}
		cursor.close();
		return bus;
	}

}
