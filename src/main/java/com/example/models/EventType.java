package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("proxy") PROXY,
    @JsonProperty("interim") INTERIM,
    @JsonProperty("corp-action") CORP_ACTION,
    @JsonProperty("post-sale") POST_SALE;
}
