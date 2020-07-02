package edu.upf.taln.welcome.dms.commons.output;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author rcarlini
 */
@JsonPropertyOrder({ "userId", "dialogueSession", "dialogueTurn", "language", "language_conf_score", "speechActs" })
public class DMOutputData {
    @NotNull
    private int userId;
    
    @NotNull
    private int dialogueSession;
    
    @NotNull
    private int dialogueTurn;
    
    private String language;
    @JsonProperty("language_conf_score")
    private double languageConfidence;
    private List<SpeechAct> speechActs;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDialogueSession() {
        return dialogueSession;
    }

    public void setDialogueSession(int dialogueSession) {
        this.dialogueSession = dialogueSession;
    }

    public int getDialogueTurn() {
        return dialogueTurn;
    }

    public void setDialogueTurn(int dialogueTurn) {
        this.dialogueTurn = dialogueTurn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getLanguageConfidence() {
        return languageConfidence;
    }

    public void setLanguageConfidence(double languageConfidence) {
        this.languageConfidence = languageConfidence;
    }

    public List<SpeechAct> getSpeechActs() {
        return speechActs;
    }

    public void setSpeechActs(List<SpeechAct> speechActs) {
        this.speechActs = speechActs;
    }
}
