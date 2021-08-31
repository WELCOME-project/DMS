package edu.upf.taln.welcome.dms.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.junit.Test;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.input.Slot;
import edu.upf.taln.welcome.dms.commons.input.Status;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;
import edu.upf.taln.welcome.dms.commons.utils.JsonLDUtils;


/**
 *
 * @author rcarlini
 */
public class DMSServiceTest {

	private final Logger logger = Logger.getLogger(DMSServiceTest.class.getName());
    /**
     * Base test to check outputs
     */
    public void testSample(File inputFile) throws Exception {
    	
    	Map<String, Frame> frames = generateAllPossiblePendingSlots(inputFile);
        
    	for (String slotId : frames.keySet()) {
    		logger.log(Level.INFO, "Test file:{0}\tSlot: {1}", new Object[]{inputFile.getName(), slotId});
    		
    		Frame frame = frames.get(slotId);

	        ObjectMapper mapper = new ObjectMapper();
	        
	        DMSService instance = new DMSService();
	        JsonNode output = instance.realizeNextTurn(frame);
	        
	        ObjectWriter writer = mapper
	                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
	                .writerWithDefaultPrettyPrinter();
	        
	        String expResult = null;
	        String[] nameParts = inputFile.getName().split("\\.");
    		if (nameParts.length == 2) {
    			File expectedFile = new File(inputFile.getParent() + "/" + nameParts[0] + "_" + slotId + "_Move.json");
    			if (!expectedFile.exists()) {
    				writer.writeValue(expectedFile, output);        
    			}
    			expResult = FileUtils.readFileToString(expectedFile, "utf-8");
    	        
    		}
    		
    		String result = writer.writeValueAsString(output);
    		//System.out.println(result);
    		
    		assertEquals(expResult, result);
    	}
    }
    
    public String cleanCompactedSchema(String value) {
    	if (value == null) { return null; }
    	
    	String cleanValue = value;
    	String[] splittedValue = value.split(":");
        if(splittedValue.length > 1) {
        	cleanValue = splittedValue[splittedValue.length-1];
        }
    	return cleanValue;
    }
    
    public Map<String, Frame> generateAllPossiblePendingSlots(File inputFile) throws WelcomeException, JsonGenerationException, JsonMappingException, IOException {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed_alt.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input, contextFile);
    	List<Slot> slots = frame.slots;
    	
    	/*ObjectWriter writer = mapper
                //.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();*/
    	
    	Map<String, Frame> frames = new LinkedHashMap<>();
    	for (int i = 0; i < slots.size(); i++) {
    		for (int j = 0; j < slots.size(); j++) {
	    		Slot slot = slots.get(j);
	    		if (i==j) {
	    			slot.status = Status.Pending;
	    		} else {
	    			slot.status = Status.Completed;
	    		}
	    		//slots.set(i, slot);
    		}
    		
    		String slotId = cleanCompactedSchema(slots.get(i).id);
    		
    		//making a swallow copy by serializing object to string
    		String strFrame = mapper.writeValueAsString(frame);
    		Frame newFrame = mapper.readValue(strFrame, Frame.class);
    		frames.put(slotId, newFrame);

    		/*String[] nameParts = inputFile.getName().split("\\.");
    		if (nameParts.length == 2) {
    			File newOutputFile = new File(inputFile.getParent() + "/" + nameParts[0] + "_" + slotId + "_Frame.json");
    			writer.writeValue(newOutputFile, frame);
    		}*/
    	}
    	
    	return frames;
    }

    /*@Test
    public void testSampleInitialExtrapolateTurn() throws Exception {
        String baseDir = "src/test/resources/initial/";
        {
            File inputFile = new File(baseDir, "OpeningDIP_input.jsonld");
            File expectedFile = new File(baseDir, "OpeningDIP_output.jsonld");
            testSample(inputFile, expectedFile);
        }
        {
            File inputFile = new File(baseDir, "ObtainRegistrationStatus_input.jsonld");
            File expectedFile = new File(baseDir, "ObtainRegistrationStatus_output.jsonld");
            testSample(inputFile, expectedFile);
        }
        {
            File inputFile = new File(baseDir, "ProposeService_input.jsonld");
            File expectedFile = new File(baseDir, "ProposeService_output.jsonld");
            testSample(inputFile, expectedFile);
        }
//        {
//            File inputFile = new File(baseDir, "InformFirstReception_input.jsonld");
//            File expectedFile = new File(baseDir, "InformFirstReception_output.jsonld");
//            testSample(inputFile, expectedFile);
//        }
    }*/
    
    private void applyTestOnDirectory(String baseDir) throws Exception {
        DialogueMove.resetCounter();
        SpeechAct.resetCounter();
        
        File folder = new File(baseDir);
        
        IOFileFilter filterFile = FileFileFilter.FILE;
        SuffixFileFilter filterSuffix = new SuffixFileFilter(".jsonld");
        FilenameFilter filter = new AndFileFilter(filterFile, filterSuffix);

        File[] fileList = folder.listFiles(filter);
        Arrays.sort(fileList);

        for (final File fileEntry : fileList) {
            testSample(fileEntry);
        }
    }
    
    @Test
    public void testDtasfPrototype1() throws Exception {
        String baseDir = "src/test/resources/proto1/dtasf/";
        applyTestOnDirectory(baseDir);
    }
    
    @Test
    public void testPraksisPrototype1() throws Exception {
        String baseDir = "src/test/resources/proto1/praksis/";
        applyTestOnDirectory(baseDir);
    }
    
    @Test
    public void testCaritasPrototype1() throws Exception {
        String baseDir = "src/test/resources/proto1/caritas/";
        applyTestOnDirectory(baseDir);
    }
}
