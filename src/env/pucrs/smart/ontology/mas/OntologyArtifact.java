package pucrs.smart.ontology.mas;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
//import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import pucrs.smart.ontology.OntoQueryLayer;
import pucrs.smart.ontology.OwlOntoLayer;


public class OntologyArtifact extends Artifact {
	private Logger logger = Logger.getLogger(OntologyArtifact.class.getName());

	private OwlOntoLayer onto = null;
	private OntoQueryLayerLiteral queryEngine;
	private OntoQueryLayer queryEngineLayer;


	void init(String ontologyPath) {
		logger.info("Importing ontology from " + ontologyPath);
		try {
			this.onto = new OwlOntoLayer(ontologyPath);

			// Load HermiT reasoner
			OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(onto.getOntology());
			onto.setReasoner(reasoner);

			// OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			// this.onto.setReasoner(reasonerFactory.createReasoner(this.onto.getOntology()));

			queryEngine = new OntoQueryLayerLiteral(this.onto);
			queryEngineLayer = new OntoQueryLayer(this.onto);

			logger.info("Ontology ready!");
		} catch (OWLOntologyCreationException e) {
			logger.info("An error occurred when loading the ontology. Error: " + e.getMessage());
		} catch (Exception e) {
			logger.info("An unexpected error occurred: " + e.getMessage());
		}
	}

	/**
	 * @param instanceName Name of the new instance.
	 * @param conceptName  Name of the concept which the new instance instances.
	 */
	@OPERATION
	void addInstance(String instanceName, String conceptName) {
		queryEngine.getQuery().addInstance(instanceName, conceptName);
	}

	/**
	 * @param instanceName Name of the new instance.
	 */
	@OPERATION
	void addInstance(String instanceName) {
		queryEngine.getQuery().addInstance(instanceName);
	}

	/**
	 * @param instanceName Name of the instance.
	 * @param conceptName  Name of the concept.
	 * @return true if the <code>instanceName</code> instances
	 *         <code>conceptName</code>.
	 */
	@OPERATION
	void isInstanceOf(String instanceName, String conceptName, OpFeedbackParam<Boolean> isInstance) {
		isInstance.set(queryEngine.getQuery().isInstanceOf(instanceName, conceptName));
	}

	/**
	 * @param conceptName Name of the concept.
	 * @param instances   A free variable to receive the list of instances in the
	 *                    form of instances(concept,instance)
	 */
	@OPERATION
	void getInstances(String conceptName, OpFeedbackParam<Literal[]> instances) {
		List<Object> individuals = queryEngine.getIndividualNames(conceptName);
		instances.set(individuals.toArray(new Literal[individuals.size()]));
	}

	/**
	 * @return A list of ({@link OWLObjectProperty}).
	 */
	@OPERATION
	void getObjectPropertyNames(OpFeedbackParam<Literal[]> objectPropertyNames) {
		List<Object> names = queryEngine.getObjectPropertyNames();
		objectPropertyNames.set(names.toArray(new Literal[names.size()]));
	}

	/**
	 * @param domainName   Name of the instance ({@link OWLNamedIndividual}} which
	 *                     represent the property <i>domain</i>.
	 * @param propertyName Name of the new property.
	 * @param rangeName    Name of the instance ({@link OWLNamedIndividual}} which
	 *                     represent the property <i>range</i>.
	 */
	@OPERATION
	void addProperty(String domainName, String propertyName, String rangeName) {
		queryEngine.getQuery().addProperty(domainName, propertyName, rangeName);
	}

	/**
	 * @param domainName   Name of the instance which represents the domain of the
	 *                     property.
	 * @param propertyName Name of the property.
	 * @param rangeName    Name of the instance which represents the range of the
	 *                     property.
	 * @return true if a instance of the property was found, false otherwise.
	 */
	@OPERATION
	void isRelated(String domainName, String propertyName, String rangeName, OpFeedbackParam<Boolean> isRelated) {
		isRelated.set(queryEngine.getQuery().isRelated(domainName, propertyName, rangeName));
	}


