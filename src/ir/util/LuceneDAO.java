package ir.util;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import ir.indexing.Indexer;
import ir.indexing.Searcher;
import ir.indexing.TextFileFilter;

public class LuceneDAO {

	String indexDir = "./IndexFiles";
	String dataDir = "./ConvertedText";
	Indexer indexer;
	Searcher searcher;

	public void createIndex() throws IOException {
		indexer = new Indexer(indexDir);
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.close();
		System.out.println(numIndexed + " Files indexed, time taken: " + (endTime - startTime) + " ms");
	}

	public String search(String searchQuery) throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		TopDocs hits = searcher.search(searchQuery);
		long endTime = System.currentTimeMillis();
		
		System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime) + " ms");
		int i = 1;
		String result = "<br>";
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			result += i++ + ". <a href = '" + doc.get(LuceneConstants.FILE_PATH) + "'>Doc No: " + doc.get(LuceneConstants.FILE_NAME) + "</a> Score: " + scoreDoc.score + "<br>";
		}
		System.out.println(result);
		searcher.close();
		return result;
	}
}
