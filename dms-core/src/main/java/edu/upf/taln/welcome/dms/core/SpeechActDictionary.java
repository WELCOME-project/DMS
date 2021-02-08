package edu.upf.taln.welcome.dms.core;

import edu.upf.taln.welcome.dms.commons.output.SpeechAct;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps DIP slots to speech acts
 */
public class SpeechActDictionary {

    private final Map<String, SpeechAct> dictionary = new HashMap<>();

    public SpeechActDictionary()
    {
        dictionary.put("confirmCommunication", SpeechAct.Yes_No_Question);
        dictionary.put("confirmLanguage", SpeechAct.Declarative_Yes_No_Question);
        dictionary.put("obtainRequest", SpeechAct.Open_Question);
        dictionary.put("obtainName", SpeechAct.Wh_Question);
        dictionary.put("obtainStatus", SpeechAct.Wh_Question);
        dictionary.put("obtainInterest", SpeechAct.Yes_No_Question);
        dictionary.put("informMessage", SpeechAct.Apology);
    }

    public SpeechAct get(String slot) { return dictionary.get(slot); }

}
