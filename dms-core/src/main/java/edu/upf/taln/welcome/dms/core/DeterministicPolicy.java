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

/**
 * Rule-base policy
 */
public class DeterministicPolicy implements Policy {

    private static final String SYSTEM_INFO_SLOT_TYPE = "https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemInfo";
    private static final String SYSTEM_DEMAND_SLOT_TYPE = "https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand";
    private static final String CONFIRMATION_REQUEST_SLOT_TYPE = "https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#ConfirmationRequest";
	
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
            if (slot.status == Status.Pending || slot.status == Status.NeedsUpdate) {
                SpeechActLabel label = speechActDictionary.get(slot.id);
                if (label == null) {
                    throw new WelcomeException("Unsupported slot type " + slot.id + "!");
                }
                SpeechAct act = new SpeechAct(label, slot);
                acts.add(act);
                
				if (slot.numAttempts > 0 && slot.type.equals(CONFIRMATION_REQUEST_SLOT_TYPE)) {
					SpeechActLabel yesNoLabel;
					if (acts.size() > 1) {
						yesNoLabel = SpeechActLabel.Say_Yes_No_Information;
					} else {
						yesNoLabel = SpeechActLabel.Say_Yes_No;
					}
					SpeechAct sp = new SpeechAct(yesNoLabel, null);
					acts.add(0, sp);
				}

				pickMore = SYSTEM_INFO_SLOT_TYPE.equals(slot.type);
            }
            idx++;
        }

        return new DialogueMove(acts);
    }
    
    private enum ExtraSpeechAct {
    	none, start, end
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

			ExtraSpeechAct extraSpeechAct = ExtraSpeechAct.none;
			SpeechActLabel label = null;
			switch(slot.status) {
				
				case FailedAnalysis:
				case UnclearAnalysis:
				{
					if (slot.numAttempts > 0 && slot.type.equals(CONFIRMATION_REQUEST_SLOT_TYPE)) {
						label = SpeechActLabel.Say_Yes_No;
						extraSpeechAct = ExtraSpeechAct.start;
					} else {
						label = SpeechActLabel.Signal_non_understanding;
					}
					more_moves = false;
				}
				break;

				case TopicSwitch:
				case TCNClarifyRequest:
				{
					if (slot.type.equals(SYSTEM_DEMAND_SLOT_TYPE)) {				
						label = SpeechActLabel.Apology_Repeat_Question;
					} else {
						label = SpeechActLabel.Apology_Repeat_Information;
					}
					more_moves = false;
					extraSpeechAct = ExtraSpeechAct.start;
				}
				break;

				case TCNElaborateRequest:
				{
					if (slot.type.equals(SYSTEM_DEMAND_SLOT_TYPE)) {				
						label = SpeechActLabel.Apology_Repeat_Question;
						more_moves = false;
						extraSpeechAct = ExtraSpeechAct.start;

					} else {
						label = SpeechActLabel.Apology_No_Extra_Information;
						more_moves = true;
					}
				}
				break;
				
				case NeedsUpdate:
					if (slot.tcnAnswer == null) {
						label = SpeechActLabel.NeedsUpdate;
					} else {
						label = SpeechActLabel.NeedsUpdateAnswer;
						extraSpeechAct = ExtraSpeechAct.end;
					}
					more_moves = false;
					break;

				default:
					break;
			}
			
			if (label != null) {
				switch(extraSpeechAct) {
					case start:
						SpeechAct sp1 = new SpeechAct(label, null);
						moves.add(sp1);
						
						SpeechActLabel repeatLabel = speechActDictionary.get(slot.id);
						SpeechAct sp2 = new SpeechAct(repeatLabel, slot);
						moves.add(sp2);
						break;
					case end:
						repeatLabel = speechActDictionary.get(slot.id);
						sp1 = new SpeechAct(repeatLabel, slot);
						moves.add(sp1);
						
						sp2 = new SpeechAct(label, slot);
						moves.add(sp2);
						break;
					default:
						SpeechAct sp = new SpeechAct(label, slot);
						moves.add(sp);
				}
			}
			
			idx++;
		}

        return more_moves;
    }
}
