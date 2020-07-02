package edu.upf.taln.welcome.dms.commons.input;

/**
 *
 * @author rcarlini
 */
public class DMInputMetadata {
	
	private String agentName;
	private String type;
	private String speechActID;
    private int dialogueTurn;

	public String getAgentName() {
		return agentName;
	}
	
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSpeechActID() {
		return speechActID;
	}
	
	public void setSpeechActID(String speechActID) {
		this.speechActID = speechActID;
	}

    public int getDialogueTurn() {
        return dialogueTurn;
    }

    public void setDialogueTurn(int dialogueTurn) {
        this.dialogueTurn = dialogueTurn;
    }
}
