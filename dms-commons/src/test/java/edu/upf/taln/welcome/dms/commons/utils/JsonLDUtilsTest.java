package edu.upf.taln.welcome.dms.commons.utils;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;


/**
 *
 * @author rcarlini
 */
public class JsonLDUtilsTest {

    /**
     * Test of readFrame method, of class JsonLDUtils.
     */
    @Test
    public void testReadFrame() throws Exception {
        System.out.println("readFrame");
        
        File inputFile = new File("src/test/resources/jsonld/Opening.jsonld");
        File expectedFile = new File("src/test/resources/jsonld/Opening_Frame.json");
        
        URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree(inputFile);
        
        Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);        

        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(frame);
		//System.out.println(result);

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }
    
    @Test
    public void readMoveTest() throws Exception {
        
        File inputFile = new File("src/test/resources/jsonld/Opening_Move.json");
        File expectedFile = new File("src/test/resources/jsonld/Opening_Move.json");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree(inputFile);
        
        DialogueMove move = JsonLDUtils.readMove(input.toString());        

        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(move);

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }

    /**
     * Test of readFrame method, of class JsonLDUtils.
	 * @throws java.lang.Exception
     */
    @Test
    public void testReadProto2() throws Exception {
        System.out.println("readProto2");
        
        File inputFile = new File("src/test/resources/proto2/ScenarioIntroduction.jsonld");
        File expectedFile = new File("src/test/resources/proto2/ScenarioIntroduction_Frame.json");
        
        URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree(inputFile);
        
        Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);        

        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();
		
//		writer.writeValue(expectedFile, frame);

        String result = writer.writeValueAsString(frame);
//		System.out.println(result);

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }

    /**
     * Test of readFrame method, of class JsonLDUtils.
	 * @throws java.lang.Exception
     */
    @Test
    public void testReadAttempts() throws Exception {
        System.out.println("readAttempts");
        
        File inputFile = new File("src/test/resources/proto2/ConfirmationRequest_pending.json");
        File expectedFile = new File("src/test/resources/proto2/ConfirmationRequest_pending_Frame.json");
        
        URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree(inputFile);
        
        Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);        

        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();
		
		writer.writeValue(expectedFile, frame);

        String result = writer.writeValueAsString(frame);
		System.out.println(result);

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }
}
