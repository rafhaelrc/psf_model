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
import org.semanticweb.owlapi.model.OWLClassExpression;
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
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.reasoner.Node;

import jade.domain.JADEAgentManagement.CreateAgent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
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

	public Set<OWLClass> getClassNamesAndReturnOWLClass() {
		Set<OWLClass> classNames = new HashSet<OWLClass>();
		try {
			for (OWLClass ontoClass : this.ontoQuery.getOntology().getClasses()) {
				classNames.add(ontoClass);

				// Literal l = ASSyntax.createLiteral(this.FunctorConcept,
				// ASSyntax.createString(ontoClass.getIRI().getFragment()));
				// l.addTerm(ASSyntax.createAtom(getNameForJason(ontoClass.getIRI().getFragment())));
				// System.out.println("Teste: " + l.toString());
				// System.out.println("First word: " + this.FunctorConcept +
				// ASSyntax.createString(ontoClass.getIRI().getFragment()));
				// System.out.println("Second word: " +
				// getNameForJason(ontoClass.getIRI().getFragment()));
				// classNames.add(l);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return classNames;
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

	public Set<OWLObjectProperty> getObjectPropertyNamesAndReturnOWLObjectProperty() {
		Set<OWLObjectProperty> objectProperties = new HashSet<OWLObjectProperty>();
		try {
			for (OWLObjectProperty objectProperty : this.ontoQuery.getOntology().getObjectProperties()) {
				if (objectProperty.isOWLBottomObjectProperty())
					continue;
				objectProperties.add(objectProperty);
			}
		} catch (Exception e) {
			System.out.println("failed to parse: " + e.getMessage());
		}
		return objectProperties;
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

	/*
	 * Pegar o nome da propriedade do objeto que tem como domínio o domain
	 */
	public Set<OWLObjectProperty> getNameOfObjectPropertyByDomain(String domain) {

		Set<OWLObjectProperty> objProperties = new HashSet<OWLObjectProperty>();

		// create a classe from string domain
		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLClass owlclass = dataFactory.getOWLClass(IRI.create((String) ((Object) baseIRI + "#" + domain)));

		// System.out.println("Classe transformada em OWL Class :" + owlclass);

		for (OWLObjectPropertyDomainAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
			if (op.getDomain().equals(owlclass)) {
				for (OWLObjectProperty oop : op.getObjectPropertiesInSignature()) {
					// System.out.println("\t\t +: " + oop.getIRI().getShortForm());
					objProperties.add(oop);
				}
				// System.out.println("\t\t +: " +
				// op.getProperty().getNamedProperty().getIRI().getShortForm());
			}
		}
		return objProperties;
	}

	/*
	 * Pegar o nome da propriedade de dados que tem como domínio o domain
	 */
	public Set<OWLDataProperty> getNameOfDataPropertyByDomain(String domain) {

		Set<OWLDataProperty> dataProperties = new HashSet<OWLDataProperty>();

		// create a classe from string domain
		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLClass owlclass = dataFactory.getOWLClass(IRI.create((String) ((Object) baseIRI + "#" + domain)));

		// System.out.println("Classe transformada em OWL Class :" + owlclass);

		for (OWLDataPropertyDomainAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
			if (op.getDomain().equals(owlclass)) {
				for (OWLDataProperty oop : op.getDataPropertiesInSignature()) {
					// System.out.println("\t\t +: " + oop.getIRI().getShortForm());
					dataProperties.add(oop);
				}
				// System.out.println("\t\t +: " +
				// op.getProperty().getNamedProperty().getIRI().getShortForm());
			}
		}
		return dataProperties;
	}

	/**
	 * método que retorna o range das propriedades de objetos
	 * @param objProperty
	 * @return
	 */
	public OWLClass getNameOfRangeByObjectProperty(OWLObjectProperty objProperty) {
		Set<OWLClass> owlclasses = getQuery().getOntology().getReasoner().getObjectPropertyRanges(objProperty)
				.getFlattened();
		Iterator<OWLClass> iterator = owlclasses.iterator();

		// getQuery().getOntology().getReasoner().getObjectPropertyRanges(objProperty).getFlattened()

		System.out.println("Primeiro Iterator: " + iterator.hasNext());
		
		
		while (iterator.hasNext()) {
			OWLClass owlClass = iterator.next();
			System.out.println("Testando iterator: " + owlClass.getIRI().getShortForm());
			
			
			if (!iterator.hasNext()) {
				// Se for relevante pegar somente o elemento específico do range (subclasse
				// especifica), sem pegar a classe mãe.
				return owlClass;
			}
		}
		return null;
	}
	
	/**
	 * precisa verificar se o Range é o informado por parâmetro.
	 * @return
	 */
	public OWLClass getNameOfRangeByObjectProperty(String domain, OWLObjectProperty objProperty) {
		
		// create a classe from string domain
		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //
		OWLClass owlDomainClass = dataFactory.getOWLClass(IRI.create((String) ((Object) baseIRI + "#" + domain)));

		//OWLObjectProperty owlObjectProperty = dataFactory
		//		.getOWLObjectProperty(IRI.create((String) ((Object) baseIRI + "#" + objProperty)));

		
		Set<OWLClass> owlRangeClasses = getQuery().getOntology().getReasoner().getObjectPropertyRanges(objProperty, true)
				.getFlattened();
		
		
		for(OWLClass rangeClassTemp : owlRangeClasses) {
			//System.out.println("Range class Temp: " + rangeClassTemp.getIRI().getShortForm());
			return rangeClassTemp;
		}
		return null;
	}
	
	


	/**
	 * método que pega o range das propriedades de objetos
	 * @param dataProperty
	 * @return
	 */
	public OWLDataPropertyRangeAxiom GetTypeOfRangeByDataProperty(OWLDataProperty dataProperty) {

		Set<OWLDataPropertyRangeAxiom> ranges = getQuery().getOntology().getOntology()
				.getDataPropertyRangeAxioms(dataProperty);
		Iterator<OWLDataPropertyRangeAxiom> iterator = ranges.iterator();
		while (iterator.hasNext()) {
			OWLDataPropertyRangeAxiom range = iterator.next();
			/// System.out.println("Range data Property: " + range);
			if (!iterator.hasNext()) {
				// Se for relevante pegar somente o elemento específico do range (subclasse
				// especifica), sem pegar a classe mãe.
				return range;
			}
		}
		return null;

	}

	/*
	 * metodo que descubra o individuo domain onde o individuo range seja passado
	 * por parametro
	 * Não funciona
	 */
	public void getIndividualOfRelationByRange(String propertyName, String range) {
		// propertyName;
		// OWLObjectProperty

		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();

		for (OWLNamedIndividual individual : ontoQuery.getObjectPropertyValues(range, propertyName)) {
			// System.out.println("TESTANDO: " + individual.getIRI().getFragment());
		}
	}
	

	/**
	 * return a literal with the format nameProperty(domain, range)
	 * @param domain
	 * @return
	 */
	public List<Object> getObjectPropertiesByDomain(String domain) {
		// property(domain, range)
		List<Object> propertiesObjectByDomain = new ArrayList<Object>();

		for (OWLObjectProperty objProperty : this.getNameOfObjectPropertyByDomain(domain)) {
			//OWLClass rangeOfClass = this.getNameOfRangeByObjectProperty(objProperty);
			OWLClass rangeOfClass = this.getNameOfRangeByObjectProperty(domain, objProperty);
			Literal l = ASSyntax.createLiteral(
					objProperty.getIRI().getFragment().substring(0, 1).toLowerCase() +
				    objProperty.getIRI().getFragment().substring(1),
					ASSyntax.createAtom(domain));
			
			l.addTerm(ASSyntax.createAtom(rangeOfClass.getIRI().getShortForm()));
			//o debaixo coloca o termo como uma constante, ou seja, letra minúscula.
			//l.addTerm(ASSyntax.createAtom(getNameForJason(rangeOfClass.getIRI().getShortForm())));
			propertiesObjectByDomain.add(l);
		}

		return propertiesObjectByDomain;
	}

	/*
	 * return a literal with the format nameProperty(domain, range)
	 */
	public List<Object> getDataPropertiesByDomain(String domain) {
		// property(domain, range) - range um tipo primitivo de dado
		List<Object> propertiesDataByDomain = new ArrayList<Object>();

		for (OWLDataProperty dataProperty : this.getNameOfDataPropertyByDomain(domain)) {

			OWLDataPropertyRangeAxiom rangeOfClass = this.GetTypeOfRangeByDataProperty(dataProperty);

			Literal l = ASSyntax.createLiteral(dataProperty.getIRI().getFragment(), ASSyntax.createAtom(domain));
			l.addTerm(ASSyntax.createAtom(rangeOfClass.getRange().toString()));
			propertiesDataByDomain.add(l);
		}

		return propertiesDataByDomain;
	}

	/**
	 * 
	 * @param individual
	 * @return
	 */
	public Literal getClassOfTheInstanceAndReturnLiteral(OWLNamedIndividual individual) {
		//System.out.println("Instance: " + individual.getSignature().getClass().toString());
		
		Set<OWLClass> owlclasses = ontoQuery.getOntology().getInstanceTypes(individual, true); // getQuery().getOntology().getReasoner().getObjectPropertyRanges(objProperty).getFlattened();
		Iterator<OWLClass> iterator = owlclasses.iterator();

		while (iterator.hasNext()) {
			OWLClass owlClass = iterator.next();
			if (!iterator.hasNext()) {
				// return owlClass;
				Literal l = ASSyntax.createLiteral(
						owlClass.getIRI().getFragment().substring(0, 1).toLowerCase()
								+ owlClass.getIRI().getFragment().substring(1),
						// ASSyntax.createString(domain));
						ASSyntax.createAtom(individual.getIRI().getFragment()));
				// l.addTerm(ASSyntax.createAtom(getNameForJason(rangeOfClass.getIRI().getShortForm())));
				// l.addTerm(ASSyntax.createAtom(rangeOfClass.getRange()));
				// l.addTerm(rangeOfClass.getRange());
				return l;
			}
		}
		return null;
	}

	/**
	 * Method that returns the name of the class that individual pertains.
	 * @param OWLIndividual
	 * @return name of the class that individual pertains.
	 */
	public String getClassOfTheInstanceAndReturnClass(OWLNamedIndividual individual) {

		Set<OWLClass> owlclasses = ontoQuery.getOntology().getInstanceTypes(individual, true); // getQuery().getOntology().getReasoner().getObjectPropertyRanges(objProperty).getFlattened();
		Iterator<OWLClass> iterator = owlclasses.iterator();

		// getQuery().getOntology().getReasoner().getObjectPropertyRanges(objProperty).getFlattened()

		while (iterator.hasNext()) {
			OWLClass owlClass = iterator.next();
			if (!iterator.hasNext()) {
				/*return owlClass.getIRI().getFragment().substring(0, 1).toLowerCase()
						+ owlClass.getIRI().getFragment().substring(1);
						*/
				if(!owlClass.getIRI().getShortForm().equalsIgnoreCase("Thing")) {
					return owlClass.getIRI().getShortForm();
				} 
			}
		}
		return null;
	}

	/**
	 * Método que pega o valor do range de uma relação do tipo objeto passando como
	 * parâmetro:
	 * 
	 * @param individualName
	 * @param propertyName
	 * @return
	 */
	public String getValueOfRangeByObjectPropertyAndIndividualDomain(String individualName, String propertyName) {

		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLNamedIndividual owlIndividualName = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + individualName)));

		OWLObjectProperty owlObjectProperty = dataFactory
				.getOWLObjectProperty(IRI.create((String) ((Object) baseIRI + "#" + propertyName)));

		ontoQuery.getOntology().getReasoner().getPrecomputableInferenceTypes();

		for (OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
			// Verifica se o individuo do domain é o individualName e se a propriedade de
			// objeto é a propertyName
			if (op.getSubject().equals(owlIndividualName) & op.getProperty().equals(owlObjectProperty)) {
				// System.out.println(op.getObject().asOWLNamedIndividual().getIRI().getShortForm());
				return op.getObject().asOWLNamedIndividual().getIRI().getShortForm();
			}

		}
		return null;
	}

	/**
	 * Esse método pega também os valores inferidos porque usa o buscador do próprio
	 * reasoner
	 * 
	 * @return
	 */
	public Object getRangeByObjectPropertyandIndividual(String individualName, String propertyName) {

		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLNamedIndividual owlIndividualName = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + individualName)));

		OWLObjectProperty owlObjectProperty = dataFactory
				.getOWLObjectProperty(IRI.create((String) ((Object) baseIRI + "#" + propertyName)));

		ontoQuery.getOntology().getReasoner().getPrecomputableInferenceTypes();

		Set<OWLNamedIndividual> owlindividualProv = ontoQuery.getOntology().getReasoner()
				.getObjectPropertyValues(owlIndividualName, owlObjectProperty).getFlattened();

		for (OWLNamedIndividual oni : owlindividualProv) {
			// System.out.println(oni.getIRI().getShortForm());
			return oni.getIRI().getShortForm();
		}

		return null;
	}

	/**
	 * Esse método pega também os valores inferidos porque usa o buscador do próprio
	 * reasoner
	 * 
	 * @return
	 */
	public Object getRangeByDataPropertyandIndividual(String individualName, String dataPropertyName) {

		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLNamedIndividual owlIndividualName = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + individualName)));

		OWLDataProperty owlDataProperty = dataFactory
				.getOWLDataProperty(IRI.create((String) ((Object) baseIRI + "#" + dataPropertyName)));

		ontoQuery.getOntology().getReasoner().getPrecomputableInferenceTypes();

		Set<OWLLiteral> owlDataIndividual = ontoQuery.getOntology().getReasoner()
				.getDataPropertyValues(owlIndividualName, owlDataProperty);

		for (OWLLiteral lit : owlDataIndividual) {
			//System.out.println(lit.getLiteral());
			return lit.getLiteral();
		}

		return "";
	}

	/**
	 * Esse método não consegue buscar os dados inferidos porque usa o Assertion e
	 * getAxioms
	 * 
	 * @param individualName
	 * @param propertyName
	 * @return
	 */
	public Object getValueOfRangeByDataPropertyAndIndividualDomain(String individualName, String propertyName) {

		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLNamedIndividual owlIndividualName = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + individualName)));

		OWLDataProperty owlDataProperty = dataFactory
				.getOWLDataProperty(IRI.create((String) ((Object) baseIRI + "#" + propertyName)));

		ontoQuery.getOntology().getReasoner().getPrecomputableInferenceTypes();

		for (OWLDataPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.DATA_PROPERTY_ASSERTION)) {
			// Verifica se o individuo do domain é o individualName e se a propriedade de
			// dados é a propertyName
			if (op.getSubject().equals(owlIndividualName) & op.getProperty().equals(owlDataProperty)) {
				// System.out.println(op.getObject().getLiteral()); // com get literal só
				// retorna o valor do dado.
				return op.getObject().getLiteral();
			}

		}
		return null;
	}

	/**
	 * Método sem uso. Apenas para guardar a forma de fazer.
	 */

	public void pegaDomainObjectanDataPropertyandRange() {
		for (OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
			// VERIFICA SE O INDIVIDUO FAZ PARTE DO DOMAIN DA PROPRIEDADE
			// if() {}
			OWLIndividual individualProv = op.getSubject();

			System.out.println("Domain: " + individualProv.asOWLNamedIndividual().getIRI().getShortForm());
			System.out.println("Object Property: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
			System.out.println("Range: " + op.getObject().asOWLNamedIndividual().getIRI().getShortForm() + "\n");

			// System.out.println(op.toString());
			// if(op.getDomain().equals(ind)) {
			// System.out.println(op.toString());
			// for (OWLObjectProperty oop : op.getObjectPropertiesInSignature()) {
			// System.out.println("Individual: " + ind.toString());
			// System.out.println("Object Property: " + oop.getIRI().getShortForm());
			// System.out.println("\t\t +: " + oop.getIRI().getShortForm());

			// }
			// }
		}

		for (OWLDataPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.DATA_PROPERTY_ASSERTION)) {
			// VERIFICA SE O INDIVIDUO FAZ PARTE DO DOMAIN DA PROPRIEDADE
			// if() {}
			OWLIndividual individualProv = op.getSubject();
			System.out.println("\t Data Property");
			System.out.println("Domain: " + individualProv.asOWLNamedIndividual().getIRI().getShortForm());
			System.out.println("Data Property: "
					+ op.getProperty().asDataPropertyExpression().asOWLDataProperty().getIRI().getShortForm());
			System.out.println("Range: " + op.getObject().toString());

		}
	}
	
	/**
	 * Verifica se existe alguma relação do tipo objeto entre o dominio e o range
	 * @return true - se existir alguma relação
	 */
	public boolean checkIfThereIsAnyObjectPropertyBetweenDomainAndRange(String domain, String range) {
		
		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLNamedIndividual owlDomain = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + domain)));
		
		OWLNamedIndividual owlRange = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + range)));

		ontoQuery.getOntology().getReasoner().getPrecomputableInferenceTypes();

		for (OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
			if (op.getSubject().asOWLNamedIndividual().equals(owlDomain) & 
				op.getObject().asOWLNamedIndividual().equals(owlRange)) {
				return true;
				
			}
		}
		return false;
	}
	
	
	
	
