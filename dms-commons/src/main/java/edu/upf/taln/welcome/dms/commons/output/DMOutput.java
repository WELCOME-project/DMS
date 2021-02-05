package edu.upf.taln.welcome.dms.commons.output;

import com.fasterxml.jackson.annotation.JsonInclude;

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