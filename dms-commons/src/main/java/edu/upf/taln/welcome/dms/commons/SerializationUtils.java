package edu.upf.taln.welcome.dms.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.upf.taln.welcome.dms.commons.input.Slot;
import edu.upf.taln.welcome.dms.commons.output.SpeechActLabel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SerializationUtils {
    public static class IntegerLiteralDeserializer extends StdDeserializer<Integer> {

        @SuppressWarnings("unused")
        public IntegerLiteralDeserializer() {
            this(null);
        }
        protected IntegerLiteralDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Integer deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            String label = node.findValue("@value") != null ? node.findValue("@value").asText() : node.asText();
            return Integer.parseInt(label);
        }
    }

    public static class DoubleLiteralDeserializer extends StdDeserializer<Double> {

        @SuppressWarnings("unused")
        public DoubleLiteralDeserializer() {
            this(null);
        }
        protected DoubleLiteralDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Double deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            String label = node.findValue("@value") != null ? node.findValue("@value").asText() : node.asText();

            return Double.parseDouble(label);
        }
    }

    public static class BooleanLiteralDeserializer extends StdDeserializer<Boolean> {

        @SuppressWarnings("unused")
        public BooleanLiteralDeserializer() {
            this(null);
        }
        protected BooleanLiteralDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Boolean deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            String label = node.findValue("@value") != null ? node.findValue("@value").asText() : node.asText();
            return label.equals("yes") || label.equals("true");
        }
    }

    public static class RDFDeserializer extends StdDeserializer<String> {

        @SuppressWarnings("unused")
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

    public static class StatusDeserializer extends StdDeserializer<List<Slot.Status>> {

        public StatusDeserializer() {
            this(null);
        }
        protected StatusDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public List<Slot.Status> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);

            return node.findValues("@id").stream()
                    .map(JsonNode::asText)
                    .map(t -> t.substring(t.indexOf(':') + 1))
                    .map(Slot.Status::valueOf)
                    .collect(Collectors.toList());
        }
    }

    public static class StatusSerializer extends StdSerializer<List<Slot.Status>> {

        public StatusSerializer() {
            this(null);
        }

        public StatusSerializer(Class<List<Slot.Status>> t) {
            super(t);
        }

        @Override
        public void serialize(List<Slot.Status> status, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartArray();
            for (Slot.Status s : status)
            {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("@id", "welcome:" + s.toString());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
    }
    public static class IRIDeserializer extends StdDeserializer<String> {

        @SuppressWarnings("unused")
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

    public static class IRISerializer extends StdSerializer<String> {

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

    public static class LabelSerializer extends StdSerializer<SpeechActLabel> {
        @SuppressWarnings("unused")
        public LabelSerializer() {
            this(null);
        }
        public LabelSerializer(Class<SpeechActLabel> t) {
            super(t);
        }

        @Override
        public void serialize(SpeechActLabel act, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("@id", "welcome:" + act.toString());
            jsonGenerator.writeStringField("@type", "welcome:SpeechActLabel");
            jsonGenerator.writeEndObject();
        }
    }

    public static class LabelDeserializer extends StdDeserializer<SpeechActLabel> {

        @SuppressWarnings("unused")
        public LabelDeserializer() {
            this(null);
        }
        protected LabelDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public SpeechActLabel deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            String label = node.findValue("@id").asText();
            return SpeechActLabel.valueOf(label.substring(label.indexOf(':') + 1));
        }
    }
}
