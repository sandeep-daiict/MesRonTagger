package com.iiit.IRE.Logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
public class ClusterCentroid 
{	static HashMap<String,Integer> rankTerm = new HashMap<String,Integer>();
	static TreeMap<Integer,HashSet<String>> reverseRankTerm = new TreeMap<Integer,HashSet<String>>();
	public static void main(String[] args) throws IOException
	{
		ClusterCentroid.run();
	}
	public static void checkingRank(int tfScore, String docid, int j) 
	{
		if(j==1)
		{
			if(reverseRankTerm.containsKey(tfScore))
			{
				HashSet<String> hs =reverseRankTerm.get(tfScore);
				hs.add(docid);
				reverseRankTerm.put(tfScore, hs);
			}
			else
			{
				HashSet<String> hs = new HashSet<String>();
				hs.add(docid);
				reverseRankTerm.put(tfScore, hs);
			}
		}
		else
		{
			if(reverseRankTerm.containsKey(tfScore))
			{
				HashSet<String> hs =reverseRankTerm.get(tfScore);
				hs.remove(docid);
				if(hs.size()==0)
					reverseRankTerm.remove(tfScore);
				else
					reverseRankTerm.put(tfScore, hs);
			}
		}
	}
	public static void run() throws IOException
	{
		//Stemmer stem = new Stemmer();
		File f_stop = new File("stop_words.txt");
		FileReader fr_stop = new FileReader(f_stop);
		BufferedReader br_stop = new BufferedReader(fr_stop);
		HashSet<String> stop_words = new HashSet<String>();
		String Line="";
		while((Line=br_stop.readLine())!=null)
		{
			stop_words.add(Line.toLowerCase());
		}
		File f = new File("DES+URL");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
		HashMap<Integer,String> description=new HashMap<Integer, String>(); 
		//String Line="";
		File f5 = new File("new_dataset/onlyID");
		FileReader fr5 = new FileReader(f5);
		BufferedReader br5 = new BufferedReader(fr5);
		String Line2= new String();
		while(((Line=br.readLine())!=null) && ((Line2=br5.readLine())!=null))
		{	
			
			description.put(Integer.parseInt(Line2), Line);
		}
		File dir = new File("cluster");
		String[] list = dir.list();
		int i=0;
		File F = new File("cluster_representative");
		FileWriter frnew = new FileWriter(F);
		BufferedWriter brnew = new BufferedWriter(frnew);
		for(i=0;i<list.length;i++)
		{
			File file = new File("cluster/" +list[i]);
			brnew.write(list[i]+" ");
			System.out.println("file:"+list[i]);
			FileReader fr1 = new FileReader(file);
			BufferedReader br1 = new BufferedReader(fr1);
			
			while((Line=br1.readLine())!=null)
			{
				String str=description.get(Integer.parseInt(Line));
				str=str.toLowerCase();
				int len = str.length();
				String word="";
				//System.out.println("string"+str);
				
				for(int j=0;j<len;j++)
				{	
					
					char c=str.charAt(j);
					if((c<=122 && c>=97) )
						word+=c;
					else
					{	
						if(word!="" && !stop_words.contains(word) && word.length()>3)
						{	//word=stem.go(word);
							//word=word.substring(0,word.length()-1);
							if(rankTerm.containsKey(word))
							{
								int d= rankTerm.get(word);
								checkingRank(d, word,0);
								d++;
								checkingRank(d, word,1);	
								rankTerm.put(word, d);
							
							}
							else
							{
								
								rankTerm.put(word, 1);
								checkingRank(1, word,1);				
							}
							word="";
						}
					}
						
				}
				if(word!="" && !stop_words.contains(word) && word.length()>3)
				{	//word=stem.go(word);
				//word=word.substring(0,word.length()-1);
					if(rankTerm.containsKey(word))
					{
						int d= rankTerm.get(word);
						checkingRank(d, word,0);
						d++;
						checkingRank(d, word,1);	
						rankTerm.put(word, d);
					
					}
					else
					{
						
						rankTerm.put(word, 1);
						checkingRank(1, word,1);				
					}
					word="";
				}
				
			}
			//System.out.println("size"+rankTerm.size());
			
			for(int k=0;k<10 ;)
			{
				if(!reverseRankTerm.isEmpty())
				{
				Entry<Integer, HashSet<String>> d=reverseRankTerm.pollLastEntry();
				
					
				Iterator<String> it = d.getValue().iterator();
				
				while(k<10 && it.hasNext())
				{		k++;
						String q=it.next();
						

						brnew.write(q+" ");
						
				}
				}
				else
					break;
			}
			rankTerm.clear();
			reverseRankTerm.clear();
			brnew.newLine();
		}		

		                                                                                         
//		frnew.close();
		brnew.close();
	}
	
}
