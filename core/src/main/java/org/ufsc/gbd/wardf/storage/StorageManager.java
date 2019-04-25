package org.ufsc.gbd.wardf.storage;

import org.ufsc.gbd.wardf.cache.Cache;
import org.ufsc.gbd.wardf.mapping.NoSQLMapper;
import org.ufsc.gbd.wardf.partition.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.fragmenter.Fragmenter;
import org.ufsc.gbd.wardf.mapping.RedisMapper;
import org.ufsc.gbd.wardf.model.Triple;

public class StorageManager {

    private Fragmenter fragmenter = new Fragmenter();
    private DistributionDictionary dictionary = new DistributionDictionary();
    private RedisMapper redisMapper = new RedisMapper();

    private final Cache cache = new Cache();

    public void store(Triple triple){

        String txId = redisMapper.storeTemporary(triple);

        for(Fragment fragment : fragmenter.fragment(triple)) {

            new Thread(() -> {
                NoSQLMapper noSQLMapper = dictionary.registerFragment(fragment);
                noSQLMapper.store(fragment);
                cache.expire(fragment.getTriples());
            }).start();
        }

        redisMapper.deleteTemporary(txId);
    }

}
