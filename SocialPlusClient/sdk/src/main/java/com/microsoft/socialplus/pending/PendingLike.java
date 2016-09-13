/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.pending;

import android.content.Context;

import com.microsoft.socialplus.autorest.models.ContentType;
import com.microsoft.socialplus.data.storage.UserActionProxy;

/**
 * Pending "like" or "dislike" action.
 */
public class PendingLike implements PendingAction {

	private final String contentHandle;
	private final boolean liked;
	private final ContentType contentType;

	public PendingLike(String contentHandle, ContentType contentType, boolean liked) {
		this.contentHandle = contentHandle;
		this.liked = liked;
		this.contentType = contentType;
	}

	@Override
	public void execute(Context context) {
		new UserActionProxy(context).setLikeStatus(contentHandle, contentType, liked);
	}
}