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


/**
 * Analyze text and return results in JSON format
 * 
 * @author jens.grivolla
 * 
 */
@Path("/api/dms")
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
		description = "Returns the list of available configurations, it is, the list of languages and available analysis module for each one.",
		responses = {
		        @ApiResponse(description = "The available configurations",
		        			content = @Content(schema = @Schema(implementation = ServiceDescription.class)
		        ))
	})
	public ServiceDescription getAvailableConfigurations() throws WelcomeException {

        LanguageConfiguration es_config = new LanguageConfiguration();
		es_config.setLanguage("es");

		List<LanguageConfiguration> configList = new ArrayList<>();
		configList.add(es_config);

		ServiceDescription description = new ServiceDescription();
		description.setConfigurations(configList);
		
		return description;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Performs a deep syntactic analysis of the input data.",
		description = "Returns the result of the deep syntatic analysis, it is, a predicate-argument structure.",
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = DMOutput.class)
		        ))
	})
	public DMOutput analyze(
			@Parameter(description = "Container for analysis input data.", required = true) DMInput container) throws WelcomeException {

        DMOutput output = new DMOutput();
        output.setConll(container.getData().getConll());
        
		return output;
	}

}