/**
 * 	Check if there is a relationship between domain and range. if true, return the name of the relation.
 * @param domain
 * @param range
 * @return name of relationship between domain and range
 */
public String nameObjectPropertyRelationBetweenDomainAndRange(String domain, String range) {
		
		OWLDataFactory dataFactory = ontoQuery.getOntology().getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = ontoQuery.getOntology().getOntology().getOntologyID().getDefaultDocumentIRI().get(); //

		OWLNamedIndividual owlDomain = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + domain)));
		
		OWLNamedIndividual owlRange = dataFactory
				.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + range)));

		ontoQuery.getOntology().getReasoner().getPrecomputableInferenceTypes();

		for (OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
			if (op.getSubject().asOWLNamedIndividual().equals(owlDomain) & 
				op.getObject().asOWLNamedIndividual().equals(owlRange)) {
				return op.getProperty().getNamedProperty().getIRI().getShortForm();
				
			}
		}
		return null;
	}
	
	/**
	 * Method that gets the purpose associated to the individual that represents a state of the system. 
	 * the name of relation that there is between individualState and purpose is irrelevant.
	 * @param individualState
	 * @return string purpose
	 */
	public ArrayList<String> getPurposesByState(String individualState) {
		ArrayList<String> purposes = new ArrayList<>();
		for (OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology()
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
			OWLIndividual individualDomProv 	= op.getSubject();
			if(individualDomProv.asOWLNamedIndividual().getIRI().getShortForm().equalsIgnoreCase(individualState)) {
				purposes.add(op.getObject().asOWLNamedIndividual().getIRI().getShortForm());
			}
		}
		return purposes;
	}
	
	
	
	
}
