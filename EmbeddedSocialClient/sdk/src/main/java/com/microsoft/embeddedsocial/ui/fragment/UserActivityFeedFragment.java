/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.ui.fragment;

import com.microsoft.embeddedsocial.ui.adapter.FetchableListAdapter;
import com.microsoft.embeddedsocial.ui.fragment.base.BaseActivityFeedFragment;
import com.microsoft.embeddedsocial.fetcher.FetchersFactory;
import com.microsoft.embeddedsocial.fetcher.base.FetchableAdapter;
import com.microsoft.embeddedsocial.server.model.view.ActivityView;
import com.microsoft.embeddedsocial.ui.adapter.renderer.UserRecentActivityRenderer;

/**
 * Shows recent activity.
 */
public class UserActivityFeedFragment extends BaseActivityFeedFragment {

	@Override
	protected FetchableAdapter<ActivityView, ?> createInitialAdapter() {
		return new FetchableListAdapter<>(
			FetchersFactory.createNotificationFeedFetcher(),
			new UserRecentActivityRenderer()
		);
	}

}
