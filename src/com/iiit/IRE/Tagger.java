package com.iiit.IRE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//import org.apache.lucene.queryParser.ParseException;

import com.iiit.IRE.Logic.*;

/**
 * Servlet implementation class Tagger
 */
//@WebServlet("/Tagger")

public class Tagger extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Searcher search = new Searcher();
	Cluster cluster = new Cluster();
	StandardAnalyzer analyzer =null;
	FSDirectory index =null;
	IndexReader reader =null;
	QueryParser qu =null;
	IndexSearcher indexSearcher =null;
	
    public Tagger() 
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException
    {
    	try
    	{
	    	System.out.println("inside init()");
	    	search.indexInMemory();
	    	cluster.indexMemory(search);
	    	analyzer = new StandardAnalyzer(Version.LUCENE_36);
		    index = FSDirectory.open(new File("/home/juhi/workspace/TagCloud/indexContent"));
		    reader = IndexReader.open(index);
		    qu = new QueryParser(Version.LUCENE_36, "T", analyzer);
		    indexSearcher = new IndexSearcher(reader);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
		
    	
  //  	cluster.indexMemory(search);
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

			DriverClass driver = new DriverClass();
			String url =request.getParameter("url");
			String desc =request.getParameter("desc");
			int num =Integer.parseInt(request.getParameter("tags"));
			System.out.println("url is : " + url + " desc : " +desc);
			String split[] = desc.split(" ");

			search.result.clear();
			ArrayList<String> list =driver.go(url,split,search,cluster,analyzer, index, reader, qu, indexSearcher,num);
			
			System.out.println("lis" + list.size());
			while(list.size()<21)
			{
				list.add(" ");
			}
			
			request.setAttribute("tags", list);
			request.setAttribute("query", desc);
			request.setAttribute("url", url);
			RequestDispatcher r = request.getRequestDispatcher("./ResultTag.jsp");
			r.forward(request, response);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("yes im inside TAGGER");
		doGet(request, response);
	}

}
