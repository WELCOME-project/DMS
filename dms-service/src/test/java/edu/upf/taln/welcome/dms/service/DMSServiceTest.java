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
import org.apache.commons.io.FilenameUtils;

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

	        
	private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .writerWithDefaultPrettyPrinter();

    /**
     * Base function to test all slots of a jsonld.
     * 
     * @param inputFile
     * @throws Exception 
     */
    public void testSample(File inputFile) throws Exception {
    	
        boolean overrideExpected = false;
        String baseName = FilenameUtils.getBaseName(inputFile.getName());
        
    	Map<String, Frame> frames = generateAllPossiblePendingSlots(inputFile);
        
    	for (String slotId : frames.keySet()) {
    		logger.log(Level.INFO, "Test file:{0}\tSlot: {1}", new Object[]{inputFile.getName(), slotId});
    		
    		Frame frame = frames.get(slotId);
	        
	        DMSService instance = new DMSService();
	        JsonNode output = instance.realizeNextTurn(frame);
	        
            File expectedFile = new File(inputFile.getParent(), baseName + "_" + slotId + "_Move.json");
            if (!expectedFile.exists() || overrideExpected) {
                writer.writeValue(expectedFile, output);        
            }
            
            String expected = FileUtils.readFileToString(expectedFile, "utf-8");    		
    		String result = writer.writeValueAsString(output);
    		//System.out.println(result);
    		
    		assertEquals(expected, result);
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
    	
        String baseName = FilenameUtils.getBaseName(inputFile.getName());

        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input, contextFile);
    	List<Slot> slots = frame.slots;

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

//			File newOutputFile = new File(inputFile.getParent(), baseName + "_" + slotId + "_Frame.json");
// 			writer.writeValue(newOutputFile, frame);

        }
    	
    	return frames;
    }
    
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
