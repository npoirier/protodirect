package com.proto.model.event;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 *
 */
public class Configuration {
    private List<Entry> entryList;

    @XmlElement(name = "entry")
    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }
}
