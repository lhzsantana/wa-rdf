package org.ufsc.gbd.wardf.partition.dictionary;

import org.ufsc.gbd.wardf.mapping.MongoDBMapper;
import org.ufsc.gbd.wardf.mapping.Neo4JMapper;
import org.ufsc.gbd.wardf.mapping.NoSQLMapper;
import org.ufsc.gbd.wardf.model.*;

import java.util.*;

public class DistributionDictionary {

    private static Set<Partition> chainPartitions = new HashSet<>();
    private static Set<Partition> starPartitions = new HashSet<>();

    private static Map<Fragment, Partition> dictionaryF2P = new HashMap<>();
    private static Map<Partition, Fragment> dictionaryP2F = new HashMap<>();

    public void registerPartitions(Partition partition){
        chainPartitions.add(partition);
        starPartitions.add(partition);
    }

    public NoSQLMapper registerFragment(Fragment fragment){

        NoSQLMapper noSQLMapper = null;

        Partition partition = choosePartition(fragment);

        dictionaryF2P.put(fragment, partition);
        dictionaryP2F.put(partition, fragment);

        if (fragment.getShape().equals(Shape.CHAIN)) {
            noSQLMapper = new Neo4JMapper(partition);
        }
        else{
            noSQLMapper = new MongoDBMapper(partition);
        }

        return noSQLMapper;
    }

    public NoSQLMapper checkDictionary(Set<TriplePattern> triplePatterns, Shape shape){

        if (shape.equals(Shape.STAR)) {
            return new MongoDBMapper(new Partition());
        }
        else{
            return new Neo4JMapper(new Partition());
        }
    }

    public Partition choosePartition(Fragment fragment){
        return new Partition();
    }
}
