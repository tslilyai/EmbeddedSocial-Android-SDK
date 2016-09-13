/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.server.model.content.comments;

import com.microsoft.socialplus.autorest.models.FeedResponseCommentView;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.socialplus.data.model.CommentFeedType;
import com.microsoft.socialplus.server.exception.NetworkRequestException;
import com.microsoft.socialplus.server.model.FeedUserRequest;

import java.io.IOException;

public final class GetCommentFeedRequest extends FeedUserRequest {

	private final String topicHandle;
	private final int commentFeedType;

	public GetCommentFeedRequest(CommentFeedType commentFeedType, String topicHandle) {
		this.commentFeedType = commentFeedType.ordinal();
		this.topicHandle = topicHandle;
	}

	public int getCommentFeedType() {
		return commentFeedType;
	}

	public String getTopicHandle() {
		return topicHandle;
	}

	@Override
	public GetCommentFeedResponse send() throws NetworkRequestException {
		ServiceResponse<FeedResponseCommentView> serviceResponse;
		try {
			serviceResponse = TOPIC_COMMENTS.getTopicComments(topicHandle, getCursor(),
					getBatchSize(), appKey, bearerToken, null);
		} catch (ServiceException|IOException e) {
			throw new NetworkRequestException(e.getMessage());
		}
		checkResponseCode(serviceResponse);
		return new GetCommentFeedResponse(serviceResponse.getBody());
	}
}