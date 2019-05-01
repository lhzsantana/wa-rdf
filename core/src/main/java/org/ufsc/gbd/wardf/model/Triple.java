package org.ufsc.gbd.wardf.model;

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Statement;

public class Triple  {

    private Node subject;

    private Node predicate;

    private Node object;

    public Triple(Statement statement){
        this.subject=statement.getSubject().asNode();
        this.predicate=statement.getPredicate().asNode();
        this.object=statement.getObject().asNode();
    }

    public Triple(Node subject, Node predicate, Node object){
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

    public void setPredicate(Node predicate) {
        this.predicate = predicate;
    }

    public Node getObject() {
        return object;
    }

    public void setObject(Node object) {
        this.object = object;
    }
}
