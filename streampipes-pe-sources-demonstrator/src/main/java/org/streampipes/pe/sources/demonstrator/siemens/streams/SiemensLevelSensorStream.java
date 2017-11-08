package org.streampipes.pe.sources.demonstrator.siemens.streams;

import java.util.ArrayList;
import java.util.List;

import org.streampipes.commons.Utils;
import org.streampipes.model.impl.EventSchema;
import org.streampipes.model.impl.EventStream;
import org.streampipes.model.impl.eventproperty.EventProperty;
import org.streampipes.model.impl.eventproperty.EventPropertyPrimitive;
import org.streampipes.model.impl.graph.SepDescription;
import org.streampipes.vocabulary.SO;
import org.streampipes.vocabulary.XSD;
import org.streampipes.pe.sources.demonstrator.config.DemonstratorVariables;
import org.streampipes.pe.sources.demonstrator.config.DemonstratorConfig;
import org.streampipes.pe.sources.demonstrator.sources.AbstractDemonstratorStream;

public class SiemensLevelSensorStream extends AbstractDemonstratorStream {

	public SiemensLevelSensorStream(DemonstratorVariables variables) {
		super(variables);
	}

	@Override
	public EventStream declareModel(SepDescription sep) {
		EventStream stream = prepareStream(variables.topic());

		EventSchema schema = new EventSchema();
		List<EventProperty> eventProperties = new ArrayList<EventProperty>();
		eventProperties.add(new EventPropertyPrimitive(XSD._string.toString(), "timestamp", "",
				Utils.createURI("http://schema.org/DateTime")));
		// TODO do I need an id
		eventProperties
				.add(new EventPropertyPrimitive(XSD._string.toString(), "sensorId", "", Utils.createURI(SO.Text)));

		eventProperties.add(new EventPropertyPrimitive(XSD._float.toString(), "level", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._float.toString(), "space", "", Utils.createURI(SO.Number)));
		eventProperties
				.add(new EventPropertyPrimitive(XSD._float.toString(), "distance", "", Utils.createURI(SO.Number)));
		eventProperties
				.add(new EventPropertyPrimitive(XSD._float.toString(), "volume", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._float.toString(), "head", "", Utils.createURI(SO.Number)));
		eventProperties.add(new EventPropertyPrimitive(XSD._float.toString(), "flow", "", Utils.createURI(SO.Number)));
		eventProperties
				.add(new EventPropertyPrimitive(XSD._float.toString(), "temperature", "", Utils.createURI(SO.Number)));

		schema.setEventProperties(eventProperties);
		stream.setEventSchema(schema);
		stream.setName(variables.eventName());
		stream.setDescription(variables.description());
		stream.setUri(sep.getUri() +"/" +variables.tagNumber());
		stream.setIconUrl(DemonstratorConfig.iconBaseUrl + "/" +variables.icon() +".png");
		return stream;
	}

}