	/**
	 * @param domain       The name of the instance which corresponds to the domain
	 *                     of the property.
	 * @param propertyName Name of the property
	 * @return A list of ({@link OWLNamedIndividual}).
	 */
	// @OPERATION
	/*
	 * void getObjectPropertyValues(String domain, String propertyName,
	 * OpFeedbackParam<String> instances) { List<String> individuals = new
	 * ArrayList<String>(); for(OWLNamedIndividual individual :
	 * queryEngine.getQuery().getObjectPropertyValues(domain, propertyName)) {
	 * System.out.println("Testing.. " +
	 * individual.getIRI().toString().substring(individual.getIRI().toString().
	 * indexOf('#')+1));
	 * individuals.add(individual.getIRI().toString().substring(individual.getIRI().
	 * toString().indexOf('#')+1)); } instances.set(individuals.toString()); }
	 */

	@OPERATION
	void getObjectPropertyValues(String domain, String propertyName, OpFeedbackParam<Literal[]> instances) {
		List<Object> individuals = queryEngine.getObjectPropertyValues(domain, propertyName);
		instances.set(individuals.toArray(new Literal[individuals.size()]));

		/*
		 *
		 * for(OWLNamedIndividual individual :
		 * queryEngine.getQuery().getObjectPropertyValues(domain, propertyName)) {
		 * System.out.println("Testing.. " +
		 * individual.getIRI().toString().substring(individual.getIRI().toString().
		 * indexOf('#')+1));
		 * individuals.add(individual.getIRI().toString().substring(individual.getIRI().
		 * toString().indexOf('#')+1)); } instances.set(individuals.toString());
		 */
	}

	/**
	 * @return A list of ({@link OWLClass}).
	 */
	@OPERATION
	void getClassNames(OpFeedbackParam<Literal[]> classes) {
		List<Object> classNames = queryEngine.getClassNames();
		classes.set(classNames.toArray(new Literal[classNames.size()]));
	}

	/**
	 * @param conceptName Name of the new concept.
	 */
	@OPERATION
	void addConcept(String conceptName) {
		queryEngine.getQuery().addConcept(conceptName);
	}

	/**
	 * @param subConceptName   Name of the supposed sub-concept.
	 * @param superConceptName Name of the concept to be tested as the
	 *                         super-concept.
	 * @return true if <code>subConceptName</code> is a sub-concept of
	 *         <code>sueperConceptName</code>, false otherwise.
	 */
	@OPERATION
	void isSubConcept(String subConceptName, String superConceptName, OpFeedbackParam<Boolean> isSubConcept) {
		isSubConcept.set(queryEngine.getQuery().isSubConceptOf(subConceptName, superConceptName));
	}

