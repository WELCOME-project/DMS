package edu.upf.taln.welcome.dms.commons.output;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DMOutput {
    
    @NotNull
    private String conll;

    public String getConll() {
        return conll;
    }

    public void setConll(String conll) {
        this.conll = conll;
    }    
}
