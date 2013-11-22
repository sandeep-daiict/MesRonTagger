package com.iiit.IRE.Logic;

import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JWindow;
import edu.mit.jwi.item.IHasVersion;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.Resnik;

public class Similarity 
{
	


		public static void main(String[] args)
		{
			

			
			TreeMap<String, Double> scores1 = res.res(args[0], args[1],"n");
			for(Entry<String, Double> e: scores1.entrySet())
			    System.out.println(e.getKey() + "\t" + e.getValue());
			System.out.println("\nhighest score\t=\t" + res.max(args[0], args[1],"n") + "\n\n\n");
			//System.out.println("Similarity between 1 and 2:"+sim1+"\nSimilarity between 1 and 2:"+sim2);
			
		}

		private static double getSim(Synset[] synsets1, Synset[] synsets2) 
		{
			
			
//			for(int i=0;i<synsets1.length;i++)
//			{
//				for(int j=0;j<synsets2.length;j++)
//				{
//					System.out.println(synsets1[i].path_similarity(synsets2[j]));
//				}
//			}
			// TODO Auto-generated method stub
			return 0;
		}
}
