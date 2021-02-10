package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.output.SpeechActLabel;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps DIP slots to speech acts
 */
public class SpeechActDictionary {

    private final Map<String, SpeechActLabel> dictionary = new HashMap<>();

    public SpeechActDictionary()
    {
        dictionary.put("confirmCommunication", SpeechActLabel.Yes_No_Question);
        dictionary.put("confirmLanguage", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("obtainRequest", SpeechActLabel.Open_Question);
        dictionary.put("obtainName", SpeechActLabel.Wh_Question);
        dictionary.put("obtainStatus", SpeechActLabel.Wh_Question);
        dictionary.put("obtainInterest", SpeechActLabel.Yes_No_Question);
        dictionary.put("informMessage", SpeechActLabel.Apology);
    }

    public SpeechActLabel get(String slot) { return dictionary.get(slot); }

}
