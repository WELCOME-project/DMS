package edu.upf.taln.welcome.dms.service;

import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfSubject;
import com.fasterxml.jackson.core.JsonProcessingException;

import edu.upf.taln.welcome.dms.commons.input.LanguageConfiguration;
import edu.upf.taln.welcome.dms.commons.input.ServiceDescription;
import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.output.DMOutput;
import edu.upf.taln.welcome.dms.utils.SampleResponses;


/**
 * Analyze text and return results in JSON format
 * 
 * @author jens.grivolla
 * 
 */
@Path("/dms")
@Produces(MediaType.APPLICATION_JSON)
public class DMSService {

	Logger logger = Logger.getLogger(DMSService.class.getName());
	
	private static final String SAMPLE_INPUT_TURN0 = "[{\n" +
            "  \"@id\": \"http://www.semanticweb.org/welcome#handshaking_1\",\n" +
            "  \"@type\": [\"http://www.semanticweb.org/welcome#Handshaking\"],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPLanguage\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#EN\"\n" +
            "    }],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPLanguageScore\": [{\n" +
            "      \"@type\": \"http://www.w3.org/2001/XMLSchema#integer\",\n" +
            "      \"@value\": \"22\"\n" +
            "    }]\n" +
            "}]";


	private static final String SAMPLE_INPUT_TURN1 = "[{\n" +
            "  \"@id\": \"http://www.semanticweb.org/welcome#request_info_1_1\",\n" +
            "  \"@type\": [\"http://www.semanticweb.org/welcome#RequestInfo\"],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPCountryOfOrigin\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#empty\"\n" +
            "    }],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPName\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#empty\"\n" +
            "    }],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPResidenceAddressCity\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#empty\"\n" +
            "    }],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPResidenceAddressNumber\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#empty\"\n" +
            "    }],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPResidenceAddressStreet\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#empty\"\n" +
            "    }],\n" +
            "  \"http://www.semanticweb.org/welcome#hasDIPTimeArrivalCurrentResidence\": [{\n" +
            "      \"@id\": \"http://www.semanticweb.org/welcome#empty\"\n" +
            "    }]\n" +
            "}]";    
    
	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;

	public DMSService() throws WelcomeException {
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
	@Operation(summary = "Determines the data needed to generate the next turn utterance given the input data.",
		description = "Returns the data needed to generate the next utterance.",
		requestBody = @RequestBody(
				content = @Content(mediaType = "application/json",
								schema = @Schema(implementation = JsonNode.class),
								examples = {
									@ExampleObject(name = "Turn 0",
											value = SAMPLE_INPUT_TURN0),
									@ExampleObject(name = "Turn 1",
											value = SAMPLE_INPUT_TURN1)
								}
				)
			),
		responses = {
		        @ApiResponse(description = "The data needed to generate the next utterance.",
		        			content = @Content(schema = @Schema(implementation = DMOutput.class)
		        ))
	})
	public DMOutput realize_next_turn(
			@Parameter(description = "Container for dms input data.", required = true) JsonNode input) throws WelcomeException {

        int turn = 1;
        if (input.size() == 0) {
            turn = 1;
            
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                StringReader sReader = new StringReader(mapper.writeValueAsString(input));
                
                Document doc = DocumentParser.parse(com.apicatalog.jsonld.http.media.MediaType.JSON_LD, sReader);
                RdfDataset rdf = JsonLd.toRdf(doc).get();
                
                List<RdfNQuad> triples = rdf.toList();
                RdfNQuad first = triples.get(0);
                RdfSubject subject = first.getSubject();
                System.out.println(subject.toString());
                
                switch(subject.toString()) {
                    case "http://www.semanticweb.org/welcome#handshaking_1":
                        turn = 1;
                        break;
                    case "http://www.semanticweb.org/welcome#request_info_1_1":
                        turn = 3;
                        break;
                    case "http://www.semanticweb.org/welcome#request_info_1_2":
                        turn = 5;
                        break;
                    case "http://www.semanticweb.org/welcome#catalan_language":
                        turn = 7;
                        break;
                    default:
                        turn = 1;
                        break;
                }
                
            } catch (JsonLdError | JsonProcessingException ex) {
                logger.log(Level.SEVERE, null, ex);
                throw new WelcomeException(ex);
            }
        }

        DMOutput output = SampleResponses.generateResponse(turn);
        
		return output;
	}

}