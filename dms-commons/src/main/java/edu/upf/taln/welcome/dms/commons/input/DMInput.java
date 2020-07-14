package edu.upf.taln.welcome.dms.commons.input;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DMInput {

    @JsonFormat(shape=JsonFormat.Shape.ARRAY)
    private List<KBInfo> kbInfoList;

    public List<KBInfo> getKbInfoList() {
        return kbInfoList;
    }

    public void setKbInfoList(List<KBInfo> kbInfoList) {
        this.kbInfoList = kbInfoList;
    }
}
