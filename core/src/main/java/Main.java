
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.engine.optimizer.reorder.PatternTriple;
import org.apache.jena.util.FileManager;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.querying.QueryingManager;
import org.ufsc.gbd.wardf.storage.StorageManager;


import java.io.InputStream;

public class Main {

    private static final Log logger = LogFactory.getLog(Main.class);
    private static final  String testFileName  = "vc-db-1.rdf";

    private static final StorageManager storageManager = new StorageManager();
    private static final QueryingManager queryingManager = new QueryingManager();

    public static void main (String args[]) {
        Main main = new Main();

        main.testStore();

        main.testQuery();
    }

    private void testStore(){

        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open( testFileName );
        if (in == null) {
            throw new IllegalArgumentException( "File: " + testFileName + " not found");
        }

        model.read(in, "");

        StmtIterator it =  model.listStatements();

        while (it.hasNext()) {

            Statement stmt = it.next();
            Triple triple = new Triple(stmt);
            storageManager.store(triple);
        }
    }

    private void testQuery(){

        String sparql = "SELECT ?thing ?str WHERE { " +
                "?thing a <predicate> . " +
                "?thing <predicate> ?str .}";

        Query query = new Query(QueryFactory.create(sparql));

        //queryingManager.query(query);
    }

}
