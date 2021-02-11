package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.Slot;
import edu.upf.taln.welcome.dms.commons.input.Slot.Status;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;
import edu.upf.taln.welcome.dms.commons.output.SpeechActLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Rule-base policy
 */
public class DeterministicPolicy implements Policy {

    private final SpeechActDictionary speechActDictionary = new SpeechActDictionary();

    /**
     * Main method of the policy mapping a DIP to a dialogue move
     * @param frame DIP representing dialogue state
     * @return list of moves
     */
    @Override
    public DialogueMove map(Frame frame) {
        ArrayList<SpeechAct> acts = new ArrayList<>();
        boolean pickMore = checkUnforeseenSituations(frame, acts);
        if (pickMore) {
            Optional<Slot> firstPending = frame.slots.stream()
                    .filter(slot -> slot.status.contains(Status.Pending))
                    .findFirst();

            if (firstPending.isPresent()) {
                Slot slot = firstPending.get();
                SpeechAct act = new SpeechAct(speechActDictionary.get(slot.id), slot);
                acts.add(act);
            }
        }

        return new DialogueMove(acts);
    }

    /**
     * Checks for unforeseen situations.
     *
     * @param frame DIP representing dialogue state
     * @param moves list of moves updated by this method
     * @return true if policy can add more moves
     */
    private boolean checkUnforeseenSituations(Frame frame, List<SpeechAct> moves)
    {
        Optional<Slot> firstFailedAnalysis = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.FailedAnalysis))
                .findFirst();
        if (firstFailedAnalysis.isPresent())
        {
            SpeechAct not_understanding = new SpeechAct();
            Slot slot = firstFailedAnalysis.get();
            not_understanding.label = SpeechActLabel.Signal_non_understanding;
            not_understanding.slot = slot;
            moves.add(not_understanding);

            return false; // no more moves
        }

        Optional<Slot> firstUnclearAnalysis = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.UnclearAnalysis))
                .findFirst();
        if (firstUnclearAnalysis.isPresent())
        {
            SpeechAct not_understanding = new SpeechAct();
            not_understanding.label = SpeechActLabel.Signal_non_understanding;
            moves.add(not_understanding);

            SpeechAct quote = new SpeechAct();
            Slot slot = firstUnclearAnalysis.get();
            quote.label = SpeechActLabel.Quotation;
            quote.slot = slot;
            moves.add(quote);

            return false; // no more moves
        }

        Optional<Slot> firstClarifyRequest = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.TCNClarifyRequest))
                .findFirst();
        if (firstClarifyRequest.isPresent())
        {
            SpeechAct accept = new SpeechAct();
            accept.label = SpeechActLabel.Agree_or_Accept;
            moves.add(accept);

            SpeechAct repeat = new SpeechAct();
            Slot slot = firstClarifyRequest.get();
            repeat.label = speechActDictionary.get(slot.id);
            repeat.slot = slot;
            moves.add(repeat);

            return false; // no more moves
        }

        Optional<Slot> firstElaborateRequest = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.TCNElaborateRequest))
                .findFirst();
        if (firstElaborateRequest.isPresent())
        {
            Slot slot = firstElaborateRequest.get();
            SpeechAct apologize = new SpeechAct();
            apologize.label = SpeechActLabel.Apology;
            apologize.slot = slot;
            moves.add(apologize);

            return true;
        }

        return true;
    }
}
