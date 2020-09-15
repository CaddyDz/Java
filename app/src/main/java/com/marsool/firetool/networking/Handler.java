package com.marsool.firetool.networking;

public interface Handler {
    void handleResponse(HttpResponse text);
    void handleError(Exception x);
}
