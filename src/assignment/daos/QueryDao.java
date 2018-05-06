package assignment.daos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import assignment.pojos.Document;
import assignment.pojos.Term;

public class QueryDao {

	public HashMap<String, Term> retrieveResults(String query, HashMap<String, Term> indexmap,
			ArrayList<String> alldocuments) {
		String[] querySplit = query.split(" ");
		ArrayList<String> queryTerms = new ArrayList<>();
		for (int i = 0; i < querySplit.length; i++) {
			if (!querySplit[i].equals("AND") && !querySplit[i].equals("OR") && !querySplit[i].equals("NOT")) {
				queryTerms.add(querySplit[i]);
			}
		}

		HashMap<String, Term> indexOfQueryTerms = new HashMap<>();
		for (String t : queryTerms) {
			if (indexmap.containsKey(t.toLowerCase()))
				indexOfQueryTerms.put(t.toLowerCase(), indexmap.get(t.toLowerCase()));
		}

		String[] notQuery = processQuery(query, "NOT");
		indexOfQueryTerms = processQueryResults(indexOfQueryTerms, notQuery, "NOT", alldocuments);
		String[] andQuery = processQuery(query, "AND");
		indexOfQueryTerms = processQueryResults(indexOfQueryTerms, andQuery, "AND", alldocuments);
		String[] orQuery = processQuery(query, "OR");
		indexOfQueryTerms = processQueryResults(indexOfQueryTerms, orQuery, "OR", alldocuments);
		return indexOfQueryTerms;
	}

	private String[] processQuery(String query, String operation) {
		String[] querySplit = query.split(" ");
		String[] queryProcessed = new String[querySplit.length];
		int j = 0;
		switch (operation) {
		case "NOT":
			for (int i = 0; i < querySplit.length; i++) {
				if (querySplit[i].equals("NOT")) {
					queryProcessed[j] = querySplit[i + 1];
					j++;
				}
			}
			break;
		case "AND":
			for (int i = 0; i < querySplit.length; i++) {
				if (querySplit[i].equals("AND")) {
					queryProcessed[j] = querySplit[i - 1];
					j++;
					if (!querySplit[i + 1].equals("NOT"))
						queryProcessed[j] = querySplit[i + 1];
					else
						queryProcessed[j] = querySplit[i + 2];
					j++;
				}
			}
			break;
		case "OR":
			for (int i = 0; i < querySplit.length; i++) {
				if (querySplit[i].equals("OR")) {
					queryProcessed[j] = querySplit[i - 1];
					j++;
					if (!querySplit[i + 1].equals("NOT"))
						queryProcessed[j] = querySplit[i + 1];
					else
						queryProcessed[j] = querySplit[i + 2];
					j++;
				}
			}
			break;

		default:
			break;
		}

		return queryProcessed;
	}

