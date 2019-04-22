package org.ufsc.gbd.wardf.model;

public class Query {

    private org.apache.jena.query.Query query;

    public Query(org.apache.jena.query.Query query){
        this.query=query;
    }

    public org.apache.jena.query.Query getQuery() {
        return query;
    }
}
