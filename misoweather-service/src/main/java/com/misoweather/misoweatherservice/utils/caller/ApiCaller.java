package com.misoweather.misoweatherservice.utils.caller;

import org.json.JSONObject;

import java.net.URISyntaxException;

public interface ApiCaller {
    JSONObject call() throws URISyntaxException;
}
