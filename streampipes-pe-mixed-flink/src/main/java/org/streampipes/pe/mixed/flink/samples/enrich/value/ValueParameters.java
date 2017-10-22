package org.streampipes.pe.mixed.flink.samples.enrich.value;

import org.streampipes.model.impl.graph.SepaInvocation;
import org.streampipes.wrapper.params.binding.EventProcessorBindingParams;


public class ValueParameters extends EventProcessorBindingParams {

    private String valueName;
    private String value;

       public ValueParameters(SepaInvocation graph, String valueName, String value) {
        super(graph);
        this.valueName = valueName;
        this.value = value;
    }

    public String getValueName() {
        return valueName;
    }

    public String getValue() {
        return value;
    }

}
