package org.ufsc.gbd.wardf.mapping.node;

import com.google.gson.Gson;
import org.apache.jena.graph.Node;

public class Node2Json {

    private final static Gson gson = new Gson();

    public static String mapNode(Node resource){
        return gson.toJson(resource);
    }
}
