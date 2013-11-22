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
import org.apache.lucene.misc.GetTermInfo;
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

import javax.activation.FileTypeMap;


public class HelloLucene 
{
	HashMap<Integer,String> tagsMap = new HashMap<Integer,String>();
	HashMap<String, HashMap<Integer, Integer>> bookmark_tag = new HashMap<String, HashMap<Integer, Integer>>();
	
	HashMap<Integer,Double> rankTag = new HashMap<Integer,Double>();
	TreeMap<Double,HashSet<Integer>> reverseRankTag = new TreeMap<Double,HashSet<Integer>>();

	int dfScore[]= new int[100];
    
	int maxResults=20;
	int averageLength=5;
	int numDocs=0;
	
  public static void main(String[] args) throws IOException, ParseException 
  {
	    HelloLucene hello = new HelloLucene();
	    
	    
	    
	    
    // 0. Specify the analyzer for tokenizing text.
    //    The same analyzer should be used for indexing and searching
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

    // 1. create the index
    FSDirectory index = FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/index"));
//    Directory index = new RAMDirectory();

    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

    IndexWriter w = new IndexWriter(index, config);

    FileTypeMap fileTypeMap = null;
    
    File dir=new File("files");
	File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) 
    {
	    File f = files[i];
	    if (f.isDirectory()) 
	    {
	    //	indexDirectory(writer, f);
	    } 
	    else if (f.getName().endsWith(".txt")) 
	    {
	    	
	    	System.out.println("ADDING DOCUMENT : " +  f.getName() + "  " + fileTypeMap.getContentType(f));
	        addDoc(w, "files/"+f.getName(), Integer.toString(i));
	    }
	   }
    
    File input=new File("des");
    FileReader fr = new FileReader(input);
    BufferedReader br = new BufferedReader(fr);
    
    String line;
    
    
    while((line= br.readLine())!=null)
    {
    	String[] splitted = line.split("\t", 2);
    	hello.numDocs++;
    	if(splitted.length==2)
    	{
    		addDoc(w, splitted[1], splitted[0]);
    	}
    }
    
    System.out.println("number of docs " + hello.numDocs);
    
    w.close();
    br.close();
    
    // 2. query

    String urlName=args[0];
    String query=new String();

    int j=1;
    while(j<args.length)
    {
        Query q1 = new QueryParser(Version.LUCENE_40, "T", analyzer).parse(args[j]);
        int hitsPerPage1 = 100000;
//        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
        IndexReader reader1 = DirectoryReader.open(index);
        IndexSearcher searcher1 = new IndexSearcher(reader1);
        TopScoreDocCollector collector1 = TopScoreDocCollector.create(hitsPerPage1, true);
        searcher1.search(q1, collector1);
        ScoreDoc[] hits = collector1.topDocs().scoreDocs;
        
        hello.dfScore[j]=hits.length;
    	query+=(args[j++]+" ");
    }
    
//    String querystr = args.length > 0 ? args[0] : "lucene";

    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    hello.indexInMemory();
    
    
    Query q = new QueryParser(Version.LUCENE_40, "T", analyzer).parse(query);

    // 3. search
    int hitsPerPage = 10;
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
    searcher.search(q, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
   
    // 4. display results
    System.out.println("Found " + hits.length + " hits.");
    for(int i=0;i<hits.length;++i) 
    {
      int docId = hits[i].doc;
      Document d = searcher.doc(docId);
      
      double sim=hello.similarityBetweenDocs(d.get("T"),query);

      System.out.println((i + 1) + ". " + d.get("isbn"));
      
      hello.ranking(d.get("isbn"),i,hits.length,sim);
    }
    
    hello.retrieveTop();
    
    // reader can only be closed when there
    // is no need to access the documents any more.
    reader.close();
  }

