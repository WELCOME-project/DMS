package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rcarlini
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RDFContent {
	
	public static class JsonldGeneric {
	    @JsonInclude(JsonInclude.Include.NON_NULL) 
		@JsonProperty("@id")
	    public String id;

        @JsonInclude(JsonInclude.Include.NON_NULL) 
	    @JsonProperty("@type")
	    public String type;
	    
	    @JsonProperty("@value")
	    public String value;
	    
	    public JsonldGeneric() {}
	    public JsonldGeneric(String value) {
	    	this.value = value;
	    }
	}
	
    @JsonProperty("@id")
    public String id;

    @JsonProperty("@type")
    public String type;
    
    @JsonProperty("rdf:subject")
    public String subject;

    @JsonProperty("rdf:predicate")
    public String predicate;

    @JsonProperty("rdf:object")
    public JsonldGeneric object;
}
