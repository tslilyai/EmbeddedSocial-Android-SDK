package com.microsoft.socialplus.server.model.account;

import com.microsoft.socialplus.autorest.models.IdentityProvider;
import com.microsoft.socialplus.autorest.models.PostLinkedAccountRequest;
import com.microsoft.rest.ServiceException;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.socialplus.server.exception.NetworkRequestException;
import com.microsoft.socialplus.server.model.UserRequest;

import java.io.IOException;

import retrofit2.Response;

public class LinkThirdPartyRequest extends UserRequest {

    private PostLinkedAccountRequest request;

    public LinkThirdPartyRequest(IdentityProvider identityProvider,
                                 String accessToken) {
        request = new PostLinkedAccountRequest();
        request.setIdentityProvider(identityProvider);
        request.setAccessToken(accessToken);
        //TODO
//        request.setRequestToken(requestToken);
    }

    @Override
    public Response send() throws NetworkRequestException {
        ServiceResponse<Object> serviceResponse;
        try {
            serviceResponse = LINKED_ACCOUNTS.postLinkedAccount(request, bearerToken);
        } catch (ServiceException|IOException e) {
            throw new NetworkRequestException(e.getMessage());
        }
        checkResponseCode(serviceResponse);
        return serviceResponse.getResponse();
    }
}