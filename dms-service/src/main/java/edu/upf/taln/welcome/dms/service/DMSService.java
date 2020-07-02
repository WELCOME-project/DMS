package edu.upf.taln.welcome.dms.service;

import java.util.ArrayList;
import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import edu.upf.taln.welcome.dms.commons.input.LanguageConfiguration;
import edu.upf.taln.welcome.dms.commons.input.ServiceDescription;
import edu.upf.taln.welcome.dms.commons.input.DMInput;
import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.output.DMOutput;
import edu.upf.taln.welcome.dms.commons.output.DMOutputData;
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
		responses = {
		        @ApiResponse(description = "The data needed to generate the next utterance.",
		        			content = @Content(schema = @Schema(implementation = DMOutput.class)
		        ))
	})
	public DMOutput realize_next_turn(
			@Parameter(description = "Container for dms input data.", required = true) DMInput input) throws WelcomeException {

        int turn = input.getMetadata().getDialogueTurn();
        DMOutput output = SampleResponses.generateResponse(turn);
        
		return output;
	}

}