	private HashMap<String, Term> processQueryResults(HashMap<String, Term> indexOfQueryTerms, String[] queryProcessed,
			String operation, ArrayList<String> alldocuments) {
		switch (operation) {
		case "NOT":
			for (int i = 0; i < queryProcessed.length; i++) {
				if (queryProcessed[i] != null) {
					Term term = indexOfQueryTerms.get(queryProcessed[i]);

					indexOfQueryTerms.remove(queryProcessed[i]);

					HashSet<Document> documents = term.getDocuments();
					ArrayList<String> docIdForTerm = new ArrayList<>();
					for (Document d : documents) {
						docIdForTerm.add(d.getDocumentId());
					}
					HashSet<Document> updatedDocuments = new HashSet<>();
					for (String docid : alldocuments) {
						if (!docIdForTerm.contains(docid)) {
							Document d = new Document();
							d.setDocumentId(docid);
							updatedDocuments.add(d);
						}
					}
					term.setDocuments(updatedDocuments);
					indexOfQueryTerms.put(queryProcessed[i], term);
				}
			}
			break;
		case "AND":
			for (int i = 0; i < queryProcessed.length; i += 2) {
				if (queryProcessed[i] != null) {
					Term term1 = indexOfQueryTerms.get(queryProcessed[i]);
					Term term2 = indexOfQueryTerms.get(queryProcessed[i + 1]);

					indexOfQueryTerms.remove(queryProcessed[i]);
					indexOfQueryTerms.remove(queryProcessed[i + 1]);

					HashSet<Document> documents1 = term1.getDocuments();
					HashSet<Document> documents2 = term2.getDocuments();
					ArrayList<String> docIdForTerm1 = new ArrayList<>();
					ArrayList<String> docIdForTerm2 = new ArrayList<>();
					for (Document d : documents1) {
						docIdForTerm1.add(d.getDocumentId());
					}
					for (Document d : documents2) {
						docIdForTerm2.add(d.getDocumentId());
					}
					HashSet<Document> updatedDocuments = new HashSet<>();
					for (String docid2 : docIdForTerm2) {
						if (docIdForTerm1.contains(docid2)) {
							Document d = new Document();
							d.setDocumentId(docid2);
							updatedDocuments.add(d);
						}
					}
					term1.setDocuments(updatedDocuments);
					term2.setDocuments(updatedDocuments);
					indexOfQueryTerms.put(queryProcessed[i], term1);
					indexOfQueryTerms.put(queryProcessed[i + 1], term2);
				}
			}
			break;
		case "OR":
			for (int i = 0; i < queryProcessed.length; i += 2) {
				if (queryProcessed[i] != null) {
					Term term1 = indexOfQueryTerms.get(queryProcessed[i]);
					Term term2 = indexOfQueryTerms.get(queryProcessed[i + 1]);

					indexOfQueryTerms.remove(queryProcessed[i]);
					indexOfQueryTerms.remove(queryProcessed[i + 1]);

					HashSet<Document> documents1 = term1.getDocuments();
					HashSet<Document> documents2 = term2.getDocuments();
					ArrayList<String> docIdForTerm1 = new ArrayList<>();
					ArrayList<String> docIdForTerm2 = new ArrayList<>();
					for (Document d : documents1) {
						docIdForTerm1.add(d.getDocumentId());
					}
					for (Document d : documents2) {
						docIdForTerm2.add(d.getDocumentId());
					}
					HashSet<Document> updatedDocuments = new HashSet<>();
					// common docs
					for (String docid2 : docIdForTerm2) {
						if (docIdForTerm1.contains(docid2)) {
							Document d = new Document();
							d.setDocumentId(docid2);
							updatedDocuments.add(d);
						}
					}
					// remaining docs of Term1
					for (String docid1 : docIdForTerm1) {
						if (!docIdForTerm2.contains(docid1)) {
							Document d = new Document();
							d.setDocumentId(docid1);
							updatedDocuments.add(d);
						}
					}
					// remaining docs of Term2
					for (String docid2 : docIdForTerm2) {
						if (!docIdForTerm1.contains(docid2)) {
							Document d = new Document();
							d.setDocumentId(docid2);
							updatedDocuments.add(d);
						}
					}
					term1.setDocuments(updatedDocuments);
					term2.setDocuments(updatedDocuments);
					indexOfQueryTerms.put(queryProcessed[i], term1);
					indexOfQueryTerms.put(queryProcessed[i + 1], term2);
				}
			}
			break;

		default:
			break;
		}
		return indexOfQueryTerms;
	}

	public static void main(String[] args) {

		String location_of_index = "/home/lovey/Downloads/IR_Assignment/index";
		IndexingDao indexingDao = new IndexingDao();
		QueryDao queryDao = new QueryDao();
		HashMap<String, Term> indexmap = indexingDao.readIndex(location_of_index);
		System.out.println("Index read complete");
		ArrayList<String> alldocuments = indexingDao.readProcessedDocumentsInIndex(location_of_index);
		System.out.println("Document read complete");
		String input = "the AND NOT or";
		System.out.println("Query:" + input);
		System.out.println(queryDao.retrieveResults(input, indexmap, alldocuments));

	}
}
