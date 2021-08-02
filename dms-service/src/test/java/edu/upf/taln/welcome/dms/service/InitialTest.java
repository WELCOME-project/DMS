package edu.upf.taln.welcome.dms.service;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.File;
import java.net.URL;

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
public class InitialTest {

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

    @Test
    public void testOpening() throws Exception {
        String jsonldPath = "src/test/resources/initial/Opening.jsonld";
        String expectedPath = "src/test/resources/initial/Opening_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }

    @Test
    public void testOpeningDIP() throws Exception {
        String jsonldPath = "src/test/resources/initial/OpeningDIP_input.jsonld";
        String expectedPath = "src/test/resources/initial/OpeningDIP_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testObtainRegistration() throws Exception {
        String jsonldPath = "src/test/resources/initial/ObtainRegistrationStatus_input.jsonld";
        String expectedPath = "src/test/resources/initial/ObtainRegistrationStatus_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testProposeService() throws Exception {
        String jsonldPath = "src/test/resources/initial/ProposeService_input.jsonld";
        String expectedPath = "src/test/resources/initial/ProposeService_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }
    
    @Test
    public void testInformFRS() throws Exception {
        String jsonldPath = "src/test/resources/initial/InformFirstReception_input.jsonld";
        String expectedPath = "src/test/resources/initial/InformFirstReception_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }

    @Test
    public void testInformContact() throws Exception {
        String jsonldPath = "src/test/resources/initial/InformContactDetails.jsonld";
        String expectedPath = "src/test/resources/initial/InformContactDetails_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }

    @Test
    public void testMakeAppointment() throws Exception {
        String jsonldPath = "src/test/resources/initial/MakeAppointment.jsonld";
        String expectedPath = "src/test/resources/initial/MakeAppointment_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }

    @Test
    public void testObtainTCNInformation() throws Exception {
        String jsonldPath = "src/test/resources/initial/ObtainTCNInformation.jsonld";
        String expectedPath = "src/test/resources/initial/ObtainTCNInformation_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }

    @Test
    public void testScenarioIntroduction() throws Exception {
        String jsonldPath = "src/test/resources/initial/ScenarioIntroduction.jsonld";
        String expectedPath = "src/test/resources/initial/ScenarioIntroduction_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }

    @Test
    public void testSimulationIntroduction() throws Exception {
        String jsonldPath = "src/test/resources/initial/SimulationIntroduction.jsonld";
        String expectedPath = "src/test/resources/initial/SimulationIntroduction_frame.json";
        
        readFrame(jsonldPath, expectedPath);
    }    
}
