package edu.upf.taln.welcome.dms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.DMInput;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.LanguageConfiguration;
import edu.upf.taln.welcome.dms.commons.input.ServiceDescription;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.utils.JsonLDUtils;
import edu.upf.taln.welcome.dms.core.DeterministicPolicy;
import edu.upf.taln.welcome.dms.core.DialogueManager;


/**
 * Dialogue manager service.
 */
@Path("/dms")
@Produces(MediaType.APPLICATION_JSON)
public class DMSService {

	private static final String OPENING_DIP_INPUT = "[ {\n" + 
			"  \"@id\" : \"_:node1fb87e8b5x1\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Yes\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Boolean\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb87e8b5x2\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"English\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Language\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb87e8b5x3\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"English\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb87e8b5x4\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Opening\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIP\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIPId\" : [ {\n" + 
			"    \"@value\" : \"1626277469451\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasSlot\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmCommunication\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmLanguage\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainRequest\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isCurrentDIP\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmCommunication\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@id\" : \"http://www.w3.org/2001/XMLSchema#boolean\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb87e8b5x1\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TConfirmCommunication\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmLanguage\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb87e8b5x2\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Language\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb87e8b5x3\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TConfirmLanguage\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainRequest\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#ServiceRequest\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Pending\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainMatterConcern\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"} ]";

	private static final String OPENING_DIP_OUTPUT = "{\n" + 
			"  \"@context\" : {\n" + 
			"    \"w3c\" : \"http://www.w3.org/2001/XMLSchema#\",\n" + 
			"    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",\n" + 
			"    \"welcome\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#\",\n" + 
			"    \"daml\" : \"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl#\",\n" + 
			"    \"rdf:subject\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"rdf:predicate\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasSlot\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasOntologyType\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasStatus\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:isCurrentDIP\" : {\n" + 
			"      \"@type\" : \"w3c:boolean\"\n" + 
			"    },\n" + 
			"    \"welcome:hasDIPStatus\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasInputRDFContents\" : {\n" + 
			"      \"@container\" : \"@set\"\n" + 
			"    },\n" + 
			"    \"welcome:hasTCNAnswer\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:confidenceScore\" : {\n" + 
			"      \"@type\" : \"w3c:float\"\n" + 
			"    },\n" + 
			"    \"welcome:hasNumberAttempts\" : {\n" + 
			"      \"@type\" : \"w3c:int\"\n" + 
			"    },\n" + 
			"    \"welcome:isOptional\" : {\n" + 
			"      \"@type\" : \"w3c:boolean\"\n" + 
			"    },\n" + 
			"    \"welcome:hasSpeechActs\" : {\n" + 
			"      \"@container\" : \"@list\"\n" + 
			"    }\n" + 
			"  },\n" + 
			"  \"@id\" : \"move_88\",\n" + 
			"  \"@type\" : \"welcome:DialogueMove\",\n" + 
			"  \"welcome:hasSpeechActs\" : [ {\n" + 
			"    \"@id\" : \"Open_Question_88\",\n" + 
			"    \"@type\" : \"welcome:SpeechAct\",\n" + 
			"    \"welcome:hasLabel\" : \"Open_Question\",\n" + 
			"    \"welcome:hasSlot\" : {\n" + 
			"      \"@id\" : \"welcome:obtainRequest\",\n" + 
			"      \"@type\" : null,\n" + 
			"      \"welcome:hasTemplateId\" : \"TObtainMatterConcern\",\n" + 
			"      \"welcome:hasOntologyType\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#ServiceRequest\",\n" + 
			"      \"welcome:hasInputRDFContents\" : [ {\n" + 
			"        \"@id\" : \"welcome:Unknown\"\n" + 
			"      } ],\n" + 
			"      \"welcome:hasStatus\" : \"welcome:Pending\",\n" + 
			"      \"welcome:hasNumberAttemps\" : 0,\n" + 
			"      \"welcome:confidenceScore\" : 1.0,\n" + 
			"      \"welcome:isOptional\" : false\n" + 
			"    }\n" + 
			"  } ]\n" + 
			"}";

	private static final String FILL_PERSONAL_INFO_DIP_INPUT = "[ {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x100\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Yes\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#NotificationPreferences\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x101\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"No\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SMSNotifications\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x102\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Yes\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#EmailNotifications\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x82\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Doe\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#FirstSurname\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x83\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Morocco\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDCountry\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x84\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"John\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Name\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x85\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"John\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Name\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x86\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Doe\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#FirstSurname\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x87\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Doe\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#FirstSurname\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x88\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Khalili\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SecondSurname\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x89\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"KJ9284014\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDNumber\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x90\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Passport\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDType\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x91\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Morocco\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDCountry\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x92\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Male\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Gender\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x93\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"1996-06-19\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Birthday\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x94\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Morocco\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#CountryOfBirth\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x95\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Rabat\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#CityOfBirth\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x96\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"Morocco\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Nationality\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x97\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"30231027435\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Landline\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x98\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"6999998456\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#MobilePhone\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"_:node1fb89p0u1x99\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"example.address@welcome.com\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Email\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#FillFormPersonalInfo\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIP\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIPId\" : [ {\n" + 
			"    \"@value\" : \"2222222222\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasSlot\" : [ {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainName\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#confirmFirstSurname\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainFirstSurname\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainSecondSurname\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainIDNumber\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainIDType\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainIDCountry\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainGender\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainBirthday\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainCountryOfBirth\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainCityOfBirth\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainNationality\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainLandline\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainMobilePhone\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainEmail\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainNotificationPreferences\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainSMSNotifications\"\n" + 
			"  }, {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainEMailNotifications\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isCurrentDIP\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#confirmFirstSurname\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"0.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x82\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"http://www.w3.org/2001/XMLSchema#boolean\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Pending\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TConfirmSurname\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainBirthday\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Birthday\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x93\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainBirthDay\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainCityOfBirth\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#CityOfBirth\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x95\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainLocationOfBirth\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainCountryOfBirth\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#CountryOfBirth\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x94\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainLocationOfBirth\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainEMailNotifications\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#EmailNotifications\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x102\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainEMailNotifications\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainEmail\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Email\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x99\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainEmail\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainFirstSurname\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"0.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x86\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#FirstSurname\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x87\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainName\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainGender\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Gender\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x92\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainGender\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainIDCountry\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDCountry\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x91\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainIDCountry\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainIDNumber\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDNumber\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x89\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainIDNumber\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainIDType\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#IDType\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x90\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainIDType\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainLandline\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Landline\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x97\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainLandline\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainMobilePhone\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#MobilePhone\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x98\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainMobilePhone\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainName\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x84\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Name\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x85\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainName\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainNationality\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x83\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Nationality\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x96\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainNationality\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainNotificationPreferences\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#NotificationPreferences\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x100\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainNotificationPreferences\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainSMSNotifications\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SMSNotifications\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x101\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainSMSNotifications\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainSecondSurname\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"1.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SecondSurname\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"_:node1fb89p0u1x88\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainSecondSurname\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"} ]";

	private static final String FILL_PERSONAL_INFO_DIP_OUTPUT = "{\n" + 
			"  \"@context\" : {\n" + 
			"    \"w3c\" : \"http://www.w3.org/2001/XMLSchema#\",\n" + 
			"    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",\n" + 
			"    \"welcome\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#\",\n" + 
			"    \"daml\" : \"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl#\",\n" + 
			"    \"rdf:subject\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"rdf:predicate\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasSlot\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasOntologyType\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasStatus\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:isCurrentDIP\" : {\n" + 
			"      \"@type\" : \"w3c:boolean\"\n" + 
			"    },\n" + 
			"    \"welcome:hasDIPStatus\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasInputRDFContents\" : {\n" + 
			"      \"@container\" : \"@set\"\n" + 
			"    },\n" + 
			"    \"welcome:hasTCNAnswer\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:confidenceScore\" : {\n" + 
			"      \"@type\" : \"w3c:float\"\n" + 
			"    },\n" + 
			"    \"welcome:hasNumberAttempts\" : {\n" + 
			"      \"@type\" : \"w3c:int\"\n" + 
			"    },\n" + 
			"    \"welcome:isOptional\" : {\n" + 
			"      \"@type\" : \"w3c:boolean\"\n" + 
			"    },\n" + 
			"    \"welcome:hasSpeechActs\" : {\n" + 
			"      \"@container\" : \"@list\"\n" + 
			"    }\n" + 
			"  },\n" + 
			"  \"@id\" : \"move_52\",\n" + 
			"  \"@type\" : \"welcome:DialogueMove\",\n" + 
			"  \"welcome:hasSpeechActs\" : [ {\n" + 
			"    \"@id\" : \"Yes_No_Question_52\",\n" + 
			"    \"@type\" : \"welcome:SpeechAct\",\n" + 
			"    \"welcome:hasLabel\" : \"Yes_No_Question\",\n" + 
			"    \"welcome:hasSlot\" : {\n" + 
			"      \"@id\" : \"frs:confirmFirstSurname\",\n" + 
			"      \"@type\" : null,\n" + 
			"      \"welcome:hasTemplateId\" : \"TConfirmSurname\",\n" + 
			"      \"welcome:hasOntologyType\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"      \"welcome:hasInputRDFContents\" : [ {\n" + 
			"        \"@type\" : \"rdf:Statement\",\n" + 
			"        \"rdf:subject\" : \"welcome:FirstSurname\",\n" + 
			"        \"rdf:predicate\" : \"welcome:hasValue\",\n" + 
			"        \"rdf:object\" : {\n" + 
			"          \"@id\" : null,\n" + 
			"          \"@type\" : null,\n" + 
			"          \"@value\" : \"Doe\"\n" + 
			"        }\n" + 
			"      } ],\n" + 
			"      \"welcome:hasStatus\" : \"welcome:Pending\",\n" + 
			"      \"welcome:hasNumberAttemps\" : 0,\n" + 
			"      \"welcome:confidenceScore\" : 0.0,\n" + 
			"      \"welcome:isOptional\" : false\n" + 
			"    }\n" + 
			"  } ]\n" + 
			"}";

	private static final String PROPOSE_SERVICE_DIP_INPUT = "[ {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#ProposeService\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIP\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIPId\" : [ {\n" + 
			"    \"@value\" : \"1626683426019\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasSlot\" : [ {\n" + 
			"    \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainInterest\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isCurrentDIP\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"true\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"http://localhost:8080/services/FirstReceptionService.owl#obtainInterest\",\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#float\",\n" + 
			"    \"@value\" : \"0.0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#reifiedInterest\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" + 
			"    \"@value\" : \"0\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#anyURI\",\n" + 
			"    \"@value\" : \"http://www.w3.org/2001/XMLSchema#boolean\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Pending\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTCNAnswer\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasTemplateId\" : [ {\n" + 
			"    \"@value\" : \"TObtainInterestInform\"\n" + 
			"  } ],\n" + 
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" + 
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"    \"@value\" : \"false\"\n" + 
			"  } ]\n" + 
			"}, {\n" + 
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#reifiedInterest\",\n" + 
			"  \"@type\" : [ \"http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement\" ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#object\" : [ {\n" + 
			"    \"@value\" : \"First Reception Service in Catalonia\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasValue\"\n" + 
			"  } ],\n" + 
			"  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#subject\" : [ {\n" + 
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#ServiceName\"\n" + 
			"  } ]\n" + 
			"} ]";

	private static final String PROPOSE_SERVICE_DIP_OUTPUT = "{\n" + 
			"  \"@context\" : {\n" + 
			"    \"w3c\" : \"http://www.w3.org/2001/XMLSchema#\",\n" + 
			"    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",\n" + 
			"    \"welcome\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#\",\n" + 
			"    \"daml\" : \"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl#\",\n" + 
			"    \"rdf:subject\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"rdf:predicate\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasSlot\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasOntologyType\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasStatus\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:isCurrentDIP\" : {\n" + 
			"      \"@type\" : \"w3c:boolean\"\n" + 
			"    },\n" + 
			"    \"welcome:hasDIPStatus\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:hasInputRDFContents\" : {\n" + 
			"      \"@container\" : \"@set\"\n" + 
			"    },\n" + 
			"    \"welcome:hasTCNAnswer\" : {\n" + 
			"      \"@type\" : \"@id\"\n" + 
			"    },\n" + 
			"    \"welcome:confidenceScore\" : {\n" + 
			"      \"@type\" : \"w3c:float\"\n" + 
			"    },\n" + 
			"    \"welcome:hasNumberAttempts\" : {\n" + 
			"      \"@type\" : \"w3c:int\"\n" + 
			"    },\n" + 
			"    \"welcome:isOptional\" : {\n" + 
			"      \"@type\" : \"w3c:boolean\"\n" + 
			"    },\n" + 
			"    \"welcome:hasSpeechActs\" : {\n" + 
			"      \"@container\" : \"@list\"\n" + 
			"    }\n" + 
			"  },\n" + 
			"  \"@id\" : \"move_89\",\n" + 
			"  \"@type\" : \"welcome:DialogueMove\",\n" + 
			"  \"welcome:hasSpeechActs\" : [ {\n" + 
			"    \"@id\" : \"Yes_No_Question_89\",\n" + 
			"    \"@type\" : \"welcome:SpeechAct\",\n" + 
			"    \"welcome:hasLabel\" : \"Yes_No_Question\",\n" + 
			"    \"welcome:hasSlot\" : {\n" + 
			"      \"@id\" : \"frs:obtainInterest\",\n" + 
			"      \"@type\" : null,\n" + 
			"      \"welcome:hasTemplateId\" : \"TObtainInterestInform\",\n" + 
			"      \"welcome:hasOntologyType\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\n" + 
			"      \"welcome:hasInputRDFContents\" : [ {\n" + 
			"        \"@id\" : \"welcome:reifiedInterest\",\n" + 
			"        \"@type\" : \"rdf:Statement\",\n" + 
			"        \"rdf:subject\" : \"welcome:ServiceName\",\n" + 
			"        \"rdf:predicate\" : \"welcome:hasValue\",\n" + 
			"        \"rdf:object\" : {\n" + 
			"          \"@id\" : null,\n" + 
			"          \"@type\" : null,\n" + 
			"          \"@value\" : \"First Reception Service in Catalonia\"\n" + 
			"        }\n" + 
			"      } ],\n" + 
			"      \"welcome:hasStatus\" : \"welcome:Pending\",\n" + 
			"      \"welcome:hasNumberAttemps\" : 0,\n" + 
			"      \"welcome:confidenceScore\" : 0.0,\n" + 
			"      \"welcome:isOptional\" : false\n" + 
			"    }\n" + 
			"  } ]\n" + 
			"}";

	private final Logger logger = Logger.getLogger(DMSService.class.getName());

    private final DialogueManager manager;
    private URL resultContextFile;
    private URL contextFile;
    
	@Context
	ServletConfig config;

	public DMSService() throws WelcomeException {
		try {
    		manager = new DialogueManager(new DeterministicPolicy());
            
            contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed_alt.jsonld");
            if (contextFile == null) {
                throw new WelcomeException("JSONLD context file not found!");
            }
            
            resultContextFile = JsonLDUtils.class.getResource("/welcome-nlg-compacted.jsonld");
        
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unexpected error! Failed to initialize service", ex);
			throw new WelcomeException(ex);
        }
	}

	@GET
	@Path("/description")
	@Operation(summary = "Retrieves the available configurations.",
			description = "Returns the list of available configurations.",
			responses = {
					@ApiResponse(description = "The available configurations",
							content = @Content(schema = @Schema(implementation = ServiceDescription.class)
							))
			})
	public ServiceDescription getAvailableConfigurations() throws WelcomeException {

		List<LanguageConfiguration> configList = new ArrayList<>();

		ServiceDescription description = new ServiceDescription();
		description.setConfigurations(configList);

		return description;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Determines the system's next dialogue moves given an edu.upf.taln.welcome.nlg.commons.input dialogue edu.upf.taln.welcome.nlg.commons.input package.",
			description = "Returns the data needed to generate the next utterance.",
			requestBody = @RequestBody(
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = DMInput.class),
							examples = {
									@ExampleObject(name = "Example using Opening DIP", value = OPENING_DIP_INPUT),
									@ExampleObject(name = "Example using Fill Personal Info DIP", value = FILL_PERSONAL_INFO_DIP_INPUT),
									@ExampleObject(name = "Example using Propose First Reception Service DIP", value = PROPOSE_SERVICE_DIP_INPUT)
							}
					)
			),
			responses = {
					@ApiResponse(description = "Dialogue moves are represented as a pair of a speech act and one or more slots taken from the edu.upf.taln.welcome.nlg.commons.input.",
							content = @Content(mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = DialogueMove.class),
									examples = {
											@ExampleObject(name = "Example using Opening DIP", value = OPENING_DIP_OUTPUT),
											@ExampleObject(name = "Example using Obtain Registration Status DIP", value = FILL_PERSONAL_INFO_DIP_OUTPUT),
											@ExampleObject(name = "Example using Propose First Reception Service DIP", value = PROPOSE_SERVICE_DIP_OUTPUT)
									}
							))
			})

	/*
	 * Unmarshalls JSON-LD edu.upf.taln.welcome.nlg.commons.input to a POJO representations of a DIP frame, and passes it to the dialogue manager.
	 * The resulting dialogue moves are serialized back into JSON-LD and returned.
	 */
	public JsonNode realizeNextTurn(@Parameter(description = "Dialogue edu.upf.taln.welcome.nlg.commons.input packages", required = true) JsonNode input) throws WelcomeException {
		try {
            
			Frame dip = JsonLDUtils.readFrame(input, contextFile);
			logger.log(Level.INFO, "dip: {0}\t{1}", new Object[]{dip.toString(), dip.type});
            
			return realizeNextTurn(dip);
            
		} catch (WelcomeException ex) {
			logger.log(Level.SEVERE, "Failed to determine next dialogue move", ex);
			throw ex;
            
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unexpected error! Failed to determine next dialogue move", ex);
			throw new WelcomeException(ex);
		}
	}
	
	protected JsonNode realizeNextTurn(Frame dip) throws WelcomeException {
		DialogueMove move = manager.map(dip);
		logger.log(Level.INFO, "edu.upf.taln.welcome.nlg.commons.output.moves {0}", move.toString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.valueToTree(move);
        
        JsonNode mergedResult = JsonLDUtils.mergeResultContext(result, resultContextFile);
        
		return mergedResult;
	}
	
	@GET
	@Path("/status")
	@Operation(summary = "Retrieve the services status.",
		description = "Returns a status description of the service.",
		responses = {
		        @ApiResponse(description = "The services status.",
		        			content = @Content(schema = @Schema(implementation = StatusOutput.class)
		        ))
	})
	public StatusOutput getStatus() throws WelcomeException {
		ServletContext application = config.getServletContext();
		String build;
		try {
			build = new String(application.getResourceAsStream("META-INF/MANIFEST.MF").readAllBytes());
		} catch (IOException e) {
			throw new WelcomeException();
		}
		return new StatusOutput(build);
	}
	
	@GET
	@Path("/status/log")
	@Operation(summary = "Retrieve the service log.",
		description = "Returns a specific amount of log messages.",
		responses = {
		        @ApiResponse(description = "The log messages.",
		        			content = @Content(schema = @Schema(implementation = StatusLogOutput.class)
		        ))
	})
	public StatusLogOutput getLog(@QueryParam("limit") int limit) throws WelcomeException {
		return new StatusLogOutput();
	}

}
