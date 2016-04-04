package dateParser;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @@author :a0125415n
 *
 */
public class GarbageCollectorParser {
	private ArrayList<String> hashes= new ArrayList<String>();
	public GarbageCollectorParser(){
	}
	
	/**@@author :a0125415n
	 * 
	 * adds xml to anything that has hashtags
	 * @param String input
	 * @return input with xml around any word with has in front
	 */
	public String xmlHash(String input){
		String toReplace = XMLParser.removeAllAttributes(input);
		hashes = new ArrayList<String>();
		Scanner sc = new Scanner(toReplace);
		while(sc.hasNext()){
			String temp = sc.next();
			if((temp.length()!=1)&&(!temp.contains("ID"))){
				if(temp.charAt(0)=='#'){
					hashes.add(temp);
					input = input.replace(temp, "<"+XMLParser.HASH_TAG+">"+temp+"</"+XMLParser.HASH_TAG+">");
				}	
			}
		}
		return input;
	}
	
	/**@@author :a0125415n
	 * 
	 * get the hashes in the string, precondition: run xmlHash first
	 * @return ArrayList of hashes in string
	 */
	public ArrayList<String> getHashes(){
		return hashes;
	}
	
	/**@@author :a0125415n
	 * 
	 * add xml to anything that hasn't already got xml.
	 * @param String input
	 * @return input with xml on any other not yet defined fields
	 */
	public String xmlAllOthers(String input){
		String toReplace = XMLParser.removeAllAttributes(input);
		Scanner sc = new Scanner(toReplace);
		while(sc.hasNext()){
			String temp = sc.next();
			if((temp.length()!=1)&&(!temp.contains("ID"))){
					input = input.replace(temp, "<"+XMLParser.OTHERS_TAG+">"+temp+"</"+XMLParser.OTHERS_TAG+">");
					
			}
		}
		String lastWord = ParserCommons.getLastWord(input);
		String firstWord = ParserCommons.getFirstWord(XMLParser.removeAllTags(input));
		if((lastWord.length()==1)&&(!firstWord.equals(lastWord))&&(!lastWord.contains("ID"))){
			input = input.substring(0, input.length()-2)+"<"+XMLParser.OTHERS_TAG+">"+lastWord+"</"+XMLParser.OTHERS_TAG+">";
		}
		return input;
	}
}
