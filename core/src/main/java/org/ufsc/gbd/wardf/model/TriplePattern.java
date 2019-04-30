package org.ufsc.gbd.wardf.model;

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.optimizer.reorder.PatternTriple;

public class TriplePattern {

    private Node subject;
    private Node predicate;
    private Node object;

    public TriplePattern(Node subject, Node predicate, Node object){
        this.subject=subject;
        this.predicate=predicate;
        this.object=object;
    }

    public Node getSubject() {
        return subject;
    }

    public void setSubject(Node subject) {
        this.subject = subject;
    }

    public Node getPredicate() {
        return predicate;
    }

    public Node getObject() {
        return object;
    }

    public void setObject(Node object) {
        this.object = object;
    }
}
