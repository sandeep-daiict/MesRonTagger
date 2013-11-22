package com.iiit.IRE.Logic;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class PosTagger
{
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{

			MaxentTagger tagger = new MaxentTagger("./english-bidirectional-distsim.tagger");
			long t=System.currentTimeMillis();
			String sample = "dog";
			String tagged = tagger.tagString(sample);
			System.out.println(tagged);
			System.out.println("time:" + Long.toString((System.currentTimeMillis()-t)));

	}
}
