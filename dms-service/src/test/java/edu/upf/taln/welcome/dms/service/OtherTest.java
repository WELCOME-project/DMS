package edu.upf.taln.welcome.dms.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.utils.JsonLDUtils;

public class OtherTest {
	private final Logger logger = Logger.getLogger(OtherTest.class.getName());

    
	private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .writerWithDefaultPrettyPrinter();
	
	private void error_management(Response response) throws WelcomeException {
        String responseStr = response.readEntity(String.class);
        ObjectMapper om = new ObjectMapper()
        		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {

        	WelcomeException we;
            try {
            	we = om.readValue(responseStr, WelcomeException.class);
            } catch (Exception e) {
                try {
                    Exception newEx = om.readValue(responseStr, Exception.class);
                    we = new WelcomeException(newEx.getMessage(), newEx);

                } catch (IOException e1) {
                    throw new WelcomeException("" + response.getStatus());
                }
            }
            throw we;
        } else {
            throw new WelcomeException("" + response.getStatus());
        }
    }
	
	public void testFrame(File inputFile) throws Exception {
		testFrame(inputFile, null, null);
	}
	
	public void testFrame(File inputFile, String lang, String inputForm) throws Exception {
    	
        boolean overrideExpected = true;
        String baseName = FilenameUtils.getBaseName(inputFile.getName());

        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
        File newOutputFile = new File(inputFile.getParent(), baseName + "_Frame.json");
        writer.writeValue(newOutputFile, frame);

        //Creating headers mock up
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>();
        if (inputForm != null) {
	        headers.putSingle("x-original-input-form", inputForm);
	        Mockito.when(httpHeaders.getRequestHeader("x-original-input-form")).thenReturn(new ArrayList<>(Arrays.asList(new String[] {inputForm})));
		}
        if (lang != null) {
        	headers.putSingle("x-language", lang);
        	Mockito.when(httpHeaders.getRequestHeader("x-language")).thenReturn(new ArrayList<>(Arrays.asList(new String[] {lang})));
        }
        Mockito.when(httpHeaders.getRequestHeaders()).thenReturn(headers);
        
        DMSService instance = new DMSService();
        Response response = instance.realizeNextTurn(httpHeaders, input);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
	        DialogueMove output = (DialogueMove) response.getEntity();
	
	        File expectedFile = new File(inputFile.getParent(), baseName + "_Move.json");
	        if (!expectedFile.exists() || overrideExpected) {
	            writer.writeValue(expectedFile, output);        
	        }
	
	        String expected = FileUtils.readFileToString(expectedFile, "utf-8");    		
	        String result = writer.writeValueAsString(output);
	        //System.out.println(result);
	
	        Assertions.assertEquals(expected, result, "Actual and expected doesn't match in " + expectedFile.getPath());
        } else {
        	error_management(response);
        }
    }
	
	@Test
	public void test() throws Exception {
		
        testFrame(new File("src/test/resources/other/ConfirmationRequest_pending.json"));
    }
	
	@Test
	public void languageTagsTest() throws Exception {
		
        testFrame(new File("src/test/resources/other/languageTag.jsonld"));
    }
	
	@Test
	public void failedAnalysisTest() throws Exception {
		
        testFrame(new File("src/test/resources/other/FailedAnalysis.jsonld"), null, "speech");
    }
	
	@Test
	public void previousSlotFailedTest() throws Exception {
		
        testFrame(new File("src/test/resources/other/PreviousSlotFailed.jsonld"), null, "speech");
    }
}
