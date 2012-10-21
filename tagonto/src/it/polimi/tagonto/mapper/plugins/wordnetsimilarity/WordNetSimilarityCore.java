/**
 *	
 *	Copyright (C) 2006 Andrea Gabrielli, Giorgio Inglese, Giorgio Orsi
 *
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 
 *	You should have received a copy of the GNU General Public License
 *	along with this program; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *  The GPL is located at: http://www.gnu.org/licenses/gpl.txt
 *
 * 	@author Andrea Gabrielli, Giorgio Inglese (andregabr@NOSPAM.gmail.com)
 *  @author Giorgio Inglese (giorgio.inglese@NOSPAM.gmail.com)
 *  @author Giorgio Orsi (orsi.giorgio@NOSPAM.gmail.com)
 * 	@version 	0.1
 * 	@since 		0.1
 *
 */
package it.polimi.tagonto.mapper.plugins.wordnetsimilarity;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.dictionary.morph.DefaultMorphologicalProcessor;

public class WordNetSimilarityCore {

/**
 * This method was taken from http://alignapi.gforge.inria.fr/ sources.
 * The code of alignapi is released under GPL licence. This code has been modified by the authors
 * of this class.
 * 
 * Compute a basic distance between 2 strings using WordNet synonym.
 * @param string s1
 * @param string s2
 * @return similarity between two input strings
 */

public static float[] computeSimilarity(String s1, String s2) {
    
    Vector s1Tokens;
    Vector s2Tokens;
    DefaultMorphologicalProcessor morphProc = new DefaultMorphologicalProcessor();
    Dictionary dictionary = Dictionary.getInstance();
    IndexWord indexNoun1, indexNoun2;
    IndexWord indexAdj1, indexAdj2;
    IndexWord indexVerb1, indexVerb2;
    Iterator pIt, gIt;
    Vector vg, vp;
    String token1, token2;
    float[] simAsAdj  = new float [3];
    float[] simAsNoun = new float [3];
    float[] simAsVerb = new float [3];
    float[] result = new float [3];
    float maxEquivSim, maxSubSim, maxSuperSim;
    float[][] equivSimMatrix, subSimMatrix, superSimMatrix;
    int i, j;
    
    s1Tokens = tokenize(s1);
    s2Tokens = tokenize(s2);
    
    //System.out.println(s1+" - "+s2);
    
    vg = (s1Tokens.size() >= s2Tokens.size()) ? s1Tokens : s2Tokens;
    vp = (s1Tokens.size() >= s2Tokens.size()) ? s2Tokens : s1Tokens;

    equivSimMatrix = new float[vg.size()][vp.size()];
    subSimMatrix = new float[vg.size()][vp.size()];
    superSimMatrix = new float[vg.size()][vp.size()];
    
    i = 0;
    gIt = vg.iterator();
    try { 	
        while (gIt.hasNext()) 
        {
        	token1 = (String) gIt.next();
            
            indexNoun1 = dictionary.lookupIndexWord(POS.NOUN, token1);
            indexAdj1 = dictionary.lookupIndexWord(POS.ADJECTIVE, token1);
            indexVerb1 = dictionary.lookupIndexWord(POS.VERB, token1);
            
            j = 0;
            pIt = vp.iterator();
            while (pIt.hasNext()) {

                token2 = (String) pIt.next();
                
                indexNoun2 = dictionary.lookupIndexWord(POS.NOUN, token2);
                indexAdj2 = dictionary.lookupIndexWord(POS.ADJECTIVE, token2);
                indexVerb2 = dictionary.lookupIndexWord(POS.VERB, token2);
                
                if (token1.equals(token2)) {
                    maxEquivSim = (float)1.0;
                    maxSubSim = -1;
                    maxSuperSim = -1;
                } else {     
                    if (indexAdj1 != null && indexAdj2 != null ) {
                        simAsAdj = computeTokenSimilarity(indexAdj1, indexAdj2);
                    } else {
                        simAsAdj[0] = -1;
                        simAsAdj[1] = -1;
                        simAsAdj[2] = -1;
                    }
                    if (indexNoun1 != null && indexNoun2 != null) {
                        simAsNoun = computeTokenSimilarity(indexNoun1, indexNoun2);
                    } else {
                        simAsNoun[0] = -1;
                        simAsNoun[1] = -1;
                        simAsNoun[2] = -1;
                    }
                    if (indexVerb1 != null && indexVerb2 != null) {
                        simAsVerb = computeTokenSimilarity(indexVerb1, indexVerb2);
                    } else {
                        simAsVerb[0] = -1;
                        simAsVerb[1] = -1;
                        simAsVerb[2] = -1;
                    }             
                    maxEquivSim = tokenAverage(simAsAdj[0], simAsNoun[0], simAsVerb[0]);
                    maxSubSim = tokenAverage(simAsAdj[1], simAsNoun[1], simAsVerb[1]);
                    maxSuperSim = tokenAverage(simAsAdj[2], simAsNoun[2], simAsVerb[2]);
                }
                equivSimMatrix[i][j] = maxEquivSim;
                subSimMatrix[i][j] = maxSubSim;
                superSimMatrix[i][j] = maxSuperSim;
                j++;
            }
            i++;
        }
    }
    catch (JWNLException ex) {
    	ex.printStackTrace();
    }
    result [0] = bestMatch (equivSimMatrix);
    result [1] = bestMatch (subSimMatrix);
    result [2] = bestMatch (superSimMatrix);
    return (result);
}


/**
 * This method was taken from http://alignapi.gforge.inria.fr/
 * The code of alignapi is released under GPL licence. 
 * 
 * This is a string tokenizer.
 * First it looks for non-alphanumeric chars in the string
 * if any, they will be taken as the only delimiters.
 * Otherwise, the standard naming convention will be assumed:
 * words start with a capital letter
 * substring of capital letters will be seen as a whole
 * if it is a suffix
 * otherwise the last letter will be taken as the new token start
 *
 * @param string s
 * @return a vector containing tokens of input string s
 */

public static Vector<String> tokenize(String s) {
	String str1 = s;
	int sLength = s.length();
	Vector<String> vTokens = new Vector<String>();
	
	// 1. detect possible delimiters
	// starts on the first character of the string
	int tkStart = 0;
	int tkEnd = 0;
	
	// looks for the first delimiter
	// while (tkStart < sLength  && isAlpha (str1.charAt(tkStart))) {
	while (tkStart < sLength  && isAlphaNum (str1.charAt(tkStart))) {
		tkStart++;
	}
	
	// if there is one then the tokens will be the
	// substrings between delimiters
	if (tkStart < sLength){
		
		// reset start and look for the first token
		tkStart = 0;

		// ignore leading separators
		// while (tkStart < sLength && ! isAlpha (str1.charAt(tkStart))) {
		while (tkStart < sLength  && ! isAlphaNum (str1.charAt(tkStart))) {
			tkStart++;
		}

		tkEnd = tkStart;

		while (tkStart < sLength) {

			// consumption of the Alpha/Num token
			if (isAlpha (str1.charAt(tkEnd))) {
				while (tkEnd < sLength  && isAlpha (str1.charAt(tkEnd))) {
					tkEnd++;
				}
			} else {
				while (tkEnd < sLength  && isNum (str1.charAt(tkEnd))) {
					tkEnd++;
				}					
			}
			
			// consumption of the Num token
			vTokens.add(str1.substring(tkStart, tkEnd));

			// ignoring intermediate delimiters
			while (tkEnd < sLength  && !isAlphaNum (str1.charAt(tkEnd))) {
				tkEnd++;
			}			
			tkStart=tkEnd;
		}			
	}
	
	// else the standard naming convention will be used
	//
	// 
	else{
		// start at the beginning of the string
		tkStart = 0;			
		tkEnd = tkStart;

		while (tkStart < sLength) {

			// the beginning of a token
			if (isAlpha (str1.charAt(tkEnd))){
				
				if (isAlphaCap (str1.charAt(tkEnd))){
					
					// This starts with a Cap
					// IS THIS an Abbreviaton ???
					// lets see how maqny Caps
					while (tkEnd < sLength  && isAlphaCap (str1.charAt(tkEnd))) {
						tkEnd++;
					}
		
					// The pointer is at:
					// a) string end: make a token and go on
					// b) number: make a token and go on
					// c) a small letter:
					// if there are at least 3 Caps,
					// separate them up to the second last one and move the
					// tkStart to tkEnd-1
					// otherwise
					// go on

					if (tkEnd == sLength || isNum (str1.charAt(tkEnd))) {
						vTokens.add(str1.substring(tkStart, tkEnd));
						tkStart=tkEnd;									
					} else {
						// small letter
						if (tkEnd - tkStart > 2) {
							// If at least 3
							vTokens.add(str1.substring(tkStart, tkEnd-1));
							tkStart=tkEnd-1;									
						}
					}
					// if (isAlphaSmall (str1.charAt(tkEnd))){}
				} else {
					// it is a small letter that follows a number : go on
					// relaxed
					while (tkEnd < sLength  && isAlphaSmall (str1.charAt(tkEnd))) {
						tkEnd++;
					}
					vTokens.add(str1.substring(tkStart, tkEnd));
					tkStart=tkEnd;										
				}
			} else {
			
				// Here is the numerical token processing
				while (tkEnd < sLength  && isNum (str1.charAt(tkEnd))) {
					tkEnd++;
				}
				vTokens.add(str1.substring(tkStart, tkEnd));
				tkStart=tkEnd;			
			}
		}	
	}
	return (vTokens);
}

public static boolean isAlphaNum(char c) {
	return isAlpha(c) || isNum(c);
}

public static boolean isAlpha(char c) {
	return isAlphaCap(c) || isAlphaSmall(c);
}

public static boolean isAlphaCap(char c) {
	return (c >= 'A') && (c <= 'Z');
}

public static boolean isAlphaSmall(char c) {
	return (c >= 'a') && (c <= 'z');
}

public static boolean isNum(char c) {
	return (c >= '0') && (c <= '9');
}

/**
 * This method was taken from http://alignapi.gforge.inria.fr/
 * The code of alignapi is released under GPL licence. 
 * 
 * @param matrix[][]
 * @return bestMatch in a matrix. The matrix contains all similarities between tokens of two different strings
 */
private static float bestMatch(float matrix[][]) {

    int nbrLines = matrix.length;
    
    if (nbrLines == 0)
        return 0;

    int nbrColumns = matrix[0].length;
    float sim = 0;

    int minSize = (nbrLines >= nbrColumns) ? nbrColumns : nbrLines;

    if (minSize == 0)
        return 0;

    for (int k = 0; k < minSize; k++) {
        
    	float max_val = 0;
        int max_i = 0;
        int max_j = 0;
        
        for (int i = 0; i < nbrLines; i++) {
            
        	for (int j = 0; j < nbrColumns; j++) {
                
            	if (max_val < matrix[i][j]) {
                    
                    max_val = matrix[i][j];
                    max_i = i;
                    max_j = j;
                }
            }
        }
        
        for (int i = 0; i < nbrLines; i++) {
        	matrix[i][max_j] = 0;
        }
        
        for (int j = 0; j < nbrColumns; j++) 
        {
            matrix[max_i][j] = 0;
        }
        sim += max_val;
    }
    
    //FIXME
    return sim / (nbrLines + nbrColumns - minSize);

}

/**
 * This method was taken from http://alignapi.gforge.inria.fr/
 * The code of alignapi is released under GPL licence. 
 * 
 * @param IndexWord index1
 * @param IndexWord index2
 * @return similarity between two WordNet words    
 */
public static float[] computeTokenSimilarity(IndexWord index1, IndexWord index2) {
    
	// the max number of common concepts between the two tokens
    float maxCommon = 0;
    
    // the result array [0]=equivalence, [1]=hyponym, [2]=hypernym,
    float[] result = new float[3];
    
    // the two lists giving the best match
    PointerTargetNodeList best1 = new PointerTargetNodeList();
    PointerTargetNodeList best2 = new PointerTargetNodeList();

    // the two lists currently compared
    PointerTargetNodeList ptnl1 = new PointerTargetNodeList();
    PointerTargetNodeList ptnl2 = new PointerTargetNodeList();

    // The two tokens exist in WordNet, we find the "depth"
    if (index1!=null && index2!=null) {
        try {      	
        	// Best match between current lists
            int maxBetweenLists = 0;

            // Synsets for each token
            Synset[] Syno1 = index1.getSenses();
            Synset[] Syno2 = index2.getSenses();
            
            //Hyponyms and hypernyms counters
            int hypo=0;
            int hyper=0;

            //For each sense of the first token...
            for (int i = 0; i < index1.getSenseCount(); i++) {
            	
                Synset synset1 = Syno1[i];
                //for each sense of the second token...
                for (int k=0; k < index2.getSenseCount(); k++) {
                	
                    Synset synset2 = Syno2[k];
                    if ( synset2.getPointers() == null) {
                        result[0]=0;
                        result[1]=0;
                        result[2]=0;
                    } else {
                        List hypernymList1 = PointerUtils.getInstance().getHypernymTree(synset1).toList();                      
                        List hypernymList2 = PointerUtils.getInstance().getHypernymTree(synset2).toList();
                    
                        if (isContained(synset2, hypernymList1)){
                            hypo++;
                        }
                        if (isContained(synset1, hypernymList2)) {
                            hyper++;
                        }
                        
                        Iterator list1It = hypernymList1.iterator();                                       
                        while (list1It.hasNext()) {
                            ptnl1 = (PointerTargetNodeList) list1It.next();
                        
                            Iterator list2It = hypernymList2.iterator();               
                            while (list2It.hasNext()) {
                                ptnl2 = (PointerTargetNodeList) list2It.next();
                               
                                //check the common concepts for these synsets...
                                int cc = getCommonConcepts(ptnl1, ptnl2);
                                if (cc > maxBetweenLists) {
                                    maxBetweenLists = cc;
                                    best1 = ptnl1;
                                    best2 = ptnl2;
                                }
                            }
                        }
                    }   
                    if (maxBetweenLists > maxCommon) {
                        maxCommon = maxBetweenLists;
                    }
                }
            }
            result[1]=(float)hypo/(index1.getSenseCount()*index2.getSenseCount());
            result[2]=(float)hyper/(index1.getSenseCount()*index2.getSenseCount());
            
            //System.out.println("hyponyms: "+hypo+" - "+"possible senses couples: "+index1.getSenseCount()*index2.getSenseCount());
            //System.out.println("hypernyms: "+hyper+" - "+"possible senses couples: "+index1.getSenseCount()*index2.getSenseCount());
            if (best1.isEmpty() && best2.isEmpty()) {
                result[0] = 0;
            } else {
                result[0] = (2 * maxCommon / (best1.size() + best2.size()));
            }
        
        } catch (JWNLException je) {
        	je.printStackTrace();
            System.exit(-1);
        }
    }
    return (result);
}

