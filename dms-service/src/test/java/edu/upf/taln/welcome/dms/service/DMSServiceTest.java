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
import edu.upf.taln.welcome.dms.commons.input.DMInputMetadata;
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
     * @param turn
     * @param expected
     * @throws java.lang.Exception
     */
    public void testSample(int turn, File expected) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        DMSService instance = new DMSService();

        DMInputMetadata metadata = new DMInputMetadata();
        metadata.setDialogueTurn(turn);
        DMInput input = new DMInput();
        input.setMetadata(metadata);
        
        System.out.println("realize_next_turn");
        String expResult = FileUtils.readFileToString(expected, "utf-8");
        DMOutput output = instance.realize_next_turn(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSampleInitial1() throws Exception {
        File turn2File = new File("src/test/resources/initial_messages/dms_output_1.json");
        testSample(1, turn2File);
    }
    
    @Test
    public void testSampleInitial2() throws Exception {
        File turn2File = new File("src/test/resources/initial_messages/dms_output_2.json");
        testSample(3, turn2File);
    }
    
    @Test
    public void testSampleInitial3() throws Exception {
        File turn2File = new File("src/test/resources/initial_messages/dms_output_3.json");
        testSample(5, turn2File);
    }

}
