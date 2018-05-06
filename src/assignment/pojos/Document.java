package assignment.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class Document implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String documentId;
	private ArrayList<Integer> postions;
	private long documentFrequency;
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public ArrayList<Integer> getPostions() {
		return postions;
	}
	public void setPostions(ArrayList<Integer> postions) {
		this.postions = postions;
	}
	public long getDocumentFrequency() {
		return documentFrequency;
	}
	public void setDocumentFrequency(long documentFrequency) {
		this.documentFrequency = documentFrequency;
	}
	@Override
	public String toString() {
		return "\"documentId\":\"" + documentId + "\", \"postions\":\"" + postions + "\", \"documentFrequency\":\""
				+ documentFrequency + "\"";
	}
	
	
}