    /**
     * This method was taken from http://alignapi.gforge.inria.fr/
     * The code of alignapi is released under GPL licence. 
     * 
     * @param PointerTargetNodeList list1
     * @param PointerTargetNodeList list2
     * @return Integer number of common concept between two lists   
     */
    public static int getCommonConcepts(PointerTargetNodeList list1,PointerTargetNodeList list2) {

        int cc = 0;
        int i = 1;
    
        while (i <= Math.min(list1.size(),list2.size()) && ((PointerTargetNode) list1.get(list1.size() - i)).getSynset() == ((PointerTargetNode) list2.get(list2.size() - i)).getSynset()) {
            cc++;
            i++;
        }    
        return (cc);
    }

    /**
     * @param synset
     * @param ptList
     * @return
     */
    private static boolean isContained (Synset synset, List ptList) {
        Iterator it = ptList.iterator();
        while (it.hasNext()) {
            Iterator listIt = ((PointerTargetNodeList)it.next()).iterator();
            while (listIt.hasNext()) {
                if (synset.equals(((PointerTargetNode)listIt.next()).getSynset())) {
                    return (true);
                }
            }
        }
        return (false);
    }
    
    /**
     * @param simAsAdj
     * @param simAsNoun
     * @param simAsVerb
     * @return
     */
    private static float tokenAverage (float simAsAdj, float simAsNoun, float simAsVerb) {
        float maxSim=0;
        
        int k=0;
        if (simAsAdj>=0) {
            k++;
            maxSim+=simAsAdj;
        }
        if (simAsVerb>=0) {
            k++;
            maxSim+=simAsVerb;
        }
        if (simAsNoun>=0) {
            k++;
            maxSim+=simAsNoun;
        }
        if (k>0) {
            maxSim=maxSim/k;
        } else {
            maxSim = -1;
        }     
        return (maxSim);
    }

}