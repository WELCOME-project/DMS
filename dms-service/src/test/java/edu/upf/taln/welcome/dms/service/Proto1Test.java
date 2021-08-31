package edu.upf.taln.welcome.dms.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.JsonObject;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.document.Document;
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
public class Proto1Test {

    public void readFrame(String jsonldPath, String expectedPath) throws Exception {
        
        File inputFile = new File(jsonldPath);
        File expectedFile = new File(expectedPath);
        
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
        assertEquals(expResult, result);
    }
    
    public void showFramed(String jsonldPath) throws Exception, JsonLdError {
        
        File inputFile = new File(jsonldPath);
        
        URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree(inputFile);
        
        try (InputStreamReader reader = new InputStreamReader(contextFile.openStream())) {
            
            Document context = JsonLDUtils.loadDocument(reader);
	        StringReader docReader = new StringReader(input.toString());
	        Document doc = JsonLDUtils.loadDocument(docReader);
	        
	        JsonObject framed = JsonLd
	                .frame(doc, context)
	                .ordered()
	                .get();
	        
	        System.out.println(framed.toString());
        }
    }
    
    /*@Test
    public void testOpening() throws Exception {
        String jsonldPath = "src/test/resources/proto1/Opening.jsonld";
        String expectedPath = "src/test/resources/proto1/Opening_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testModifiedRegistration() throws Exception {
        String jsonldPath = "src/test/resources/proto1/ModifiedRegistrationStatus.jsonld";
        String expectedPath = "src/test/resources/proto1/ModifiedRegistrationStatus_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testProposeService() throws Exception {
        String jsonldPath = "src/test/resources/proto1/ProposeService.jsonld";
        String expectedPath = "src/test/resources/proto1/ProposeService_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }*/
    
    //NEW FILES
    
    @Test
    public void testOpening() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/Opening.jsonld";
        String expectedPath = "src/test/resources/proto1/new/Opening_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testProposeService() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/ProposeService.jsonld";
        String expectedPath = "src/test/resources/proto1/new/ProposeService_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testNormalClosing() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/NormalClosing.jsonld";
        String expectedPath = "src/test/resources/proto1/new/NormalClosing_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testInformFirstReceptionService() throws Exception, JsonLdError {
        String jsonldPath = "src/test/resources/proto1/new/InformFirstReceptionService.jsonld";
        String expectedPath = "src/test/resources/proto1/new/InformFirstReceptionService_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testFillFormPersonalInfo() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/FillFormPersonalInfo.jsonld";
        String expectedPath = "src/test/resources/proto1/new/FillFormPersonalInfo_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testFillFormOther() throws Exception {
    	//TODO not working properly although not crashing.
        String jsonldPath = "src/test/resources/proto1/new/FillFormOther.jsonld";
        String expectedPath = "src/test/resources/proto1/new/FillFormOther_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testFillFormModules() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/FillFormModules.jsonld";
        String expectedPath = "src/test/resources/proto1/new/FillFormModules_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testFillFormCourse() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/FillFormCourse.jsonld";
        String expectedPath = "src/test/resources/proto1/new/FillFormCourse_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testFillFormAddressInfo() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/FillFormAddressInfo.jsonld";
        String expectedPath = "src/test/resources/proto1/new/FillFormAddressInfo_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testClosingUponFailure() throws Exception {
        String jsonldPath = "src/test/resources/proto1/new/ClosingUponFailure.jsonld";
        String expectedPath = "src/test/resources/proto1/new/ClosingUponFailure_Frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    
}
