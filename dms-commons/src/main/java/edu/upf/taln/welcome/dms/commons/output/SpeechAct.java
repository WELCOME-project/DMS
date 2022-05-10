package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upf.taln.welcome.dms.commons.input.Slot;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeechAct {
    
    @JsonProperty("@id")
    public String id;

    @JsonProperty("@type")
    public String type;

    @JsonProperty("welcome:hasLabel")
    public SpeechActLabel label = SpeechActLabel.Other;

    @JsonProperty("welcome:hasSlot")
    public Slot slot;

    @JsonIgnore
    private static int counter = 0;
    
    public static void resetCounter() {
        counter = 0;
    }

    public SpeechAct() { }
    public SpeechAct(SpeechActLabel label, Slot slot)
    {
        this.id = label.toString() + "_" + counter++;
        this.type = "welcome:SpeechAct";
        this.label = label;
        this.slot = slot;
		System.out.println(id);
    }
}
