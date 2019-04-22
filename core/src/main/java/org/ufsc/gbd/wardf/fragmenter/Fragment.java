package org.ufsc.gbd.wardf.fragmenter;

import org.ufsc.gbd.wardf.model.Triple;

import java.util.ArrayList;
import java.util.List;

public class Fragment {
    public static final Object STAR = "";
    public static final Object CHAIN = "";

    private List<Triple> triples = new ArrayList<>();

    public Object getType() {

        return null;
    }

    public List<Triple> getTriples() {
        return triples;
    }

    public void setTriples(List<Triple> triples) {
        this.triples = triples;
    }
}
