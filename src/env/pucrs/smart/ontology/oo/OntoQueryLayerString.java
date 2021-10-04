package pucrs.smart.ontology.oo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import jason.asSyntax.Literal;
import pucrs.smart.ontology.OntoQueryLayer;
import pucrs.smart.ontology.OwlOntoLayer;

public class OntoQueryLayerString{
	private OntoQueryLayer ontoQuery;

    public OntoQueryLayerString(OwlOntoLayer ontology) {
        this.ontoQuery = new OntoQueryLayer(ontology); 
    }
    
    public OntoQueryLayer getQuery() {
    	return this.ontoQuery;
    }

    public List<Object> getObjectPropertieNames() {
        ArrayList<Object> objectProperties = new ArrayList<Object>();
        for (OWLObjectProperty objectProperty : this.ontoQuery.getOntology().getObjectProperties()) {
            if (objectProperty.isOWLBottomObjectProperty()) continue;
            objectProperties.add(objectProperty.asOWLObjectProperty().getIRI().getFragment());
        }
        return objectProperties;
    }
    
    public List<Object> getDataPropertieNames() {
        ArrayList<Object> objectProperties = new ArrayList<Object>();
        for (OWLDataProperty dataProperty : this.ontoQuery.getOntology().getDataProperties()) {
            if (dataProperty.isOWLBottomObjectProperty()) continue;
            objectProperties.add(dataProperty.asOWLDataProperty().getIRI().getFragment());
        }
        return objectProperties;
    }

    public List<Object> getAnnotationPropertieNames() {
        ArrayList<Object> annotationProperties = new ArrayList<Object>();
        for (OWLAnnotationProperty annotationProperty : this.ontoQuery.getOntology().getAnnotationProperties()) {
            if (annotationProperty.isBottomEntity()) continue;
            annotationProperties.add(annotationProperty.asOWLAnnotationProperty().getIRI().getFragment());
        }
        return annotationProperties;
    }
    
    
    /**
     * Method gets the name of states that are related to predicate.
     * @param predicate
     * @return list of strings that contains one or more name of states that are related to the predicate.
     */
    public ArrayList<String> getStatesByPredicate(Literal predicate){
    	ArrayList<String> states = new ArrayList<>();
    	OWLNamedIndividual owlIndividualFunctor = ontoQuery.getOWLIndividual(predicate.getFunctor().toString()); //isOwnerOf
		OWLObjectProperty owlObjectPropertyisPredicateOf = ontoQuery.getOWLObjectProperty("isPredicateOf");
    	
    	Set<OWLNamedIndividual> rangePredicateRelation =  
        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividualFunctor, owlObjectPropertyisPredicateOf);
    	
