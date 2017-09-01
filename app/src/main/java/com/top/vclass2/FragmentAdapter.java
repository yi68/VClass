package com.top.vclass2;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @FragmentAdapter 自定义适配器（本类用在MainActivity类中，用于装载所有Fragment）
 * @author zym
 *
 */
public class FragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragmentList;

	public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
