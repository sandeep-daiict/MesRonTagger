package com.iiit.IRE.Logic;

import java.awt.PageAttributes.MediaType;
import java.io.*;

import javax.activation.MimeType;

import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.pdfbox.*;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.mime.*;


public class ParseHTML 
{

	 public static void main(String[] args)
	 {

		 try 
		 {		

			 	File f = new File("Final/"+args[0]);
			 	FileWriter fw = new FileWriter(f);
			 	BufferedWriter bw = new BufferedWriter(fw);
	
				InputStream input = new FileInputStream(args[0]);
				
				BodyContentHandler handler = new BodyContentHandler();
				Metadata metadata = new Metadata();
				new HtmlParser().parse(input, handler, metadata, new ParseContext());
				bw.write(handler.toString());
				
				bw.flush();
				bw.close();
				input.close();
		 }
		 
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
		 
     }
}