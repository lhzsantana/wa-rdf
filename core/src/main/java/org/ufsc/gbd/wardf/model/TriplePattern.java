package org.ufsc.gbd.wardf.model;

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.optimizer.reorder.PatternTriple;

public class TriplePattern {

    private PatternTriple patternTriple;

    public TriplePattern(Node subject, Node predicate, Node object){

    }


    public PatternTriple getPatternTriple() {
        return patternTriple;
    }

    public Node getObject() {
        return null;
    }

    public Node getSubject() {
        return null;
    }

    public Node getPredicate() {
        return null;
    }
}
