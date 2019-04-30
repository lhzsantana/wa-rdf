package org.ufsc.gbd.wardf.fragmenter;

import org.ufsc.gbd.wardf.mapping.NoSQLMapper;
import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;
import org.ufsc.gbd.wardf.partition.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.wac.WAc;

import java.util.ArrayList;
import java.util.List;

public class Fragmenter {

    private WAc wac = WAc.getInstance();
    private final DistributionDictionary dictionary = new DistributionDictionary();

    public List<Fragment> fragment(Triple triple) {

        List<Fragment> fragments = new ArrayList<>();

        for(Shape shape : wac.getShape(triple)){

            List<Triple> triples = new ArrayList<>();
            triples.add(triple);
            triples.addAll(expandTriple(triple, shape));
            fragments.add(new Fragment(triple, shape, triples));
        }

        return fragments;
    }

    private List<Triple> expandTriple(Triple triple, Shape shape){

        List<Triple> triples = new ArrayList<>();

        List<TriplePattern> triplePatterns = wac.getTypicalWorkload(triple);

        NoSQLMapper noSQLMapper = dictionary.checkDictionary(triplePatterns, shape);

        List<Triple> starResponse = noSQLMapper.query(triplePatterns);
        triples.addAll(starResponse);

        return triples;
    }
}
