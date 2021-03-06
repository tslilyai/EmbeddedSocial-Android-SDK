/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.server.model.content.topics;

import com.microsoft.embeddedsocial.server.exception.NetworkRequestException;
import com.microsoft.embeddedsocial.server.model.UserRequest;
import com.microsoft.embeddedsocial.autorest.models.DeleteTopicNameRequest;
import com.microsoft.embeddedsocial.autorest.models.PublisherType;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;

import java.io.IOException;

import retrofit2.Response;

public class RemoveTopicNameRequest extends UserRequest {

    private final String topicName;
    private final DeleteTopicNameRequest requestBody;

    public RemoveTopicNameRequest(String topicName, PublisherType publisherType) {
        this.topicName = topicName;
        requestBody = new DeleteTopicNameRequest();
        requestBody.setPublisherType(publisherType);
    }

    @Override
    public Response send() throws NetworkRequestException {
        ServiceResponse<Object> serviceResponse;
        try {
            serviceResponse = TOPICS.deleteTopicName(topicName, requestBody, authorization);
        } catch (ServiceException|IOException e) {
            throw new NetworkRequestException(e.getMessage());
        }
        checkResponseCode(serviceResponse);

        return serviceResponse.getResponse();
    }
}
