package org.ufsc.gbd.wardf.mapping;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisMapper {

    Jedis jedis = new Jedis();

    public void store(Query subquery, List<Triple> response) {
        jedis.set(subquery.getTriplePatterns().toString(), response.toString());
    }

    public Set<Triple> query(Set<TriplePattern> triplePatterns) {

        jedis.get(triplePatterns.toString());

        return new HashSet<>();
    }

    public void expire(List<Triple> triples) {

    }

    public String storeTemporary(Triple triple) {

        String id = UUID.randomUUID().toString();

        jedis.set(id, triple.toString());

        return id;
    }

    public void deleteTemporary(String id) {
        jedis.del(id);
    }
}
