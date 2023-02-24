package edu.upf.taln.welcome.dms.core;


import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;

/**
 * Base class for all policies
 */
public interface Policy {
    DialogueMove map(Frame frame) throws WelcomeException;
}
