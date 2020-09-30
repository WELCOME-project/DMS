package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rcarlini
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Handshaking {
    
    @JsonProperty("@id")
    private String id;
    @JsonProperty("hasDIPLanguage")
    private Language hasDIPLanguage;
    @JsonProperty("hasDIPLanguageScore")
    private int score;

    // constructors, getters, setters

    public static class Language {
        @JsonProperty("@id")
        private String code;

        // constructors, getters, setters
    }
}
