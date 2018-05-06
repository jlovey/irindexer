package assignment.testing;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.*;

import com.google.gson.Gson;

import assignment.daos.IndexingDao;
import assignment.daos.ProcessingDao;
import assignment.pojos.Term;

/**
 * @author lovey joshi
 *
 */
public class IndexTesting {

	private static Logger log = Logger.getLogger("IndexTesting");

	public static void main(String[] args) {
		if (args[0] == null || args[0].equals("")) {
			log.log(Level.SEVERE, "Please provide directory path as argument");
			System.exit(0);
		}
		if (args[1] == null || args[1].equals("")) {
			log.log(Level.SEVERE, "Please provide directory path for index as argument");
			System.exit(0);
		}
		String location_of_files = args[0];
		String location_of_index = args[1];

		File f_dir = new File(location_of_files);
		if (!f_dir.exists() || !f_dir.isDirectory()) {
			log.log(Level.SEVERE, "Specified file directory doesn't exist! Please re-intialize with proper path");
			System.exit(0);
		}

		ProcessingDao dao = new ProcessingDao();
		IndexingDao indexingDao = new IndexingDao();
		HashMap<String, Term> indexmap = new HashMap<>();

		File[] f = f_dir.listFiles();
		String documents = "";
		if (f.length == 0) {
			log.log(Level.WARNING, "Specified file directory is empty & has no readable documents");
			System.exit(0);
		}
		for (int i = 0; i < f.length; i++) {

			log.log(Level.INFO, "Processing document: " + f[i].getName());
			documents += f[i].getName();
			documents += "\n";
			String text = dao.processDocument(f[i]);
			String documentId = (f[i].getName());
			dao.createIndexTerms(text, documentId, indexmap);

			indexingDao.writeIndex(indexmap, location_of_index);
		}

		Gson gson = new Gson();
		log.log(Level.INFO, "Document indexed: " + f.length);
		indexingDao.writeProcessedDocumentInIndex(documents, location_of_index);
		log.log(Level.INFO, "Size of dictionary terms:" + indexmap.size());
		log.log(Level.INFO, "Index successfully created at path:" + location_of_index);
		log.log(Level.INFO, "Use Query.jar to query the index!");
	}

}
