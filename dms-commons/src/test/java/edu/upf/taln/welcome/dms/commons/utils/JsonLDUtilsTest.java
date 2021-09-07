package edu.upf.taln.welcome.dms.commons.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import java.io.IOException;
import java.util.Map;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author rcarlini
 */
public class JsonLDUtilsTest {

	@Test
	public void testLoadJsonObject() throws MalformedURLException, WelcomeException {
		File jsonFile = new File("src/test/resources/json/merge/header.json");
		URL jsonUrl = jsonFile.toURI().toURL();
		
		JsonObjectBuilder builder = Json.createObjectBuilder();
		JsonObject expected = builder.add("d", 8)
				.add("e", "e")
				.add("f", false)
				.build();

		JsonObject actual = JsonLDUtils.loadJsonObject(jsonUrl);
		
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void testToJsonObject() throws MalformedURLException, WelcomeException, IOException {
		File jsonFile = new File("src/test/resources/json/merge/header.json");

		ObjectMapper mapper = new ObjectMapper();
		Map values = mapper.readValue(jsonFile, Map.class);

		JsonObjectBuilder builder = Json.createObjectBuilder();
		JsonObject expected = builder.add("d", 8)
				.add("e", "e")
				.add("f", false)
				.build();

		JsonObject actual = JsonLDUtils.toJsonObject(values);
		
		Assertions.assertEquals(expected, actual);
	}
	
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

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }
    
    @Test
    public void readMoveTest() throws Exception {
        
        File inputFile = new File("src/test/resources/jsonld/Opening_Move.json");
        File expectedFile = new File("src/test/resources/jsonld/Opening_Move_expected.json");

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
}
