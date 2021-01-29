package edu.upf.taln.welcome.dms.core;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.jsonld.http.media.MediaType;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfSubject;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.output.DMOutput;
import edu.upf.taln.welcome.dms.core.utils.SampleResponses;
import java.util.logging.Logger;

/**
 *
 * @author rcarlini
 */
public class DialogManager {
    
    Logger logger = Logger.getLogger(DialogManager.class.getName());

 	public DMOutput realizeNextTurn(JsonNode input) throws WelcomeException {

        int turn = 1;
        if (input.size() == 0) {
            turn = 1;
            
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonStr = mapper.writeValueAsString(input);
                
                turn = realizeNextTurn(jsonStr);
                
            } catch (JsonLdError | JsonProcessingException ex) {
                logger.log(Level.SEVERE, null, ex);
                throw new WelcomeException(ex);
            }
        }

        DMOutput output = SampleResponses.generateResponse(turn);
        
		return output;
	}

    private int realizeNextTurn(String jsonStr) throws JsonLdError {
        
        StringReader sReader = new StringReader(jsonStr);
        
        Document doc = DocumentParser.parse(MediaType.JSON_LD, sReader);
        
        RdfDataset rdf = JsonLd.toRdf(doc).get();
        List<RdfNQuad> triples = rdf.toList();
        
        RdfNQuad first = triples.get(0);
        RdfSubject subject = first.getSubject();
        System.out.println(subject.toString());
        
        int turn;
        switch(subject.toString()) {
            case "http://www.semanticweb.org/welcome#handshaking_1":
                turn = 1;
                break;
            case "http://www.semanticweb.org/welcome#request_info_1_1":
                turn = 3;
                break;
            case "http://www.semanticweb.org/welcome#request_info_1_2":
                turn = 5;
                break;
            case "http://www.semanticweb.org/welcome#catalan_language":
                turn = 7;
                break;
            default:
                turn = 1;
                break;
        }
        return turn;
    }
   
}
