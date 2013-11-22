package com.iiit.IRE.Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class NewDataSetIndex 
{

	public static void main(String[] args) 
	{
		new NewDataSetIndex().go();

	}

	private void go() 
	{
		
	  	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		try 
		{
		    FSDirectory directory = FSDirectory.open(new File("/home/juhi/SEM II/major/dataset/indexContent"));
		    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		    IndexWriter w = new IndexWriter(directory, config);

		    File f = new File("new_dataset/des_old");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			String str = new String();

			while ((str = br.readLine()) != null) 
			{
				System.out.println(str);
				String[] split = str.split("\t", 2);
				addDoc(w, split[0], split[1]);
			}

			w.close();
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
		doc.add(new Field("isbn", isbn, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(field_title);
		
//		System.out.println("doc " + doc.toString()));
		w.addDocument(doc);
	}


}
