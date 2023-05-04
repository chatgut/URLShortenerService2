package com.example.urlshortenerservice.dto;

public class UrlErrorReponseDto {

    private String status;
    private String error;

    public UrlErrorReponseDto(String status, String error) {
        this.status = status;
        this.error = error;
    }
    public UrlErrorReponseDto(){
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "UrlErrorReponseDto{" +
                "status='" + status + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
