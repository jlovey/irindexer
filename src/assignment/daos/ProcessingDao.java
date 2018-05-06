package assignment.daos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import assignment.pojos.Document;
import assignment.pojos.Term;

public class ProcessingDao {

	private static InputStream input = null;
	private static Properties prop = null;
	private static ArrayList<String> stopwords = null;
	static {
		input = ProcessingDao.class.getClassLoader().getResourceAsStream("stopwords.properties");
		if (input != null) {
			try {
				prop = new Properties();
				prop.load(input);
				stopwords = new ArrayList<>();
				String line = prop.getProperty("stopwords");

				String[] linesplit = line.split(",");
				for (int i = 0; i < linesplit.length; i++) {
					stopwords.add(linesplit[i].trim().toLowerCase());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				throw new FileNotFoundException("Stopword file '" + input + "' not found in the classpath");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	public HashMap<String, Term> createIndexTerms(String text, String documentId, HashMap<String, Term> indexmap) {

		// Process spilit_text
		String[] split_text = processText(text);
		int position_cursor = 0;
		// Create Terms and add in index
		for (int i = 0; i < split_text.length; i++) {
			if (indexmap.containsKey(split_text[i])) {
				Term t = indexmap.get(split_text[i]);
				HashSet<Document> documentlistfort = t.getDocuments();
				boolean flag = false;
				for (Document d : documentlistfort) {
					if (d.getDocumentId().equals(documentId)) {
						documentlistfort.remove(d);
						d.setDocumentId(documentId);
						d.setDocumentFrequency(d.getDocumentFrequency() + 1);
						ArrayList<Integer> positions = d.getPostions();
						positions.add(position_cursor);
						d.setPostions(positions);
						documentlistfort.add(d);
						flag = true;
						break;
					}
				}
				if (flag == false) // i.e. document d is a new document for term t
				{
					Document document = new Document();
					document.setDocumentId(documentId);
					ArrayList<Integer> positions = new ArrayList<>();
					positions.add(position_cursor);
					document.setPostions(positions);
					document.setDocumentFrequency(1);
					documentlistfort.add(document);
				}
				t.setDocuments(documentlistfort);
				t.setTermFrequency(t.getTermFrequency() + 1);
				indexmap.put(split_text[i], t);
			} else {
				Term t = new Term();
				HashSet<Document> documentlistfort = new HashSet<>();
				Document document = new Document();
				document.setDocumentId(documentId);
				ArrayList<Integer> positions = new ArrayList<>();
				positions.add(position_cursor);
				document.setPostions(positions);
				document.setDocumentFrequency(1);
				documentlistfort.add(document);
				t.setDocuments(documentlistfort);
				t.setTermFrequency(1);
				indexmap.put(split_text[i], t);
			}
			position_cursor += (split_text[i].length() + 1);

		}
		return indexmap;
	}

	private String[] processText(String text) {
		String[] textsplit = text.split(" ");
		// Remove stopwords
		if (stopwords != null) {

			String tempstring = "";
			for (int i = 0; i < textsplit.length; i++) {
				if (!stopwords.contains(textsplit[i].trim().toLowerCase()))
					tempstring += textsplit[i].trim().toLowerCase() + " ";
			}
			textsplit = tempstring.trim().split(" ");
		}
		return textsplit;
	}

	public String processDocument(File file) {
		StringBuffer buffer = new StringBuffer();
		try (FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				XWPFDocument document = new XWPFDocument(fis);) {
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph para : paragraphs) {
				buffer.append(para.getText());
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString().toLowerCase();

	}

	public static void main(String[] args) {
		ProcessingDao dao = new ProcessingDao();
		IndexingDao indexingDao = new IndexingDao();
		HashMap<String, Term> indexmap = new HashMap<>();
		File f = new File("/home/lovey/Downloads/Doc1.docx");
		String text = dao.processDocument(f);
		String documentId = f.getName();
		dao.createIndexTerms(text, documentId, indexmap);
		System.out.println(indexmap);
		indexingDao.writeIndex(indexmap, "/opt/index.dat");

	}

}
