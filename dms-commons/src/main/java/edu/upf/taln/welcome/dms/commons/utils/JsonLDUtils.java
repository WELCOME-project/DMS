package edu.upf.taln.welcome.dms.commons.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

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
    
	public static JsonObject loadJsonObject(URL jsonURL) throws WelcomeException {
		
		try (InputStream inStream = jsonURL.openStream();
				JsonReader jsonReader = Json.createReader(inStream)) {
			
			return jsonReader.readObject();
				
		} catch (IOException ex) {
            throw new WelcomeException(ex);
		}
	}

	public static JsonObject toJsonObject(Object obj) throws WelcomeException {
		
		try(ByteArrayOutputStream out = new ByteArrayOutputStream();) {
		
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(out, obj);

			byte[] buf = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(buf)) {
				JsonReader reader = Json.createReader(in);
				JsonObject jsonObj = reader.readObject();

				return jsonObj;
			}
		} catch (IOException ex) {
			throw new WelcomeException(ex);
		}
	}
	
    public static JsonObject mergeJsonObjects(JsonObject json1, JsonObject json2) throws WelcomeException, IOException {
    	
    	JsonObject merged = JsonUtils.merge(json1, json2);
		return merged;
    }
    
    private static Document loadDocument(Reader reader) throws WelcomeException {
        try {
            Document doc = DocumentParser.parse(MediaType.JSON_LD, reader);
            return doc;
            
        } catch (JsonLdError ex) {
            throw new WelcomeException(ex);
        }
    }
    
    private static Document loadDocument(InputStream inStream) throws WelcomeException {
		
		try {
			Document doc = DocumentParser.parse(MediaType.JSON_LD, inStream);
			return doc;
			
		} catch (JsonLdError ex) {
			throw new WelcomeException(ex);
		}
    }
    
    private static Document loadDocument(URL documentURL) throws WelcomeException {
		
        try (InputStream inStream = documentURL.openStream()) {
            
            Document doc = loadDocument(inStream);
            return doc;
            
        } catch (IOException ex) {
            throw new WelcomeException(ex);
        }
    }
    
    private static Document loadDocument(String content) throws WelcomeException {
		
        try (StringReader reader = new StringReader(content)) {
            
            Document doc = loadDocument(reader);
            return doc;            
        }
    }

	public static Frame readFrame(String content, URL contextURL) throws WelcomeException {
		Document context = loadDocument(contextURL);
		return readFrame(content, context);
	}

    public static Frame readFrame(String content, Document context) throws WelcomeException {
        
		try {
			Document doc = loadDocument(content);
            
            JsonObject framed = JsonLd
                    .frame(doc, context)
                    .ordered()
                    .get();
			
			String framedJson = framed.toString();
            //System.out.println(framedJson);
			
            ObjectMapper mapper = new ObjectMapper();
            Frame dip = mapper.readValue(framedJson, Frame.class);
            
            return dip;
            
        } catch (JsonLdError | IOException ex) {
            throw new WelcomeException(ex);
        }
    }

    public static DialogueMove readMove(String content) throws WelcomeException {
		
        try {	
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(framed.toString());
            DialogueMove move = mapper.readValue(content, DialogueMove.class);
            
            return move;
            
        } catch (IOException ex) {
            throw new WelcomeException(ex);
        }
    }
    
    //NOT WORKING
    public static DialogueMove readMove(String content, URL contextURL) throws WelcomeException {
		Document context = loadDocument(contextURL);
		return readMove(content, context);
	}
	
    //NOT WORKING
	public static DialogueMove readMove(String content, Document context) throws WelcomeException {
        try {
            
            Document inputDoc = loadDocument(content);
            
            JsonLdOptions options = new JsonLdOptions();
            //options.setCompactArrays(true);
            options.setExpandContext(context);
            
            JsonArray expanded = JsonLd
                    .expand(inputDoc)
                    .options(options)
                    .ordered()
                    .get();
            //JsonObject expObject = expanded.getJsonObject(0);
            JsonDocument expDocument = JsonDocument.of(expanded);
            
            System.out.println(expanded.toString());
            
            JsonObject framed = JsonLd
                    .frame(expDocument, context)
                    .options(options)
                    .ordered()
                    .get();

			String framedJson = framed.toString();
            //System.out.println(framedJson);
			
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(framed.toString());
            DialogueMove move = mapper.readValue(framedJson, DialogueMove.class);
            
            return move;
            
        } catch (JsonLdError | IOException ex) {
            throw new WelcomeException(ex);
        }
    }
}
