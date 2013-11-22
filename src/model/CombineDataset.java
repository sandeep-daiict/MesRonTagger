package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;


public class CombineDataset 
{

	int max = 107253;
	int tag =64306;
	
	public static void main(String[] args) 
	{
		
		CombineDataset combine = new CombineDataset();
		
//		combine.bookmarks();
//		combine.description();
		combine.tags();

	}

	private void tags() 
	{
		try
		{
			
			HashMap<String, Integer> map= new HashMap<String, Integer>();
			
			File read = new File("new_dataset/tags.dat");
			FileReader fr = new FileReader(read);
			BufferedReader br = new BufferedReader(fr);
			
			String str = new String();
			
			while((str=br.readLine())!=null)
			{
				System.out.println(str);
				String[] split = str.split("\t");
				map.put(split[1], Integer.parseInt(split[0]));
			}
			
			br.close();
			
			File f = new File("new_dataset/tags.dat");
			FileWriter fw = new FileWriter(f,true);
			BufferedWriter tags = new BufferedWriter(fw);

			File read1 = new File("new_dataset/tags_new");
//			FileReader fr1 = new FileReader(read1);
//			BufferedReader br1 = new BufferedReader(fr1);
			Scanner br1 = new Scanner(read1);
			
			
			File f1 = new File("new_dataset/bookmark_tags_old");
			FileWriter fw1 = new FileWriter(f1,true);
			BufferedWriter book = new BufferedWriter(fw1);

			String string = new String();
			
			int count=max;
			
			while(br1.hasNextLine())
			{

				string=br1.nextLine();
				count++;

				if(string==null)
					continue;
				
				HashSet<String> list =parseString(string);
				System.out.println("id " + count + " " +list.toString());

				Iterator<String> it = list.iterator();
				
				while(it.hasNext())
				{	
					String q=it.next();
					
					if(!map.containsKey(q))
					{
						tag++;
						map.put(q, tag);
						book.write(Integer.toString(count) + "\t" +Integer.toString(tag));
						book.newLine();
						tags.write(Integer.toString(tag) + "\t" + q);
						tags.newLine();
					}
					else
					{
						book.write(Integer.toString(count) + "\t" +Integer.toString(map.get(q)));
						book.newLine();
					}
				}
			}
			
			book.flush();
			book.close();
			tags.flush();
			tags.close();
			br1.close();
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private HashSet<String> parseString(String string) 
	{
		
		int len= string.length();
		
		char[] c =string.toCharArray();
		
		int i=0,start=0,length=0;
//		StringBuilder word = new StringBuilder();
		HashSet<String> list = new HashSet<String>();
		
		
		
		while(i<len)
		{
			start=i;
			while((i<len) && (string.charAt(i)!=':') && (string.charAt(i)!=','))
			{
				i++;
				length++;
			}
			
			String word=new String(c,start,length);
			length=0;
			i++;
			
			if(word!=null)
			{
				list.add(word.toString());
				word=null;
			}
			
		}
		
		return list;
	}

	private void description() 
	{
		try
		{
			
			File f = new File("new_dataset/des_old");
			FileWriter fw = new FileWriter(f,true);
			BufferedWriter bw = new BufferedWriter(fw);

			File read = new File("new_dataset/des_new");
			FileReader fr = new FileReader(read);
			BufferedReader br = new BufferedReader(fr);
			
			
			String str = new String();
			int count=max+1;
			
			while((str=br.readLine())!=null)
			{
				bw.write(Integer.toString(count++) + "\t" + str);
				bw.newLine();	
			}
			
			bw.flush();
			bw.close();
			br.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}

	private void bookmarks() 
	{
		
		try
		{
			
			File f = new File("new_dataset/urls_old");
			FileWriter fw = new FileWriter(f,true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			File read = new File("new_dataset/urls");
			FileReader fr = new FileReader(read);
			BufferedReader br = new BufferedReader(fr);
			
			
			String str = new String();
			int count=max+1;
			
			while((str=br.readLine())!=null)
			{
				bw.write(Integer.toString(count++) + "\t" + str);
				bw.newLine();	
			}
			
			bw.flush();
			bw.close();
			br.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
