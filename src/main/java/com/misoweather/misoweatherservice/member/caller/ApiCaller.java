package com.misoweather.misoweatherservice.member.caller;

import org.json.JSONObject;

import java.net.URISyntaxException;

public interface ApiCaller {
    JSONObject call() throws URISyntaxException;
}
