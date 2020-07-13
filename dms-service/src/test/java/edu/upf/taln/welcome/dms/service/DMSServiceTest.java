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
     * @param input
     * @param expected
     * @throws java.lang.Exception
     */
    public void testSample(DMInput input, File expected) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        DMSService instance = new DMSService();
        
        String expResult = FileUtils.readFileToString(expected, "utf-8");
        DMOutput output = instance.realize_next_turn(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSampleInitialExtrapolateTurn() throws Exception {

        Frame frame = new Frame();
        DMInputData data = new DMInputData();
        data.setFrame(frame);
        DMInputMetadata metadata = new DMInputMetadata();
        //metadata.setDialogueTurn(1);
        DMInput input = new DMInput();
        input.setMetadata(metadata);
        input.setData(data);

//        File turn0File = new File("src/test/resources/initial_messages/dms_output_turn0.json");
//        testSample(input, turn0File);

        
        File turn2File = new File("src/test/resources/initial_messages/dms_output_1.json");
        testSample(input, turn2File);

        frame.setName("Karim");
        File turn4File = new File("src/test/resources/initial_messages/dms_output_2.json");
        testSample(input, turn4File);

        frame.setAddress("Sant Pau, 2");
        File turn6File = new File("src/test/resources/initial_messages/dms_output_3.json");
        testSample(input, turn6File);
    }
    
    @Test
    public void testSampleInitial1() throws Exception {

        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(1);
        DMInput input = new DMInput();
        input.setMetadata(metadata);

        File turn2File = new File("src/test/resources/initial_messages/dms_output_1.json");
        testSample(input, turn2File);
    }
    
    @Test
    public void testSampleInitial2() throws Exception {
        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(3);
        DMInput input = new DMInput();
        input.setMetadata(metadata);
        
        File turn2File = new File("src/test/resources/initial_messages/dms_output_2.json");
        testSample(input, turn2File);
    }
    
    @Test
    public void testSampleInitial3() throws Exception {
        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(5);
        DMInput input = new DMInput();
        input.setMetadata(metadata);

        File turn2File = new File("src/test/resources/initial_messages/dms_output_3.json");
        testSample(input, turn2File);
    }

    @Test
    public void testJsonLD() throws FileNotFoundException, JsonLdError {
        FileReader reader = new FileReader("src/test/resources/initial_messages/turn1.json");
        Document doc = DocumentParser.parse(MediaType.JSON_LD, reader);
        JsonObject compacted = JsonLd.compact(doc, doc).get();
        System.out.println(compacted);
    }
}
