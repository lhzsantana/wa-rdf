package org.ufsc.gbd.wardf.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.ufsc.gbd.wardf.fragmenter.Fragment;

public abstract class AbstractMapper {

    final static Log logger = LogFactory.getLog(AbstractMapper.class);

    public void mapAll(Model model){

        logger.info("--------------------------------------------");

        int triple = 0;
        long tripleSum = 0;
        int runs = 0;
        long sum = 0;
        int totalRuns=1000;

        StmtIterator it =  model.listStatements();
        while (it.hasNext()) {

            Statement stmt = it.next();
            logger.info(stmt.toString());
            tripleSum=0;

            for(int i=0;i<totalRuns;i++){

                long tStart = System.nanoTime();
                store(stmt);
                long tEnd = System.nanoTime();
                sum += tEnd - tStart;
                tripleSum +=  tEnd - tStart;
                runs++;
            }

            triple++;
            logger.info("Elapsed time for triple "+triple+" = "+tripleSum/totalRuns);
        }

        logger.info("Elapsed time "+sum/runs);
    }

    abstract protected void store(Statement stmt );

    public abstract void register(Fragment fragment);
}
