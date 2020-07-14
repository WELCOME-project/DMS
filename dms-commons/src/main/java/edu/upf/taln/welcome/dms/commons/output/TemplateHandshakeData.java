package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author rcarlini
 */

@JsonPropertyOrder({ "type", "language" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateHandshakeData extends TemplateData {
    
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
