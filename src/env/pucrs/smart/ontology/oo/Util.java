package pucrs.smart.ontology.oo;

import java.util.ArrayList;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public final class Util {
	

	/**
	 * method that gets an ArrayList of Strings (that can contain duplicate elements) 
	 * and return another ArrayList of String without duplicate elements.
	 * @return an arraylist of elements not duplicate.
	 */
	public static ArrayList<String> removeDuplicateElementsOfAnArrayofString(ArrayList<String> statusFunctions){
		ArrayList<String> newElements = new ArrayList<>();
		for(String element : statusFunctions) {
			if(!newElements.contains(element)) {
				newElements.add(element);
			}
		}
		return newElements;
	}
	
	/**
	 * Method that convert an arraylist of String in a list of String.
	 * @param arrayList
	 * @return list of strings
	 */
	public static String[] convertArrayListOfStringinArrayofString(ArrayList<String> arrayList){
		String arr[] = new String[arrayList.size()];
		int i=0;
        // iterating over the hashset
        for(String ele: arrayList){
          arr[i] = ele;
          i++;
        }
		return arr;
	}
	
	/**
	 * 
	 * @param predicate
	 * @return set that contains the predicate. Position[0] is the functor and from the position[1] are the terms.
	 */
	public static ArrayList<String> convertStringOfPredicateInSet(String predicate) {
		//System.out.println("String Predicate: " + predicate);
		String functor = "";
		String term1 = "";
		String term2 = "";
		ArrayList<String> setPredicate = new ArrayList<>();
		
		
		if(predicate.contains(",")) { // Relation Predicate eg. mae(maria,rafhael). 
			String[] words = predicate.split("\\(");
			functor = words[0];
			String[] args = words[1].split(",");
			term1 = args[0];
			term2 = args[1];
			term2 = term2.replace(")", "");
			term2 = term2.replace(" ", "");
			setPredicate.add(functor);
			setPredicate.add(term1);
			setPredicate.add(term2);
		}
		if(!predicate.contains(",")) { // property predicate  eg. book(bookA) // tem que testar essa segunda parte
			String[] words = predicate.split("\\(");
			functor = words[0];
			term1 = words[1];
			term1 = term1.replace(")", "");
			term1 = term1.replace(" ", "");
			setPredicate.add(functor);
			setPredicate.add(term1);
		}
		
		return setPredicate;
	}	
	
	/**
	 * Literal = Predicate or negation of a predicate.
	 * @param predicate
	 * @return
	 */
	public static Literal createLiteral(String predicate) {
		
		if(predicate.contains(",")) { // Relation Predicate eg. mae(maria,rafhael). 
			String[] words = predicate.split("\\(");
			//words[0] = mae
			//words[1] = maria, rafhael)
			
			Term concept = ASSyntax.createString(words[0]);
			Literal l = ASSyntax.createLiteral(words[0], concept);
			l.delTerm(0); // because functor and first term are equals.
			
			String[] args = words[1].split(",");
			
			args[args.length-1] = args[args.length-1].replace(")", ""); // retired ")" in the final of the word.
			//args[args.length-1] = args[args.length-1].replace(" ", "");
			
			for (int i = 0; i < args.length; i++) {	
				l.addTerm(ASSyntax.createString(args[i]));
			}
			
//			System.out.println(l.toString());
//			System.out.println(l.getFunctor());
//			System.out.println(l.getTerm(0));
//			System.out.println(l.getArity());
			return l;
			
		}
		if(!predicate.contains(",")) { // property predicate  eg. book(bookA) // tem que testar essa segunda parte
			String[] words = predicate.split("\\(");
			
			Term concept = ASSyntax.createString(words[0]);
			Literal l = ASSyntax.createLiteral(words[0], concept);
			l.delTerm(0); // because functor and first term are equals.
			words[1] = words[1].replace(")", "");
			l.addTerm(ASSyntax.createString(words[1]));
			return l;
		}
		
		return null;
		
		/*
		
			//l.addTerm(ASSyntax.createString(individual.getIRI().getFragment()));
			
			functor = words[0];
			
			term1 = args[0];
			term2 = args[1];
			term2 = term2.replace(")", "");
			term2 = term2.replace(" ", "");
			setPredicate.add(functor);
			setPredicate.add(term1);
			setPredicate.add(term2);
		}
		if(!predicate.contains(",")) { // property predicate  eg. book(bookA) // tem que testar essa segunda parte
			String[] words = predicate.split("\\(");
			functor = words[0];
			term1 = words[1];
			term1 = term1.replace(")", "");
			term1 = term1.replace(" ", "");
			setPredicate.add(functor);
			setPredicate.add(term1);
		}
		
		*/
		
		
		
	}
	

}