	/**
	 * @param outputFile Path to the new file in the structure of directories.
	 * @throws OWLOntologyStorageException
	 */
	@OPERATION
	void saveOntotogy(String outputFile) {
		try {
			queryEngine.getQuery().saveOntology(outputFile);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return A list of ({@link OWLAnnotationProperty}).
	 */
	@OPERATION
	void getAnnotationPropertyNames(OpFeedbackParam<Literal[]> AnnotationPropertyNames) {
		List<Object> names = queryEngine.getAnnotationPropertyNames();
		AnnotationPropertyNames.set(names.toArray(new Literal[names.size()]));
	}

	/**
	 * @return A list of ({@link OWLDataProperty}).
	 */
	@OPERATION
	void getDataPropertyNames(OpFeedbackParam<Literal[]> dataPropertyNames) {
		List<Object> names = queryEngine.getDataPropertyNames();
		dataPropertyNames.set(names.toArray(new Literal[names.size()]));
	}

	/**
	 * Method that:
	 * 1) Verify if domain(purpose) is an individual
	 * 2) Verify if range (product) is an individual.
	 * 3) Verify if there is a relationship between domain and range.
	 * 4) looking for status-functions related with individual domain (purpose) through of "isPurposeOf" object Property.
	 * @param domain - Purpose of relationship
	 * @param range - Product of relationship
	 * @return name of status-function related to domain (that is the purpose this of relation).
	 */
	@OPERATION
	void isPurposeOfStatusFunction(String domain, String range, OpFeedbackParam<Literal> statusFunctionName) {
		// e.g Domain = haveBook
		// e.g range = "multiagentSystems"

		OWLDataFactory dataFactory = onto.getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = onto.getOntology().getOntologyID().getDefaultDocumentIRI().get();

		OWLNamedIndividual owlIndividualDomain = dataFactory.getOWLNamedIndividual(IRI.create((String)
										   ((Object) baseIRI + "#" + domain)));

		OWLNamedIndividual owlIndividualRange = dataFactory.getOWLNamedIndividual(IRI.create((String)
				   ((Object) baseIRI + "#" + range)));

		// first verification
		boolean domainIsAnIndividual = queryEngine.getClassOfTheInstanceAndReturnClass(owlIndividualDomain) != null ? true : false;
		// second // first verification
		boolean rangeIsAnIndividual = queryEngine.getClassOfTheInstanceAndReturnClass(owlIndividualRange) != null ? true : false;
		// third verification
		if(queryEngine.checkIfThereIsAnyObjectPropertyBetweenDomainAndRange(domain, range) &
		   domainIsAnIndividual & rangeIsAnIndividual) {
			// Finally, fourth search.
			for (OWLNamedIndividual individual : queryEngineLayer.getObjectPropertyValues(domain, "isPurposeOf")) {
				Literal L = ASSyntax.createLiteral(individual.getIRI().getFragment());
				statusFunctionName.set(L);

			}
		}
	}

	/**
	 * Method for get all relation involved that has Domain the concept
	 * @param concept
	 * @param dataString
	 */

	@OPERATION
	void propertiesWhoseDomainIsTheConcept(String concept, OpFeedbackParam<String> dataString) {


		//getObjectPropertiesInSignature()

		Set<OWLClass> classes = onto.getOntology().getClassesInSignature();

		Set<OWLObjectProperty> properties = onto.getOntology().getObjectPropertiesInSignature();

		// getFragment() //getShortForm
		for (OWLClass cls : classes) {
			//System.out.println("+: " + cls.getIRI().getShortForm());

			if (cls.getIRI().getShortForm().equals(concept)) {
				System.out.println("+: " + cls.getIRI().getShortForm()); // class
				System.out.println(" \tObject Property Domain");
				for (OWLObjectPropertyDomainAxiom op : onto.getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
					if (op.getDomain().equals(cls)) {
						for (OWLObjectProperty oop : op.getObjectPropertiesInSignature()) {
							System.out.println("\t\t +: " + oop.getIRI().getShortForm());
						}
						// System.out.println("\t\t +: " +
						// op.getProperty().getNamedProperty().getIRI().getShortForm());
					}
				}


				System.out.println(" \tData Property Domain");
				for (OWLDataPropertyDomainAxiom op : onto.getOntology().getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
					if (op.getDomain().equals(cls)) {
						for (OWLDataProperty oop : op.getDataPropertiesInSignature()) {
							System.out.println("\t\t +: " + oop.getIRI().getShortForm());
						}
						// System.out.println("\t\t +: " +
						// op.getProperty().getNamedProperty().getIRI().getShortForm());
					}
				}



				System.out.println(" \tObject Property Range");
				for (OWLObjectPropertyRangeAxiom op : onto.getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_RANGE)) {
					if (op.getRange().equals(cls)) {
						for (OWLObjectProperty oop : op.getObjectPropertiesInSignature()) {
							System.out.println("\t\t +: " + oop.getIRI().getShortForm());
						}
						// System.out.println("\t\t +: " +
						// op.getProperty().getNamedProperty().getIRI().getShortForm());
					}
				}
			}
		}
	}



	/**
	 * Metodo que a partir do dominio monta um literal com todas as propriedades de objetos e dados.
	 * Que tem como dominio o "domain".
	 * O literal tem a seguinte formula: predicado(domain, range) ou predicado(domain, tipo primitivo de dado) 
	 * (no caso de propriedade de dado)
	 * @return A list of ({@link OWLDataProperty}).
	 */
	@OPERATION
	void getFullPropertiesByDomain(String domain, OpFeedbackParam<Literal[]> properties) {
		List<Object> objectPropertiesByDomain = queryEngine.getObjectPropertiesByDomain(domain);
		List<Object> dataPropertiesByDomain = queryEngine.getDataPropertiesByDomain(domain);
		List<Object> all = new ArrayList<Object>();

		for(Object temp : objectPropertiesByDomain) {
			all.add(temp);
		}
		for(Object temp2 : dataPropertiesByDomain) {
			all.add(temp2);
		}
		properties.set(all.toArray(new Literal[all.size()]));
	}

	/**
	 * Metodo que a partir do dominio monta um literal com todas as propriedades de objeto que tem como dominio o domain
	 * O literal tem a seguinte formula: predicado(domain, range)
	 * @return A list of ({@link OWLDataProperty}).
	 */
	@OPERATION
	void getFullObjectPropertiesByDomain(String domain, OpFeedbackParam<Literal[]> objProperties) {
		List<Object> objectPropertiesByDomain = queryEngine.getObjectPropertiesByDomain(domain);
		objProperties.set(objectPropertiesByDomain.toArray(new Literal[objectPropertiesByDomain.size()]));
	}


	/**
	 * Metodo que a partir do dominio monta um literal com todas as propriedades de dados que tem como dominio o domain
	 * O literal tem a seguinte fórmula: predicado(domain, range), range = um tipo primitivo de dado
	 * @return A list of ({@link OWLDataProperty}).
	 */
	@OPERATION
	void getFullDataPropertiesByDomain(String domain, OpFeedbackParam<Literal[]> dataProperties) {
		List<Object> dataPropertiesByDomain = queryEngine.getDataPropertiesByDomain(domain);
		dataProperties.set(dataPropertiesByDomain.toArray(new Literal[dataPropertiesByDomain.size()]));
	}

	
	/**
	 * Return a literal with the formula nameOfClassThatIndividualPertains(individual)
	 * @param individual
	 * @param classe
	 */
	@OPERATION
	void getClassOfTheInstanceAndReturnliteral(String individual, OpFeedbackParam<Literal> classe) {

		OWLDataFactory dataFactory = onto.getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = onto.getOntology().getOntologyID().getDefaultDocumentIRI().get();

		// pegar instance e transformar pra individuo

		OWLNamedIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + individual)));
		System.out.println(queryEngine.getClassOfTheInstanceAndReturnLiteral(owlIndividual)); 
		classe.set(queryEngine.getClassOfTheInstanceAndReturnLiteral(owlIndividual));
	}


	/**
	 * return the name of class that individual pertains. 
	 * @param individual
	 * @param classe
	 */
	@OPERATION
	void getClassOfTheInstanceAndReturnClass(String individual, OpFeedbackParam<String> classe) {

		OWLDataFactory dataFactory = onto.getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = onto.getOntology().getOntologyID().getDefaultDocumentIRI().get();

		// pegar instance e transformar pra individuo

		OWLNamedIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create((String)
										   ((Object) baseIRI + "#" + individual)));

		classe.set(queryEngine.getClassOfTheInstanceAndReturnClass(owlIndividual));
	}

	/**
	 * Verify if concept pertains a class of the system. Return true if yes or false if not.
	 * @param concept
	 * @param isClass
	 */
	@OPERATION
	void isaClass(String concept, OpFeedbackParam<Boolean> isClass){
		//containsClassInSignature(IRI owlClassIRI)
		OWLDataFactory dataFactory = onto.getOntology().getOWLOntologyManager().getOWLDataFactory();
		IRI baseIRI = onto.getOntology().getOntologyID().getDefaultDocumentIRI().get();
		OWLClass owlClass = dataFactory.getOWLClass(IRI.create((String) ((Object) baseIRI + "#" + concept)));

		isClass.set(onto.getOntology().containsClassInSignature(owlClass.getIRI()));
	}

	/**
	 * método retorna apenas o dado (Range) de uma propriedade de objetos
	 * Método construído porque desta forma o dado é buscado através de uma pesquisa do reasoner
	 * Desta forma, encontra-se também dados inferidos através de regras SWRL relacionados a propriedade de objetos.
	 * @param individual (domain) da relação
	 * @param objectProperty nome da propriedade de objeto
	 */

	@OPERATION
	void getRangeByObjectPropertyandIndividual(String individual, String objectProperty,
			 OpFeedbackParam<String> nameRange) {
		nameRange.set(queryEngine.getRangeByObjectPropertyandIndividual(individual, objectProperty).toString());
	}



	/**
	 * método retorna apenas o dado (Range) de uma propriedade de dados
	 * Método construído porque desta forma o dado é buscado através de uma pesquisa do reasoner
	 * Ainda não consegue buscar dados inferidos, mas a ideia é permitir.
	 * @param individual (domain) da relação
	 * @param dataProperty nome da propriedade de dados
	 */

	@OPERATION
	void getRangeByDataPropertyandIndividual(String individual, String dataProperty, OpFeedbackParam<String> nameRange) {
		nameRange.set(queryEngine.getRangeByDataPropertyandIndividual(individual, dataProperty).toString());
	}

}
