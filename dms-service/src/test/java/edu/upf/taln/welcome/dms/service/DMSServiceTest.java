package edu.upf.taln.welcome.dms.service;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.json.JsonObject;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.jsonld.http.media.MediaType;

import edu.upf.taln.welcome.dms.commons.input.DMInput;
import edu.upf.taln.welcome.dms.commons.input.DMInputData;
import edu.upf.taln.welcome.dms.commons.input.DMInputMetadata;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DMOutput;


/**
 *
 * @author rcarlini
 */
public class DMSServiceTest {

    /**
     * Base test to check outputs
     * @param inputFile
     * @param expectedFile
     * @throws java.lang.Exception
     */
    public void testSample(File inputFile, File expectedFile) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        DMInput input = mapper.readValue(inputFile, DMInput.class);

        DMSService instance = new DMSService();
        
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
    public void testJsonLD() throws FileNotFoundException, JsonLdError {
        FileReader reader = new FileReader("src/test/resources/initial_messages/turn1.json");
        Document doc = DocumentParser.parse(MediaType.JSON_LD, reader);
        JsonObject compacted = JsonLd.compact(doc, doc).get();
        System.out.println(compacted);
    }
}
