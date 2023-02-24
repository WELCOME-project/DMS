package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;

/**
 * Dialogue manager holds a policy used to map DIP frames to dialogue moves.
 */
public class DialogueManager {

    private final Policy policy;

    public DialogueManager(Policy policy)
    {
        this.policy = policy;
    }

    public DialogueMove map(Frame frame) throws WelcomeException
    {
        return policy.map(frame);
    }
}
