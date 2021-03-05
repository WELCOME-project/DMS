package edu.upf.taln.welcome.dms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;


/**
 *
 * @author rcarlini
 */
public class DMSServiceTest {

    /**
     * Base test to check outputs
     */
    public void testSample(File inputFile, File expectedFile) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readValue(inputFile, JsonNode.class);

        DMSService instance = new DMSService();
        JsonNode output = instance.realize_next_turn(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);

        String expected = FileUtils.readFileToString(expectedFile, "utf-8");
        assertEquals(expected, result);
    }

    @Test
    public void testSampleInitialExtrapolateTurn() throws Exception {
        {
            File inputFile = new File("src/test/resources/OpeningDIP_input.jsonld");
            File expectedFile = new File("src/test/resources/OpeningDIP_output.jsonld");
            testSample(inputFile, expectedFile);
        }
        {
            File inputFile = new File("src/test/resources/ObtainRegistrationStatus_input.jsonld");
            File expectedFile = new File("src/test/resources/ObtainRegistrationStatus_output.jsonld");
            testSample(inputFile, expectedFile);
        }
        {
            File inputFile = new File("src/test/resources/ProposeService_input.jsonld");
            File expectedFile = new File("src/test/resources/ProposeService_output.jsonld");
            testSample(inputFile, expectedFile);
        }
//        {
//            File inputFile = new File("src/test/resources/InformFirstReception_input.jsonld");
//            File expectedFile = new File("src/test/resources/InformFirstReception_output.jsonld");
//            testSample(inputFile, expectedFile);
//        }
    }
}
