package com.yushchenkoaleksey.edu.quiz.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class SessionToken {

    @JsonAlias({"response_code", "responseCode"})
    private Integer responseCode;
    @JsonAlias({"response_message", "responseMessage"})
    private String responseMessage;
    private String token;

    public SessionToken() {
    }

    /**
     * @param responseMessage
     * @param responseCode
     * @param token
     */

    public SessionToken(Integer responseCode, String responseMessage, String token) {
        super();
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.token = token;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}