package com.ShopEase.Payment.dtos;

import com.ShopEase.Payment.entities.Status;

public class StripeResponse {
    private Status status;
    private String message;
    private String sessionId;
    private String sessionUrl;

    public StripeResponse(Status status, String message, String sessionUrl, String sessionId) {
        this.status = status;
        this.message = message;
        this.sessionUrl = sessionUrl;
        this.sessionId = sessionId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public static class Builder{
        private Status status;
        private String message;
        private String sessionId;
        private String sessionUrl;

        public Builder status(Status status){
            this.status=status;
            return this;
        }

        public Builder message(String message){
            this.message=message;
            return this;
        }

        public Builder sessionId(String sessionId){
            this.sessionId=sessionId;
            return this;
        }

        public Builder sessionUrl(String sessionUrl){
            this.sessionUrl=sessionUrl;
            return this;
        }
        public StripeResponse build(){
            return new StripeResponse(status,message,sessionId,sessionUrl);
        }
    }
    public static Builder builder(){
        return new Builder();
    }
}
