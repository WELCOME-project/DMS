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
    @JsonDeserialize(contentUsing = SerializationUtils.IRIDeserializer.class)
    @JsonSerialize(contentUsing = SerializationUtils.IRISerializer.class)
    public List<String> type;

    @JsonProperty("welcome:hasTemplate")
    public List<Template> template;

    @JsonProperty("welcome:hasInputRDFContents")
    @JsonDeserialize(contentUsing = SerializationUtils.RDFDeserializer.class)
    public List<String> rdf;

    public enum Status {Pending, Completed, FailedAnalysis, UnclearAnalysis, TCNClarifyRequest, TCNElaborateRequest, TopicSwitch}
    @JsonProperty("welcome:hasStatus")
    @JsonDeserialize(contentUsing = SerializationUtils.StatusDeserializer.class)
    @JsonSerialize(contentUsing = SerializationUtils.StatusSerializer.class)
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
