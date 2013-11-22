package com.iiit.IRE.Logic;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class addURL
{
	
	HashSet<String> words = new HashSet<String>();
	FileWriter fileWriter;
	BufferedWriter fi2;
//	static MaxentTagger tagger;
	
	Set<String> stoplist = new HashSet<String>(Arrays.asList(new String[]{"com", "org", "edu","html", "pdf", "doc", "etc.","main", "default","home","www"}));
	
	

	public static void main(String[] args) throws ClassNotFoundException, IOException
	{
	
//		tagger = new MaxentTagger("/home/pallavi/iiit/workspace/TagCloud/lib_36/stanford-postagger-2012-11-11/models/english-bidirectional-distsim.tagger");
		addURL d = new addURL();
		d.addurl();
		
	}
	
	public static boolean isNoun(String word,MaxentTagger tagger)
	{
		String tagged = tagger.tagString(word);
		String pos=tagged.split("_")[1];
		
		//System.out.print(pos);
		if((pos.compareTo("NN ")==0) || (pos.compareTo("FW ")==0) || (pos.compareTo("NP ")==0) || (pos.compareTo("NPS ")==0))
		{
			return true;
		}
		return false;	
		
	}
	
	/*public boolean toadd(String str)
	{
		return true;
	}*/
	
	public int removestop(String str3)
	{
		//System.out.println("entered remeovestop");
			int flag=1;
	
			if(stoplist.contains(str3.toLowerCase()) == true)
			{
			     flag=0;				     
			}
			
		  return (flag);


	}
	
	public void addurl()
	{
		FileInputStream in;
		 String strLine;
		 
		try 
		{
			in = new FileInputStream("FINALINPUT");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			fileWriter = new FileWriter("DES+URL");
			fi2=new BufferedWriter(fileWriter);
				int j=0;
			 while((strLine = br.readLine())!= null)
			  {
				  String mytoken[]=strLine.split(" ",2);
				  //System.out.println(mytoken[0]);
				  //System.out.println(mytoken[1]);
				  System.out.println(j);
				  parseurl(mytoken[0]);
				  parsedes(mytoken[1]);
				  writefile(fi2);
				  j++;
			  }
			 in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("here");
			e.printStackTrace();
		}
	//	words.clear();
	}
	
	public void parseurl(String url)
	{
		int i=0;
		//System.out.println(url);
	//	String temp=new String();
			//while(url.contains("://"))
		int len=url.length();
			while(i<len && url.charAt(i)!='\0' && url.charAt(i)!=':') 
			{
			i++;
			}
			if(i<len &&url.charAt(i)!='\0' && url.charAt(i+1)!='\0'  && url.charAt(i+2)!='\0'  && ( url.charAt(i+1)=='/'&& url.charAt(i+2)=='/') )
			{
				i=i+2;;	
			}
			//System.out.println(url.charAt(i));
			while(i<len && url.charAt(i)!='\0')
			{
				String temp=new String();
			//	System.out.println(url.charAt(i));
				while(i<len &&url.charAt(i)!='\0' && ((url.charAt(i)>=65 && url.charAt(i)<=90) || (url.charAt(i)>=97 && url.charAt(i)<=122))) 
				{
					temp=temp+url.charAt(i);
					//System.out.println(temp);
					i++;
				}

//				break;
				i++;
//				if(removestop(temp)==1 && temp.length()>3 && isNoun(temp,tagger))
				if(removestop(temp)==1 && temp.length()>3)
				{
					words.add(temp);
				}
			}
	}
			
	public void parsedes( String str)
	{
		 String mytoken[]=str.split(" ");
		 for(int i=0;i<mytoken.length;i++)
		 {
				mytoken[i]=mytoken[i].toLowerCase();
				words.add(mytoken[i]);
		 }
	}
	public void writefile(BufferedWriter fi2)
	{
		
		try 
		{

			for (String s : words) {
				fi2.write(s + " ");
			}
			fi2.write("\n");
			
			fi2.flush();
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		words.clear();

	}
		
		
}