package cn.com.jian.smarttransxa.fragment;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.jian.smarttransxa.activity.R;

/**
 * more ����
 * 
 * @author Jian
 * 
 */
public class MoreFragment extends Fragment {

	// ����more �� ListView ����,adapter,����
	private ListView lvMore;
	private ArrayAdapter<String> adapter;
	private List<String> list;

	public MoreFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Log.d("smartTransXA", "MoreFragment");
		View v = inflater.inflate(R.layout.fragment_trans_more, container,
				false);

		// ��ȡ�ؼ�
		initViews(v);

		// ���ü�����
		setListeners();

		// ��Ӽ���
		addList();

		// ����adapter������
		setAdapter();

		return v;
	}

	private void initViews(View v) {
		lvMore = (ListView) v.findViewById(R.id.lv_more);
	}

	private void addList() {
		list = new LinkedList<String>();
		list.add("���˵�� ");
		list.add("����ģ��������߲�ѯ������վ�㡢��·�ͻ�����Ϣ");
		list.add("�����߹�����·���ݻ��ڰ��﹫��������");
		list.add("��ʹ������������bug����ӭ�����ҷ��ʼ�!");
		list.add("��ȡ����");
		list.add("��ϵ����");
		list.add("mr.lv.arrow@gmail.com");

	}

	private void setListeners() {
		OnItemClickListener listener = new InnerOnItemClickListener();
		lvMore.setOnItemClickListener(listener);

	}

	private void setAdapter() {
		adapter = new ArrayAdapter<>(getActivity(), R.layout.more_item, list);
		lvMore.setAdapter(adapter);
	}

	private class InnerOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(getActivity(), "�������°汾", Toast.LENGTH_SHORT).show();

		}
	}

}
