package org.ufsc.gbd.wardf.wac;

import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.HashSet;
import java.util.Set;

public class TypicalWorkload {

    private Set<Shape> shapes = new HashSet<>();
    private Set<TriplePattern> triplePatterns = new HashSet<>();

    public Set<TriplePattern> getTriplePatterns() {
        return triplePatterns;
    }

    public void setTriplePatterns(Set<TriplePattern> triplePatterns) {
        this.triplePatterns = triplePatterns;
    }

    public Set<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(Set<Shape> shapes) {
        this.shapes = shapes;
    }
}
