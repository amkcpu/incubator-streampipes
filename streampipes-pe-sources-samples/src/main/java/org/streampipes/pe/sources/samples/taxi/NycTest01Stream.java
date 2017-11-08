package org.streampipes.pe.sources.samples.taxi;

import org.streampipes.model.impl.EventGrounding;
import org.streampipes.model.impl.EventSchema;
import org.streampipes.model.impl.EventStream;
import org.streampipes.model.impl.TransportFormat;
import org.streampipes.model.impl.graph.SepDescription;
import org.streampipes.vocabulary.MessageFormat;
import org.streampipes.pe.sources.samples.config.SampleSettings;
import org.streampipes.pe.sources.samples.config.SourcesConfig;
import org.streampipes.commons.Utils;

import java.io.File;

public class NycTest01Stream extends AbstractNycStream {

	public NycTest01Stream()
	{
		super(NycSettings.test01Topic);
	}
	
	@Override
	public EventStream declareModel(SepDescription sep) {
		EventStream stream = new EventStream();
		stream.setIconUrl(SourcesConfig.iconBaseUrl + "/Taxi_Icon_2" +"_HQ.png");
		EventSchema schema = NycTaxiUtils.getEventSchema();

		EventGrounding grounding = new EventGrounding();
		grounding.setTransportProtocol(SampleSettings.kafkaProtocol(NycSettings.test01Topic));
		grounding.setTransportFormats(Utils.createList(new TransportFormat(MessageFormat.Json)));
		
		stream.setEventGrounding(grounding);
		stream.setEventSchema(schema);
		stream.setName("Test Stream 1");
		stream.setDescription("NYC Taxi Debs Test Stream 1");
		stream.setUri("test01");
		return stream;
	}

	@Override
	public void executeStream() {
		File file = new File(NycSettings.test01DatasetFilename);
		executeReplay(file);
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

}
