Objective:

Build an uncompressed index from the given dataset. Execute the code on the given data set and generate the Index for it. Give some Conjunctive Boolean Queries and test your Index generated.

Perquisites:
JRE1.8 or JDK1.8

Solution:
I’ve used Java to create a solution for aforementioned Objective. With the amalgamation of POJOS and DAO layer, an executable version of both Indexer and QueryTester JAR is provided in the Zip package. 
I’ve created 2 POJOS, a Term POJO having {termFrequency and a Set of Document} and the other is Document POJO having {documentId, List of positions & documentFrequency}. The ProcessingDao contains functions that will be directly accessed from Tester, like createIndexTerms() & processDocument(). It also contains private function processText() that is used by createIndexTerms() to process the retrieved text (like tokenizing, stop word removal). processText() is made modular, as in additional functionalities such as Stemming/Lemmatizing, etc. can be added easily. Time being, the stop-word removal is enabled by default. The IndexingDao performs indexing related tasks, such as reading index & writing to index. The QueryDao contains retrieveResults() that internally calls two private function of same class, processQuery() and processQueryResults() to process Boolean queries and bring out the best results. 
Execution Steps:
The working of code is command line based. In the zipped folder, two runnable jar files are provided – indexer.jar and query.jar, along with this README.docx and 2 folders, data and index. Data contains the provided dataset (docx) and index contains already built indexfiles. In order to generate new index file, follow the steps:

1.	Open a terminal shell/command prompt in the extracted zip directory and copy the command:

java -jar indexer.jar path_to_data_folder path_to_index_folder

where 
a)	path_to_data_folder is the absolute/relative path to folder containing .docx files and
b)	path_to_index_folder is absolute/relative path to folder where index will be created.

2.	Once the command is processed, it’ll output the dictionary terms, and the path where index is created. Copy that path. 

3.	In the same terminal, copy the command:

java -jar query.jar path_to_index_folder

where
a)	path_to_index_folder is absolute/relative path to folder where index was created

This will initialize the created index for query operations. 

4.	The terminal will ask for query input. Test an example query:
Enter your query: 
radiology AND medical

The program will then output the document containing the above words. The queryDao is able to cater AND, NOT and OR boolean operators, and their combinations. If no operator is provided, the program will output the documents for each tokenized word of query.  

Ex: NOT radiology AND medical 


