package edu.upf.taln.welcome.dms.service;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.welcome.dms.commons.input.DMInput;
import edu.upf.taln.welcome.dms.commons.input.DMInputData;
import edu.upf.taln.welcome.dms.commons.input.DMInputMetadata;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DMOutput;

/**
 *
 * @author rcarlini
 */
public class DMSServiceTest {
    
    public DMSServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Base test to check outputs
     * @param input
     * @param expected
     * @throws java.lang.Exception
     */
    public void testSample(DMInput input, File expected) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        DMSService instance = new DMSService();
        
        System.out.println("realize_next_turn");
        String expResult = FileUtils.readFileToString(expected, "utf-8");
        DMOutput output = instance.realize_next_turn(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSampleInitialExtrapolateTurn() throws Exception {

        Frame frame = new Frame();
        DMInputData data = new DMInputData();
        data.setFrame(frame);
        DMInputMetadata metadata = new DMInputMetadata();
        //metadata.setDialogueTurn(1);
        DMInput input = new DMInput();
        input.setMetadata(metadata);
        input.setData(data);

        File turn2File = new File("src/test/resources/initial_messages/dms_output_1.json");
        testSample(input, turn2File);

        frame.setName("Karim");
        File turn4File = new File("src/test/resources/initial_messages/dms_output_2.json");
        testSample(input, turn4File);

        frame.setAddress("Sant Pau, 2");
        File turn6File = new File("src/test/resources/initial_messages/dms_output_3.json");
        testSample(input, turn6File);
    }
    
    @Test
    public void testSampleInitial1() throws Exception {

        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(1);
        DMInput input = new DMInput();
        input.setMetadata(metadata);

        File turn2File = new File("src/test/resources/initial_messages/dms_output_1.json");
        testSample(input, turn2File);
    }
    
    @Test
    public void testSampleInitial2() throws Exception {
        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(3);
        DMInput input = new DMInput();
        input.setMetadata(metadata);
        
        File turn2File = new File("src/test/resources/initial_messages/dms_output_2.json");
        testSample(input, turn2File);
    }
    
    @Test
    public void testSampleInitial3() throws Exception {
        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(5);
        DMInput input = new DMInput();
        input.setMetadata(metadata);

        File turn2File = new File("src/test/resources/initial_messages/dms_output_3.json");
        testSample(input, turn2File);
    }

}
