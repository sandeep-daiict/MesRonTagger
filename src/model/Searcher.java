package model;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.byTask.tasks.ReadTask;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.QueryTermExtractor;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;


public class Searcher 
{
	int dfScore[]= new int[100];
    
	int maxResults=30;
	int averageLength=5;
	int numDocs=50000;
	
	HashMap<Integer,String> tagsMap = new HashMap<Integer,String>();
	HashMap<String, HashMap<Integer, Integer>> bookmark_tag = new HashMap<String, HashMap<Integer, Integer>>();
	
	HashMap<Integer,Double> rankTag = new HashMap<Integer,Double>();
	TreeMap<Double,HashSet<Integer>> reverseRankTag = new TreeMap<Double,HashSet<Integer>>();
	
	HashMap<Integer,Double> rankCluster = new HashMap<Integer,Double>();
	TreeMap<Double,HashSet<Integer>> reverseRankCluster = new TreeMap<Double,HashSet<Integer>>();

	HashMap<Integer,Double> simDoc = new HashMap<Integer, Double>();
	TreeMap<Double, ArrayList<Integer>> reverseSimDoc = new TreeMap<Double, ArrayList<Integer>>();
	
	HashSet<Integer> tagID = new HashSet<Integer>();
	public HashMap<Integer,Integer> internalDocID = new HashMap<Integer, Integer>();


	public String query(String url, String[] args) throws ParseException 
	{

		String query=new String();
		try
		{
			String urlName=url;

//		  	SnowballAnalyzer analyzer = new SnowballAnalyzer(Version.LUCENE_36, "English",hs);

		  	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		    FSDirectory index = FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/indexContent"));



		    int j=0;

		    while(j<args.length)
		    {
		        IndexReader reader = IndexReader.open(FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/indexContent")));
		        
//		        String[] aa = args[j].split(":",2);
		        
		        
//		        QueryParser qp = new QueryParser(Version.LUCENE_36, "T", analyzer);
//		        Query q=qp.parse(args[j]);
		        
//		        QueryTermExtractor.getTerms(q);
//		        query.extractTerms(term);

		        Term t = new Term("T",args[j].toLowerCase());

		        dfScore[j]=reader.docFreq(t);

		        System.out.println("query is " + " " + t.toString() + " score is : " + dfScore[j]);

//		        System.out.println(args[j] +" score " +dfScore[j]);
		        
		        query+=(args[j++]+" ");

//		        query+=("C:"+args[j]+" OR T:"+args[j++] +" ");
		    	
		    	reader.close(); 	
		    }
		    
		    searchFullQuery(query,analyzer,index);
		    
		    System.out.println("after search full query");
		    
		    
		    
//		    collectTags();
//		    rankTags();
//		    retrieveTop();

//		    reader1.close();
			
		}
		catch(IOException e)
		{
			System.out.println("Exception in IO while reading index");
		}
		
		return query;

	}
	
	public void rankingOLD(String string, double sim) 
	  {

//		System.out.println("inside ranking old");
		if(!bookmark_tag.containsKey(string))
		{
			return;
		}
		
		HashMap<Integer,Integer> map = bookmark_tag.get(string);
		
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) 
		{
			int tagID=entry.getKey();
			double weight=sim;
			
			if(rankTag.containsKey(tagID))
			{
				double d= rankTag.get(tagID);
				checkingRank(d, tagID,0);
				d+=(weight);
				checkingRank(d, tagID,1);	
				rankTag.put(tagID, d);
			
			}
			else
			{
				
				rankTag.put(tagID, weight);
				checkingRank(weight, tagID,1);				
			}
		}		
	  }

	public void checkingRank(double tfScore, int docid, int j) 
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

