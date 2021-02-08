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
import edu.upf.taln.welcome.dms.commons.output.DMOutput;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
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

	private static final String SAMPLE_INPUT = "[\n" +
			"  {\n" +
			"    \"@id\": \"http://www.semanticweb.org/welcome#OpeningDIP\",\n" +
			"    \"@type\": [\n" +
			"      \"http://www.semanticweb.org/welcome#DIP\"\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#hasSlot\": [\n" +
			"      {\n" +
			"        \"@id\": \"http://www.semanticweb.org/welcome#confirmCommunication\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"@id\": \"http://www.semanticweb.org/welcome#confirmLanguage\"\n" +
			"      },\n" +
			"      {\n" +
			"        \"@id\": \"http://www.semanticweb.org/welcome#obtainRequest\"\n" +
			"      }\n" +
			"    ]\n" +
			"  },\n" +
			"  {\n" +
			"    \"@id\": \"http://www.semanticweb.org/welcome#confirmCommunication\",\n" +
			"    \"@type\": [\n" +
			"      \"http://www.semanticweb.org/welcome#SystemDemand\"\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#confidenceScore\": 0.0,\n" +
			"    \"http://www.semanticweb.org/welcome#hasInputRDFContents\": null,\n" +
			"    \"http://www.semanticweb.org/welcome#hasNumberAttemps\": 0,\n" +
			"    \"http://www.semanticweb.org/welcome#hasTemplate\": {\n" +
			"      \"@id\": \"http://www.semanticweb.org/welcome#TemplateConfirmCommunication\"\n" +
			"    },\n" +
			"    \"http://www.semanticweb.org/welcome#hasStatus\": [\n" +
			"      {\n" +
			"        \"@id\": \"http://www.semanticweb.org/welcome#Completed\"\n" +
			"      }\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#isOptional\": true\n" +
			"  },\n" +
			"  {\n" +
			"    \"@id\": \"http://www.semanticweb.org/welcome#confirmLanguage\",\n" +
			"    \"@type\": [\n" +
			"      \"http://www.semanticweb.org/welcome#SystemDemand\"\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#confidenceScore\": 1.0,\n" +
			"    \"http://www.semanticweb.org/welcome#hasInputRDFContents\": null,\n" +
			"    \"http://www.semanticweb.org/welcome#hasNumberAttemps\": 0,\n" +
			"    \"http://www.semanticweb.org/welcome#hasTemplate\": {\n" +
			"      \"@id\": \"http://www.semanticweb.org/welcome#TemplateConfirmLanguage\"\n" +
			"    },\n" +
			"    \"http://www.semanticweb.org/welcome#hasStatus\": [\n" +
			"      {\n" +
			"        \"@id\": \"http://www.semanticweb.org/welcome#Completed\"\n" +
			"      }\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#isOptional\": true\n" +
			"  },\n" +
			"  {\n" +
			"    \"@id\": \"http://www.semanticweb.org/welcome#obtainRequest\",\n" +
			"    \"@type\": [\n" +
			"      \"http://www.semanticweb.org/welcome#SystemDemand\"\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#confidenceScore\": 0.0,\n" +
			"    \"http://www.semanticweb.org/welcome#hasInputRDFContents\": null,\n" +
			"    \"http://www.semanticweb.org/welcome#hasNumberAttemps\": 0,\n" +
			"    \"http://www.semanticweb.org/welcome#hasTemplate\": {\n" +
			"      \"@id\": \"http://www.semanticweb.org/welcome#TemplateObtainRequest\"\n" +
			"    },\n" +
			"    \"http://www.semanticweb.org/welcome#hasStatus\": [\n" +
			"      {\n" +
			"        \"@id\": \"http://www.semanticweb.org/welcome#Pending\"\n" +
			"      }\n" +
			"    ],\n" +
			"    \"http://www.semanticweb.org/welcome#isOptional\": false\n" +
			"  }\n" +
			"]";

	private static final String SAMPLE_OUTPUT = "[ {\n" +
			"  \"welcome:speechAct\" : {\n" +
			"    \"@id\" : \"welcome:Open_Question\",\n" +
			"    \"@type\" : \"welcome:SpeechAct\"\n" +
			"  },\n" +
			"  \"welcome:slot\" : {\n" +
			"    \"@id\" : \"welcome:obtainRequest\",\n" +
			"    \"@type\" : \"welcome:SystemDemand\",\n" +
			"    \"welcome:hasTemplate\" : {\n" +
			"      \"@id\" : \"welcome:TemplateObtainRequest\"\n" +
			"    },\n" +
			"    \"welcome:hasInputRDFContents\" : null,\n" +
			"    \"welcome:hasStatus\" : [ {\n" +
			"      \"@id\" : \"welcome:Pending\"\n" +
			"    } ],\n" +
			"    \"welcome:hasNumberAttempts\" : 0,\n" +
			"    \"welcome:confidenceScore\" : 0.0,\n" +
			"    \"welcome:isOptional\" : false\n" +
			"  }\n" +
			"} ]";

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
	@Operation(summary = "Determines the system's next dialogue moves given an input dialogue input package.",
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
					@ApiResponse(description = "Dialogue moves are represented as a pair of a speech act and one or more slots taken from the input.",
							content = @Content(mediaType = MediaType.APPLICATION_JSON,
									schema = @Schema(implementation = DMOutput.class),
									examples = {
											@ExampleObject(name = "Example",
													value = SAMPLE_OUTPUT)
									}
							))
			})

	/*
	 * Unmarshalls JSON-LD input to a POJO-based representations of a DIP frame, and passes it to the dialogue manager.
	 * The resulting dialogue moves are serialized back into JSON-LD and returned.
	 */
	public JsonNode realize_next_turn(@Parameter(description = "Dialogue input packages", required = true) JsonNode input) throws WelcomeException {
		try {
			StringReader inputReader = new StringReader(input.toString());
			Document inputDoc = DocumentParser.parse(com.apicatalog.jsonld.http.media.MediaType.JSON_LD, inputReader);
			JsonObject framed = JsonLd.frame(inputDoc, jsonldContextDoc)
					.ordered()
					.get();
			ObjectMapper mapper = new ObjectMapper();
			Frame dip = mapper.readValue(framed.toString(), Frame.class);
			logger.log(Level.INFO, "dip: "+dip.toString()+"\t"+dip.type);
			List<DialogueMove> moves = manager.map(dip);
			DMOutput output = new DMOutput();
			logger.log(Level.INFO, "output.moves: "+output.moves.toString());
			output.moves.addAll(moves);

			return mapper.valueToTree(output.moves);
		} catch (JsonLdError | IOException | NullPointerException ex) {
			logger.log(Level.SEVERE, "Failed to determine next dialogue move", ex);
			throw new WelcomeException(ex);
		}
	}
}
