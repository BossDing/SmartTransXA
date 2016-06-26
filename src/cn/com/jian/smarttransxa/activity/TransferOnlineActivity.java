package cn.com.jian.smarttransxa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.jian.smarttransxa.util.OverlayManager;
import cn.com.jian.smarttransxa.util.TransitRouteOverlay;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * ���߻��˲�ѯ��
 * 
 * @author Jian
 * 
 */
public class TransferOnlineActivity extends Activity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
	// �����ؼ�
	private EditText etStart;
	private EditText etEnd;

	// �����ٶȵ�ͼ������
	private RoutePlanSearch mSearch;
	private BaiduMap mBaidumap;
	RouteLine<?> route;
	MapView mMapView;

	// ���·�߽ڵ����
	Button mBtnPre; // ��һ���ڵ�
	Button mBtnNext; // ��һ���ڵ�
	int nodeIndex = -1; // �ڵ�����,������ڵ�ʱʹ��
	boolean useDefaultIcon = false;
	private TextView popupText; // ����view
	OverlayManager routeOverlay;

	// ������γ�� 34.26667, 108.95000
	// �ʵ�ѧԺ��У�� 34.162719, 108.907775
	// ����ٸ 34.372371, 109.292528
	// protected LatLng stLatLng = new LatLng(34.162719, 108.907775);
	// protected LatLng enLatLng = new LatLng(34.372371, 109.292528);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.activity_trans_online);

		// ��ȡ�ؼ�
		initViews();

		// ���ü�����
		setListeners();
	}

	private void initViews() {
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		etStart = (EditText) findViewById(R.id.et_online_transstart);
		etEnd = (EditText) findViewById(R.id.et_online_transend);

		// ��ʼ����ͼ
		mMapView = (MapView) findViewById(R.id.map);

		mBaidumap = mMapView.getMap();

		// �趨��ͼ��ʼ���ĵ����꣨�����ʵ��ѧ����У����
		LatLng xayd = new LatLng(34.162719, 108.907775);
		// �趨��ͼ״̬���趨��ʼ���ĵ�����ż�����
		MapStatus mMapStatus = new MapStatus.Builder().target(xayd).zoom(12)
				.build();
		// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);

		// ���õ�ͼ״̬
		mBaidumap.setMapStatus(mMapStatusUpdate);

		// ��ʼ������ģ�飬ע���¼�����
		mSearch = RoutePlanSearch.newInstance();

	}

	private void setListeners() {
		mBaidumap.setOnMapClickListener(this);
		mSearch.setOnGetRoutePlanResultListener(this);
	}

	/**
	 * ���˷�����ťѡ��
	 * 
	 * @param v
	 */

	public void searchButtonProcess(View v) {
		// ��������ڵ��·������
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);

		mBaidumap.clear();
		// �������յ���Ϣ,���ó�����û�ã�Ĭ�ϱ���
		// PlanNode stNode = PlanNode.withCityNameAndPlaceName("����", etStart
		// .getText().toString());
		// PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", etEnd
		// .getText().toString());
		PlanNode stNode = PlanNode.withCityCodeAndPlaceName(233, etStart
				.getText().toString().trim());
		PlanNode enNode = PlanNode.withCityCodeAndPlaceName(233, etEnd
				.getText().toString().trim());

		// ͨ�������������յ���Ϣ
		// PlanNode stNode = PlanNode.withLocation(stLatLng);
		// PlanNode enNode = PlanNode.withLocation(enLatLng);

		if (v.getId() == R.id.btn_search_timefirst) {
			mSearch.transitSearch((new TransitRoutePlanOption())
					.policy(TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST)
					.from(stNode).city("����").to(enNode));
		} else if (v.getId() == R.id.btn_search_transferfirst) {
			mSearch.transitSearch((new TransitRoutePlanOption())
					.policy(TransitRoutePlanOption.TransitPolicy.EBUS_TRANSFER_FIRST)
					.from(stNode).city("����").to(enNode));
		} else if (v.getId() == R.id.btn_search_walkfirst) {
			mSearch.transitSearch((new TransitRoutePlanOption())
					.policy(TransitRoutePlanOption.TransitPolicy.EBUS_WALK_FIRST)
					.from(stNode).city("����").to(enNode));
		} else if (v.getId() == R.id.btn_search_nosubway) {
			mSearch.transitSearch((new TransitRoutePlanOption())
					.policy(TransitRoutePlanOption.TransitPolicy.EBUS_NO_SUBWAY)
					.from(stNode).city("����").to(enNode));
		}
	}

	/**
	 * �ڵ����ʾ��
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}
		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// ���ýڵ�����
		if (v.getId() == R.id.next) {
			if (nodeIndex < route.getAllStep().size() - 1) {
				nodeIndex++;
			} else {
				return;
			}
		} else if (v.getId() == R.id.pre) {
			if (nodeIndex > 0) {
				nodeIndex--;
			} else {
				return;
			}
		}
		// ��ȡ�ڽ����Ϣ
		LatLng nodeLocation = null;
		String nodeTitle = null;
		Object step = route.getAllStep().get(nodeIndex);
		nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance()
				.getLocation();
		nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
		if (nodeLocation == null || nodeTitle == null) {
			return;
		}
		// �ƶ��ڵ�������
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(TransferOnlineActivity.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(TransferOnlineActivity.this, "��Ǹ��δ�ҵ����",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// ����RouteOverly
	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

}
