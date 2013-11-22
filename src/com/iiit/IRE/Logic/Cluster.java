package com.iiit.IRE.Logic;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.FSDirectory;

import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.Resnik;


public class Cluster 
{

	HashMap<Integer, Integer> centroid= new HashMap<Integer, Integer>();
	HashMap<Integer, ArrayList<Integer>> clusterDoc = new HashMap<Integer, ArrayList<Integer>>();
	TreeSet<Integer> finalClusterDoc = new TreeSet<Integer>();
	int averageLength=5;
	HashMap<Integer, HashSet<String>> clusterRepresentative = new HashMap<Integer, HashSet<String>>(); 

	public void indexMemory(Searcher search) 
	{	
		
		System.out.println("In Cluster");
		
		try
		{
			File dir = new File("/home/juhi/workspace/TagCloud/cluster");
			String[] list = dir.list();
			
			int i=0;
			
			for(i=0;i<list.length;i++)
			{
				File file = new File("/home/juhi/workspace/TagCloud/cluster/" +list[i]);
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				
				int center = Integer.parseInt(br.readLine());
				centroid.put(search.internalDocID.get(center), Integer.parseInt(list[i]));
				
				//System.out.println("center is " + center + " " + list[i]);
				
				String str = new String();
				
				ArrayList<Integer> temp = new ArrayList<Integer>();
				
				while((str=br.readLine())!=null)
				{
					temp.add(search.internalDocID.get(Integer.parseInt(str)));				
				}

				clusterDoc.put(Integer.parseInt(list[i]), temp);
				
				
				br.close();
			}
			File F =  new File("/home/juhi/workspace/TagCloud/cluster_representative");
			FileReader Fr = new FileReader(F);
			BufferedReader Br=new BufferedReader(Fr);
			
			String str = new String();
			while((str=Br.readLine())!=null)
			{
				HashSet<String> hs = new HashSet<String>();
				String split[] = str.split(" ");
				int len=split.length;
				for(int i1=1;i1<len;i1++)
				{
					hs.add(split[i1]);
				}
				//System.out.println("cid:"+split[0]);
				clusterRepresentative.put(Integer.parseInt(split[0]),hs);
			}
			
			Br.close();
			
			//System.out.println("sizes centroid : " + clusterRepresentative.size());
			//System.out.println("sizes clusterdoc : " + clusterDoc.size());
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}



	public void matchCluster(Searcher search, String[] split) 
	{
		
		similarityBetweenDocsNew(search,split,1);
		
		//System.out.println("size : " + search.reverseRankCluster.size() + " " + search.rankCluster.size());
		
		if(search.reverseRankCluster.size()==0)
			return;
		
		Entry<Double,HashSet<Integer>> entry =search.reverseRankCluster.lastEntry();		
		
		
		HashSet<Integer> hash=entry.getValue();
		
		Iterator<Integer> it = hash.iterator();
		
		//System.out.println("score " + entry.getKey());
		
		if(entry.getKey()!=0.0)
		{
			while(it.hasNext())
			{	
				int a=it.next().intValue();

				finalClusterDoc.addAll(clusterDoc.get(a));	
				
				if(finalClusterDoc.size()>20000)
					break;
			}
		}

	//	System.out.println("Number of DOCS:"+finalClusterDoc.size());
	}

	/*private void similarityBetweenDocsNewWordNet(Searcher search,
		String[] split, int i) {
		
		for (Entry<Integer, HashSet<String>> entry : clusterRepresentative.entrySet())
		{
			HashSet<String> h = entry.getValue();
			double result=0;
			int j=0;
			while(j<split.length)		
			{	
				String word=split[j];
				
			  
				  
				  if(h!=null)
				  {  
					  Iterator<String> it = h.iterator();
					  while(it.hasNext())
					  {	
						  result+=res.max(split[j], it.next(),"n");
					  }
							  
				  }
			  
			  j++;
			}
			//System.out.println("Cluster  "+entry.getKey()+"  result:"+result);
			 search.ranking(entry.getKey(),result);
		}
		
	}*/



	public void similarityBetweenDocsNew(Searcher search, String[] split, int index) 
	{	  
			 
			  for(Entry<Integer, HashSet<String>> entry : clusterRepresentative.entrySet())
			  {
				  
				  double result=0;
				  HashSet<String> h = entry.getValue();
				
	  
				  int frequency=0;
				  
				  if(h!=null)
				  {  

					  int j=0;
						  
						  while(j<split.length)		
						  {
							  String word=split[j];
							  if(h.contains(word.toLowerCase()))
							  {		
								  frequency=1;
								  result+=frequency;
							  }
							  
							  j++;

						  }
						  search.ranking(entry.getKey(),result);
					  
				  }

			  }
		  		
	  }


	public double matchDocument(Searcher search, String[] query,IndexReader reader) 
	{
		double sum=0;
		sum=search.similarityBetweenDocs(finalClusterDoc, query, 0,reader);
		finalClusterDoc.clear();		
		return sum;
		
	}

}


class clutoThread implements Runnable
{
	
	Searcher search;
	Cluster cluster;
	String[] query;
	double sum;
	IndexReader reader;

	public clutoThread(Searcher s, Cluster cluster, String[] split,IndexReader r)
	{
		this.search=s;
		this.query=split;
		this.cluster=cluster;
		this.reader=r;
	}

	@Override
	public void run() 
	{
		System.out.println("inside run cluster");
		cluster.matchCluster(search,query);
		sum=cluster.matchDocument(search,query,reader);
		
	}
}