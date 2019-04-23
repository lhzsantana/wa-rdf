package org.ufsc.gbd.wardf.storage;

import org.ufsc.gbd.wardf.cache.Cache;
import org.ufsc.gbd.wardf.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.fragmenter.Fragmenter;
import org.ufsc.gbd.wardf.mapping.MongoDBMapper;
import org.ufsc.gbd.wardf.mapping.Neo4JMapper;
import org.ufsc.gbd.wardf.mapping.RedisMapper;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;

public class StorageManager {

    private Fragmenter fragmenter = new Fragmenter();
    private DistributionDictionary dictionary = new DistributionDictionary();
    private MongoDBMapper mongoDBMapper = new MongoDBMapper();
    private Neo4JMapper neo4JMapper = new Neo4JMapper();
    private RedisMapper redisMapper = new RedisMapper();

    private final Cache cache = new Cache();

    public void store(Triple triple){

        String txId = redisMapper.storeTemporary(triple);

        for(Fragment fragment : fragmenter.fragment(triple)) {

            new Thread(() -> {
                dictionary.registerFragment(fragment);

                if (fragment.getShape().equals(Shape.STAR)) {
                    mongoDBMapper.store(fragment);
                } else if (fragment.getShape().equals(Shape.CHAIN)) {
                    neo4JMapper.store(fragment.getTriples());
                }
                cache.expire(fragment.getTriples());
            }).start();
        }

        redisMapper.deleteTemporary(txId);
    }

}