    	for(OWLNamedIndividual inv : rangePredicateRelation){
    		states.add(inv.getIRI().getShortForm());
//    		System.out.println("State chegaaaaando: " + inv.getIRI().getShortForm());
    	}
    	return states;
    }
    
    
    /**
	 * Method that gets the purpose associated to the individual that represents a state of the system. 
	 * @param individualState
	 * @return string purpose
	 */
    
	public ArrayList<String> getPurposesByState(String individualState) {
		ArrayList<String> purposes = new ArrayList<>();
		OWLNamedIndividual owlIndividual = ontoQuery.getOWLIndividual(individualState); //isOwnerOf
		OWLObjectProperty owlObjectPropertyisConsequenceOf = ontoQuery.getOWLObjectProperty("isConsequenceOf");
    	
    	Set<OWLNamedIndividual> rangePredicateRelation =  
        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividual, owlObjectPropertyisConsequenceOf);
    	
    	for(OWLNamedIndividual inv : rangePredicateRelation){
    		purposes.add(inv.getIRI().getShortForm());
    	}
		return purposes;
	}
    
	/**
	 * 
	 * @param purpose
	 * @return
	 */
	public ArrayList<String> getStatusFunctionsByPurpose(String purpose) {
		ArrayList<String> statusFunction = new ArrayList<>();
		OWLNamedIndividual owlIndividual = ontoQuery.getOWLIndividual(purpose); //isOwnerOf
		OWLObjectProperty owlObjectPropertyisPurposeOf = ontoQuery.getOWLObjectProperty("isPurposeOf");
    	
    	Set<OWLNamedIndividual> rangePredicateRelation =  
        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividual, owlObjectPropertyisPurposeOf);
    	
    	for(OWLNamedIndividual inv : rangePredicateRelation){
    		statusFunction.add(inv.getIRI().getShortForm());
    	}
		return statusFunction;
	}
	
	/**
	 * Get the purposes related to status-function
	 * @param statusFunction
	 * @return
	 */
	public ArrayList<String> getPurposesByStatusFunctions(String statusFunction) {
		ArrayList<String> purposes = new ArrayList<>();
		OWLNamedIndividual owlIndividual = ontoQuery.getOWLIndividual(statusFunction); //isOwnerOf
		OWLObjectProperty owlObjectPropertyisPurposeOf = ontoQuery.getOWLObjectProperty("hasPurpose");
    	
    	Set<OWLNamedIndividual> rangePredicateRelation =  
        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividual, owlObjectPropertyisPurposeOf);
    	
    	for(OWLNamedIndividual inv : rangePredicateRelation){
    		purposes.add(inv.getIRI().getShortForm());
    	}
		return purposes;
	}
    
    
	/**
	 * Get states related to purposes.
	 * @param purpose
	 * @return ArrayList of states.
	 */
	 public ArrayList<String> getStatesByPurpose(String purpose){
	    	ArrayList<String> states = new ArrayList<>();
	    	OWLNamedIndividual owlIndividualFunctor = ontoQuery.getOWLIndividual(purpose); //isOwnerOf
			OWLObjectProperty owlObjectPropertyhasConsequence = ontoQuery.getOWLObjectProperty("hasConsequence");
	    	
	    	Set<OWLNamedIndividual> rangehasConsequenceRelation =  
	        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividualFunctor, owlObjectPropertyhasConsequence);
	    	
	    	for(OWLNamedIndividual inv : rangehasConsequenceRelation){
	    		states.add(inv.getIRI().getShortForm());
	    	}
	    	return states;
	 }
    
    
    
	 /**
	     * Method gets the predicates that are related to state.
	     * Create a temporary vector to store the temporary predicate. The position[0] this vector stores the functor and
	     * from position[1] stores the terms of the predicate.
	     * @param predicate
	     * @return 
	     */
	    public ArrayList<Literal> getPredicatesByState(String state){
	    	OWLNamedIndividual owlIndividualState = ontoQuery.getOWLIndividual(state); //hasPredicate
			OWLObjectProperty owlObjectPropertyhasPredicate = ontoQuery.getOWLObjectProperty("hasPredicate");
			
			OWLObjectProperty owlObjectPropertyhasParameter = ontoQuery.getOWLObjectProperty("hasParameter");
	    	
	    	Set<OWLNamedIndividual> rangePredicateRelation =  
	        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividualState, owlObjectPropertyhasPredicate);
	    	
	    	ArrayList<Literal> predicates = new ArrayList<>();
	    	
	    	for(OWLNamedIndividual functor : rangePredicateRelation){
	    		
	    		// pego o nome do predicado . preciso pegar os termos se tiver.
	    		Set<OWLNamedIndividual> rangeParameterRelation =  
		        ontoQuery.getOntology().getObjectPropertyValues(functor, owlObjectPropertyhasParameter);
	    		
	    		String temporaryPredicate[] = new String[rangeParameterRelation.size()+1];
	    		temporaryPredicate[0] = functor.getIRI().getShortForm();
	    		
	    		Set<OWLObjectPropertyAssertionAxiom> objectPropertyAssertionAxiom  =
    					ontoQuery.getOntology().getOntology().getObjectPropertyAssertionAxioms(functor);
	    		
	    		// Varro aqui para achar as posições dos termos no predicado.
    			for(OWLObjectPropertyAssertionAxiom testin : objectPropertyAssertionAxiom) {
    				if(testin.getProperty().getNamedProperty().getIRI().getShortForm().equals("hasParameter")) {
    					for(OWLAnnotation annot : testin.getAnnotations()) {
    						if(annot.getProperty().getIRI().getShortForm().equals("position")) {
    							int begin = annot.annotationValue().toString().indexOf("\"");
    							int end = annot.annotationValue().toString().lastIndexOf("\"");
    							int valueFiltrado = Integer.valueOf(annot.annotationValue().toString().substring(begin+1, end));
    							//System.out.println("Anotação Name: " + annot.getProperty().getIRI().getShortForm()); 
        						//System.out.println("Anotação Value Filtrada: " + valueFiltrado);
        						//System.out.println("Termo:" + predicate.getTerm(valueFiltrado-1));
        						//System.out.println("Objecto:" + (testin.getObject().asOWLNamedIndividual().getIRI().getShortForm()));
        						temporaryPredicate[valueFiltrado] = testin.getObject().asOWLNamedIndividual().getIRI().getShortForm();
    						}
    					}
    				}
    			}
    			predicates.add(Util.convertVetorWithAPredicateInLiteral(temporaryPredicate));
	    		
	    	}
	    	return predicates; // retorna um arrayList de predicados relacionados a um state.
	    }
	    
	    
	    
	     
	 

}

