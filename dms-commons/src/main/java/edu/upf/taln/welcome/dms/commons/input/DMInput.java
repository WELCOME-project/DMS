package edu.upf.taln.welcome.dms.commons.input;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DMInput {
    
    @NotNull
    public Frame frame; // Will have to be changed to a list of frames at some point

}
