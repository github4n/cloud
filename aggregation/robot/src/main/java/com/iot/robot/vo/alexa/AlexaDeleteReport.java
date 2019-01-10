package com.iot.robot.vo.alexa;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/7 17:13
 * @Modify by:
 */
public class AlexaDeleteReport {
    private static final String NAME_SPACE = "Alexa.Discovery";
    private static final String NAME = "DeleteReport";

    private Event event = new Event();
    public Event getEvent() {
        return event;
    }

    public AlexaDeleteReport addEndpoint(String endPointId) {
        event.getPayload().addEndpoint(endPointId);
        return this;
    }
    public AlexaDeleteReport setToken(String token) {
        event.getPayload().getScope().setToken(token);
        return this;
    }

    static class Event {
        private Header header = new Header(NAME_SPACE, NAME);
        private Payload payload = new Payload();

        public Header getHeader() {
            return header;
        }
        public Payload getPayload() {
            return payload;
        }
    }

    static class Payload {
        private List<Map<String, String>> endpoints = Lists.newArrayList();
        private Scope scope = new Scope();

        public List<Map<String, String>> getEndpoints() {
            return endpoints;
        }

        public void addEndpoint(String endpointId) {
            Map<String, String> endpointMap = Maps.newHashMap();
            endpointMap.put("endpointId", endpointId);
            this.endpoints.add(endpointMap);
        }

        public Scope getScope() {
            return scope;
        }
    }
}
