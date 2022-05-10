package edu.upf.taln.welcome.dms.core;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import edu.upf.taln.welcome.dms.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.dms.commons.input.Frame;
import edu.upf.taln.welcome.dms.commons.output.DialogueMove;
import edu.upf.taln.welcome.dms.commons.output.SpeechAct;

/**
 *
 * @author rcarlini
 */
public class DeterministicPolicyTest {

	@Before
	public void reset() {
		SpeechAct.resetCounter();
		DialogueMove.resetCounter();
	}	
	
    /**
     * Test of map method, of class DeterministicPolicy.
     */
    @Test
    public void testProto1Opening() throws IOException, WelcomeException {
        
        ObjectMapper mapper = new ObjectMapper();
        Frame frame = mapper.readValue(new File("src/test/resources/proto1/DTASF_Opening_Framed.json"), Frame.class);
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(frame));
        
        DeterministicPolicy instance = new DeterministicPolicy();
        DialogueMove move = instance.map(frame);

        File expectedFile = new File("src/test/resources/proto1/DTASF_Opening_Move.json");
        //mapper.writerWithDefaultPrettyPrinter().writeValue(expectedFile, move);
        
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(move);
        //System.out.println(result);
        
        String expResult = FileUtils.readFileToString(expectedFile, "utf8");
        assertEquals(expResult, result);
    }
    
}
