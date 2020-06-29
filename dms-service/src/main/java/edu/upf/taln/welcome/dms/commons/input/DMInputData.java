package edu.upf.taln.welcome.dms.commons.input;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DMInputData {
    
    @NotNull
    private String conll;

    public String getConll() {
        return conll;
    }

    public void setConll(String conll) {
        this.conll = conll;
    }    
}
