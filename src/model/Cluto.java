package model;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class Cluto 
{
	
	HashMap<String,ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	HashMap<String,String> document = new HashMap<String, String>();
	HashMap<String,Double> maxVal = new HashMap<String, Double>();
	
	public static void main(String args[])
	{
		Cluto c = new Cluto();
		c.go(args);
		c.createFile();		
	}
	
	void createFile() 
	{
		for (Entry<String, ArrayList<String>> entry : map.entrySet()) 
		{
			try
			{
				File f = new File("cluster/" +entry.getKey());
				FileWriter fw = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(fw);
				
				int i=0;
				ArrayList<String> s =entry.getValue();
				
				bw.write(document.get(entry.getKey()));
				bw.newLine();
				
				for(i=0; i<s.size(); i++)
				{
					
					bw.write(s.get(i));
					bw.newLine();
				}
				
				bw.flush();
				bw.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		
	}

	void go(String[] args)
	{
		File f = new File(args[0]);
		File f1 = new File(args[1]);
		
		try
		{
			FileReader fr = new FileReader(f);
			FileReader fr1 = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			BufferedReader br1 = new BufferedReader(fr1);
			
			String s= new String();
			String docID= new String();
			
			while(((s=br.readLine())!=null) && ((docID=br1.readLine())!=null))
			{
				String split[] = s.split(" ", 3);

				Double d = Double.parseDouble(split[1]);
				Double d1 = Double.parseDouble(split[2]);
				
				if(map.containsKey(split[0]))
				{
					ArrayList<String> ar = map.get(split[0]);
					ar.add(docID);
					map.put(split[0], ar);
					
					if(maxVal.get(split[0])<(d-d1))
					{
						maxVal.put(split[0], (d-d1));
						document.put(split[0], docID);
					}
					
				}
				else
				{
					ArrayList<String> ar = new ArrayList<String>();
					ar.add(docID);
					map.put(split[0], ar);
					maxVal.put(split[0], (d-d1));
					document.put(split[0], docID);
				}
				
			}
			
			br.close();
			br1.close();
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
