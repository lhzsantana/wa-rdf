package org.ufsc.gbd.wardf.mapping;

import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.List;
import java.util.Set;

public abstract class NoSQLMapper {
    public abstract void store(Fragment fragment);
    public abstract List<Triple> query(Query subQuery);
    public abstract List<Triple> query(Set<TriplePattern> triplePatterns);
}
