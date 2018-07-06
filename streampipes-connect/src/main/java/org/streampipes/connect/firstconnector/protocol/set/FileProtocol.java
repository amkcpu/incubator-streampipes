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

package org.streampipes.connect.firstconnector.protocol.set;



import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.streampipes.connect.SendToKafka;
import org.streampipes.connect.firstconnector.format.Format;
import org.streampipes.connect.firstconnector.guess.SchemaGuesser;
import org.streampipes.connect.firstconnector.protocol.Protocol;
import org.streampipes.connect.firstconnector.sdk.ParameterExtractor;
import org.streampipes.model.modelconnect.GuessSchema;
import org.streampipes.model.modelconnect.ProtocolDescription;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.model.staticproperty.FreeTextStaticProperty;
import org.streampipes.connect.firstconnector.format.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileProtocol extends Protocol {

    Logger logger = LoggerFactory.getLogger(FileProtocol.class);
    public static String ID = "https://streampipes.org/vocabulary/v1/protocol/set/file";


    private Parser parser;
    private Format format;
    private String fileUri;

    public FileProtocol() {
    }

    public FileProtocol(Parser parser, Format format, String fileUri) {
        this.parser = parser;
        this.format = format;
        this.fileUri = fileUri;
    }

    @Override
    public ProtocolDescription declareModel() {
        ProtocolDescription pd = new ProtocolDescription(ID,"File (Set)","This is the " +
                "description for the File protocol");
        FreeTextStaticProperty urlProperty = new FreeTextStaticProperty("fileUri", "fileUri",
                "This property defines the URL for the http request.");
        pd.setSourceType("SET");
        pd.addConfig(urlProperty);
        return pd;
    }

    @Override
    public Protocol getInstance(ProtocolDescription protocolDescription, Parser parser, Format format) {
        ParameterExtractor extractor = new ParameterExtractor(protocolDescription.getConfig());

        String fileUri = extractor.singleValue("fileUri");

        return new FileProtocol(parser, format, fileUri);
    }

    @Override
    public void run(String broker, String topic) {
        FileReader fr = null;

        // TODO fix this. Currently needed because it must be wait till the whole pipeline is up and running
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SendToKafka stk = new SendToKafka(format, broker, topic);
        try {
            fr = new FileReader(fileUri);
            BufferedReader br = new BufferedReader(fr);

            InputStream inn = new FileInputStream(fileUri);
            parser.parse(inn, stk);

            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }


    @Override
    public GuessSchema getGuessSchema() {


        InputStream dataInputStream = getDataFromEndpoint();

        List<byte[]> dataByte = parser.parseNEvents(dataInputStream, 20);

        EventSchema eventSchema= parser.getEventSchema(dataByte);

        GuessSchema result = SchemaGuesser.guessSchma(eventSchema, getNElements(20));

        return result;

//        EventSchema result = null;
//
//        FileReader fr = null;
//
//        try {
//            fr = new FileReader(fileUri);
//            BufferedReader br = new BufferedReader(fr);
//
//            InputStream inn = new FileInputStream(fileUri);
//            result = parser.getEventSchema(parser.parseNEvents(inn, 1).get(0));
//
//            fr.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        return result;
    }


    public InputStream getDataFromEndpoint() {
        FileReader fr = null;
        InputStream inn = null;

        try {
            fr = new FileReader(fileUri);
            BufferedReader br = new BufferedReader(fr);

            inn = new FileInputStream(fileUri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inn;
    }
    @Override
    public List<Map<String, Object>> getNElements(int n) {
        List<Map<String, Object>> result = new ArrayList<>();

        InputStream dataInputStream = getDataFromEndpoint();

        List<byte[]> dataByteArray = parser.parseNEvents(dataInputStream, n);

        // Check that result size is n. Currently just an error is logged. Maybe change to an exception
        if (dataByteArray.size() < n) {
            logger.error("Error in File Protocol! User required: " + n + " elements but the resource just had: " +
                    dataByteArray.size());
        }

        for (byte[] b : dataByteArray) {
            result.add(format.parse(b));
        }

        return result;
    }


    @Override
    public String getId() {
        return ID;
    }

}
