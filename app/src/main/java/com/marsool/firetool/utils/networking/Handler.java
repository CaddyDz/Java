package com.marsool.firetool.utils.networking;

public interface Handler {
    void handleResponse(HttpResponse text);
    void handleError(String text);
}
