package org.ufsc.gbd.wardf.mapping;

import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;

import java.util.List;

public abstract class NoSQLMapper {
    public abstract List<Triple> query(Query subQuery);

    public abstract void store(Fragment fragment);
}
