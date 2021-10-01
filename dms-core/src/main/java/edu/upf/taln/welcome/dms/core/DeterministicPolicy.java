package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.Slot;
import edu.upf.taln.welcome.dms.commons.input.Status;
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

    private static final String SYSTEM_INFO_SLOT_TYPE = "https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemInfo";
    private static final String SYSTEM_DEMAND_SLOT_TYPE = "https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand";
	
    private final SpeechActDictionary speechActDictionary = new SpeechActDictionary();

    /**
     * Main method of the policy mapping a DIP to a dialogue move
     * @param frame DIP representing dialogue state
     * @return list of moves
     */
    @Override
    public DialogueMove map(Frame frame) throws WelcomeException {
        
        ArrayList<SpeechAct> acts = new ArrayList<>();
        
        boolean pickMore = checkUnforeseenSituations(frame, acts);
        
        int idx = 0;
        while (pickMore && idx < frame.slots.size()) {
            
            Slot slot = frame.slots.get(idx);
            if (slot.status == Status.Pending) {
                SpeechActLabel label = speechActDictionary.get(slot.id);
                if (label == null) {
                    throw new WelcomeException("Unsupported slot type " + slot.id + "!");
                }
                SpeechAct act = new SpeechAct(label, slot);
                acts.add(act);
                
                pickMore = SYSTEM_INFO_SLOT_TYPE.equals(slot.type);
            }
            idx++;
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
    private boolean checkUnforeseenSituations(Frame frame, List<SpeechAct> moves) {
		
		int idx = 0;
		boolean more_moves = true;
		List<Slot> slots = frame.slots;
		
		while (idx < slots.size() && moves.isEmpty()) {
			
			Slot slot = slots.get(idx);
			switch(slot.status) {
				
				case FailedAnalysis:
				{
					SpeechAct not_understanding = new SpeechAct();
					not_understanding.label = SpeechActLabel.Signal_non_understanding;
					moves.add(not_understanding);

					more_moves = false;
				}
				break;

				case UnclearAnalysis:
				{
					SpeechAct not_understanding = new SpeechAct();
					not_understanding.label = SpeechActLabel.Signal_non_understanding;
					moves.add(not_understanding);

					SpeechAct quote = new SpeechAct();
					quote.label = SpeechActLabel.Quotation;
					quote.slot = slot;
					moves.add(quote);

					more_moves = false;
				}
				break;

				case TopicSwitch:
				case TCNClarifyRequest:
				{
					SpeechAct accept = new SpeechAct();
					if (slot.type.equals(SYSTEM_DEMAND_SLOT_TYPE)) {				
						accept.label = SpeechActLabel.Apology_Repeat_Question;
					} else {
						accept.label = SpeechActLabel.Apology_Repeat_Information;
					}
					moves.add(accept);

					SpeechAct repeat = new SpeechAct();
					repeat.label = speechActDictionary.get(slot.id);
					repeat.slot = slot;
					moves.add(repeat);

					more_moves = false;
				}
				break;

				case TCNElaborateRequest:
				{
					SpeechAct apologize = new SpeechAct();
					if (slot.type.equals(SYSTEM_DEMAND_SLOT_TYPE)) {				
						apologize.label = SpeechActLabel.Apology_Repeat_Question;
						moves.add(apologize);

						SpeechAct repeat = new SpeechAct();
						repeat.label = speechActDictionary.get(slot.id);
						repeat.slot = slot;
						moves.add(repeat);

						more_moves = false;

					} else {
						apologize.label = SpeechActLabel.Apology_No_Extra_Information;
						moves.add(apologize);
					}
				}
				break;

				default:
					break;
			}
			idx++;
		}

        return more_moves;
    }
}
