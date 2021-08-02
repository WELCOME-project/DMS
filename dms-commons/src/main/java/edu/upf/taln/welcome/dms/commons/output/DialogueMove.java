package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialogue move represents the output of the DMS.
 * It consists of a list of speech acts.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DialogueMove {

    @JsonProperty("@id")
    public String id;

    @JsonProperty("@type")
    public String type;

    @JsonProperty("welcome:hasSpeechActs")
    public List<SpeechAct> speechActs = new ArrayList<>();

    @JsonIgnore
    private static int counter = 0;

    public DialogueMove() { }
    public DialogueMove(List<SpeechAct> speechActs)
    {
        this.id = "move_" + counter++;
        this.type = "welcome:DialogueMove";
        this.speechActs = speechActs;
    }
}