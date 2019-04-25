package org.ufsc.gbd.wardf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ufsc.gbd.wardf.model.Partition;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.partition.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.querying.QueryingManager;
import org.ufsc.gbd.wardf.storage.StorageManager;

import java.util.List;

public class Server {

    private static final Log logger = LogFactory.getLog(Server.class);
    private static final StorageManager storageManager = new StorageManager();
    private static final QueryingManager queryingManager = new QueryingManager();
    private final DistributionDictionary dictionary = new DistributionDictionary();

    public void configure(List<Partition> partitions){
        for(Partition partition:partitions) {
            dictionary.registerPartitions(partition);
        }
    }

    public void store(Triple triple){
        storageManager.store(triple);
    }

    public void query(Query query){
        queryingManager.query(query);
    }


}