	public void collectTags() 
	{
//		System.out.println("collect tags  " + simDoc.size());
		Set<Integer> set =simDoc.keySet();
		
		Iterator<Integer> it = set.iterator();
		
		while(it.hasNext())
		{		
			int q=it.next().intValue();
//			System.out.println("DOC ID  is "+q + " Simi is "+ simDoc.get(q));
			
			if(bookmark_tag.containsKey(Integer.toString(q)))
			{
				HashMap<Integer,Integer> map = bookmark_tag.get(Integer.toString(q));
				Set<Integer> setTag =map.keySet();
				
//				Iterator<Integer> it1 =setTag.iterator();
//				while(it1.hasNext())
//				{
//					System.out.println("tag is : " + tagsMap.get(it1.next().intValue()));
//				}
				tagID.addAll(setTag);				
			}
		}
		
//		System.out.println("map size is " + tagID.size());		
	}

//	public void rankTags() 
//	{
//	
//		System.out.println("inside rank rag");
//		try 
//		{
//	        IndexReader reader = IndexReader.open(FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/indexContent")));
//			int doc;
//			
//			Iterator<Integer> it = tagID.iterator();
//			
//			while(it.hasNext())
//			{
//
//				int tagid =it.next().intValue();
//				String i=tagsMap.get(tagid);
//
//				DocsEnum de = MultiFields.getTermDocsEnum(reader, MultiFields.getLiveDocs(reader), "T", new BytesRef(i));
//				double result=0;
//				int total=0;
//				int count=0;
//				
//				if(de!=null)
//				{
//
//					while((doc = de.nextDoc()) != DocsEnum.NO_MORE_DOCS) 
//					{
//						Document document=reader.document(doc);
//						int ID = Integer.parseInt(document.get("isbn"));
//						
//						if(simDoc.containsKey(ID))
//						{	
//							count++;
//							result+=(de.freq()*simDoc.get(ID));
//							total+=de.freq();	
//						}
//					}
//					System.out.println("tag is " + i + " total is " + total + " count is " + count);
//					if(total>0)
//					{
//						double weight=(result/total)*count;
//
//						ranking(tagid, weight);
//					}
//				}
//			}
//		}
//		catch (IOException e) 
//		{
//				e.printStackTrace();
//		}
////		int totalFreq = 0;
////	    TermDocs termDocs = reader.termDocs();
////	    termDocs.seek(new Term("my_field", "congress"));
////	    for (int id : docIds) {
////	        termDocs.skipTo(id);
////	        totalFreq += termDocs.freq();
//		
//		
//				
//	}


