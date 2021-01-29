package edu.upf.taln.welcome.dms.client;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import edu.upf.taln.welcome.dms.commons.output.DMOutput;
import org.junit.Ignore;


/**
 *
 * @author rcarlini
 */
@Ignore
public class DMSServiceTest {

    /**
     * Base test to check outputs
     * @param inputFile
     * @param expectedFile
     * @throws java.lang.Exception
     */
    public void testSample(File inputFile, File expectedFile) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readValue(inputFile, JsonNode.class);

        DMSClient instance = new DMSClient();
        
        DMOutput output = instance.realize_next_turn(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        System.out.println(result);

        String expected = FileUtils.readFileToString(expectedFile, "utf-8");
        assertEquals(expected, result);
    }

    @Test
    public void testSampleInitialExtrapolateTurn() throws Exception {
        
        File inputFile0 = new File("src/test/resources/initial/turn0_input.json");
        File expectedFile0= new File("src/test/resources/initial/turn0_output.json");
        testSample(inputFile0, expectedFile0);

        File inputFile1 = new File("src/test/resources/initial/turn1_input.json");
        File expectedFile1 = new File("src/test/resources/initial/turn1_output.json");
        testSample(inputFile1, expectedFile1);


        File inputFile2 = new File("src/test/resources/initial/turn2_input.json");
        File expectedFile2 = new File("src/test/resources/initial/turn2_output.json");
        testSample(inputFile2, expectedFile2);

        File inputFile3 = new File("src/test/resources/initial/turn3_input.json");
        File expectedFile3 = new File("src/test/resources/initial/turn3_output.json");
        testSample(inputFile3, expectedFile3);
    }

    @Test
    public void testJsonLDFile() throws Exception {
        
        File inputFile0 = new File("src/test/resources/initial/turn0.jsonld");
        File expectedFile0= new File("src/test/resources/initial/turn0_output.json");
        testSample(inputFile0, expectedFile0);
    }

}
