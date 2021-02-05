package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.upf.taln.welcome.dms.commons.input.Slot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogueMove {
    private static class SpeechActSerializer extends StdSerializer<SpeechAct> {

        public SpeechActSerializer() {
            this(null);
        }

        public SpeechActSerializer(Class<SpeechAct> t) {
            super(t);
        }

        @Override
        public void serialize(SpeechAct act, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("@id", "welcome:" + act.toString());
            jsonGenerator.writeEndObject();
        }
    }


    @JsonProperty("welcome:speechAct")
    @JsonSerialize(using = SpeechActSerializer.class)
    public SpeechAct speechAct = SpeechAct.Other;

    @JsonProperty("welcome:slots")
    public List<Slot> slots = new ArrayList<>();
}
