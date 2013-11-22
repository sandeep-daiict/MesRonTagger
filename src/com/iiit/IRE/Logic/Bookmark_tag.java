package com.iiit.IRE.Logic;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class Bookmark_tag 
{
	public static void main(String[] args)
	{
		
		new Bookmark_tag().go();
	}

	private void go() 
	{
		TreeMap<Integer, TreeMap<Integer, Integer>> bookmark_tag = new TreeMap<Integer, TreeMap<Integer, Integer>>();
//		HashMap<Integer, Integer> bookmark_tag = new HashMap<Integer, Integer>();
		
		try
		{
			
			File f =new File("new_dataset/bookmark_tags_old");
			FileReader fr = new FileReader(f);
			BufferedReader bookmarks = new BufferedReader(fr);
			
			String line = new String();
			
			while((line=bookmarks.readLine())!=null)
			{
				String[] split = line.split("\t", 2);

				if(split.length==2)
				{

					int id = Integer.parseInt(split[0]);

					if(bookmark_tag.containsKey(id))
					{
						TreeMap<Integer, Integer> h = bookmark_tag.get(id);
						h.put(Integer.parseInt(split[1]), 1);				
						bookmark_tag.put(id, h);			
					}
					else 
					{
						TreeMap<Integer, Integer> h = new TreeMap<Integer, Integer>();
						h.put(Integer.parseInt(split[1]), 1);				
						bookmark_tag.put(id, h);			
					}
				}
				split=null;
			}
			
			
			File write = new File("new_dataset/bookmarks_tags_final");
			FileWriter fw = new FileWriter(write);
			BufferedWriter bw = new BufferedWriter(fw);
			
			  for(Entry<Integer, TreeMap<Integer, Integer>> entry : bookmark_tag.entrySet())
			  {
				  
				  double result=0;
				  TreeMap<Integer, Integer> h = entry.getValue();
				  bw.write(entry.getKey()+"$");
				  for(Entry<Integer,Integer>  entry1 : h.entrySet())
				  {
					  bw.write(entry1.getKey()+",");  			
				  }
				  bw.newLine();
			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
