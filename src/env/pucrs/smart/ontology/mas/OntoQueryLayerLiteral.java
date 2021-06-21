package pucrs.smart.ontology.mas;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import pucrs.smart.ontology.OntoQueryLayer;
import pucrs.smart.ontology.OwlOntoLayer;

public class OntoQueryLayerLiteral {
	private final String FunctorInstance = "instance"; // instance(Concept,Instance,InstanceNameForJason)
	private final String FunctorConcept = "concept"; // concept(Concept,ConceptNameForJason)
	private final String FunctorObjectProperty = "objectProperty"; // objectProperty(ObjectPropertyName,objectPropertyNameForJason)
	private final String FunctorAnnotationProperty = "annotationProperty";// annotationProperty(AnnotationProperty,annotationPropertyNameForJason)
	private final String FunctorDataProperty = "dataProperty"; // dataProperty(DataProperty,dataPropertyNameForJason)

	private OntoQueryLayer ontoQuery;

	public OntoQueryLayerLiteral(OwlOntoLayer ontology) {
		this.ontoQuery = new OntoQueryLayer(ontology);
	}

	public OntoQueryLayer getQuery() {
		return this.ontoQuery;
	}
	
	public static String removeAccents(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		str = str.replaceAll("-", "_");
		return str;
	}

	public static String getNameForJason(String str) {
		str = removeAccents(str.substring(0, 1).toLowerCase() + str.substring(1));
		return str;
	}
	
	
	
	
	public List<Object> getIndividualNames(String conceptName) {
		List<Object> individuals = new ArrayList<Object>();
		try {
			Term concept = ASSyntax.createString(conceptName);

			for (OWLNamedIndividual individual : ontoQuery.getInstances(conceptName)) {
				Literal l = ASSyntax.createLiteral(this.FunctorInstance, concept);
				l.addTerm(ASSyntax.createString(individual.getIRI().getFragment()));
				// l.addTerm(ASSyntax.createAtom(getNameForJason(individual.getIRI().getFragment())));
				individuals.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return individuals;
	}
	
	public List<Object> getObjectPropertyNames() {
		List<Object> objectProperties = new ArrayList<Object>();
		try {
			for (OWLObjectProperty objectProperty : this.ontoQuery.getOntology().getObjectProperties()) {
				if (objectProperty.isOWLBottomObjectProperty())
					continue;
				Literal l = ASSyntax.createLiteral(this.FunctorObjectProperty,
						ASSyntax.createString(objectProperty.asOWLObjectProperty().getIRI().getFragment()));
				l.addTerm(ASSyntax
						.createAtom(getNameForJason(objectProperty.asOWLObjectProperty().getIRI().getFragment())));
				objectProperties.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return objectProperties;
	}
	
	public List<Object> getObjectPropertyValues(String domain, String property) {
		List<Object> individuals = new ArrayList<Object>();
		try {

			Term concept = ASSyntax.createString(property);
			System.out.println("Domain: " + domain);
			System.out.println("Property: " + property);

			for (OWLNamedIndividual individual : ontoQuery.getObjectPropertyValues(domain, property)) {
				// SINTAXE 1 argumento e.g x fica primeiro e segundo argumento dentro do
				// parentêses.
				// Literal l = ASSyntax.createLiteral(this.FunctorObjectProperty, concept);

				Literal l = ASSyntax.createLiteral(property.toString(), ASSyntax.createAtom(domain));
				System.out.println("L again: " + l);
				// l.addTerm(ASSyntax.createString(individual.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(individual.getIRI().getFragment()));

				// Literal teste = ASSyntax.createLiteral("p", ASSyntax.createAtom("a"),
				// ASSyntax.createNumber(3));
				// System.out.println("Literal Teste: " + teste);

				// l.addTerm(ASSyntax.createAtom(getNameForJason(individual.getIRI().getFragment())));
				System.out.println("LITERAL: " + l);
				individuals.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return individuals;
	}
	
	public List<Object> getClassNames() {
		List<Object> classNames = new ArrayList<Object>();
		try {
			for (OWLClass ontoClass : this.ontoQuery.getOntology().getClasses()) {
				Literal l = ASSyntax.createLiteral(this.FunctorConcept,
						ASSyntax.createString(ontoClass.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(ontoClass.getIRI().getFragment())));
				// System.out.println("Teste: " + l.toString());
				// System.out.println("First word: " + this.FunctorConcept +
				// ASSyntax.createString(ontoClass.getIRI().getFragment()));
				// System.out.println("Second word: " +
				// getNameForJason(ontoClass.getIRI().getFragment()));
				classNames.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return classNames;
	}
	
	public List<Object> getAnnotationPropertyNames() {
		List<Object> annotationProperties = new ArrayList<Object>();
		try {
			for (OWLAnnotationProperty annotationProperty : this.ontoQuery.getOntology().getAnnotationProperties()) {
				if (annotationProperty.isBottomEntity())
					continue;
				Literal l = ASSyntax.createLiteral(this.FunctorAnnotationProperty,
						ASSyntax.createString(annotationProperty.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(annotationProperty.getIRI().getFragment())));
				annotationProperties.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return annotationProperties;
	}
	
	public List<Object> getDataPropertyNames() {
		List<Object> dataProperties = new ArrayList<Object>();
		try {
			for (OWLDataProperty dataProperty : this.ontoQuery.getOntology().getDataProperties()) {
				if (dataProperty.isOWLBottomObjectProperty())
					continue;
				Literal l = ASSyntax.createLiteral(this.FunctorDataProperty,
						ASSyntax.createString(dataProperty.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(dataProperty.getIRI().getFragment())));
				dataProperties.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return dataProperties;
	}
	
}
