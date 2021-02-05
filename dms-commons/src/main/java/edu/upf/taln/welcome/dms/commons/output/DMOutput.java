package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rcarlini
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DMOutput {
    public List<DialogueMove> moves = new ArrayList<>();
}