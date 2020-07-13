package edu.upf.taln.welcome.dms.commons.input;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DMInputData {
    
    @NotNull
    private Frame frame;

    public void setFrame(Frame frame) {
		this.frame = frame;
	}
    
    public Frame getFrame() {
		return frame;
	}
}
