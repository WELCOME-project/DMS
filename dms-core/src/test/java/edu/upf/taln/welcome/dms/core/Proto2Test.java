package edu.upf.taln.welcome.dms.core;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author rcarlini
 */
public class Proto2Test {

    /**
     * Test of map method, of class DeterministicPolicy.
     */
    @Test
    public void testScenarioIntroduction() throws IOException, WelcomeException {
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter();
		
        Frame frame = mapper.readValue(new File("src/test/resources/proto2/ScenarioIntroduction_Frame.json"), Frame.class);
        //System.out.println(writer.writeValueAsString(frame));
        
        DeterministicPolicy instance = new DeterministicPolicy();
        DialogueMove move = instance.map(frame);

        File expectedFile = new File("src/test/resources/proto2/ScenarioIntroduction_Move.json");
        //writer.writeValue(expectedFile, move);
        
        String result = writer.writeValueAsString(move);
        //System.out.println(result);
        
        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        assertEquals(expResult, result);
    }
    
}
