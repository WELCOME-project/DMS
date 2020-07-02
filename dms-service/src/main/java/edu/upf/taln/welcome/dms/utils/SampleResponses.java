package edu.upf.taln.welcome.dms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upf.taln.welcome.dms.commons.output.TemplateRequestData;
import edu.upf.taln.welcome.dms.commons.output.Address;
import edu.upf.taln.welcome.dms.commons.output.DMOutput;
import edu.upf.taln.welcome.dms.commons.output.DMOutputData;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;
import edu.upf.taln.welcome.dms.commons.output.TemplateData;
import edu.upf.taln.welcome.dms.commons.output.TimePeriod;

/**
 *
 * @author rcarlini
 */
public class SampleResponses {
    
    public static DMOutput generateResponse(int turn) {
        
        List<SpeechAct> speechActs = new ArrayList<>();
        switch (turn) {
            case 1:
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_1");
                    speechAct.setType("conventional opening");
                    speechActs.add(speechAct);
                }
                {
                    TemplateData templateData = new TemplateRequestData();
                    templateData.setAddress(new Address());

                    HashMap<String, TemplateData> data = new HashMap<>();
                    data.put("request_info", templateData);
                    
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_2");
                    speechAct.setType("declarative wh-question");
                    speechAct.setData(data);
                    speechActs.add(speechAct);
                }
                break;

            case 3:
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_1");
                    speechAct.setType("conventional opening");
                    speechActs.add(speechAct);
                }
                {
                    Address address = new Address();
                    address.setCity("Terrassa");

                    TemplateData templateData = new TemplateData();
                    templateData.setName("Karim");
                    templateData.setAddress(address);

                    HashMap<String, TemplateData> data = new HashMap<>();
                    data.put("request_info", templateData);
                    
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_2");
                    speechAct.setType("declarative wh-question");
                    speechAct.setData(data);
                    speechActs.add(speechAct);
                }
                break;

            case 5:
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_1");
                    speechAct.setType("response acknowledgement");
                    speechActs.add(speechAct);
                }
                {
                    Address address = new Address();
                    address.setCity("Terrassa");

                    TemplateData templateData = new TemplateData();
                    templateData.setName("Karim");
                    templateData.setAddress(address);

                    HashMap<String, TemplateData> data = new HashMap<>();
                    data.put("TCN", templateData);
                    
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_2");
                    speechAct.setType("action directive");
                    speechAct.setTemplate("inform_registration_process.requirement_resgistration");
                    speechAct.setData(data);
                    speechActs.add(speechAct);
                }
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_3");
                    speechAct.setType("non opinion statement");
                    speechAct.setTemplate("If you move to a new address, you will need to update your registration.");
                    speechActs.add(speechAct);
                }
                {
                    BuildingAddress address = new BuildingAddress();
                    address.setCity("Terrassa");
                    address.setStreet("Raval de Montserrat");
                    address.setNumber("14");

                    TimePeriod period1 = new TimePeriod();
                    period1.setDay("Weekday");
                    period1.setOpening("9:00");
                    period1.setClosing("15:00");
                    
                    List<TimePeriod> hours = new ArrayList<>();
                    hours.add(period1);

                    TemplateHoursData templateData = new TemplateHoursData();
                    templateData.setName("Registration_office");
                    templateData.setAddress(address);
                    templateData.setHours(hours);

                    HashMap<String, TemplateData> data = new HashMap<>();
                    data.put("registration_office", templateData);
                    
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_4");
                    speechAct.setType("non opinion statement");
                    speechAct.setTemplate("inform_registration_process.closest_office");
                    speechAct.setData(data);
                    speechActs.add(speechAct);
                }
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_5");
                    speechAct.setType("commit");
                    speechAct.setTemplate("Once you have registered, we will process your application.");
                    speechActs.add(speechAct);
                }
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_6");
                    speechAct.setType("offer");
                    speechAct.setTemplate("I can already give you some information on the First Reception Service.");
                    speechActs.add(speechAct);
                }
                {
                    BuildingAddress address = new BuildingAddress();
                    address.setCity("Terrassa");
                    address.setStreet("Carrer de València");
                    address.setNumber("142");
                    address.setBuilding("Col·legi Sagrat Cor");

                    TimePeriod period1 = new TimePeriod();
                    period1.setDay("Monday");
                    period1.setOpening("19:00");
                    period1.setClosing("21:00");

                    TimePeriod period2 = new TimePeriod();
                    period2.setDay("Friday");
                    period2.setOpening("19:00");
                    period2.setClosing("21:00");
                    
                    List<TimePeriod> hours = new ArrayList<>();
                    hours.add(period1);
                    hours.add(period2);

                    TemplateHoursData templateData = new TemplateHoursData();
                    templateData.setName("Catalan_language");
                    templateData.setAddress(address);
                    templateData.setHours(hours);

                    HashMap<String, TemplateData> data = new HashMap<>();
                    data.put("module_info", templateData);
                    
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_7");
                    speechAct.setType("non opinion statement");
                    speechAct.setTemplate("inform_course.module_info");
                    speechAct.setData(data);
                    speechActs.add(speechAct);
                }
                {
                    BuildingAddress address = new BuildingAddress();
                    address.setCity("Terrassa");
                    address.setStreet("Plaça de la Vila");
                    address.setNumber("1");
                    address.setBuilding("City Council");

                    TimePeriod period1 = new TimePeriod();
                    period1.setDay("Wednesday");
                    period1.setOpening("19:00");
                    period1.setClosing("21:00");
                    
                    List<TimePeriod> hours = new ArrayList<>();
                    hours.add(period1);

                    TemplateHoursData templateData = new TemplateHoursData();
                    templateData.setName("Labour_society");
                    templateData.setAddress(address);
                    templateData.setHours(hours);

                    HashMap<String, TemplateData> data = new HashMap<>();
                    data.put("module_info", templateData);
                    
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_8");
                    speechAct.setType("non opinion statement");
                    speechAct.setTemplate("inform_course.module_info");
                    speechAct.setData(data);
                    speechActs.add(speechAct);
                }
                {
                    SpeechAct speechAct = new SpeechAct();
                    speechAct.setId("act_9");
                    speechAct.setType("conventional closing");
                    speechActs.add(speechAct);
                }
                break;

            default:
                break;
        }

        DMOutputData data = new DMOutputData();
        data.setDialogueSession(1);
        data.setUserId(1);
        data.setDialogueTurn(turn+1);
        data.setLanguage("en");
        data.setLanguageConfidence(0.5);
        data.setSpeechActs(speechActs);
        
        DMOutput output = new DMOutput();
        output.setData(data);
        
        return output;
    }
}
