package edu.upf.taln.welcome.dms.commons.input;

public class DMInput {
    private DMInputMetadata metadata;
    private DMInputData data;

    public DMInputMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DMInputMetadata metadata) {
        this.metadata = metadata;
    }

    public DMInputData getData() {
        return data;
    }

    public void setData(DMInputData data) {
        this.data = data;
    }
}
