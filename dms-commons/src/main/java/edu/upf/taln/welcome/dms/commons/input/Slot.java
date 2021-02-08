package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Slot {
    private static class RDFDeserializer extends StdDeserializer<String> {

        public RDFDeserializer() {
            this(null);
        }
        protected RDFDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            return node.toString();
        }
    }

    private static class StatusDeserializer extends StdDeserializer<List<Status>> {

        public StatusDeserializer() {
            this(null);
        }
        protected StatusDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public List<Status> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);

            return node.findValues("@id").stream()
                    .map(JsonNode::asText)
                    .map(t -> t.substring(t.indexOf(':') + 1))
                    .map(Status::valueOf)
                    .collect(Collectors.toList());
        }
    }

    private static class StatusSerializer extends StdSerializer<List<Status>> {

        public StatusSerializer() {
            this(null);
        }

        public StatusSerializer(Class<List<Status>> t) {
            super(t);
        }

        @Override
        public void serialize(List<Status> status, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartArray();
            for (Status s : status)
            {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("@id", "welcome:" + s.toString());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
    }

    private static class IRIDeserializer extends StdDeserializer<String> {

        public IRIDeserializer() {
            this(null);
        }
        protected IRIDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            String t = node.asText();

            return t.substring(t.indexOf(':') + 1);
        }
    }

    private static class IRISerializer extends StdSerializer<String> {

        public IRISerializer() {
            this(null);
        }

        public IRISerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String iri, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeString("welcome:" + iri);
        }
    }

    @JsonProperty("@id")
    @JsonDeserialize(using = IRIDeserializer.class)
    @JsonSerialize(using = IRISerializer.class)
    public String id;

    @JsonProperty("@type")
    @JsonDeserialize(using = IRIDeserializer.class)
    @JsonSerialize(using = IRISerializer.class)
    public String type;

    @JsonProperty("welcome:hasTemplate")
    public Template template;

    @JsonProperty("welcome:hasInputRDFContents")
    @JsonDeserialize(using = RDFDeserializer.class)
    public String rdf;

    public enum Status {Pending, Completed, FailedAnalysis, UnclearAnalysis, TCNClarifyRequest, TCNElaborateRequest, TopicSwitch}
    @JsonProperty("welcome:hasStatus")
    @JsonDeserialize(using = StatusDeserializer.class)
    @JsonSerialize(using = StatusSerializer.class)
    public List<Status> status = new ArrayList<>();

    @JsonProperty("welcome:hasNumberAttempts")
    public int numAttempts;

    @JsonProperty("welcome:confidenceScore")
    public double confidenceScore;

    @JsonProperty("welcome:isOptional")
    public boolean isOptional;
}