  private double similarityBetweenDocs(String string, String query) 
  {
	String[] split = query.split(" ");
	String[] splitDesc = string.split(" ");
	
	int i=0;
	double result=0;
	while(i<split.length)
	{
		String word=split[i];
		int j=0,count=0;
		
		while(j<splitDesc.length)
		{
			if(word.equalsIgnoreCase(splitDesc[j]))
			{
				count++;
			}
			j++;
		}
		
		double t=(1-0.75) + 0.75*(split.length/averageLength) + count;
		t=(count/t);
		t=(t*idfCalculate(i));
		
		result+=t;
		i++;
	}

	return result;
  }

private double idfCalculate(int i) 
{
	return Math.log((numDocs + dfScore[i+1] + 0.5)/ (dfScore[i+1]+0.5));
}

private void retrieveTop() 
  {
	  int tmp;
	  if(rankTag.size()<maxResults)
	  {
		  tmp=rankTag.size();
	  }
	  else
	  {
		  tmp=maxResults;
		  //tmp=rankTag.size();
	  }
	  
	  for(int k=0;k<tmp;)
		{
			Entry<Double, HashSet<Integer>> d=reverseRankTag.pollLastEntry();
			Iterator<Integer> it = d.getValue().iterator();
			while(k<tmp && it.hasNext())
			{		k++;
					int q=it.next().intValue();
					System.out.println(k + " :"+tagsMap.get(q));
			}
		}
	  
  }

private void ranking(String string, int i, int length, double sim) 
  {
//	System.out.println("string is " + string);

	if(!bookmark_tag.containsKey(string))
	{
//		System.out.println("false");
		return;
	}
	
	HashMap<Integer,Integer> map = bookmark_tag.get(string);
	
//	System.out.println("String : " +string +" size :" +map.size());
	
	for (Map.Entry<Integer, Integer> entry : map.entrySet()) 
	{
		int tagID=entry.getKey();
		//double weight =((length-i)/10) +1;
		//weight*=entry.getValue();

		double weight=sim;
//		System.out.println("weight is " + weight);
		
		if(rankTag.containsKey(tagID))
		{
			double d= rankTag.get(tagID);
			check(d, tagID,0);
			d+=weight;
			check(d, tagID,1);	
			rankTag.put(tagID, d);
		
		}
		else
		{
			
			rankTag.put(tagID, weight);
			check(weight, tagID,1);				
		}
	}		
  }

private void check(double tfScore, int docid, int j) 
{
	if(j==1)
	{
		if(reverseRankTag.containsKey(tfScore))
		{
			HashSet<Integer> hs =reverseRankTag.get(tfScore);
			hs.add(docid);
			reverseRankTag.put(tfScore, hs);
		}
		else
		{
			HashSet<Integer> hs = new HashSet<Integer>();
			hs.add(docid);
			reverseRankTag.put(tfScore, hs);
		}
	}
	else
	{
		if(reverseRankTag.containsKey(tfScore))
		{
			HashSet<Integer> hs =reverseRankTag.get(tfScore);
			hs.remove(docid);
			if(hs.size()==0)
				reverseRankTag.remove(tfScore);
			else
				reverseRankTag.put(tfScore, hs);
		}
	}
}



private void indexInMemory() throws IOException 
  {
	File f = new File("tags.dat");
	FileReader fr = new FileReader(f);
	BufferedReader tag = new BufferedReader(fr);

	File fb = new File("bookmark_tags.dat");
	FileReader frb = new FileReader(fb);
	BufferedReader bookmarks = new BufferedReader(frb);

	String line=new String();
	
	while((line=tag.readLine())!=null)
	{
		String[] split = line.split("\t", 2);
		if(split.length==2)
		{
			tagsMap.put(Integer.parseInt(split[0]), split[1]);			
		}
	}
	
	while((line=bookmarks.readLine())!=null)
	{
		String[] split = line.split("\t", 3);
		
		if(split.length==3)
		{
			if(bookmark_tag.containsKey(split[0]))
			{
				HashMap<Integer, Integer> h = bookmark_tag.get(split[0]);
				h.put(Integer.parseInt(split[1]), Integer.parseInt(split[2]));				
				bookmark_tag.put(split[0], h);			
			}
			else 
			{
				HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
				h.put(Integer.parseInt(split[1]), Integer.parseInt(split[2]));				
				bookmark_tag.put(split[0], h);			
			}
		}
	}

	System.out.println("size of tags " + tagsMap.size() + " bookmarks " + bookmark_tag.size());
	tag.close();
	bookmarks.close();
	
  }

private static void addDoc(IndexWriter w, String title, String isbn) throws IOException 
  {
    Document doc = new Document();
    doc.add(new TextField("T", title, Field.Store.YES));
    //File f = new File(title);
    //doc.add(new TextField("contents", new FileReader(f)));

    // use a string field for isbn because we don't want it tokenized
    doc.add(new StringField("isbn", isbn, Field.Store.YES));
    w.addDocument(doc);
  }
}