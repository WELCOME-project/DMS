package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.Slot;
import edu.upf.taln.welcome.dms.commons.input.Slot.Status;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;

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
    public List<DialogueMove> map(Frame frame) {
        ArrayList<DialogueMove> moves = new ArrayList<>();
        boolean pickMore = checkUnforeseenSituations(frame, moves);
        if (pickMore) {
            Optional<Slot> firstPending = frame.slots.stream()
                    .filter(slot -> slot.status.contains(Status.Pending))
                    .findFirst();

            if (firstPending.isPresent()) {
                DialogueMove move = new DialogueMove();
                Slot slot = firstPending.get();
                move.speechAct = speechActDictionary.get(slot.id);
                move.slot = slot;

                moves.add(move);
            }
        }

        return moves;
    }

    /**
     * Checks for unforeseen situations.
     *
     * @param frame DIP representing dialogue state
     * @param moves list of moves updated by this method
     * @return true if policy can add more moves
     */
    private boolean checkUnforeseenSituations(Frame frame, List<DialogueMove> moves)
    {
        Optional<Slot> firstFailedAnalysis = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.FailedAnalysis))
                .findFirst();
        if (firstFailedAnalysis.isPresent())
        {
            DialogueMove not_understanding = new DialogueMove();
            Slot slot = firstFailedAnalysis.get();
            not_understanding.speechAct = SpeechAct.Signal_non_understanding;
            not_understanding.slot = slot;
            moves.add(not_understanding);

            return false; // no more moves
        }

        Optional<Slot> firstUnclearAnalysis = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.UnclearAnalysis))
                .findFirst();
        if (firstUnclearAnalysis.isPresent())
        {
            DialogueMove not_understanding = new DialogueMove();
            not_understanding.speechAct = SpeechAct.Signal_non_understanding;
            moves.add(not_understanding);

            DialogueMove quote = new DialogueMove();
            Slot slot = firstUnclearAnalysis.get();
            quote.speechAct = SpeechAct.Quotation;
            quote.slot = slot;
            moves.add(quote);

            return false; // no more moves
        }

        Optional<Slot> firstClarifyRequest = frame.slots.stream()
                .filter(slot -> slot.status.contains(Status.TCNClarifyRequest))
                .findFirst();
        if (firstClarifyRequest.isPresent())
        {
            DialogueMove accept = new DialogueMove();
            accept.speechAct = SpeechAct.Agree_or_Accept;
            moves.add(accept);

            DialogueMove repeat = new DialogueMove();
            Slot slot = firstClarifyRequest.get();
            repeat.speechAct = speechActDictionary.get(slot.id);
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
            DialogueMove apologize = new DialogueMove();
            apologize.speechAct = SpeechAct.Apology;
            apologize.slot = slot;
            moves.add(apologize);

            return true;
        }

        return true;
    }
}
