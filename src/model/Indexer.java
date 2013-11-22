package model;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.valuesource.NumDocsValueSource;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


public class Indexer 
{
	
		
	  public static void main(String[] args) throws IOException, ParseException 
	  {
		  
		    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		    FSDirectory directory = FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/index"));

		    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		    IndexWriter w = new IndexWriter(directory, config);

		    File input=new File("des");
		    FileReader fr = new FileReader(input);
		    BufferedReader br = new BufferedReader(fr);
		    
		    String line = new String();
			int numDocs=0;

		    
		    while((line= br.readLine())!=null)
		    {
		    	String[] splitted = line.split("\t", 2);
		    	numDocs++;

		    	if(splitted.length==2)
		    	{
		    		addDoc(w, splitted[1], splitted[0]);
		    	}
		    }
		    
		    w.close();
		    br.close();
		  
		  
	  }
	  
	  private static void addDoc(IndexWriter w, String title, String isbn) throws IOException 
	  {
		    Document doc = new Document();
		    doc.add(new TextField("T", title, Field.Store.YES));
		    doc.add(new StringField("isbn", isbn, Field.Store.YES));
		    w.addDocument(doc); 
	  }

}
