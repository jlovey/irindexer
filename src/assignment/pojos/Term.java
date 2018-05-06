package assignment.pojos;

import java.io.Serializable;

import java.util.HashSet;

/**
 * @author lovey joshi
 *
 */
public class Term implements Serializable {

	private static final long serialVersionUID = 1L;

	private long termFrequency;
	private HashSet<Document> documents;

	public long getTermFrequency() {
		return termFrequency;
	}

	/**
	 * @param termFrequency
	 */
	public void setTermFrequency(long termFrequency) {
		this.termFrequency = termFrequency;
	}

	public HashSet<Document> getDocuments() {
		return documents;
	}

	/**
	 * @param documents
	 */
	public void setDocuments(HashSet<Document> documents) {
		this.documents = documents;
	}

	@Override
	public String toString() {
		return "\"termFrequency\":\"" + termFrequency + "\", \"documents\":" + documents + "\"";
	}

}
