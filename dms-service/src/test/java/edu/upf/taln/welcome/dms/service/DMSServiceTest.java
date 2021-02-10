package edu.upf.taln.welcome.dms.service;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfGraph;
import com.apicatalog.rdf.RdfTriple;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;
import edu.upf.taln.welcome.dms.commons.output.SpeechActLabel;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.json.JsonObject;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;

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
        
        File inputFile0 = new File("src/test/resources/OpeningDIP_input.jsonld");
        File expectedFile0= new File("src/test/resources/OpeningDIP_output.jsonld");
        testSample(inputFile0, expectedFile0);

//        File inputFile1 = new File("src/test/resources/initial/turn1_input.json");
//        File expectedFile1 = new File("src/test/resources/initial/turn1_output.json");
//        testSample(inputFile1, expectedFile1);
//
//
//        File inputFile2 = new File("src/test/resources/initial/turn2_input.json");
//        File expectedFile2 = new File("src/test/resources/initial/turn2_output.json");
//        testSample(inputFile2, expectedFile2);
//
//        File inputFile3 = new File("src/test/resources/initial/turn3_input.json");
//        File expectedFile3 = new File("src/test/resources/initial/turn3_output.json");
//        testSample(inputFile3, expectedFile3);
    }
}
