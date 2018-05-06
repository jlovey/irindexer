package assignment.daos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import assignment.pojos.Term;

/**
 * @author lovey joshi
 *
 */
public class IndexingDao {

	/**
	 * @param location_of_index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Term> readIndex(String location_of_index) {

		HashMap<String, Term> readData = new HashMap<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(location_of_index + "/index.dat"))) {

			readData = (HashMap<String, Term>) ois.readObject();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return readData;

	}

	/**
	 * @param location_of_index
	 * @return
	 */
	public ArrayList<String> readProcessedDocumentsInIndex(String location_of_index) {
		ArrayList<String> docs = new ArrayList<>();
		try (BufferedReader bfr = new BufferedReader(new FileReader(new File(location_of_index + "/indexdocs.txt")))) {
			String readline = "";
			while ((readline = bfr.readLine()) != null) {
				if (readline.length() != 0 || !readline.trim().equals("")) {
					docs.add(readline);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docs;
	}

	/**
	 * @param index
	 * @param location_of_index
	 * @return
	 */
	public boolean writeIndex(HashMap<String, Term> index, String location_of_index) {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(location_of_index + "/index.dat"))) {

			oos.writeObject(index);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;

	}

	/**
	 * @param documents
	 * @param location_of_index
	 * @return
	 */
	public boolean writeProcessedDocumentInIndex(String documents, String location_of_index) {
		try (BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(location_of_index + "/indexdocs.txt")))) {

			bfw.write(documents);
			bfw.flush();
			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