	public void searchFullQuery(String query, StandardAnalyzer analyzer, FSDirectory index) 
	{
	
		System.out.println("inside search full query");
		try
		{
			TreeSet<Integer> docList = new TreeSet<Integer>();

//			MultiFieldQueryParser multi = new MultiFieldQueryParser(Version.LUCENE_36, new String[]{"T","C"}, analyzer);
//			Query q = multi.parse(query);

			Query q = new QueryParser(Version.LUCENE_36, "T", analyzer).parse(query);
	
		    System.out.println("query is " + q.toString());
		    int hitsPerPage = 200;
		    IndexReader reader = IndexReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		   
//		    System.out.println("Found " + hits.length + " hits.");
		    
		    for(int i=0;i<hits.length;++i) 
		    {
		    	
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      int docNum=Integer.parseInt(d.get("isbn"));
		      
		      System.out.println("document no " + docNum + " internal " +docId);
//		      docList.add(docId);
		      rankingOLD(Integer.toString(docNum),hits[i].score);
		    }
		    
//		    similarityBetweenDocs(docList, q.toString(), 0);
		      
		}
		catch(IOException e)
		{
			System.out.println("Exception in IO inside searching");
		}
		catch(ParseException e)
		{
			System.out.println("Exception in parsing inside searching");
		}

	}
	
	
	  public void similarityBetweenDocs(TreeSet<Integer> treeSet, String query, int index) 
	  {
		  
		
		  String[] split = query.split(" ");
		  double t=((1-0.75) + 0.75*(split.length/averageLength))*2;
		  int i=0;
		  double result=0;


		  try
		  {
			  IndexReader reader = IndexReader.open(FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/indexContent")));

			  Stemmer stem = new Stemmer();
			  
			  				  
			
			  Iterator<Integer> itr=treeSet.iterator();

				  int i1=0;
				  while(itr.hasNext())
				  {
					  i1++;
					  int doc = itr.next().intValue();
					
//					  System.out.println("doc is " + doc);
//					  TermDocs term=reader.termDocs(new Term("T",word.toLowerCase()));
//					  term.skipTo(doc);
					  
					  TermFreqVector tv= reader.getTermFreqVector(doc, "T");
					 
					  int frequency=0;
					  
					  if(tv!=null)
					  {  
						  String[] terms = tv.getTerms();
						  int[] freq = tv.getTermFrequencies();
						  
						  int l=0;
						  while(l<terms.length)
						  {
							  int j=0;
							  
							  while(j<split.length)		
							  {
								  String word=stem.go(split[j]);
								  result=0;
									
								  if(stem.go(terms[l]).equalsIgnoreCase(word))
								  {
									  frequency=freq[l];
									  result=t + frequency;
									  result=(frequency/result);

									  double validf=idfCalculate(i);
									  result=(result*validf);

									  Document document=reader.document(doc);
									  int ID = Integer.parseInt(document.get("isbn"));

									  if(index==1)
									  {
										  System.out.println("doc id " + doc);
										  ranking(doc, result);
									  }
									  else
									  {
										  rankingOLD(Integer.toString(ID),result);						  
									  }

								  }
									
								  j++;
							  }

							  l++;
						  }
						  
					  }
					  else
					  {
//						  System.out.println("NULLL --------> " + doc);
					  }

//					  result=t + frequency;
//					  result=(frequency/result);
//
//					  double validf=idfCalculate(i);
//					  result=(result*validf);
//						
//					  
//					  Document document=reader.document(doc);
//					  int ID = Integer.parseInt(document.get("isbn"));
//
//					  if(index==1)
//					  {
////					  System.out.println("doc id " + doc);
//						  ranking(doc, result);
//					  }
//					  else
//					  {
//						  rankingOLD(Integer.toString(ID),result);						  
//					  }

				  }
					  			  
		  }
		  catch(IOException e)
		  {
			  e.printStackTrace();
		  }
		  
		
	  }
	  
		  
	public void insertInMap(int docNum, double sim) 
	{
		
	      if(simDoc.size()<100)
	      {
	    	  
	    	  if(reverseSimDoc.containsKey(sim))
	    	  {
	    		  ArrayList<Integer> arrlist = reverseSimDoc.get(sim);
	    		  arrlist.add(docNum);
	    		  reverseSimDoc.put(sim, arrlist);
	    		  simDoc.put(docNum, sim);
	    	  }
	    	  else
	    	  {
		    	  simDoc.put(docNum, sim);
		    	  ArrayList<Integer> h = new ArrayList<Integer>();
		    	  h.add(docNum);
		    	  reverseSimDoc.put(sim, h);		    		  
	    	  }
  	  
	  	  }
	  	  else
	  	  {
	  		  System.out.println("size of reverse map : "+ reverseSimDoc.size());
		    	  double small=reverseSimDoc.firstKey();
		    	  if(small<sim)
		    	  {
		    		  java.util.Map.Entry<Double, ArrayList<Integer>> entry=reverseSimDoc.pollFirstEntry();
		    		  ArrayList<Integer> h=entry.getValue();
	
		    		  if(h.size()>0)
		    		  {
		    			  int val = h.remove(h.size()-1);
			    		  simDoc.remove(val);
			    		  if(h.size()==0)
			    		  {
			    			  reverseSimDoc.remove(entry.getKey());  
			    		  }
			    		  else
			    		  {
					    	  reverseSimDoc.put(entry.getKey(), h);			    			  
			    		  }
		    		  }
			    	  
			    	  if(reverseSimDoc.containsKey(sim))
			    	  {
			    		  ArrayList<Integer> arrlist = reverseSimDoc.get(sim);
			    		  arrlist.add(docNum);
			    		  reverseSimDoc.put(sim, arrlist);
			    		  simDoc.put(docNum, sim);
			    	  }
			    	  else
			    	  {
				    	  simDoc.put(docNum, sim);
				    	  ArrayList<Integer> h1 = new ArrayList<Integer>();
				    	  h1.add(docNum);
				    	  reverseSimDoc.put(sim, h);		    		  
			    	  }			    	  
		    	  }
	  	  }
	     
}

	public double idfCalculate(int i) 
	{
		return Math.log((numDocs + dfScore[i+1] + 0.5)/ (dfScore[i+1]+0.5));
	}

	public ArrayList<String> retrieveTop() 
	  {
		System.out.println("inside retrieve top " + reverseRankTag.size());
		ArrayList<String> tags = new ArrayList<String>();
		int tmp;
		
		if(rankTag.size()<maxResults)
		{
			tmp=rankTag.size();
		}
		  else
		  {
			  tmp=maxResults;
		  }
		  
		  for(int k=0;k<tmp;)
			{
				java.util.Map.Entry<Double, HashSet<Integer>> d=reverseRankTag.pollLastEntry();
				Iterator<Integer> it = d.getValue().iterator();
				
				while(k<tmp && it.hasNext())
				{		k++;
						int q=it.next().intValue();
						tags.add(tagsMap.get(q));
//						System.out.println(k + " :"+tagsMap.get(q));
				}
			}
		  
		  rankTag.clear();
		  reverseRankTag.clear();
		  rankCluster.clear();
		  reverseRankCluster.clear();
		  
		  return tags;
	  
	  }

	public void ranking(int tagID, double sim) 
	  {
			double weight=sim;

			if(rankCluster.containsKey(tagID))
			{

				double d= rankCluster.get(tagID);
				check(d, tagID,0);
				d+=weight;
				check(d, tagID,1);	
				rankCluster.put(tagID, d);
			}
			else
			{
				rankCluster.put(tagID, weight);
				check(weight, tagID,1);				
			}
		}		
	  

	public void check(double tfScore, int docid, int j) 
	{
		if(j==1)
		{
			if(reverseRankCluster.containsKey(tfScore))
			{
				HashSet<Integer> hs =reverseRankCluster.get(tfScore);
				hs.add(docid);
				reverseRankCluster.put(tfScore, hs);
			}
			else
			{
				HashSet<Integer> hs = new HashSet<Integer>();
				hs.add(docid);
				reverseRankCluster.put(tfScore, hs);
			}
		}
		else
		{
			if(reverseRankCluster.containsKey(tfScore))
			{
				HashSet<Integer> hs =reverseRankCluster.get(tfScore);
				hs.remove(docid);
				if(hs.size()==0)
					reverseRankCluster.remove(tfScore);
				else
					reverseRankCluster.put(tfScore, hs);
			}
		}
	}
	
	
	public void indexInMemory() 
	{
		
		try
		{
			File f = new File("new_dataset/tags.dat");
			FileReader fr = new FileReader(f);
			BufferedReader tag = new BufferedReader(fr);

			File fb = new File("new_dataset/bookmark_tags_old");
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
				String[] split = line.split("\t", 2);

				if(split.length==2)
				{
					if(bookmark_tag.containsKey(split[0]))
					{
						HashMap<Integer, Integer> h = bookmark_tag.get(split[0]);
						h.put(Integer.parseInt(split[1]), 1);				
						bookmark_tag.put(split[0], h);			
					}
					else 
					{
						HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
						h.put(Integer.parseInt(split[1]), 1);				
						bookmark_tag.put(split[0], h);			
					}
				}
			}
	
			System.out.println("size of tags " + tagsMap.size() + " bookmarks " + bookmark_tag.size());
			tag.close();
			bookmarks.close();
			
			File file = new File("internalDocID");
			FileReader freader = new FileReader(file);
			BufferedReader breader = new BufferedReader(freader);
			
			String string = new String();
			
			while((string=breader.readLine())!=null)
			{
				String split[] =string.split("  ");
				internalDocID.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			}
			
			breader.close();
			
		}
		
		catch(IOException e)
		{
			System.out.println("Inside IO Exception while bringing index in memory");
		}

	}

	public void printId() 
	{
		
		try
		{
			File f = new File("internalDocID");
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			
			IndexReader reader = IndexReader.open(FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/indexContent")));
			
			for(int i=0;i<reader.maxDoc();i++)
			{
				Document document=reader.document(i);
				int ID = Integer.parseInt(document.get("isbn"));
				
				bw.write(Integer.toString(ID) + "  " +Integer.toString(i));
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
