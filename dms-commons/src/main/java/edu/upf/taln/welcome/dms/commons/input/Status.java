package edu.upf.taln.welcome.dms.commons.input;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rcarlini
 */
public enum Status {
    
    @JsonProperty("welcome:Pending")
    Pending,
    
    @JsonProperty("welcome:Completed")
    Completed,
    
    @JsonProperty("welcome:FailedAnalysis")
    FailedAnalysis,
    
    @JsonProperty("welcome:UnclearAnalysis")
    UnclearAnalysis,
    
    @JsonProperty("welcome:TCNClarifyRequest")
    TCNClarifyRequest,
    
    @JsonProperty("welcome:TCNElaborateRequest")
    TCNElaborateRequest,
    
    @JsonProperty("welcome:TopicSwitch")
    TopicSwitch,
    
    @JsonProperty("welcome:Unfinished")
    Unfinished,
    
    @JsonProperty("welcome:Undefined")
    Undefined,
	
    @JsonProperty("welcome:NeedsUpdate")
	NeedsUpdate
}
