package pucrs.smart.ontology.oo;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import jason.asSyntax.Literal;
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
    
    
    
    public boolean thereIsAPredicateInOntology(Literal predicate) {
    	//isOwnerOf(joao,bookA);
    	System.out.println("Predicate: " + predicate);
    	
    	/**
    	 * check if name of predicate is a individual that pertains a predicate class
    	 * check if the arguments of predicate are related to individual
    	 * check if the order of the arguments are right
    	 */
    	
//    	OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
//    	IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); 
//    	OWLClass owlclass = dataFactory.getOWLClass(IRI.create((String) ((Object) baseIRI + "#" + "Predicate")));
//    	OWLNamedIndividual owlIndividualName = dataFactory
//				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + predicate.getFunctor().toString())));
    	
    	OWLNamedIndividual owlIndividualFunctor = ontoQuery.getOWLIndividual(predicate.getFunctor().toString());
    	OWLClass owlclass =  ontoQuery.getOWLClass("Predicate");
    	
    	OWLObjectProperty owlObjectPropertyhasParameter = ontoQuery.getOWLObjectProperty("hasParameter");
    	

    	/* there is a individual that has the same name of functor of the predicate 
    	   and this individual pertains to predicate class
    	*/
    	if(ontoQuery.getOntology().isInstanceOf(owlIndividualFunctor, owlclass)) {
    		System.out.println("Ok");
    		
    		Set<OWLNamedIndividual> rangePredicateRelation =  
    					ontoQuery.getOntology().getObjectPropertyValues(owlIndividualFunctor, owlObjectPropertyhasParameter);
    		
    		
    		//ontoQuery.getOntology().getOntology().getAnn
    		
    		Set<OWLObjectPropertyAssertionAxiom> teste  =
    				ontoQuery.getOntology().getOntology().getObjectPropertyAssertionAxioms(owlIndividualFunctor);
    		
    		for(OWLObjectPropertyAssertionAxiom testin : teste) {
    			System.out.println("Sujeito: " + testin.getSubject().toString());
    			System.out.println("Relação: " + testin.getProperty().toString());
    			System.out.println("Objeto: " + testin.getObject().toString());
    			
    			for(OWLAnnotation annot : testin.getAnnotations()) {
    				System.out.println("Anotação Name: " + annot.getProperty()); 
    				System.out.println("Anotação Value: " + annot.annotationValue());
    			}
    			
    			
    		}
    		
    		
    		for(OWLNamedIndividual individual : rangePredicateRelation) {
    			System.out.println("Range: " + individual.getIRI().getShortForm());
    		}
    		
    	}
    	else {
    		System.out.println("Nok");
    	}
    	
    	
    	return false;
    }
    
    
    public boolean thereIsAState(Literal predicate) {
    	return false;
    }
    
    

}
