package org.ufsc.gbd.wardf.model;

import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;

import java.util.List;
import java.util.UUID;

public class Fragment {

    private final String id = UUID.randomUUID().toString();
    private Shape shape;
    private List<Triple> triples;
    private Triple coreTriple;

    public Fragment(Triple coreTriple, Shape shape, List<Triple> triples){
        this.coreTriple=coreTriple;
        this.shape=shape;
        this.triples=triples;
    }

    public String getId() {
        return id;
    }

    public Shape getShape() {
        return shape;
    }

    public List<Triple> getTriples() {
        return triples;
    }

}
