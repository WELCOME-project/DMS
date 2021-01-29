/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upf.taln.welcome.dms.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.jsonld.http.media.MediaType;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfSubject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;

import edu.upf.taln.welcome.dms.commons.output.DMOutput;

/**
 *
 * @author rcarlini
 */
public class DialogManagerTest {

    /**
     * Base test to check outputs
     * @param inputFile
     * @param expectedFile
     * @throws java.lang.Exception
     */
    public void testSample(File inputFile, File expectedFile) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readValue(inputFile, JsonNode.class);

        DialogManager instance = new DialogManager();
        
        DMOutput output = instance.realizeNextTurn(input);
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
    public void testJsonLDFile() throws Exception {
        
        File inputFile0 = new File("src/test/resources/initial/turn0.jsonld");
        File expectedFile0= new File("src/test/resources/initial/turn0_output.json");
        testSample(inputFile0, expectedFile0);
    }
    
    @Test
    public void testJsonLD() throws FileNotFoundException, JsonLdError, IOException {
        
        String jsonPath = "src/test/resources/initial/turn0_input.json";
        //jsonPath = "src/test/resources/initial/turn0.jsonld";
        FileReader fReader = new FileReader(jsonPath);

        //*
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readValue(fReader, JsonNode.class);
        StringReader sReader = new StringReader(mapper.writeValueAsString(input));
        Reader reader = sReader;
        /*/
        Reader reader = fReader;
        //*/
        
        Document doc = DocumentParser.parse(MediaType.JSON_LD, reader);
        RdfDataset rdf = JsonLd.toRdf(doc).get();
        System.out.println(rdf.size());
        
        List<RdfNQuad> triples = rdf.toList();
        RdfNQuad first = triples.get(0);
        RdfSubject subject = first.getSubject();
        subject.toString();
        
    }
}
