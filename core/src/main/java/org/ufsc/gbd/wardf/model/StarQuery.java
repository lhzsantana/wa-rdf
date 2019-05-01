package org.ufsc.gbd.wardf.model;

import com.google.common.collect.Multimap;
import org.apache.jena.graph.Node;

public class StarQuery extends Query {

    private Node center;

    public Node getCenter() {
        return center;
    }

    public void setCenter(Node center) {
        this.center = center;
    }
}
