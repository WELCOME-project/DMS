package edu.upf.taln.welcome.dms.commons.output;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author rcarlini
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DMOutputData {
    private List<SpeechAct> speechActs;

    public List<SpeechAct> getSpeechActs() {
        return speechActs;
    }

    public void setSpeechActs(List<SpeechAct> speechActs) {
        this.speechActs = speechActs;
    }
}
