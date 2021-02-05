package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;

import java.util.List;

public class DialogueManager {

    private final Policy policy;

    public DialogueManager(Policy policy)
    {
        this.policy = policy;
    }

    public List<DialogueMove> map(Frame frame)
    {
        return policy.map(frame);
    }
}
