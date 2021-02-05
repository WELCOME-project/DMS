package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.Slot;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;

import java.util.List;
import java.util.Optional;

public class DeterministicPolicy implements Policy {

    private final SpeechActDictionary speechActDictionary = new SpeechActDictionary();
    @Override
    public List<DialogueMove> map(Frame frame) {
        Optional<Slot> firstPending = frame.slots.stream()
                .filter(slot -> slot.status.contains(Slot.Status.Pending))
                .findFirst();

        if (firstPending.isPresent()) {
            Slot slot = firstPending.get();
            DialogueMove move = new DialogueMove();
            move.speechAct = speechActDictionary.get(slot.type);
            move.slots.add(slot);

            return List.of(move);
        }

        return null;
    }
}
