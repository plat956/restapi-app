package com.epam.esm.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The service contains methods to access
 * remote authorization and resource server data.
 */
public interface CommonService {

    /**
     * Get remote data.
     *
     * @param url   the remote url
     * @param token the access token
     * @return the server response in json
     */
    JsonNode getRemoteData(String url, String token);

    /**
     * Receive access token from auth server.
     *
     * @param code the authorization code
     * @return the json object with access token data
     */
    JsonNode receiveAccessToken(String code);
}
