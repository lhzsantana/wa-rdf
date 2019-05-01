package org.ufsc.gbd.wardf.cache;

import org.ufsc.gbd.wardf.mapping.RedisMapper;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.List;
import java.util.Set;

public class Cache {

    private final RedisMapper redisMapper = new RedisMapper();

    public Set<Triple> checkCache(Set<TriplePattern> triplePatterns){
        return redisMapper.query(triplePatterns);
    }

    public void store(Query subquery, List<Triple> response) {
        //redisMapper.store(subquery, response);
    }

    public void expire(List<Triple> triples) {
        redisMapper.expire(triples);
    }
}
