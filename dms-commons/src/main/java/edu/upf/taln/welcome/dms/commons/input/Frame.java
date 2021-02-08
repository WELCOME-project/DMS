package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Frame
{
    @JsonProperty("@id")
    public String type;

    @JsonProperty("welcome:hasSlot")
    public List<Slot> slots = new ArrayList<>();
}
