package edu.upf.taln.welcome.dms.commons.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.api.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.http.media.MediaType;
import com.apicatalog.jsonld.json.JsonUtils;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;

/**
 *
 * @author rcarlini
 */
public class JsonLDUtils {
    
    private static Document loadDocument(Reader reader) throws WelcomeException {
        try {
            Document doc = DocumentParser.parse(MediaType.JSON_LD, reader);
            return doc;
            
        } catch (JsonLdError ex) {
            throw new WelcomeException(ex);
        }
    }
    
    public static Frame readFrame(JsonNode input, URL contextURL) throws WelcomeException {
        
        try (InputStreamReader reader = new InputStreamReader(contextURL.openStream())) {
            
            Document doc = loadDocument(reader);
            return readFrame(input, doc);
            
        } catch (IOException ex) {
            throw new WelcomeException(ex);
        }
    }
    
    public static Frame readFrame(JsonNode input, Document context) throws WelcomeException {
        
        try {
            StringReader reader = new StringReader(input.toString());
            Document doc = loadDocument(reader);
            
            JsonObject framed = JsonLd
                    .frame(doc, context)
                    .ordered()
                    .get();
            
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(framed.toString());
            Frame dip = mapper.readValue(framed.toString(), Frame.class);
            
            return dip;
            
        } catch (JsonLdError | IOException ex) {
            throw new WelcomeException(ex);
        }
    }
    
    public static JsonNode mergeJsons(URL jsonFile1, URL jsonFile2) throws WelcomeException {
        
        try (InputStreamReader reader1 = new InputStreamReader(jsonFile1.openStream());
        		InputStreamReader reader2 = new InputStreamReader(jsonFile2.openStream());) {

            JsonReader jsonReader1 = Json.createReader(reader1);
            JsonObject json1 = jsonReader1.readObject();
            jsonReader1.close();
            
            JsonReader jsonReader2 = Json.createReader(reader2);
            JsonObject json2 = jsonReader2.readObject();
            jsonReader2.close();

            return mergeJsons(json1, json2);
        } catch (IOException ex) {
            throw new WelcomeException(ex);
        }
    }
    
    public static JsonNode mergeJsons(JsonNode jsonNode1, JsonNode jsonNode2) throws WelcomeException, IOException {
    	
    	JsonObject json1 = jsonNode2JsonObject(jsonNode1);
        JsonObject json2 = jsonNode2JsonObject(jsonNode2);	

		return mergeJsons(json1, json2);
    }
    
    public static JsonNode mergeJsons(JsonObject json1, JsonObject json2) throws WelcomeException, IOException {
    	
    	JsonObject merged = JsonUtils.merge(json1, json2);
		return jsonObject2JsonNode(merged);
    }
       
    public static JsonObject jsonNode2JsonObject(JsonNode jsonNode) throws IOException {

        // Parse a JsonNode into a JSON string
    	ObjectMapper objectMapper = new ObjectMapper();
    	String jsonStr = objectMapper.writeValueAsString(jsonNode);
    	
    	// Parse a JSON string into a JsonObject
    	StringReader strReader = new StringReader(jsonStr);
    	JsonReader jsonReader = Json.createReader(strReader);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        return jsonObject;
    }
    
    public static JsonNode jsonObject2JsonNode(JsonObject jsonObject) throws IOException {

        // Parse a JsonObject into a JSON string
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(jsonObject);
        }
        String json = stringWriter.toString();

        // Parse a JSON string into a JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        return jsonNode;
    }

    public static DialogueMove readMove(JsonNode input) throws WelcomeException {
        try (StringReader readerInput = new StringReader(input.toString())) {
                        
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(framed.toString());
            DialogueMove move = mapper.readValue(readerInput, DialogueMove.class);
            
            return move;
            
        } catch (IOException ex) {
            throw new WelcomeException(ex);
        }
    }
    
    //NOT WORKING
    public static DialogueMove readMove(JsonNode input, URL contextURL) throws WelcomeException {
               
        try (InputStreamReader readerContext = new InputStreamReader(contextURL.openStream());
                StringReader readerInput = new StringReader(input.toString())) {
            
            Document contextDoc = loadDocument(readerContext);
            Document inputDoc = loadDocument(readerInput);
            
            JsonLdOptions options = new JsonLdOptions();
            //options.setCompactArrays(true);
            options.setExpandContext(contextDoc);
            
            JsonArray expanded = JsonLd
                    .expand(inputDoc)
                    .options(options)
                    .ordered()
                    .get();
            //JsonObject expObject = expanded.getJsonObject(0);
            JsonDocument expDocument = JsonDocument.of(expanded);
            
            System.out.println(expanded.toString());
            
            JsonObject framed = JsonLd
                    .frame(expDocument, contextDoc)
                    .options(options)
                    .ordered()
                    .get();
            
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(framed.toString());
            DialogueMove move = mapper.readValue(framed.toString(), DialogueMove.class);
            
            return move;
            
        } catch (JsonLdError | IOException ex) {
            throw new WelcomeException(ex);
        }
    }
}
