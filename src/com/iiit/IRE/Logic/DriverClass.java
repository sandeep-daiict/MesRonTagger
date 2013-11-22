package com.iiit.IRE.Logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.Resnik;

public class DriverClass 
{
	
	
	
	public ArrayList<String> go(String url, String[] arg, Searcher search, Cluster cluster, StandardAnalyzer analyzer, FSDirectory index, IndexReader reader, QueryParser queryParser, IndexSearcher indexSearcher, int num)
	{	

		ArrayList<String> list=null;

		try
		{
			
			search.maxResults=num;
				
			String q=search.parseQuery(url,arg,reader);
			
			int count=Thread.activeCount();
			System.out.println("count :" +Thread.activeCount() + " " +count);
			
			luceneThread lt = new luceneThread(search, q,analyzer,index,queryParser,indexSearcher,reader);
			Thread lucene = new Thread(lt);
			lucene.setName("lucene thread");
			lucene.start();
	
			System.out.println("after lucene :" +Thread.activeCount());
			
			clutoThread ct = new clutoThread(search,cluster,arg,reader);
			Thread cluto = new Thread(ct);
			cluto.setName("cluto");
			cluto.start();

			System.out.println("after cluto :" +Thread.activeCount());

			while(Thread.activeCount()!=count)
			{
				System.out.println(Thread.currentThread().getName() + " " + count);
				Thread.sleep(500);
			}
	
				
			search.iterateMap(ct.sum);
			
			list =search.retrieveTop();
			

		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
			
		//System.out.println(System.currentTimeMillis()-t);			
		//System.out.print("Enter description : ");
			
		return list;

	}

	

}
