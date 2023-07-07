package com.polaris.lesscode.permission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.polaris.lesscode.permission.bo.Event;

public interface GoPushService {
    void pushMqtt(Long orgId, Long projectId, Event event) throws JsonProcessingException;
}
