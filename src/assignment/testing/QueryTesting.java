package assignment.testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import assignment.daos.IndexingDao;
import assignment.daos.QueryDao;
import assignment.pojos.Document;
import assignment.pojos.Term;

public class QueryTesting {
	private static Logger log = Logger.getLogger("QueryTesting");

	public static void main(String[] args) {
		if (args[0] == null || args[0].equals("")) {
			log.log(Level.SEVERE, "Please provide Index Directory path as argument");
			System.exit(0);
		}
		String location_of_index = args[0];
		IndexingDao indexingDao = new IndexingDao();
		QueryDao queryDao = new QueryDao();
		HashMap<String, Term> indexmap = indexingDao.readIndex(location_of_index);
		ArrayList<String> alldocuments = indexingDao.readProcessedDocumentsInIndex(location_of_index);
		if (indexmap != null) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your query: ");
			String input = sc.nextLine();
			HashMap<String, Term> resultMap = queryDao.retrieveResults(input, indexmap, alldocuments);
			if (resultMap != null) {
				System.out.println("Query Results:");
				if (input.contains("AND") || input.contains("OR") || input.contains("NOT")) {
					if (resultMap.size() == 0)
						System.out.println("--No Documents found--");
					for (String t : resultMap.keySet()) {
						Term term = resultMap.get(t);
						System.out.println("\"" + input + "\" is found in documents:");
						HashSet<Document> documents = term.getDocuments();
						if (documents.size() == 0) {
							System.out.println("--No Documents found--");
							break;
						}
						for (Document d : documents) {
							System.out.println(d.getDocumentId());
						}
						break;
					}
				} else {
					if (resultMap.size() == 0)
						System.out.println("--No Documents found--");
					for (String t : resultMap.keySet()) {
						Term term = resultMap.get(t);
						System.out.println(t + " is found in documents:");
						HashSet<Document> documents = term.getDocuments();
						if (documents.size() == 0) {
							System.out.println("--No Documents found--");
							continue;
						}
						for (Document d : documents) {
							System.out.println(d.getDocumentId());
						}
					}
				}
			} else {
				System.out.println("--No Documents found--");
			}
		} else {
			System.out.println("--Unable to load index--");
		}

	}

}
