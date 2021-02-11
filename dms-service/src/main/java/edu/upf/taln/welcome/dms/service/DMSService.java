package edu.upf.taln.welcome.dms.service;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.DMInput;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.LanguageConfiguration;
import edu.upf.taln.welcome.dms.commons.input.ServiceDescription;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;
import edu.upf.taln.welcome.dms.core.DeterministicPolicy;
import edu.upf.taln.welcome.dms.core.DialogueManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.json.JsonObject;
import javax.servlet.ServletConfig;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Dialogue manager service.
 */
@Path("/dms")
@Produces(MediaType.APPLICATION_JSON)
public class DMSService {

	private static final String SAMPLE_INPUT = "[ {\n" +
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

	private static final String SAMPLE_OUTPUT = "{\n" +
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

	private final DialogueManager manager;
	private final Document jsonldContextDoc;
	private final Logger logger = Logger.getLogger(DMSService.class.getName());

	@Context
	ServletConfig config;

	public DMSService() throws WelcomeException {
		manager = new DialogueManager(new DeterministicPolicy());
		try {
			Reader contextReader = new InputStreamReader(DMSService.class.getResourceAsStream("/welcome-context.jsonld"));
			jsonldContextDoc = DocumentParser.parse(com.apicatalog.jsonld.http.media.MediaType.JSON_LD, contextReader);
		} catch (Exception | JsonLdError ex) {
			logger.log(Level.SEVERE, "Failed to initialize service", ex);
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
									@ExampleObject(name = "Example",
											value = SAMPLE_INPUT)
							}
					)
			),
			responses = {
					@ApiResponse(description = "Dialogue moves are represented as a pair of a speech act and one or more slots taken from the edu.upf.taln.welcome.nlg.commons.input.",
							content = @Content(mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = DialogueMove.class),
									examples = {
											@ExampleObject(name = "Example",
													value = SAMPLE_OUTPUT)
									}
							))
			})

	/*
	 * Unmarshalls JSON-LD edu.upf.taln.welcome.nlg.commons.input to a POJO representations of a DIP frame, and passes it to the dialogue manager.
	 * The resulting dialogue moves are serialized back into JSON-LD and returned.
	 */
	public JsonNode realize_next_turn(@Parameter(description = "Dialogue edu.upf.taln.welcome.nlg.commons.input packages", required = true) JsonNode input) throws WelcomeException {
		try {
			StringReader inputReader = new StringReader(input.toString());
			Document inputDoc = DocumentParser.parse(com.apicatalog.jsonld.http.media.MediaType.JSON_LD, inputReader);

			JsonObject framed = JsonLd.frame(inputDoc, jsonldContextDoc)
					.ordered()
					.get();
			ObjectMapper mapper = new ObjectMapper();
			Frame dip = mapper.readValue(framed.toString(), Frame.class);
			logger.log(Level.INFO, "dip: " + dip.toString() + "\t" + dip.type);
			DialogueMove move = manager.map(dip);
			logger.log(Level.INFO, "edu.upf.taln.welcome.nlg.commons.output.moves " + move.toString());

			return mapper.valueToTree(move);
		} catch (JsonLdError | IOException | NullPointerException ex) {
			logger.log(Level.SEVERE, "Failed to determine next dialogue move", ex);
			throw new WelcomeException(ex);
		}
	}
}
