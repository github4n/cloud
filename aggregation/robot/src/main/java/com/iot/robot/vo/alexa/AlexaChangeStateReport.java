package com.iot.robot.vo.alexa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 	https://developer.amazon.com/zh/docs/device-apis/alexa-interface.html#changereport
 */
public class AlexaChangeStateReport {
	private static final String NAME_SPACE = "Alexa";
	private static final String NAME = "ChangeReport";

	private Context context = new Context();
	
	public Context getContext() {
		return context;
	}
	public Event getEvent() {
		return event;
	}
	private Event event = new Event();

	public AlexaChangeStateReport setEndPointId(String endPointId) {
		event.getEndpoint().setEndpointId(endPointId);
		return this;
	}
	public AlexaChangeStateReport setToken(String token) {
		event.getEndpoint().getScope().setToken(token);
		return this;
	}
	public AlexaChangeStateReport addChangeState(String namespace, String name, Object val) {
		event.getPayload().getChange().addPropertie(namespace, name, val);
		return this;
	}
	public AlexaChangeStateReport addState(String namespace, String name, Object val) {
		context.addPropertie(namespace, name, val);
		return this;
	}
	public AlexaChangeStateReport setCause(String cause) {
		event.getPayload().getChange().getCause().setType(cause);
		return this;
	}

	static class Event {
		private Header header = new Header(NAME_SPACE, NAME);
		private Endpoint endpoint = new Endpoint();
		private Payload payload = new Payload();
		public Header getHeader() {
			return header;
		}
		public Endpoint getEndpoint() {
			return endpoint;
		}
		public Payload getPayload() {
			return payload;
		}
	}
	static class Payload {
		private Change change = new Change();

		public Change getChange() {
			return change;
		}
	}
	static class Change {
		private Cause cause = new Cause();
		
		private ArrayList<Propertie> properties = new ArrayList<Propertie>();

		public ArrayList<Propertie> getProperties() {
			return properties;
		}
		
		public void addPropertie(String namespace, String name, Object val) {
			Propertie p = new Propertie();
			p.setName(name);
			p.setNamespace(namespace);
			p.setValue(val);
			properties.add(p);
		}

		public Cause getCause() {
			return cause;
		}

	}
	
	static class Context {
		private ArrayList<Propertie> properties = new ArrayList<Propertie>();

		public ArrayList<Propertie> getProperties() {
			return properties;
		}
		
		public void addPropertie(String namespace, String name, Object val) {
			Propertie p = new Propertie();
			p.setName(name);
			p.setNamespace(namespace);
			p.setValue(val);
			properties.add(p);
		}
	}
	
	static class Cause {
		private String type;

		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
	static class Propertie {
        private String namespace;
	    private String name;
	    private Object value;
	    private String timeOfSample;
	    private Integer uncertaintyInMilliseconds;
	    public Propertie() {
	    	Calendar cl = Calendar.getInstance();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sd.setTimeZone(TimeZone.getTimeZone("GMT"));
			timeOfSample = sd.format(cl.getTime());
			uncertaintyInMilliseconds = 60000;
		}
		public String getNamespace() {
			return namespace;
		}
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object val) {
			this.value = val;
		}
		public String getTimeOfSample() {
			return timeOfSample;
		}
		public void setTimeOfSample(String timeOfSample) {
			this.timeOfSample = timeOfSample;
		}
		public Integer getUncertaintyInMilliseconds() {
			return uncertaintyInMilliseconds;
		}
		public void setUncertaintyInMilliseconds(Integer uncertaintyInMilliseconds) {
			this.uncertaintyInMilliseconds = uncertaintyInMilliseconds;
		}
	}
}
