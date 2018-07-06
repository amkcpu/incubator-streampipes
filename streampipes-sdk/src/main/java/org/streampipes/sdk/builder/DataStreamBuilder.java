/*
 * Copyright 2018 FZI Forschungszentrum Informatik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.streampipes.sdk.builder;

import org.streampipes.model.SpDataStream;
import org.streampipes.model.grounding.EventGrounding;
import org.streampipes.model.grounding.TransportFormat;
import org.streampipes.model.grounding.TransportProtocol;
import org.streampipes.model.schema.EventProperty;
import org.streampipes.model.schema.EventSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataStreamBuilder extends AbstractPipelineElementBuilder<DataStreamBuilder, SpDataStream> {

    private List<EventProperty> eventProperties;
    private EventGrounding eventGrounding;

    protected DataStreamBuilder(String id, String label, String description) {
        super(id, label, description, new SpDataStream());
        this.eventProperties = new ArrayList<>();
        this.eventGrounding = new EventGrounding();
    }

    /**
     * Creates a new data stream using the builder pattern.
     * @param id A unique identifier of the new element, e.g., com.mycompany.stream.mynewdatastream
     * @param label A human-readable name of the element. Will later be shown as the element name in the StreamPipes UI.
     * @param description A human-readable description of the element.
     * @return a new instance of {@link DataStreamBuilder}
     */
    public static DataStreamBuilder create(String id, String label, String description) {
        return new DataStreamBuilder(id, label, description);
    }

    /**
     * Assigns a new event property to the stream's schema.
     * @param property The event property that should be added. Use {@link org.streampipes.sdk.helpers.EpProperties}
     *                 for defining simple property definitions or
     *                 {@link org.streampipes.sdk.builder.PrimitivePropertyBuilder} for defining more complex
     *                 definitions.
     * @return this
     */
    public DataStreamBuilder property(EventProperty property) {
        this.eventProperties.add(property);
        return me();
    }

    /**
     * Assigns a new {@link org.streampipes.model.grounding.TransportProtocol} to the stream definition.
     * @param protocol The transport protocol of the stream at runtime (e.g., Kafka or MQTT). Use
     * {@link org.streampipes.sdk.helpers.Protocols} to use some pre-defined protocols (or create a new protocol as
     *                 described in the developer guide).
     * @return this
     */
    public DataStreamBuilder protocol(TransportProtocol protocol) {
        this.eventGrounding.setTransportProtocol(protocol);
        return this;
    }

    /**
     * Assigns a new {@link org.streampipes.model.grounding.TransportFormat} to the stream definition.
     * @param format The transport format of the stream at runtime (e.g., JSON or Thrift). Use
     * {@link org.streampipes.sdk.helpers.Formats} to use some pre-defined formats (or create a new format as
     *               described in the developer guide).
     * @return this
     */
    public DataStreamBuilder format(TransportFormat format) {
        this.eventGrounding.setTransportFormats(Collections.singletonList(format));
        return this;
    }

    @Override
    protected DataStreamBuilder me() {
        return this;
    }

    @Override
    protected void prepareBuild() {
        this.elementDescription.setEventGrounding(eventGrounding);
        this.elementDescription.setEventSchema(new EventSchema(eventProperties));
    }
}
