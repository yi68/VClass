package com.top.guide;

import com.top.vclass2.MainActivity;
import com.top.vclass2.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ImageFragment3 extends Fragment {
	private Button bt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.g_img3, container, false);
		bt = (Button) view.findViewById(R.id.g_guide_button);
		bt.setOnClickListener(new click());
		return view;
	}

	class click implements OnClickListener {

		@Override
		public void onClick(View v) {
			SharedPreferences preferences = getActivity().getSharedPreferences("one", getActivity().MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putInt("one", 1);
			editor.commit();
			startActivity(new Intent(getActivity(), MainActivity.class));
			getActivity().finish();
		}
	}
}
