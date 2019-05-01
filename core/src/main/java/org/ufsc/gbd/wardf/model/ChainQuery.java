package org.ufsc.gbd.wardf.model;

import java.util.ArrayList;
import java.util.List;

public class ChainQuery extends Query{

    private List<TriplePattern> chain = new ArrayList<>();


    public List<TriplePattern> getChain() {
        return chain;
    }

    public void setChain(List<TriplePattern> chain) {
        this.chain = chain;
    }
}
