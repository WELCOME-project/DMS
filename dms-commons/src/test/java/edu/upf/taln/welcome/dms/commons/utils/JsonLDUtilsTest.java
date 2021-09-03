package edu.upf.taln.welcome.dms.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

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
        
        Frame frame = JsonLDUtils.readFrame(input, contextFile);        

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
        
        DialogueMove move = JsonLDUtils.readMove(input);        

        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(move);

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }
    
    @Test
    public void mergeResultContextTest() throws WelcomeException, IOException {

    	File baseJsonFile = new File("src/test/resources/json/toMergeBody.json");
    	File extraJsonFile = new File("src/test/resources/json/toMergeHeader.json");
    	File expectedJsonFile = new File("src/test/resources/json/toMergeExpected.json");
    	
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode baseJson = mapper.readTree(baseJsonFile);
    	
    	JsonNode jsonResult = JsonLDUtils.mergeResultContext(baseJson, extraJsonFile.toURI().toURL());
    	
    	ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(jsonResult);

        String expectedJson = FileUtils.readFileToString(expectedJsonFile, "utf8");
        Assertions.assertEquals(expectedJson, result);
    }
    
    @Test
    public void jsonObject2jsonNodeTest() throws MalformedURLException, IOException {
    	File jsonFile = new File("src/test/resources/json/toMergeBody.json");
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try (InputStreamReader reader = new InputStreamReader(jsonFile.toURI().toURL().openStream())) {
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            
            JsonNode jsonNode = JsonLDUtils.toJsonNode(jsonObject);
            ObjectWriter writer = mapper
                    .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                    .writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(jsonNode);
            
            String expectedString = FileUtils.readFileToString(jsonFile, "utf8");
            Assertions.assertEquals(expectedString, jsonString);
    	}
    }
    
}
