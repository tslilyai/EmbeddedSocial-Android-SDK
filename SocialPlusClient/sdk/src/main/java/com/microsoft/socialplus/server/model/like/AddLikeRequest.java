/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.server.model.like;

import com.microsoft.socialplus.autorest.models.ContentType;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.socialplus.server.exception.NetworkRequestException;

import java.io.IOException;

import retrofit2.Response;

public class AddLikeRequest extends GenericLikeRequest {

    public AddLikeRequest(String contentHandle, ContentType contentType) {
        super(contentHandle, contentType);
    }

    public Response send() throws NetworkRequestException {
        ServiceResponse<Object> serviceResponse;
        try {
            switch (contentType) {
                case TOPIC:
                    serviceResponse = TOPIC_LIKES.postLike(contentHandle, bearerToken);
                    break;
                case COMMENT:
                    serviceResponse = COMMENT_LIKES.postLike(contentHandle, bearerToken);
                    break;
                case REPLY:
                    serviceResponse = REPLY_LIKES.postLike(contentHandle, bearerToken);
                    break;
                default:
                    throw new IllegalStateException("Unknown type for like");
            }
        } catch (ServiceException|IOException e) {
            throw new NetworkRequestException(e.getMessage());
        }
        checkResponseCode(serviceResponse);

        return serviceResponse.getResponse();
    }
}