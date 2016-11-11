/*
 * @name Programming Assignment 5
 * @file PA5.java
 * @author Eric Schulze
 * @version 1.0 11/10/16
 *
 */
import java.util.*;
import java.io.*;
import java.lang.*;

public class PA5{

	//Attributes
	BinarySearchTree[] dictionary;
    long wordsFound;
    long wordsNotFound;
    long comparisonsIfWordFound;
    long comparisonsIfWordNotFound;
    

/***************************************************************************************/
/***************************************************************************************/

	//Constructors
	public PA5(){
		//instantiate array
        
		dictionary = new BinarySearchTree[26];

		//instantiate dictionary binary search trees
		for(int i = 0; i < 26; i++){
			dictionary[i] = new BinarySearchTree<String>();
		}//end for
        
        //instantiate counters
        wordsFound = 0;
        wordsNotFound = 0;
        comparisonsIfWordFound = 0;
        comparisonsIfWordNotFound = 0;
	}

/***************************************************************************************/
/***************************************************************************************/

	//methods
	public static void main(String[] args){
		PA5 pa = new PA5();
        
		pa.dictionaryPopulate();
        
        long start = System.nanoTime();
        pa.compareFileAgainstDictionary("oliver.txt");
        long stop = System.nanoTime();
        
        System.out.println("\nTotal words compared to dictionary: " + (pa.wordsFound + pa.wordsNotFound));
        System.out.println("\nTotal words found in dictionary: " + pa.wordsFound);
        System.out.println("Total words not found in dictionary: " + pa.wordsNotFound);
        System.out.println("\nTotal comparisons: " + (pa.comparisonsIfWordFound + pa.comparisonsIfWordNotFound));
        System.out.println("Total comparisons if word was found: " + pa.comparisonsIfWordFound);
        System.out.println("Total comparisons if word was not found: " + pa.comparisonsIfWordNotFound);
        System.out.println("\nAverage number of comparisons if word was found: " + (pa.comparisonsIfWordFound / pa.wordsFound));
        System.out.println("\nAverage number of comparisons if word was not found: " + (pa.comparisonsIfWordNotFound / pa.wordsNotFound));
        System.out.println("\nTime required for comparison: " + ((stop - start) / Math.pow(10,9)) + " seconds");

	}//main
	
    /***************************************************************************************/
	
	/* public void dictionaryPopulate populates the dictionary attribute using the provided
	 *		random_dictionary.txt file
 	 * 
	 * @param none
	 * @return none
	 *
	 * @requires random_dictionary.txt to be accessible
	 * @ensures dictionary attribute is populated with words from random_dictionary.txt and organized 
	 * 		according to the first letter of the word, with one starting letter per linked list
	 */
	public void dictionaryPopulate(){
		//read in file, assign each word to correct linked list 
		try{
			File file = new File("random_dictionary.txt");
			Scanner input = new Scanner(file);
			String word;
			while(input.hasNext()){
				word = input.next();
                if(word.charAt(0) < 97)
                    word = word.toLowerCase();
				boolean attempt = dictionary[word.charAt(0) - 97].insert(word);
			}//end while
            input.close();
		}//end try

		catch(IOException e){
			System.out.println("Unable to read file");
		}//end catch
	}
    
    /***************************************************************************************/
    
    /* public void compareFileAgainstDictionary brings in the fileName of the file required to compare against the
     *              dictionary, then parses each string so that is only has lower case letters and uses this string 
     *              to compare against the dictionary by calling the isInDictionary method. This method also increases the
     *              wordFound and wordNotFound counters accordingly
     *
     * @param fileName: name of the file to do a word-by-word comparison to dictionary
     * @return none
     *
     * @requires fileName is not empty
     * @ensures every string in fileName is compared to dictionary and wordFound and wordNotFound counters are adjusted accordingly
     *
     */
    public void compareFileAgainstDictionary(String fileName){
        File file = new File(fileName);
        try{
            Scanner input = new Scanner(file);
            String word = "";
            String comparison = "";
            char currentChar;
            while(input.hasNext()){
                word = input.next();
                for(int i = 0; i < word.length(); i++){
                    currentChar = word.charAt(i);
                    if(Character.isLetter(currentChar) && Character.isLowerCase(currentChar)){
                        comparison += Character.toString(currentChar);
                    }
                    else if(Character.isLetter(currentChar) && Character.isUpperCase(currentChar)){
                        Character.toLowerCase(currentChar);
                        comparison.concat(Character.toString(currentChar));
                    }
                    else if(currentChar == 45){
                        if(comparison.length() > 0){
                            if(isInDictionary(comparison))
                                wordsFound++;
                            else
                                wordsNotFound++;
                            comparison = "";
                        }
                    }
                }//end for
                
                if(comparison.length() > 0){
                    if(isInDictionary(comparison))
                        wordsFound++;
                    else
                        wordsNotFound++;
                }
                comparison = "";
            }//end while
            input.close();
            System.out.println("File Successfully Compared to Dictionary");
        }//end try
        
        catch(IOException e){
            System.out.println("Unable to read file " + fileName);
        }//end catch
    }//compareFileAgainstDictionary
    
    /***************************************************************************************/
    
    /* public boolean isInDictionary returns true if the incoming word is in the dictionary array and
     *          returns false otherwise and increases the appropriate comparisons count accordingly
     *
     * @param word: string to check to see if it is contained in the dictionary
     * @return boolean
     *
     * @requires |word| > 0
     * @ensures returns true if word is in dictionary and comparisonsIfWordFound is increased by the returned
     *          comparisonsCount if word is found, otherwise returns false and comparisonsIfWordNotFound is increased
     */
    public boolean isInDictionary(String word){
        int[] comparisonsCount = new int[1];
        if(dictionary[word.charAt(0) - 97].search(word, comparisonsCount)){
            comparisonsIfWordFound += comparisonsCount[0];
            return true;
        }
        else{
            comparisonsIfWordNotFound += comparisonsCount[0];
            return false;
        }
    }//isInDictionary
}//PA4