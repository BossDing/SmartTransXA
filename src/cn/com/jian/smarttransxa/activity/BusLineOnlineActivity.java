package cn.com.jian.smarttransxa.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.jian.smarttransxa.util.BusLineOverlay;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * ���߹�����Ϣ��ѯ��
 * 
 * @author Jian
 *
 */
public class BusLineOnlineActivity extends FragmentActivity implements
		OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener,
		BaiduMap.OnMapClickListener {

	// �����ؼ�
	private Button mBtnPre;
	private Button mBtnNext;
	private EditText etRouteOnline;

	private int nodeIndex = -2;// �ڵ�����,������ڵ�ʱʹ��
	private BusLineResult route;// ����ݳ�/����·�����ݵı�����������ڵ�ʱʹ��
	private List<String> busLineIDList;
	private int busLineIndex = 0;
	// �������
	private PoiSearch mSearch; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private BusLineSearch mBusLineSearch;
	private BaiduMap mBaiduMap;
	BusLineOverlay overlay;// ����·�߻��ƶ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.activity_busline_online);

		// ��ȡ�ؼ�
		initViews();

		// ���ü�����
		setListeners();
	}

	private void initViews() {
		mBtnPre = (Button) findViewById(R.id.btn_busline_pre);
		mBtnNext = (Button) findViewById(R.id.btn_busline_next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		etRouteOnline = (EditText) findViewById(R.id.et_route_online);
		mBaiduMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.bmapView)).getBaiduMap();
		// �趨��ͼ��ʼ���ĵ����꣨�����ʵ��ѧ����У����
		LatLng xayd = new LatLng(34.162719, 108.907775);
		// �趨��ͼ״̬���趨��ʼ���ĵ�����ż�����
		MapStatus mMapStatus = new MapStatus.Builder().target(xayd).zoom(12)
				.build();
		// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);

		// ���õ�ͼ״̬
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		
	}

	private void setListeners() {
		mBaiduMap.setOnMapClickListener(this);
		mSearch = PoiSearch.newInstance();
		mSearch.setOnGetPoiSearchResultListener(this);
		mBusLineSearch = BusLineSearch.newInstance();
		mBusLineSearch.setOnGetBusLineSearchResultListener(this);
		busLineIDList = new ArrayList<String>();
		overlay = new BusLineOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(overlay);

	}

	/**
	 * �������
	 * 
	 * @param v
	 */
	public void searchRouteProcess(View v) {
		busLineIDList.clear();
		busLineIndex = 0;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// ����poi�������ӵõ�����poi���ҵ�������·���͵�poi����ʹ�ø�poi��uid���й�����������
		mSearch.searchInCity((new PoiCitySearchOption()).city("����").keyword(
				etRouteOnline.getText().toString()));
	}

	public void SearchOppositeBusline(View v) {
		if (busLineIndex >= busLineIDList.size()) {
			busLineIndex = 0;
		}
		if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
				&& busLineIDList.size() > 0) {
			mBusLineSearch.searchBusLine((new BusLineSearchOption().city("����")
					.uid(busLineIDList.get(busLineIndex))));

			busLineIndex++;
		}
	}

	/**
	 * �ڵ����ʾ��
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {

		if (nodeIndex < -1 || route == null
				|| nodeIndex >= route.getStations().size())
			return;
		TextView popupText = new TextView(this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xff000000);
		// ��һ���ڵ�
		if (mBtnPre.equals(v) && nodeIndex > 0) {
			// ������
			nodeIndex--;
		}
		// ��һ���ڵ�
		if (mBtnNext.equals(v) && nodeIndex < (route.getStations().size() - 1)) {
			// ������
			nodeIndex++;
		}
		if (nodeIndex >= 0) {
			// �ƶ���ָ������������
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(route
					.getStations().get(nodeIndex).getLocation()));
			// ��������
			popupText.setText(route.getStations().get(nodeIndex).getTitle());
			mBaiduMap.showInfoWindow(new InfoWindow(popupText, route
					.getStations().get(nodeIndex).getLocation(), 0));
		}
	}

	@Override
	public void onGetBusLineResult(BusLineResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BusLineOnlineActivity.this, "��Ǹ��δ�ҵ����",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		route = result;
		nodeIndex = -1;
		overlay.removeFromMap();
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
		mBtnPre.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		Toast.makeText(BusLineOnlineActivity.this, result.getBusLineName(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BusLineOnlineActivity.this, "��Ǹ��δ�ҵ����",
					Toast.LENGTH_LONG).show();
			return;
		}
		// ��������poi���ҵ�����Ϊ������·��poi
		busLineIDList.clear();
		for (PoiInfo poi : result.getAllPoi()) {
			if (poi.type == PoiInfo.POITYPE.BUS_LINE
					|| poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
				busLineIDList.add(poi.uid);
			}
		}
		SearchOppositeBusline(null);
		route = null;

	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {

	}

	@Override
	public void onMapClick(LatLng result) {
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi result) {
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mBusLineSearch.destroy();
		super.onDestroy();
	}
}
