package com.iiit.IRE.Logic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class IndexerContent {

	HashMap<String, String> map = new HashMap<String, String>();

	public static void main(String[] args) throws IOException, ParseException {
		IndexerContent index = new IndexerContent();
		index.descriptionInMemory();
		index.go();
	}

	private void go() throws IOException 
	  {
		  
		  	HashSet<String> hs = new HashSet<String>();
		  	
		  	

		  	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			try 
			{
			    FSDirectory directory = FSDirectory.open(new File("indexContent"));
			    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
			    IndexWriter w = new IndexWriter(directory, config);


//		    	File dir=new File("/home/juhi/SEM II/major/dataset/Final/files");
//				File[] files = dir.listFiles();
		    
			
				for (Entry<String, String> entry : map.entrySet()) 
				{
						
//						File f = new File("/home/juhi/SEM II/major/dataset/Final/files/"+entry.getKey());
//						if(f.exists())
//						{
//							FileReader fr = new FileReader(f);
////						    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());					
//						    addDoc(w, fr, f.getName(), entry.getValue());
//									
//						}
//						else
//						{
//							FileWriter fw = new FileWriter(f);
//							fw.write("");
//							fw.close();
//							FileReader fr = new FileReader(f);
//						    System.out.println("NEW FILE CREATED Key = " + entry.getKey() + ", Value = " + entry.getValue());
							addDoc(w, entry.getKey(), entry.getValue());
//						}
					
				}
				
				w.close();
			
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

		
	  }

	private void descriptionInMemory() 
	{
		
		File f = new File("DES+URL");
		File f1 = new File("new_dataset/onlyID");

		try 
		{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String str = new String();

			FileReader fr1 = new FileReader(f1);
			BufferedReader br1 = new BufferedReader(fr1);

			String str1 = new String();
			
			while (((str = br.readLine()) != null) && ((str1 = br1.readLine()) != null)) 
			{
				System.out.println("id " + str1);
				map.put(str1, str);
			}

			br.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private static void addDoc(IndexWriter w, String isbn, String desc) throws IOException 
	{
		
		Document doc = new Document();

		Field field_title=new Field("T", desc, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES);
//		field_title.setBoost(1.5f);
//
//		
//		
//		doc.add(new Field("C", title));
		doc.add(new Field("isbn", isbn, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(field_title);
		
//		System.out.println("doc " + doc.toString()));
		w.addDocument(doc);
	}

}