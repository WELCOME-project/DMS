package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rcarlini
 */
public class KBInfo {

    @JsonProperty("@id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }    
}
