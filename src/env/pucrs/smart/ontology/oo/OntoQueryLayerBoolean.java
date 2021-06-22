package pucrs.smart.ontology.oo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import pucrs.smart.ontology.OntoQueryLayer;
import pucrs.smart.ontology.OwlOntoLayer;

public class OntoQueryLayerBoolean {
	private OntoQueryLayer ontoQuery;

    public OntoQueryLayerBoolean(OwlOntoLayer ontology) {
        this.ontoQuery = new OntoQueryLayer(ontology); 
    }
    
    public OntoQueryLayer getQuery() {
    	return this.ontoQuery;
    }
    
    /*######################### My codes ############################ */
    
    public boolean thereIsAnIndividualInOntology(String nameIndividual) {
    	return false;
    }
    
    
    /**
 	* (1) check if name of predicate is a individual that pertains a predicate class
 	* (2) check if the arguments of predicate are related to individual
 	* (3) check if the order of the arguments are right
 	*/
    
    public boolean thereIsAPredicateInOntology(Literal predicate) {
    	//isOwnerOf(joao,bookA);
    	//System.out.println("Quantidade de termos: " + predicate.getTerms().size());
    	
    	if(predicate.getTerms().size() <= 1) {
    		System.out.println("property ++ code later");
    	}
    	// It means that the predicate contains more than one terms.
    	else {
    		OWLNamedIndividual owlIndividualFunctor = ontoQuery.getOWLIndividual(predicate.getFunctor().toString());
    		OWLClass owlclassPredicate =  ontoQuery.getOWLClass("Predicate");
    		OWLObjectProperty owlObjectPropertyhasParameter = ontoQuery.getOWLObjectProperty("hasParameter");
    		
    		
    		
    		/* (1) there is a individual that has the same name of functor of the predicate 
    	   		and this individual pertains to predicate class
    		 */
    		if(!ontoQuery.getOntology().isInstanceOf(owlIndividualFunctor, owlclassPredicate)){
    			System.out.println("First verification");
    			return false;
    		}
    		
    		/*
    		 * (2) check if the arguments of predicate are related to an individual 
    		 *  (that is the range of relation with the predicate)
    		 * 
    		 */
    		Set<OWLNamedIndividual> rangePredicateRelation =  
    	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividualFunctor, owlObjectPropertyhasParameter);
    	    		
    		for (int i = 0; i < predicate.getTerms().size(); i++) {
    			boolean status = false;
    			for(OWLNamedIndividual inv : rangePredicateRelation) {
    				if(predicate.getTerm(i).toString().equals(inv.asOWLNamedIndividual().getIRI().getShortForm())){
    					status = true;
    					break;
    				}
    			}
    			if(status == false && i == predicate.getTerms().size()-1) {
    				System.out.println("Não achou nenhum");
    				return false;
    			}
    		}
    		
    		
    		/*
    		 * (3) check if the order of the arguments are right
    		 */
    		Set<OWLObjectPropertyAssertionAxiom> objectPropertyAssertionAxiom  =
    		ontoQuery.getOntology().getOntology().getObjectPropertyAssertionAxioms(owlIndividualFunctor);
    		for(OWLObjectPropertyAssertionAxiom testin : objectPropertyAssertionAxiom) {
    			if(testin.getProperty().getNamedProperty().getIRI().getShortForm().equals("hasParameter")) {
//    				System.out.println("Sujeito: " + testin.getSubject().toString());
//        			System.out.println("Relação: " + testin.getProperty().toString());
//        			System.out.println("Objeto: " + testin.getObject().toString());
        			for(OWLAnnotation annot : testin.getAnnotations()) {
        				if(annot.getProperty().getIRI().getShortForm().equals("position")) {
        					int begin = annot.annotationValue().toString().indexOf("\"");
        					int end = annot.annotationValue().toString().lastIndexOf("\"");
        					int valueFiltrado = Integer.valueOf(annot.annotationValue().toString().substring(begin+1, end));
        					
//        						System.out.println("Anotação Name: " + annot.getProperty().getIRI().getShortForm()); 
//        						System.out.println("Anotação Value Filtrada: " + valueFiltrado);
//        						System.out.println("Termo:" + predicate.getTerm(valueFiltrado-1));
//        						System.out.println("Objecto:" + (testin.getObject().asOWLNamedIndividual().getIRI().getShortForm()));
//        					
        					if(!predicate.getTerm(valueFiltrado-1).toString().equals
        							(testin.getObject().asOWLNamedIndividual().getIRI().getShortForm())) {
        							//System.out.println("Estou verificando");
        						System.out.println("Third verification");
        						return false;
        					}
        				}
        			}
    			}
    		}
    	}
    	return true;
    }
    
    
    public boolean thereIsAState(Literal predicate) {
    	return false;
    }
    
    

}
