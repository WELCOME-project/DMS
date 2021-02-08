package edu.upf.taln.welcome.dms.core;


import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;

import java.util.List;

/**
 * Base class for all policies
 */
public interface Policy {
    List<DialogueMove> map(Frame frame);
}
