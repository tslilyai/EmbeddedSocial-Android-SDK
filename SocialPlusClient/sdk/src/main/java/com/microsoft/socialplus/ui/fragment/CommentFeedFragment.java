/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.microsoft.socialplus.data.model.AccountData;
import com.microsoft.socialplus.data.model.DiscussionItem;
import com.microsoft.socialplus.data.storage.UserActionProxy;
import com.microsoft.socialplus.event.content.CommentAddedEvent;
import com.microsoft.socialplus.event.content.CommentPostedToBackendEvent;
import com.microsoft.socialplus.event.content.CommentRemovedEvent;
import com.microsoft.socialplus.event.content.PinAddedEvent;
import com.microsoft.socialplus.event.content.PinRemovedEvent;
import com.microsoft.socialplus.event.content.TopicRemovedEvent;
import com.microsoft.socialplus.event.relationship.UserFollowedStateChangedEvent;
import com.microsoft.socialplus.fetcher.EmptyDataException;
import com.microsoft.socialplus.fetcher.FetchersFactory;
import com.microsoft.socialplus.fetcher.base.Callback;
import com.microsoft.socialplus.fetcher.base.Fetcher;
import com.microsoft.socialplus.sdk.R;
import com.microsoft.socialplus.server.model.view.TopicView;
import com.microsoft.socialplus.server.model.view.UserCompactView;
import com.microsoft.socialplus.service.IntentExtras;
import com.microsoft.socialplus.ui.adapter.DiscussionFeedAdapter;
import com.squareup.otto.Subscribe;

public class CommentFeedFragment extends DiscussionFeedFragment {

	private String topicHandle;
	private Fetcher<Object> commentFeedFetcher;

	private final Callback callback = new Callback() {

		@Override
		public void onDataRequestFailed(Exception e) {
			if (e instanceof EmptyDataException) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(IntentExtras.TOPIC_REMOVE_EXTRA, getHandle());
				getActivity().setResult(Activity.RESULT_OK, returnIntent);
				getActivity().finish();
				Toast.makeText(getActivity(), R.string.sp_message_failed_to_refresh_topic, Toast.LENGTH_LONG).show();
			}
		}

	};

	public CommentFeedFragment() {
		addThemeToMerge(R.style.SocialPlusSdkThemeOverlayTopic);
	}

	@Override
	protected DiscussionFeedAdapter createInitialAdapter() {
		Bundle arguments = getArguments();
		topicHandle = arguments.getString(IntentExtras.TOPIC_HANDLE);
		if (commentFeedFetcher == null) {
			commentFeedFetcher = FetchersFactory.createCommentFeedFetcher(
				topicHandle,
				arguments.getParcelable(IntentExtras.TOPIC_EXTRA)
			);
		}

		DiscussionFeedAdapter adapter = new DiscussionFeedAdapter(
			getActivity(),
			commentFeedFetcher,
			DiscussionFeedAdapter.FeedType.COMMENT);

		adapter.addFetcherCallback(callback);

		return adapter;
	}

	@Override
	protected int getNoteHint() {
		return R.string.sp_hint_add_comment;
	}

	@Override
	protected AccountData getAuthorProfile() {
		return commentFeedFetcher.isEmpty() ? null : ((TopicView) commentFeedFetcher.getAllData().get(0)).getUserProfile();
	}

	@Override
	protected UserCompactView getAuthor() {
		return commentFeedFetcher.isEmpty() ? null : ((TopicView) commentFeedFetcher.getAllData().get(0)).getUser();
	}

	@Override
	protected String getHandle() {
		return topicHandle;
	}

	@Override
	protected void onDonePressed(String text, String imagePath) {
		DiscussionItem discussionItem = DiscussionItem.newComment(getHandle(), text, imagePath);
		new UserActionProxy(getActivity()).postComment(discussionItem);
	}

	@Subscribe
	public void onTopicRemoved(TopicRemovedEvent topicRemovedEvent) {
		if (topicRemovedEvent.isSuccessful()) {
			Toast.makeText(getActivity(), R.string.sp_content_removed_topic, Toast.LENGTH_SHORT).show();
			Intent returnIntent = new Intent();
			returnIntent.putExtra(IntentExtras.TOPIC_REMOVE_EXTRA, getHandle());
			getActivity().setResult(Activity.RESULT_OK, returnIntent);
			getActivity().finish();
		}
	}

	@Subscribe
	public void onPinAdded(PinAddedEvent pinAddedEvent) {
		if (pinAddedEvent.isResult()) {
			getAdapter().setTopicPin(true);
		}
	}

	@Subscribe
	public void onPinRemoved(PinRemovedEvent pinRemovedEvent) {
		if (pinRemovedEvent.isResult()) {
			getAdapter().setTopicPin(false);
		}
	}

	@Subscribe
	public void onCommentRemoved(CommentRemovedEvent commentRemovedEvent) {
		if (commentRemovedEvent.isSuccessful()) {
			getAdapter().removeComment(commentRemovedEvent.getData().getHandle());
			Toast.makeText(getActivity(), R.string.sp_content_removed_comment, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), R.string.sp_message_failed_to_remove_comment, Toast.LENGTH_SHORT).show();
		}
	}

	@Subscribe
	public void onCommentAdded(CommentAddedEvent commentAddedEvent) {
		if (commentAddedEvent.isResult()) {
			getAdapter().addComment(commentAddedEvent.getData());
		}
	}

	@Subscribe
	public void onCommentPostedToBackend(CommentPostedToBackendEvent event) {
		getAdapter().refreshData();
	}

	@Subscribe
	public void onFollowedStatusChanged(UserFollowedStateChangedEvent event) {
		getAdapter().setFollowerStatus(event.getUserHandle(), event.getFollowedStatus());
	}

}