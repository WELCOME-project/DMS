package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.upf.taln.welcome.dms.commons.SerializationUtils;
import edu.upf.taln.welcome.dms.commons.input.Slot;

public class SpeechAct {
    @JsonProperty("@id")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String id;

    @JsonProperty("@type")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String type;

    @JsonProperty("welcome:hasLabel")
    @JsonDeserialize(using = SerializationUtils.LabelDeserializer.class)
    @JsonSerialize(using = SerializationUtils.LabelSerializer.class)
    public SpeechActLabel label = SpeechActLabel.Other;

    @JsonProperty("welcome:hasSlot")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Slot slot;

    @JsonIgnore
    private static int counter = 0;

    public SpeechAct() { }
    public SpeechAct(SpeechActLabel label, Slot slot)
    {
        this.id = label.toString() + "_" + counter++;
        this.type = "SpeechAct";
        this.label = label;
        this.slot = slot;
    }
}
