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
        //DTASF
        dictionary.put("frs:obtainYearCompletionLabourMarket", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainDurationLabourMarket", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCertificateLabourMarket", SpeechActLabel.Yes_No_Question);

        dictionary.put("welcome:confirmCommunication", SpeechActLabel.Yes_No_Question);
        dictionary.put("welcome:confirmLanguage", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("welcome:obtainRequest", SpeechActLabel.Open_Question);
        
        dictionary.put("welcome:informMessage", SpeechActLabel.Apology);
        
        dictionary.put("welcome:obtainName", SpeechActLabel.Wh_Question);
        dictionary.put("welcome:obtainStatus", SpeechActLabel.Wh_Question);
        
        dictionary.put("frs:obtainInterest", SpeechActLabel.Yes_No_Question);
        
       	dictionary.put("frs:informNeedRegistration", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:obtainResidenceAddressCity", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainResidenceAddressStreet", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainResidenceAddressNumber", SpeechActLabel.Wh_Question);
        
        dictionary.put("frs:informOfficeAddress", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informOfficeHours", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informNeedAskAppointment", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informDocumentsNeeded", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informResumeApplication", SpeechActLabel.Statement_non_opinion);

        dictionary.put("frs:informOverview", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informLanguageModule", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informLanguageModuleAddress", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informLanguageModuleHours", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informLabourModule", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informLabourModuleAddress", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informLabourModuleHours", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informSocietyModule", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informSocietyModuleAddress", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informSocietyModuleHours", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informDetailsTime", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informDetailsCompletion", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informDetailsLanguage", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:informDetailsValue", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:obtainInterestRegistration", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("frs:obtainName", SpeechActLabel.Wh_Question);
        dictionary.put("frs:confirmFirstSurname", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainFirstSurname", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainSecondSurname", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainIDNumber", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainIDType", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainIDCountry", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainGender", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainBirthday", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCountryOfBirth", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCityOfBirth", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainNationality", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainLandline", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainMobilePhone", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainEmail", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainNotificationPreferences", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainSMSNotifications", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainEMailNotifications", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("frs:obtainStreetType", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainStreetName", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainStreetNumber", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainBuildingName", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainEntrance", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainBuildingType", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainFloorNumber", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainDoorNumber", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainPostCode", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainProvince", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainMunicipality", SpeechActLabel.Wh_Question);
        
        dictionary.put("frs:obtainMaritalStatus", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainAsylumClaim", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainYearArrival", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainYearArrivalRegion", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainPreviousResidenceCatalonia", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainPreviousResidenceSpain", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainPreviousResidenceOther", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("frs:obtainRegistrationYear", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainLearningHandicap", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainIlliteracy", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("frs:informTraining", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:obtainKnowledgeCatalan", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCoursesCatalan", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainKnowledgeSpanish", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCoursesSpanish", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainKnowledgeLabour", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCoursesLabour", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainKnowledgeSociety", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCoursesSociety", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("frs:obtainCourseNameCatalan", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCourseInstitutionCatalan", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainYearCompletionCatalan", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainDurationCatalan", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCertificateCatalan", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCourseNameSpanish", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCourseInstitutionSpanish", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainYearCompletionSpanish", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainDurationSpanish", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCertificateSpanish", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCourseNameLabourMarket", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCourseInstitutionLabourMarket", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainYearCompletionLabourMarket", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainDurationLabourMarket", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCertificateLabourMarket", SpeechActLabel.Yes_No_Question);
        dictionary.put("frs:obtainCourseNameCatalanSociety", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCourseInstitutionCatalanSociety", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainYearCompletionCatalanSociety", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainDurationCatalanSociety", SpeechActLabel.Wh_Question);
        dictionary.put("frs:obtainCertificateCatalanSociety", SpeechActLabel.Yes_No_Question);

        dictionary.put("welcome:informEnd", SpeechActLabel.Statement_non_opinion);
        dictionary.put("welcome:obtainConfirmation", SpeechActLabel.Yes_No_Question);

        dictionary.put("frs:informGenericDTASFContact", SpeechActLabel.Statement_non_opinion);
        dictionary.put("frs:obtainConfirmation", SpeechActLabel.Yes_No_Question);
        
        //CARITAS SCENARIO
        dictionary.put("pas:reactiveFlag", SpeechActLabel.Statement_non_opinion);
        
        dictionary.put("pas:confirmCommunication", SpeechActLabel.Yes_No_Question);
        dictionary.put("pas:confirmLanguage", SpeechActLabel.Yes_No_Question);
        dictionary.put("pas:obtainRequest", SpeechActLabel.Declarative_Yes_No_Question);
        
        dictionary.put("pas:informMessage", SpeechActLabel.Apology);
        
        dictionary.put("pas:informSimulation", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:informPersonNameIntro", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:obtainReadiness", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pas:informAuthority", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:informPersonName", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:obtainMatterConcern", SpeechActLabel.Wh_Question);
        
        dictionary.put("pas:obtainName", SpeechActLabel.Wh_Question);
        dictionary.put("pas:obtainSurname", SpeechActLabel.Wh_Question);
        dictionary.put("pas:obtainBirthDay", SpeechActLabel.Wh_Question);
        dictionary.put("pas:obtainBirthMonth", SpeechActLabel.Wh_Question);
        dictionary.put("pas:obtainBirthYear", SpeechActLabel.Wh_Question);
        
        dictionary.put("pas:informMatterConcern", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:obtainMatterConcernStatus", SpeechActLabel.Declarative_Yes_No_Question);
        dictionary.put("pas:informNeedAppointment", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:proposeTimeSlot", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:confirmTimeSlot", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pas:informAppointmentDocuments", SpeechActLabel.Yes_No_Question);
        dictionary.put("pas:obtainConfirmationDetailsAppointment", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pas:informContactPerson", SpeechActLabel.Yes_No_Question);
        dictionary.put("pas:obtainDetailsKnown", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pas:informContactPhone", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:informContactOtherInfo", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:obtainConfirmationContactDetails", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pas:informSimulationEnd", SpeechActLabel.Statement_non_opinion);
        
        dictionary.put("pas:informEnd", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:obtainConfirmationNormalClosing", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("welcome:informGenericCARITASContact", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pas:obtainConfirmation", SpeechActLabel.Yes_No_Question);
        
        //PRAKSIS
        dictionary.put("pps:confirmCommunication", SpeechActLabel.Yes_No_Question);
        dictionary.put("pps:confirmLanguage", SpeechActLabel.Yes_No_Question);
        dictionary.put("pps:obtainRequest", SpeechActLabel.Wh_Question);
        
        dictionary.put("pps:informMessage", SpeechActLabel.Apology);
        
        //dictionary.put("pps:obtainInterest", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informNeedInformation", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainCity", SpeechActLabel.Wh_Question);
        dictionary.put("pps:obtainFirstApplication", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informNeedRegistration", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informNeedSkype", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainKnowledgeSkypeCreation", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informDownload", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informCreateAccount", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informPlatforms", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainInternetConnection", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informNGOSupport", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informPRAKSISContact", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmationNGOSupport", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informTimeSlotSkypeID", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informTimeSlot", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informSkypeID", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informAccess", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmationSkypeUser", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informNeedContact", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informTelephoneNumber", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informEmail", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informAddress", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmationContactPRAKSIS", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informAskPersonalInfo", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informDetailedInfo", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:informPurpose", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmationSkypeProcess", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informConnection", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmationInternetConnection", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informEnd", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmationNormalClosing", SpeechActLabel.Yes_No_Question);
        
        dictionary.put("pps:informGenericPRAKSISContact", SpeechActLabel.Statement_non_opinion);
        dictionary.put("pps:obtainConfirmation", SpeechActLabel.Yes_No_Question);
    }

    public SpeechActLabel get(String slot) { return dictionary.get(slot); }

}
