package edu.upf.taln.welcome.dms.service;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.utils.JsonLDUtils;

public class OtherTest {
	private final Logger logger = Logger.getLogger(OtherTest.class.getName());

    
	private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .writerWithDefaultPrettyPrinter();
	
	public void testFrame(File inputFile) throws Exception {
    	
        boolean overrideExpected = true;
        String baseName = FilenameUtils.getBaseName(inputFile.getName());

        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
        File newOutputFile = new File(inputFile.getParent(), baseName + "_Frame.json");
        writer.writeValue(newOutputFile, frame);

        DMSService instance = new DMSService();
        DialogueMove output = instance.realizeNextTurn(frame);

        File expectedFile = new File(inputFile.getParent(), baseName + "_Move.json");
        if (!expectedFile.exists() || overrideExpected) {
            writer.writeValue(expectedFile, output);        
        }

        String expected = FileUtils.readFileToString(expectedFile, "utf-8");    		
        String result = writer.writeValueAsString(output);
        //System.out.println(result);

        Assertions.assertEquals(expected, result);
    }
	
	@Test
	public void test() throws Exception {
		
        testFrame(new File("src/test/resources/other/ConfirmationRequest_pending.json"));
    }
	
	@Test
	public void languageTagsTest() throws Exception {
		
        testFrame(new File("src/test/resources/other/languageTag.jsonld"));
    }
}
