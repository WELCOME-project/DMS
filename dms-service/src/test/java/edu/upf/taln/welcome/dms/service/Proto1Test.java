package edu.upf.taln.welcome.dms.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
import java.util.TreeMap;
import org.junit.jupiter.api.Test;


/**
 *
 * @author rcarlini
 */
public class Proto1Test {

	private final Logger logger = Logger.getLogger(Proto1Test.class.getName());

	        
	private final ObjectWriter writer = new ObjectMapper()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .writerWithDefaultPrettyPrinter();

    /**
     * Base function to test a jsonld frame as is.
     * 
     * @param inputFile
     * @throws Exception 
     */
    public void testFrame(File inputFile) throws Exception {
    	
        boolean overrideExpected = false;
        String baseName = FilenameUtils.getBaseName(inputFile.getName());

        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
        File newOutputFile = new File(inputFile.getParent(), baseName + "_Frame.json");
        writer.writeValue(newOutputFile, frame);

        DMSService instance = new DMSService();
        DialogueMove output = instance.realizeNextTurn(frame);

        File expectedFile = new File(inputFile.getParent(), baseName + "_Move.json");
        if (!expectedFile.exists() || overrideExpected) {
            writer.writeValue(expectedFile, output);        
        }

        String expected = FileUtils.readFileToString(expectedFile, "utf-8");    		
        String result = writer.writeValueAsString(output);
        //System.out.println(result);

        Assertions.assertEquals(expected, result);
    }

    /**
     * Base function to test separately each slot of a jsonld frame.
     * 
     * @param inputFile
     * @throws Exception 
     */
    public void testSeparateSlot(File inputFile) throws Exception {
    	
        boolean overrideExpected = false;
        String baseName = FilenameUtils.getBaseName(inputFile.getName());
        
    	Map<String, Frame> frames = generateAllPossiblePendingSlots(inputFile);
        
    	for (String slotId : frames.keySet()) {
    		logger.log(Level.INFO, "Test file:{0}\tSlot: {1}", new Object[]{inputFile.getName(), slotId});
    		
    		Frame frame = frames.get(slotId);
	        
	        DMSService instance = new DMSService();
	        DialogueMove output = instance.realizeNextTurn(frame);
	        
            File expectedFile = new File(inputFile.getParent(), baseName + "_" + slotId + "_Move.json");
            if (!expectedFile.exists() || overrideExpected) {
                writer.writeValue(expectedFile, output);        
            }
            
            String expected = FileUtils.readFileToString(expectedFile, "utf-8");    		
    		String result = writer.writeValueAsString(output);
    		//System.out.println(result);
    		
            Assertions.assertEquals(expected, result);
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
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
        
    	List<Slot> slots = frame.slots;
        Map<String, Frame> frames = new TreeMap();
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

    static File[] getDirectoryInputs(String baseDir) {
        
        File folder = new File(baseDir);
        
        IOFileFilter filterFile = FileFileFilter.FILE;
        SuffixFileFilter filterSuffix = new SuffixFileFilter(".jsonld");
        FilenameFilter filter = new AndFileFilter(filterFile, filterSuffix);

        File[] fileList = folder.listFiles(filter);
        Arrays.sort(fileList);
        
        return fileList;
    }
    
    static Stream<File> proto1DtasfInputs() {
        String baseDir = "src/test/resources/proto1/dtasf/";
        File[] fileList = getDirectoryInputs(baseDir);
        
        return Stream.of(fileList);
    }
    
    static Stream<File> proto1PraksisInputs() {
        String baseDir = "src/test/resources/proto1/praksis/";
        File[] fileList = getDirectoryInputs(baseDir);
        
        return Stream.of(fileList);
    }
    
    static Stream<File> proto1CaritasInputs() {
        String baseDir = "src/test/resources/proto1/caritas/";
        File[] fileList = getDirectoryInputs(baseDir);
        
        return Stream.of(fileList);
    }
    
    @BeforeEach
    public void resetCounters() {
        DialogueMove.resetCounter();
        SpeechAct.resetCounter();        
    }
    
    @DisplayName("DTASF inputs separate tests")
    @ParameterizedTest(name = "{0}")
    @MethodSource("proto1DtasfInputs")
    public void testDtasfPrototype1Separate(File jsonLDInput) throws Exception {
        testSeparateSlot(jsonLDInput);
    }
    
    @DisplayName("DTASF inputs tests")
    @ParameterizedTest(name = "{0}")
    @MethodSource("proto1DtasfInputs")
    public void testDtasfPrototype1(File jsonLDInput) throws Exception {
        testFrame(jsonLDInput);
    }
    
    @DisplayName("Praksis inputs tests")
    @ParameterizedTest(name = "{0}")
    @MethodSource("proto1PraksisInputs")
    public void testPraksisPrototype1Separate(File jsonLDInput) throws Exception {
        testSeparateSlot(jsonLDInput);
    }
    
    @DisplayName("Caritas inputs tests")
    @ParameterizedTest(name = "{0}")
    @MethodSource("proto1CaritasInputs")
    public void testCaritasPrototype1Separate(File jsonLDInput) throws Exception {
        testSeparateSlot(jsonLDInput);
    }

	@Test
    public void testClarifyPrototype1() throws Exception {
		
		File inputFile = new File("src/test/resources/proto1/input_to_dms.txt");
		
        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
		
		DMSService instance = new DMSService();
		DialogueMove output = instance.realizeNextTurn(frame);
		
		//String result = writer.writeValueAsString(output);
		//System.out.println(result);
    }

	@Test
    public void testElaboratePrototype1() throws Exception {
		
		File inputFile = new File("src/test/resources/proto1/input_to_dms_elaborate.json");
		
        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
		
		DMSService instance = new DMSService();
		DialogueMove output = instance.realizeNextTurn(frame);
		
		//String result = writer.writeValueAsString(output);
		//System.out.println(result);
    }

	@Test
    public void testError400Prototype1() throws Exception {
		
		File inputFile = new File("src/test/resources/proto1/error_400.json");
		
        ObjectMapper mapper = new ObjectMapper();
    	JsonNode input = mapper.readValue(inputFile, JsonNode.class);
    	
    	URL contextFile = JsonLDUtils.class.getResource("/welcome-dms-framed.jsonld");
    	Frame frame = JsonLDUtils.readFrame(input.toString(), contextFile);
		
		DMSService instance = new DMSService();
		DialogueMove output = instance.realizeNextTurn(frame);
		
		String result = writer.writeValueAsString(output);
		System.out.println(result);
    }
}
