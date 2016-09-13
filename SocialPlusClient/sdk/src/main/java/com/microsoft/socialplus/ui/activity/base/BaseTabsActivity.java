/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.ui.activity.base;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.microsoft.socialplus.base.utils.ViewUtils;
import com.microsoft.socialplus.base.view.SlidingTabLayout;
import com.microsoft.socialplus.sdk.R;

/**
 * Base class for activities with tabs.
 */
public abstract class BaseTabsActivity extends BaseActivity {

	private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			BaseTabsActivity.this.onPageSelected(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};

	private ViewPager viewPager;
	private SlidingTabLayout slidingTabLayout;
	private static TabColorizer customTabColorizer;

	protected BaseTabsActivity() {
	}

	protected BaseTabsActivity(int activeNavigationItemId) {
		super(activeNavigationItemId);
	}

	public interface TabColorizer {
		@ColorRes int getBackgroundColor();
		@ColorRes int getActiveTextColor();
		@ColorRes int getNotActiveTextColor();
		@ColorRes int getSelectorColor();
	}

	public static void setTabColorizer(TabColorizer tabColorizer) {
		customTabColorizer = tabColorizer;
	}

	public static TabColorizer getCustomTabColorizer() {
		return customTabColorizer;
	}

	@Override
	protected void setupLayout() {
		setActivityContent(R.layout.sp_fragment_tabs);
		viewPager = (ViewPager) findViewById(R.id.sp_viewpager);

		PagerAdapter adapter = createPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(adapter.getCount());

		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sp_slidingTabs);
		slidingTabLayout.setViewPager(viewPager);

		int selectorColorId = R.color.sp_lime_A400;
		if (customTabColorizer != null) {
			slidingTabLayout.setBackgroundColor(ContextCompat.getColor(this, customTabColorizer.getBackgroundColor()));
			selectorColorId = customTabColorizer.getSelectorColor();
		}
		slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, selectorColorId));

		viewPager.addOnPageChangeListener(onPageChangeListener);
	}

	/**
	 * Creates an adapter for {@link ViewPager}.
	 */
	protected abstract PagerAdapter createPagerAdapter();

	public int getCurrentPagePosition() {
		return viewPager.getCurrentItem();
	}

	/**
	 * Set whether the tabs indicator is visible (we hide it when there is only one tab).
	 */
	protected void setTabsIndicatorVisible(boolean visible) {
		ViewUtils.setVisible(slidingTabLayout, visible);
	}

	protected void notifyTabsChanged() {
		if (viewPager != null && slidingTabLayout != null) {
			PagerAdapter adapter = viewPager.getAdapter();
			adapter.notifyDataSetChanged();
			viewPager.setOffscreenPageLimit(adapter.getCount());
			slidingTabLayout.setViewPager(viewPager);
		}
	}

	protected void onPageSelected(int position) {
		// do nothing
	}
}