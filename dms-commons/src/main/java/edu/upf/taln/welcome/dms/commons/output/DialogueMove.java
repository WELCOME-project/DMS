package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.upf.taln.welcome.dms.commons.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialogue move represents the output of the DMS.
 * It consists of a list of speech acts.
 */
public class DialogueMove {
    @JsonProperty("@id")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String id;

    @JsonProperty("@type")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String type;

    public List<SpeechAct> speechActs = new ArrayList<>();

    @JsonIgnore
    private static int counter = 0;

    public DialogueMove() { }
    public DialogueMove(List<SpeechAct> speechActs)
    {
        this.id = "move_" + counter++;
        this.type = "DialogueMove";
        this.speechActs = speechActs;
    }

}