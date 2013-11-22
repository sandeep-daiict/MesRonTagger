package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.FSDirectory;


public class Cluster 
{

	HashMap<Integer, Integer> centroid= new HashMap<Integer, Integer>();
	HashMap<Integer, ArrayList<Integer>> clusterDoc = new HashMap<Integer, ArrayList<Integer>>();
	TreeSet<Integer> finalClusterDoc = new TreeSet<Integer>();
	int averageLength=123;
	

	
	public void indexMemory(Searcher search) 
	{
		
		try
		{
			File dir = new File("cluster");
			String[] list = dir.list();
			
			int i=0;
			
			for(i=0;i<list.length;i++)
			{
				File file = new File("cluster/" +list[i]);
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				
				int center = Integer.parseInt(br.readLine());
				centroid.put(search.internalDocID.get(center), Integer.parseInt(list[i]));
				
				System.out.println("center is " + center + " " + list[i]);
				
				String str = new String();
				
				ArrayList<Integer> temp = new ArrayList<Integer>();
				
				while((str=br.readLine())!=null)
				{
					temp.add(search.internalDocID.get(Integer.parseInt(str)));				
				}

				clusterDoc.put(Integer.parseInt(list[i]), temp);
				
				
				br.close();
			}
			
			System.out.println("sizes centroid : " + centroid.size());
			System.out.println("sizes clusterdoc : " + clusterDoc.size());
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}



	public void matchCluster(Searcher search, String q) 
	{
		
		Set<Integer> set=centroid.keySet();
				
		search.similarityBetweenDocs(new TreeSet<Integer>(set), q,1);

		System.out.println("size : " + search.reverseRankCluster.size() + " " + search.rankCluster.size());
		
		if(search.reverseRankCluster.size()==0)
			return;
		
		Entry<Double,HashSet<Integer>> entry =search.reverseRankCluster.lastEntry();		
		
		
		HashSet<Integer> hash=entry.getValue();
		
		Iterator<Integer> it = hash.iterator();
		
		System.out.println("score " + entry.getKey());

//		if(entry.getKey()!=0.0)
		{
			while(it.hasNext())
			{						
				int a=it.next().intValue();
				System.out.println("cluster is : " + a + "  " + centroid.get(a));
				finalClusterDoc.addAll(clusterDoc.get(centroid.get(a)));			
			}
		}
		

	}



	public void matchDocument(Searcher search, String query) 
	{
		search.similarityBetweenDocs(finalClusterDoc, query, 0);
		finalClusterDoc.clear();		
		
	}



}