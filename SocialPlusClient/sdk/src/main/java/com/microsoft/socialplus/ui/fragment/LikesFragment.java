/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.microsoft.socialplus.autorest.models.ContentType;
import com.microsoft.socialplus.fetcher.FetchersFactory;
import com.microsoft.socialplus.fetcher.base.Fetcher;
import com.microsoft.socialplus.sdk.R;
import com.microsoft.socialplus.server.model.view.UserCompactView;
import com.microsoft.socialplus.service.IntentExtras;
import com.microsoft.socialplus.ui.adapter.renderer.Renderer;
import com.microsoft.socialplus.ui.adapter.renderer.UserRenderer;
import com.microsoft.socialplus.ui.adapter.viewholder.UserListItemHolder;
import com.microsoft.socialplus.ui.fragment.base.BaseUsersListFragment;

/**
 * Screen with users who liked a topic.
 */
public class LikesFragment extends BaseUsersListFragment {

	private String contentHandle;
	private ContentType contentType;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentHandle = getActivity().getIntent().getStringExtra(IntentExtras.CONTENT_EXTRA);
		contentType = ContentType.fromValue(getActivity().getIntent().getStringExtra(IntentExtras.CONTENT_TYPE));
		if (contentType == null) {
			throw new IllegalArgumentException("Invalid Content Type");
		}
		if (TextUtils.isEmpty(contentHandle)) {
			finishActivity();
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setEmptyDataMessage(R.string.sp_message_no_likes);
	}

	@Override
	protected Renderer<? super UserCompactView, ? extends UserListItemHolder> createRenderer() {
		return new UserRenderer(getContext());
	}

	@Override
	protected Fetcher<UserCompactView> createFetcher() {
		return FetchersFactory.createLikeFeedFetcher(contentHandle, contentType);
	}
}