package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.upf.taln.welcome.dms.commons.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Frame
{
    @JsonProperty("@id")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String id;

    @JsonProperty("@type")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String type;

    @JsonProperty("welcome:hasSlot")
    public List<Slot> slots = new ArrayList<>();
}
