package edu.upf.taln.welcome.dms.commons.input;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rcarlini
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Slot {
    @JsonProperty("@id")
    public String id;

    @JsonProperty("rdf:type")
    public String type;

    @JsonProperty("welcome:hasTemplateId")
    public String templateId;
    
    @JsonProperty("welcome:hasOntologyType")
    public String ontology;

    @JsonProperty("welcome:hasInputRDFContents")
    public List<RDFContent> rdf;

    //public enum Status {Pending, Completed, FailedAnalysis, UnclearAnalysis, TCNClarifyRequest, TCNElaborateRequest, TopicSwitch}
    @JsonProperty("welcome:hasStatus")
    public Status status;

    @JsonProperty("welcome:hasNumberAttemps")
    public int numAttempts;

    @JsonProperty("welcome:confidenceScore")
    public double confidenceScore;

    @JsonProperty("welcome:isOptional")
    public boolean isOptional;
}
