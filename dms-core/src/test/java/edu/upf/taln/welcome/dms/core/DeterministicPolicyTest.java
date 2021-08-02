package edu.upf.taln.welcome.dms.core;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;

import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author rcarlini
 */
public class DeterministicPolicyTest {

    /**
     * Test of map method, of class DeterministicPolicy.
     */
    @Test
    public void testProto1Opening() throws IOException, WelcomeException {
        
        ObjectMapper mapper = new ObjectMapper();
        Frame frame = mapper.readValue(new File("src/test/resources/proto1/Opening_Frame.json"), Frame.class);
        
        DeterministicPolicy instance = new DeterministicPolicy();
        DialogueMove move = instance.map(frame);

        File expectedFile = new File("src/test/resources/proto1/Opening_Move.json");
        //mapper.writerWithDefaultPrettyPrinter().writeValue(expectedFile, move);
        
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(move);
        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        assertEquals(expResult, result);
    }
    
}
