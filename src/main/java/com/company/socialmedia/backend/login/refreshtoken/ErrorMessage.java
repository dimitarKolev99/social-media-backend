package com.company.socialmedia.backend.login.refreshtoken;

import java.util.Date;

public class ErrorMessage {

    private int value;

    private Date date;

    private String message;

    private String description;

    public ErrorMessage() {

    }
    public ErrorMessage(int value, Date date, String message, String description) {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
