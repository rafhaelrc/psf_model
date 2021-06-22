package pucrs.smart.ontology.oo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
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
    public List<String> getStatesByPredicate(Literal predicate){
    	ArrayList<String> states = new ArrayList<>();
    	OWLNamedIndividual owlIndividualFunctor = ontoQuery.getOWLIndividual(predicate.getFunctor().toString()); //isOwnerOf
		OWLObjectProperty owlObjectPropertyisPredicateOf = ontoQuery.getOWLObjectProperty("isPredicateOf");
    	
    	Set<OWLNamedIndividual> rangePredicateRelation =  
        	    ontoQuery.getOntology().getObjectPropertyValues(owlIndividualFunctor, owlObjectPropertyisPredicateOf);
    	
    	for(OWLNamedIndividual inv : rangePredicateRelation){
    		states.add(inv.getIRI().getShortForm());
    		System.out.println("State chegaaaaando: " + inv.getIRI().getShortForm());
    	}
    	return states;
    }
    
    
    /**
	 * Method that gets the purpose associated to the individual that represents a state of the system. 
	 * the name of relation that there is between individualState and purpose is irrelevant.
	 * @param individualState
	 * @return string purpose
	 */
    
    /// ESSE METODO TA ERRADO.. FAZER IGUAL AO DE CIMA
	public ArrayList<String> getPurposesByState(String individualState) {
		ArrayList<String> purposes = new ArrayList<>();
		for (OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
			OWLIndividual individualDomProv 	= op.getSubject();
			if(individualDomProv.asOWLNamedIndividual().getIRI().getShortForm().equals(individualState)) {
				purposes.add(op.getObject().asOWLNamedIndividual().getIRI().getShortForm());
			}
		}
		return purposes;
	}
    
    
    
    
    
    

}

