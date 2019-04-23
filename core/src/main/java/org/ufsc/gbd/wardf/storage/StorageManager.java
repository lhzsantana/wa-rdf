package org.ufsc.gbd.wardf.storage;

import org.ufsc.gbd.wardf.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.fragmenter.Fragment;
import org.ufsc.gbd.wardf.fragmenter.Fragmenter;
import org.ufsc.gbd.wardf.mapping.MongoDBMapper;
import org.ufsc.gbd.wardf.mapping.Neo4JMapper;
import org.ufsc.gbd.wardf.model.Triple;

public class StorageManager {

    private Fragmenter fragmenter = new Fragmenter();
    private DistributionDictionary dictionary = new DistributionDictionary();
    private MongoDBMapper mongoDBMapper = new MongoDBMapper();
    private Neo4JMapper neo4JMapper = new Neo4JMapper();

    public void store(Triple triple){

        Fragment fragment = fragmenter.fragment(triple);

        dictionary.registerFragment(fragment);

        if(fragment.getType().equals(Fragment.STAR)){
            mongoDBMapper.store(fragment.getTriples());
        }else if(fragment.getType().equals(Fragment.CHAIN)){
            neo4JMapper.store(fragment.getTriples());
        }
    }
}
