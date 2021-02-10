package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.upf.taln.welcome.dms.commons.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Slot {
    @JsonProperty("@id")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String id;

    @JsonProperty("@type")
    @JsonDeserialize(using = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(using = SerializationUtils.IRISerializer.class)
    public String type;

    @JsonProperty("welcome:hasTemplate")
    public Template template;

    @JsonProperty("welcome:hasInputRDFContents")
    @JsonDeserialize(using = SerializationUtils.RDFDeserializer.class)
    public String rdf;

    public enum Status {Pending, Completed, FailedAnalysis, UnclearAnalysis, TCNClarifyRequest, TCNElaborateRequest, TopicSwitch}
    @JsonProperty("welcome:hasStatus")
    @JsonDeserialize(using = SerializationUtils.StatusDeserializer.class)
    @JsonSerialize(using = SerializationUtils.StatusSerializer.class)
    public List<Status> status = new ArrayList<>();

    @JsonProperty("welcome:hasNumberAttemps")
    @JsonDeserialize(using = SerializationUtils.IntegerLiteralDeserializer.class)
    public int numAttempts;

    @JsonProperty("welcome:confidenceScore")
    @JsonDeserialize(using = SerializationUtils.DoubleLiteralDeserializer.class)
    public double confidenceScore;

    @JsonProperty("welcome:isOptional")
    @JsonDeserialize(using = SerializationUtils.BooleanLiteralDeserializer.class)
    public boolean isOptional;
}
