/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.navigationdrawerstudio;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.microsoft.socialplus.sdk.SocialPlus;
import com.microsoft.socialplus.sdk.ui.DrawerDisplayMode;

public class NavigationDrawerApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SocialPlus.init(this, R.raw.social_plus_config);
		SocialPlus.initDrawer(this, new DrawerFactory(), DrawerDisplayMode.TABS, "STUDIO");
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}