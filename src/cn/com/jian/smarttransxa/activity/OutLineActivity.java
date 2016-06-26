package cn.com.jian.smarttransxa.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import cn.com.jian.smarttransxa.adapter.OutlineFragmentAdapter;

/**
 * ����ģ��������
 * 
 * @author Jian
 *
 */
public class OutLineActivity extends FragmentActivity {

	public static final int TAB_STATION = 0;
	public static final int TAB_ROUTE = 1;
	public static final int TAB_TRANSFER = 2;
	public static final int TAB_MORE = 3;

	// �����ؼ�
	private ViewPager viewPager;
	private RadioButton rbTabMenuStation;
	private RadioButton rbTabMenuRoute;
	private RadioButton rbTabMenuTransfer;
	private RadioButton rbTabMenuMore;

	// ��ȡfragment������
	FragmentManager fm = getSupportFragmentManager();
	// ������
	FragmentTransaction ft = fm.beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_trans_outline);

		// ��ȡ�ؼ�
		initViews();

		// ���ü�����
		setListeners();

	}

	private void initViews() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		rbTabMenuStation = (RadioButton) findViewById(R.id.rb_tab_menu_station);
		rbTabMenuRoute = (RadioButton) findViewById(R.id.rb_tab_menu_route);
		rbTabMenuTransfer = (RadioButton) findViewById(R.id.rb_tab_menu_transfer);
		rbTabMenuMore = (RadioButton) findViewById(R.id.rb_tab_menu_more);
	}

	private void setListeners() {
		// ���õ��������
		OnClickListener listener = new InnerOnClickListener();
		rbTabMenuStation.setOnClickListener(listener);
		rbTabMenuRoute.setOnClickListener(listener);
		rbTabMenuTransfer.setOnClickListener(listener);
		rbTabMenuMore.setOnClickListener(listener);

		// ���û���������
		OnPageChangeListener listener2 = new InnerOnPageChangeListener();
		viewPager.setOnPageChangeListener(listener2);

		// ����FragmentAdapter ������
		OutlineFragmentAdapter adapter = new OutlineFragmentAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
	}

	// ����¼�������
	private class InnerOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// ���ݰ�ťѡ����
			switch (v.getId()) {
			case R.id.rb_tab_menu_station:
				viewPager.setCurrentItem(TAB_STATION);
				break;
			case R.id.rb_tab_menu_route:
				viewPager.setCurrentItem(TAB_ROUTE);
				break;
			case R.id.rb_tab_menu_transfer:
				viewPager.setCurrentItem(TAB_TRANSFER);
				break;
			case R.id.rb_tab_menu_more:
				viewPager.setCurrentItem(TAB_MORE);
				break;
			default:
				break;
			}
		}

	}

	// ViewPager����������
	private class InnerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int id) {
			switch (id) {
			case TAB_STATION:
				rbTabMenuStation.setChecked(true);
				break;
			case TAB_ROUTE:
				rbTabMenuRoute.setChecked(true);
				break;
			case TAB_TRANSFER:
				rbTabMenuTransfer.setChecked(true);
				break;
			case TAB_MORE:
				rbTabMenuMore.setChecked(true);
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
