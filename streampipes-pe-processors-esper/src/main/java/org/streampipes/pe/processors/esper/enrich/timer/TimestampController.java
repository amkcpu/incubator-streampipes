package org.streampipes.pe.processors.esper.enrich.timer;

import org.streampipes.model.impl.EpaType;
import org.streampipes.model.impl.eventproperty.EventProperty;
import org.streampipes.model.impl.graph.SepaDescription;
import org.streampipes.model.impl.graph.SepaInvocation;
import org.streampipes.model.impl.output.AppendOutputStrategy;
import org.streampipes.model.util.SepaUtils;
import org.streampipes.vocabulary.SO;
import org.streampipes.pe.processors.esper.config.EsperConfig;
import org.streampipes.sdk.builder.ProcessingElementBuilder;
import org.streampipes.sdk.helpers.EpProperties;
import org.streampipes.sdk.helpers.EpRequirements;
import org.streampipes.sdk.helpers.OutputStrategies;
import org.streampipes.sdk.helpers.SupportedFormats;
import org.streampipes.sdk.helpers.SupportedProtocols;
import org.streampipes.wrapper.ConfiguredEventProcessor;
import org.streampipes.wrapper.runtime.EventProcessor;
import org.streampipes.wrapper.standalone.declarer.StandaloneEventProcessorDeclarerSingleton;

import java.util.ArrayList;
import java.util.List;

public class TimestampController extends StandaloneEventProcessorDeclarerSingleton<TimestampParameter> {

	@Override
	public SepaDescription declareModel() {
		return ProcessingElementBuilder.create("enrich_timestamp", "Timestamp Enrichment", "Appends the current time in ms to the event payload")
						.iconUrl(EsperConfig.getIconUrl("Timer_Icon_HQ"))
						.category(EpaType.ENRICH)
						.requiredPropertyStream1(EpRequirements.anyProperty())
						.outputStrategy(OutputStrategies.append(EpProperties.longEp("appendedTime", SO.DateTime)))
						.supportedProtocols(SupportedProtocols.kafka(), SupportedProtocols.jms())
						.supportedFormats(SupportedFormats.jsonFormat())
						.build();
	}

	@Override
	public ConfiguredEventProcessor<TimestampParameter, EventProcessor<TimestampParameter>> onInvocation(SepaInvocation
																																																								 sepa) {
		AppendOutputStrategy strategy = (AppendOutputStrategy) sepa.getOutputStrategies().get(0);

		String appendTimePropertyName = SepaUtils.getEventPropertyName(strategy.getEventProperties(), "appendedTime");

		List<String> selectProperties = new ArrayList<>();
		for(EventProperty p : sepa.getInputStreams().get(0).getEventSchema().getEventProperties())
		{
			selectProperties.add(p.getRuntimeName());
		}

		TimestampParameter staticParam = new TimestampParameter (
						sepa,
						appendTimePropertyName,
						selectProperties);

		return new ConfiguredEventProcessor<>(staticParam, TimestampEnrichment::new);
	}
}
