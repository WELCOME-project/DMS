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
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#OpeningDIP\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIP\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIPId\" : [ {\n" +
			"    \"@value\" : \"0d7b802e-ba0d-470c-bb51-d4ec3653e371\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasDIPStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Incomplete\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasSlot\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmCommunication\"\n" +
			"  }, {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmLanguage\"\n" +
			"  }, {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainRequest\"\n" +
			"  } ]\n" +
			"}, {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmCommunication\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Template\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" +
			"    \"@value\" : \"yes\"\n" +
			"  } ]\n" +
			"}, {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confirmLanguage\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#decimal\",\n" +
			"    \"@value\" : \"0.22\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#EN\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Template\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" +
			"    \"@value\" : \"yes\"\n" +
			"  } ]\n" +
			"}, {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainRequest\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Template\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Pending\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" +
			"    \"@value\" : \"no\"\n" +
			"  } ]\n" +
			"} ]";

	private static final String OPENING_DIP_OUTPUT = "{\n" +
			"  \"speechActs\" : [ {\n" +
			"    \"@id\" : \"welcome:Open_Question_0\",\n" +
			"    \"@type\" : \"welcome:SpeechAct\",\n" +
			"    \"welcome:hasLabel\" : {\n" +
			"      \"@id\" : \"welcome:Open_Question\",\n" +
			"      \"@type\" : \"welcome:SpeechActLabel\"\n" +
			"    },\n" +
			"    \"welcome:hasSlot\" : {\n" +
			"      \"@id\" : \"welcome:obtainRequest\",\n" +
			"      \"@type\" : \"welcome:SystemDemand\",\n" +
			"      \"welcome:hasTemplate\" : null,\n" +
			"      \"welcome:hasInputRDFContents\" : \"{\\\"@id\\\":\\\"welcome:Unknown\\\"}\",\n" +
			"      \"welcome:hasStatus\" : [ {\n" +
			"        \"@id\" : \"welcome:Pending\"\n" +
			"      } ],\n" +
			"      \"welcome:hasNumberAttemps\" : 0,\n" +
			"      \"welcome:confidenceScore\" : 0.0,\n" +
			"      \"welcome:isOptional\" : false\n" +
			"    }\n" +
			"  } ],\n" +
			"  \"@id\" : \"welcome:move_0\",\n" +
			"  \"@type\" : \"welcome:DialogueMove\"\n" +
			"}";

	private static final String OBTAIN_STATUS_DIP_INPUT = "[ {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#ObtainRegistrationStatus\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIP\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIPId\" : [ {\n" +
			"    \"@value\" : \"2bdb0fc7-533b-4791-ba25-b0fa5a67a56c\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasDIPStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Incomplete\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasSlot\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainName\"\n" +
			"  }, {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainStatus\"\n" +
			"  } ]\n" +
			"}, {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainName\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Name\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Completed\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" +
			"    \"@value\" : \"yes\"\n" +
			"  } ]\n" +
			"}, {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainStatus\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemDemand\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Template\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Pending\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" +
			"    \"@value\" : \"no\"\n" +
			"  } ]\n" +
			"} ]";

	private static final String OBTAIN_STATUS_DIP_OUTPUT = "{\n" +
			"  \"speechActs\" : [ {\n" +
			"    \"@id\" : \"welcome:Wh_Question_1\",\n" +
			"    \"@type\" : \"welcome:SpeechAct\",\n" +
			"    \"welcome:hasLabel\" : {\n" +
			"      \"@id\" : \"welcome:Wh_Question\",\n" +
			"      \"@type\" : \"welcome:SpeechActLabel\"\n" +
			"    },\n" +
			"    \"welcome:hasSlot\" : {\n" +
			"      \"@id\" : \"welcome:obtainStatus\",\n" +
			"      \"@type\" : [ \"welcome:SystemDemand\" ],\n" +
			"      \"welcome:hasTemplate\" : null,\n" +
			"      \"welcome:hasInputRDFContents\" : [ \"{\\\"@id\\\":\\\"welcome:Unknown\\\"}\" ],\n" +
			"      \"welcome:hasStatus\" : [ {\n" +
			"        \"@id\" : \"welcome:Pending\"\n" +
			"      } ],\n" +
			"      \"welcome:hasNumberAttemps\" : 0,\n" +
			"      \"welcome:confidenceScore\" : 0.0,\n" +
			"      \"welcome:isOptional\" : false\n" +
			"    }\n" +
			"  } ],\n" +
			"  \"@id\" : \"welcome:move_1\",\n" +
			"  \"@type\" : \"welcome:DialogueMove\"\n" +
			"}";

	private static final String PROPOSE_SERVICE_DIP_INPUT = "[ {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#ProposeService\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIP\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#DIPId\" : [ {\n" +
			"    \"@value\" : \"52f46f10-96ae-4aee-a2e5-dc6dbea9b524\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasDIPStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Incomplete\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasSlot\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainInterest\"\n" +
			"  } ]\n" +
			"}, {\n" +
			"  \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#obtainInterest\",\n" +
			"  \"@type\" : [ \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#SystemInfo\" ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#confidenceScore\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasInputRDFContents\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Unknown\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasNumberAttempts\" : [ {\n" +
			"    \"@type\" : \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
			"    \"@value\" : \"0\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasOntologyType\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Template\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#hasStatus\" : [ {\n" +
			"    \"@id\" : \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#Pending\"\n" +
			"  } ],\n" +
			"  \"https://raw.githubusercontent.com/gtzionis/WelcomeOntology/main/welcome.ttl#isOptional\" : [ {\n" +
			"    \"@value\" : \"no\"\n" +
			"  } ]\n" +
			"} ]";

	private static final String PROPOSE_SERVICE_DIP_OUTPUT = "{\n" +
			"  \"speechActs\" : [ {\n" +
			"    \"@id\" : \"welcome:Yes_No_Question_2\",\n" +
			"    \"@type\" : \"welcome:SpeechAct\",\n" +
			"    \"welcome:hasLabel\" : {\n" +
			"      \"@id\" : \"welcome:Yes_No_Question\",\n" +
			"      \"@type\" : \"welcome:SpeechActLabel\"\n" +
			"    },\n" +
			"    \"welcome:hasSlot\" : {\n" +
			"      \"@id\" : \"welcome:obtainInterest\",\n" +
			"      \"@type\" : [ \"welcome:SystemInfo\" ],\n" +
			"      \"welcome:hasTemplate\" : null,\n" +
			"      \"welcome:hasInputRDFContents\" : [ \"{\\\"@id\\\":\\\"welcome:Unknown\\\"}\" ],\n" +
			"      \"welcome:hasStatus\" : [ {\n" +
			"        \"@id\" : \"welcome:Pending\"\n" +
			"      } ],\n" +
			"      \"welcome:hasNumberAttemps\" : 0,\n" +
			"      \"welcome:confidenceScore\" : 0.0,\n" +
			"      \"welcome:isOptional\" : false\n" +
			"    }\n" +
			"  } ],\n" +
			"  \"@id\" : \"welcome:move_2\",\n" +
			"  \"@type\" : \"welcome:DialogueMove\"\n" +
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
									@ExampleObject(name = "Example using Obtain Registration Status DIP", value = OBTAIN_STATUS_DIP_INPUT),
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
											@ExampleObject(name = "Example using Obtain Registration Status DIP", value = OBTAIN_STATUS_DIP_OUTPUT),
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
