package org.ufsc.gbd.wardf.model;

import org.apache.jena.rdf.model.Statement;

public class Triple  {

    private Statement statement;

    public Triple(Statement statement){
        this.statement=statement;
    }

    public Statement getStatement() {
        return statement;
    }
}
