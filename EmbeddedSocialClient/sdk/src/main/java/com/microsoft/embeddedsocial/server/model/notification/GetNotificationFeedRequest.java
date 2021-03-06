/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.server.model.notification;

import com.microsoft.embeddedsocial.server.exception.NetworkRequestException;
import com.microsoft.embeddedsocial.autorest.models.FeedResponseActivityView;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.embeddedsocial.server.model.FeedUserRequest;

import java.io.IOException;

public class GetNotificationFeedRequest extends FeedUserRequest {

    @Override
    public GetNotificationFeedResponse send() throws NetworkRequestException {
        ServiceResponse<FeedResponseActivityView> serviceResponse;
        try {
            serviceResponse = NOTIFICATIONS.getNotifications(authorization, getCursor(), getBatchSize());
        } catch (ServiceException|IOException e) {
            throw new NetworkRequestException(e.getMessage());
        }
        checkResponseCode(serviceResponse);

        return new GetNotificationFeedResponse(serviceResponse.getBody());
    }
}
