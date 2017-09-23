package com.coopsystem.web.socket.notification;

/**
 * Created by Master on 14.05.2017.
 */

public class Notification<T> {
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
