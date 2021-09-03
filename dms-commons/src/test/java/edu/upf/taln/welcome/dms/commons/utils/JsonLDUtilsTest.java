package edu.upf.taln.welcome.dms.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;

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
    public void mergeJsonNodeTest() throws WelcomeException, IOException {

    	File jsonFile1 = new File("src/test/resources/json/merge/header.json");
    	File jsonFile2 = new File("src/test/resources/json/merge/body.json");
    	File expectedJsonFile = new File("src/test/resources/json/merge/expected.json");
    	
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode baseJson = mapper.readTree(jsonFile1);
    	JsonNode extraJson = mapper.readTree(jsonFile2);
    	
    	JsonNode jsonResult = JsonLDUtils.mergeJsons(baseJson, extraJson);
    	
    	ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(jsonResult);

        String expectedJson = FileUtils.readFileToString(expectedJsonFile, "utf8");
        Assertions.assertEquals(expectedJson, result);
    }
    
    @Test
    public void mergeJsonURLTest() throws WelcomeException, IOException {

    	File jsonFile1 = new File("src/test/resources/json/merge/header.json");
    	File jsonFile2 = new File("src/test/resources/json/merge/body.json");
    	File expectedJsonFile = new File("src/test/resources/json/merge/expected.json");
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	JsonNode jsonResult = JsonLDUtils.mergeJsons(jsonFile1.toURI().toURL(), jsonFile2.toURI().toURL());
    	
    	ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(jsonResult);

        String expectedJson = FileUtils.readFileToString(expectedJsonFile, "utf8");
        Assertions.assertEquals(expectedJson, result);
    }
    
    @Test
    public void jsonObject2jsonNodeTest() throws MalformedURLException, IOException {
    	File jsonFile = new File("src/test/resources/json/object2Node/base.json");
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try (InputStreamReader reader = new InputStreamReader(jsonFile.toURI().toURL().openStream())) {
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            
            JsonNode jsonNode = JsonLDUtils.jsonObject2JsonNode(jsonObject);
            ObjectWriter writer = mapper
                    .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                    .writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(jsonNode);
            
            String expectedString = FileUtils.readFileToString(jsonFile, "utf8");
            Assertions.assertEquals(expectedString, jsonString);
    	}
    }
    
    @Test
    public void jsonNode2jsonObjectTest() throws MalformedURLException, IOException {
    	File jsonFile = new File("src/test/resources/json/node2Object/base.json");
    	File expectedFile = new File("src/test/resources/json/node2Object/expected.json");
    	
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode jsonNode = mapper.readTree(jsonFile);
    	
    	JsonObject jsonObject = JsonLDUtils.jsonNode2JsonObject(jsonNode);
    	StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(jsonObject);
        }
        String jsonString = stringWriter.toString();
        
        String expectedString = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expectedString, jsonString);
    	
    }
    
}
