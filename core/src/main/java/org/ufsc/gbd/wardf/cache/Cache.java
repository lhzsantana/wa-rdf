package org.ufsc.gbd.wardf.cache;

import org.ufsc.gbd.wardf.mapping.RedisMapper;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.List;

public class Cache {

    private final RedisMapper redisMapper = new RedisMapper();

    public List<Triple> checkCache(List<TriplePattern> triplePatterns){
        return redisMapper.query(triplePatterns);
    }

    public void store(Query subquery, List<Triple> response) {
        redisMapper.store(subquery, response);
    }

    public void expire(List<Triple> triples) {
        redisMapper.expire(triples);
    }
}
