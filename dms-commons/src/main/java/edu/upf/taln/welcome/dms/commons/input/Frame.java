package edu.upf.taln.welcome.dms.commons.input;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 *
 * @author rcarlini
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Frame {
    
    @JsonProperty("@id")
    public String id;

    @JsonProperty("rdf:type")
    public String type;

    @JsonProperty("welcome:hasSlot")
    public List<Slot> slots = new ArrayList<>();
}
