import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.ufsc.gbd.wardf.Server;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;

import java.io.InputStream;

public class Main {

    private static final  String testFileName  = "vc-db-1.rdf";

    public static void main (String args[]) {

        Server server = new Server();

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
            server.store(triple);
        }


        String sparql = "SELECT ?thing ?str WHERE { " +
                "?thing a <predicate> . " +
                "?thing <predicate> ?str .}";

        Query query = new Query(QueryFactory.create(sparql));
        server.query(query);
    }

}
