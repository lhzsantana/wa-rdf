package org.ufsc.gbd.wardf.model;

import java.util.List;

public class Query {

    private List<TriplePattern> triplePatterns;

    private org.apache.jena.query.Query query;

    private Shape shape;

    public Query(){}

    public Query(org.apache.jena.query.Query query){
        this.query=query;
    }

    public org.apache.jena.query.Query getQuery() {
        return query;
    }

    public List<TriplePattern> getTriplePatterns() {
        return triplePatterns;
    }

    public void setTriplePatterns(List<TriplePattern> triplePatterns) {
        this.triplePatterns = triplePatterns;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
