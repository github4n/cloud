package com.iot.robot.vo.alexa;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/8 11:46
 * @Modify by:
 */
public class AlexaAddOrUpdateReport {
    private static final String NAME_SPACE = "Alexa.Discovery";
    private static final String NAME = "AddOrUpdateReport";

    private Event event = new Event();
    public Event getEvent() {
        return event;
    }

    public AlexaAddOrUpdateReport addEndpoint(AlexaEndpoint alexaEndpoint) {
        event.getPayload().addEndpoint(alexaEndpoint);
        return this;
    }
    public AlexaAddOrUpdateReport setToken(String token) {
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
        private List<AlexaEndpoint> endpoints = Lists.newArrayList();
        private Scope scope = new Scope();

        public List<AlexaEndpoint> getEndpoints() {
            return endpoints;
        }

        public void addEndpoint(AlexaEndpoint alexaEndpoint) {
            this.endpoints.add(alexaEndpoint);
        }

        public Scope getScope() {
            return scope;
        }
    }
}
