package com.xiaokai.sectionview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private android.os.Handler mHandler = new android.os.Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TextView tv = (TextView) findViewById(R.id.tv);
		SectionView sv = (SectionView) findViewById(R.id.sv);

		sv.setOnSelectChageListener(new SectionView.OnSelectChangeListener() {
			@Override
			public void onHandleSelected(int section, String s) {
				tv.setVisibility(View.VISIBLE);
				tv.setText(s);
			}

			@Override
			public void onHandleComplete() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						tv.setVisibility(View.INVISIBLE);
					}
				}, 400);
			}
		});
	}
}
