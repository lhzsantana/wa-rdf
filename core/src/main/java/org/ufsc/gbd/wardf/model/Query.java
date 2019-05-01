package org.ufsc.gbd.wardf.model;

import org.apache.jena.sparql.core.PathBlock;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementPathBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Query {

    private Set<TriplePattern> triplePatterns = new HashSet<>();

    private org.apache.jena.query.Query q;

    private Shape shape;

    public Query(){}

    public Query(org.apache.jena.query.Query q){

        PathBlock pathBlock = ((ElementPathBlock) ((ElementGroup) q.getQueryPattern()).getElements().get(0)).getPattern();

        for(TriplePath triplePath : pathBlock.getList()){
            triplePatterns.add(new TriplePattern(triplePath.getSubject(), triplePath.getPredicate(), triplePath.getObject()));
        }

        this.q=q;
    }

    public org.apache.jena.query.Query getQuery() {
        return q;
    }

    public Set<TriplePattern> getTriplePatterns() {
        return triplePatterns;
    }

    public void setTriplePatterns(Set<TriplePattern> triplePatterns) {
        this.triplePatterns = triplePatterns;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
