package model;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.queryParser.ParseException;




public class DriverClass 
{
	
	public static void main(String arg[]) throws ParseException
	{
		
//		new DriverClass().go(arg);
		
	}
	
	public ArrayList<String> go(String url, String[] arg) throws ParseException 
	{
		
		Searcher search = new Searcher();
		search.indexInMemory();
		System.out.println("search index in memory");

		Cluster cluster = new Cluster();
		cluster.indexMemory(search);
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
			String q=search.query(url,arg);
			System.out.println("query is " + q);

			cluster.matchCluster(search,q);
			cluster.matchDocument(search,q);

			ArrayList<String> list =search.retrieveTop();
//			System.out.println(System.currentTimeMillis()-t);			
//			System.out.print("Enter description : ");
//			str= scan.nextLine();

//		}
			return list;

	}

	

}
