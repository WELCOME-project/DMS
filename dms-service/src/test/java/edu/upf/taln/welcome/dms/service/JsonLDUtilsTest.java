package edu.upf.taln.welcome.dms.service;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.utils.JsonLDUtils;

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
        
        File inputFile = new File("src/test/resources/proto1/Opening.jsonld");
        File expectedFile = new File("src/test/resources/proto1/Opening_Frame.json");
        
        URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree(inputFile);
        
        Frame frame = JsonLDUtils.readFrame(input, contextFile);        

        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();

        String result = writer.writeValueAsString(frame);
        //writer.writeValue(expectedFile, frame);
        //System.out.println(result);

        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        Assertions.assertEquals(expResult, result);
    }
    
}
