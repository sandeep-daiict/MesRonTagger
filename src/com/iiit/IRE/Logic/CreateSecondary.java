package com.iiit.IRE.Logic;

import java.io.*;
import java.util.ArrayList;


class CreateSecondary
{
	
	public static void main(String arg[])
	{
	//	new CreateSecondary().go();
		new CreateSecondary().start();
		
	}
	
	public void start() 
	{
		try
		{
			
			File myFile = new File("new_dataset/bookmarks_tags_final");
			RandomAccessFile r = new RandomAccessFile(myFile,"r");
	
			File myFile1 = new File("new_dataset/SecondaryTitles");
			FileWriter fw = new FileWriter(myFile1);
			BufferedWriter bw = new BufferedWriter(fw);
			
			r.seek(0);
			int count=0;
			long location=0;
			String tmp;
			boolean flag=true;
	//		title.add(location);
			
			while((tmp=r.readLine())!=null)
			{
				String[] split = tmp.split("\\$",2);

				System.out.println(tmp + " : " +split[0]);
				if(flag)
				{
					flag=false;
					bw.write(split[0]+" " +Long.toString(location));
				}
				else
				{
					bw.newLine();
					bw.write(split[0]+" " +Long.toString(location));					
				}
				location=r.getFilePointer();
			}
			
			bw.flush();
			bw.close();
	//		br.close();
			r.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception : "+e);
		}
	
		
	}
	
	public void go()
	{
			try
			{
				File myFile = new File("subpart/1");
				FileReader fr = new FileReader(myFile);
				BufferedReader br = new BufferedReader(fr);
	
				File sec = new File("secondary");
				FileWriter fw = new FileWriter(sec);
				BufferedWriter bw = new BufferedWriter(fw);
				System.out.println("file created");
				String tmp;
				long location=0;
				
				ArrayList<String> arr = new ArrayList<String>();
				
				tmp=br.readLine();
				location+=(tmp.length() +1);
	
				tmp=br.readLine();
				
				String[] c=tmp.split("\\?", 2);
				arr.add(c[0] + ":" +location + "\n");
	
				location+=(tmp.length() +1);
				
				
				int count=0;
				while((tmp=br.readLine())!=null)
				{
	
					if(count==20)
					{
	//					System.out.println("inside 20");
						count=0;
						String[] ch=tmp.split("\\?", 2);
						
						location+=(tmp.length() +1);
	
						tmp=br.readLine();
						
						String ch1[]=null;
						
						boolean flag=true;
						
						
						while(flag && tmp!=null)
						{
							ch1=tmp.split("\\?",2);
					//		System.out.println(ch[0] + " " +ch1[0]);
	
							if(ch[0].substring(0, ch[0].length()-1).equals(ch1[0].substring(0, ch1[0].length()-1)))
							{
								location+=(tmp.length() +1);
								tmp=br.readLine();
							}
							else
							{
								flag=false;
								
								arr.add(ch1[0] + ":" + location + "\n");
								location+=(tmp.length() +1);
							}							
						}					
					}
					else
						location+=(tmp.length() +1);
	
	//				System.out.println(count);
					
					if(arr.size()==10000)
					{
						System.out.println("inside write");
						int k=0;
						while(k<arr.size())
							bw.write(arr.get(k++));
						
						arr.clear();
					}
				
					count++;
				}
	
	//			String[] ch=tmp.split("\\?", 2);
				
				arr.add("zzzzzzzzzzzzzzzzzzzzzzzzz:" + location);
	
				System.out.println("final add");
				int k=0;
				while(k<arr.size())
					bw.write(arr.get(k++));
				
				arr.clear();
	
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