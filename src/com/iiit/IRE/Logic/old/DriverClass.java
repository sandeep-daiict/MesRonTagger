package com.iiit.IRE.Logic;

import java.util.ArrayList;
import java.util.Scanner;
import com.iiit.IRE.Logic.Searcher;
import com.iiit.IRE.Logic.Cluster;
//import org.apache.lucene.queryParser.ParseException;




public class DriverClass 
{
	
	public static void main(String arg[]) 
	{
		
//		new DriverClass().go(arg);
		
	}
	
	public ArrayList<String> go(String url, String[] arg, Searcher search2, Cluster cluster2)
	{
		
		System.out.println("inside index");
//		Searcher search = new Searcher();
		System.out.println("search object made");
//		search.indexInMemory();
		System.out.println("search index in memory");

//		Cluster cluster = new Cluster();
//		cluster.indexMemory(search);
		System.out.println("cluster index in memory");

//		search.printId();

//		System.out.print("Enter description : ");
//		Scanner scan = new Scanner(System.in);
//
//		String str= scan.nextLine();
//		
//		while(!str.equals("exit"))
//		{
			long t=System.currentTimeMillis();	
//			String split[] = str.split(" ");
			String q=search2.query(url,arg);
			System.out.println("query is " + q);

			cluster2.matchCluster(search2,q);
			cluster2.matchDocument(search2,q);

			ArrayList<String> list =search2.retrieveTop();

//			System.out.println(System.currentTimeMillis()-t);			
//			System.out.print("Enter description : ");
//			str= scan.nextLine();

//		}
			return list;

	}

	

}
