package assignment.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lovey joshi
 *
 */
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;

	private String documentId;
	private ArrayList<Integer> postions;
	private long documentFrequency;

	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public ArrayList<Integer> getPostions() {
		return postions;
	}

	/**
	 * @param postions
	 */
	public void setPostions(ArrayList<Integer> postions) {
		this.postions = postions;
	}

	public long getDocumentFrequency() {
		return documentFrequency;
	}

	/**
	 * @param documentFrequency
	 */
	public void setDocumentFrequency(long documentFrequency) {
		this.documentFrequency = documentFrequency;
	}

	@Override
	public String toString() {
		return "\"documentId\":\"" + documentId + "\", \"postions\":\"" + postions + "\", \"documentFrequency\":\""
				+ documentFrequency + "\"";
	}

}
