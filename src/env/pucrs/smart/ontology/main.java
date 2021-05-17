package pucrs.smart.ontology;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.impl.OWLReasonerBase;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.Searcher;
import org.semanticweb.owlapi.util.InferredObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import pucrs.smart.ontology.mas.OntoQueryLayerLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLDeclarationAxiomImpl;

/**
 * main
 */
public class main {
	
	static OwlOntoLayer onto;
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		
		onto = new OwlOntoLayer("src/resources/initial_ontology_model_two_version.owl");

		try {
			
		   //OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();

			// Load HermiT reasoner
			
			Configuration configuration = new Configuration();
		    configuration.ignoreUnsupportedDatatypes = false;
		    //configuration.prepareReasonerInferences.dataPropertyClassificationRequired = false;
		    
		   OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(onto.getOntology(), configuration);
		   onto.setReasoner(reasoner);
	        //System.out.println(reasoner.isConsistent());

	        
			
			// PELLET
		
			//OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
			//OWLReasoner reasoner = reasonerFactory.createReasoner(onto.getOntology(), new SimpleConfiguration());
			//onto.setReasoner(reasoner);
			
			//PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner( onto.getOntology() );

			
			
			//OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
	        //OWLReasoner reasoner = reasonerFactory.createReasoner(onto.getOntology(), new SimpleConfiguration());
			
			//onto.setReasoner(reasoner);
			
			
			/*
			OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
		    Configuration configuration = new Configuration();
		    configuration.throwInconsistentOntologyException = false;
		    OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(onto.getOntology(), configuration);
	        onto.setReasoner(reasoner);
	        */
	        
			
			//Load Pellet reasoner
			//OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
	        //OWLReasoner reasoner = reasonerFactory.createReasoner(onto.getOntology(), new SimpleConfiguration());
			//onto.setReasoner(reasoner);
			
			//onto.setReasoner(reasonerFactory.createReasoner(onto.getOntology()));

			// OntoQueryLayerString queryEngine = new OntoQueryLayerString(onto);

			OntoQueryLayerLiteral queryEngine = new OntoQueryLayerLiteral(onto);

			System.out.println("Ontology ready");

			System.out.println("Reasoner Name: " + onto.getReasoner().getReasonerName());
			System.out.println("Reasoner Version: " + onto.getReasoner().getReasonerVersion());

			OWLDataFactory dataFactory = onto.getOntology().getOWLOntologyManager().getOWLDataFactory();
			IRI baseIRI = onto.getOntology().getOntologyID().getDefaultDocumentIRI().get(); // getOntologyID().getDefaultDocumentIRI().get();

			OWLClass owlclass = dataFactory.getOWLClass(IRI.create((String) ((Object) baseIRI + "#" + "TransferBook")));

			OWLObjectProperty objectProperty = dataFactory
					.getOWLObjectProperty(IRI.create((String) ((Object) baseIRI + "#" + "have")));

			// Stream<OWLClass> stream = onto.getOntology().classesInSignature();

			Stream<OWLAxiom> stream = onto.getOntology().axioms();
			Set<OWLClass> set = onto.getOntology().classesInSignature().collect(Collectors.toSet());
			OWLClass[] array = onto.getOntology().classesInSignature().toArray(OWLClass[]::new);

			// stream.forEach(teste -> System.out.println(teste));


			Set<OWLClass> classes;
			Set<OWLObjectProperty> prop;
			Set<OWLDataProperty> dataProp;
			Set<OWLNamedIndividual> individuals;

			classes = onto.getOntology().getClassesInSignature();
			prop = onto.getOntology().getObjectPropertiesInSignature();
			dataProp = onto.getOntology().getDataPropertiesInSignature();
			individuals = onto.getOntology().getIndividualsInSignature();
			// configurator = new OWLAPIOntologyConfigurator(this);

			
			
			/* ArrayList<OWLClass> classes = new ArrayList<OWLClass>();
				ontology.classesInSignature().forEach(classes::add); */
			
			//onto.getOntology().inSignature
			
			OWLObjectProperty objectProperty1 = dataFactory.getOWLObjectProperty(IRI.create((String) ((Object) baseIRI + "#" + "hasReason")));
			Stream<OWLIndividual> streamIndividual =  Searcher.values(onto.getOntology().axioms(AxiomType.OBJECT_PROPERTY_ASSERTION), objectProperty1);
		    Stream<OWLClass> stream1 = onto.getOntology().classesInSignature();
			

		    

		    
		    Set<InferenceType> inferenceType = onto.getReasoner().getPrecomputableInferenceTypes();

		    
		    
		    

		    
		    
		    /*
		    System.out.println(queryEngine.getValueOfRangeByObjectPropertyAndIndividualDomain("teste1" , "hasReason"));
 			System.out.println("hasReceiver: " + queryEngine.getValueOfRangeByObjectPropertyAndIndividualDomain("teste1" , "hasReceiver"));
		    
 			System.out.println("hasValue: " + queryEngine.getValueOfRangeByDataPropertyAndIndividualDomain("teste1" , "hasValue"));

 			System.out.println(queryEngine.getValueOfRangeByDataPropertyAndIndividualDomain("multiAgentSystems" , "hasTitle"));
 			System.out.println(queryEngine.getValueOfRangeByDataPropertyAndIndividualDomain("multiAgentSystems" , "hasPrice"));
 			*/
		    

 			OWLNamedIndividual owlIndividualName = dataFactory
 					.getOWLNamedIndividual(IRI.create((String) ((Object) baseIRI + "#" + "transferBook1")));

 			OWLObjectProperty owlObjectProperty = dataFactory
 					.getOWLObjectProperty(IRI.create((String) ((Object) baseIRI + "#" + "hasReceiver")));
 			
 			
 			OWLDataProperty owlDataProperty = dataFactory
 					.getOWLDataProperty(IRI.create((String) ((Object) baseIRI + "#" + "hasValue")));
 			
 			//System.out.println(owlDataProperty);

 			
 			//onto.getOntology().getAxioms
 			
 			//System.out.println(onto.getReasoner().isEntailed(""));
 			
 			//onto.getReasoner().getRootOntology().getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
 			//
 			
 			//System.out.println(queryEngine.getQuery().getOntology().getOntology().getLogicalAxioms());
 			
 			
//  OWLObjectPropertyAssertionAxiom op : ontoQuery.getOntology().getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)
 			
 			
 			//InferredObjectPropertyAxiomGenerator generator = new InferredObjectPropertyAxiomGenerator();
 			//generator.createAxioms(owldatafactory, reasoner);
 			
 			
 			Set<OWLNamedIndividual> owlindividualProv = onto.getReasoner().getObjectPropertyValues(owlIndividualName, owlObjectProperty).getFlattened();
 			
 			for(OWLNamedIndividual oni : owlindividualProv) {
 				System.out.println(oni.getIRI().getShortForm());
 			}
 			
 			
 			Set<OWLLiteral> owlDataIndividual =  onto.getReasoner().getDataPropertyValues(owlIndividualName, owlDataProperty);
 			
 			//String nameTemp = onto.getReasoner().getDataPropertyValues(owlIndividualName, owlDataProperty).iterator().next().getLiteral();
 			//System.out.println("Name: " + nameTemp);
 			
 			onto.getReasoner().precomputeInferences();
 			
 			for(OWLLiteral lit : onto.getReasoner().getDataPropertyValues(owlIndividualName, owlDataProperty)) {
 				System.out.println(lit.getLiteral());
 			}
 			
 			System.out.println(owlDataIndividual.toString());
 			
 			
 			
 			
 			
 				
 			for (OWLObjectPropertyAssertionAxiom op : onto.getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {	
 				
 				// Verifica se o individuo do domain é o individualName e se a propriedade de objeto é a propertyName
 				
 			if(op.getSubject().equals(owlIndividualName)) {	
 				System.out.println("\n\nDominio: " + op.getSubject().asOWLNamedIndividual().getIRI().getShortForm());
 				System.out.println("propriedade: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
 				System.out.println("Range: " + op.getObject().asOWLNamedIndividual().getIRI().getShortForm());
 			}
 				
 				if(op.getSubject().equals(owlIndividualName) & op.getProperty().equals(owlObjectProperty)) {
 					//System.out.println(op.getObject().asOWLNamedIndividual().getIRI().getShortForm());
 					System.out.println(op.getObject().asOWLNamedIndividual().getIRI().getShortForm());
 				}
 				
 			} 
 			
 			
 			
 			
 			
 			
 			
 			
 			
			/*
			System.out.println("Classes");
			System.out.println("--------------------------------");
			for (OWLClass cls : classes) {
				System.out.println("+: " + cls.getIRI().getShortForm());

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
				
				
				System.out.println(" \tObject Property Range");
				for (OWLObjectPropertyRangeAxiom opr : onto.getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_RANGE)) {
					if (opr.getRange().equals(cls)) {
						for (OWLObjectProperty oop : opr.getObjectPropertiesInSignature()) {
							System.out.println("\t\t +: " + oop.getIRI().getShortForm());

						}
						// System.out.println("\t\t +: " +
						// op.getProperty().getNamedProperty().getIRI().getShortForm());
					}
				}
				

				System.out.println(" \tData Property Domain");
				for (OWLDataPropertyDomainAxiom dp : onto.getOntology().getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
					if (dp.getDomain().equals(cls)) {
						for (OWLDataProperty odp : dp.getDataPropertiesInSignature()) {
							System.out.println("\t\t +: " + odp.getIRI().getShortForm());
						}
						// System.out.println("\t\t +:" + dp.getProperty());
					}
				}
			}*/
		} catch (Exception e) {
			System.out.println("an exception has occurred: " + e.getMessage());
		}
	}
	
	
	public void metodoquepegavaloresdedomainpropertyerange() {
		
		

		// PEGA UM INDIVIDUO DIFERENTE
		for(OWLDifferentIndividualsAxiom ind : onto.getOntology().getAxioms(AxiomType.DIFFERENT_INDIVIDUALS)) {
			// PEGA TODOS AXIOMAS DE OBJETC PROPRIEDADES
			//String individual = ind.toString();
			//System.out.println(individual);
			//for (OWLObjectPropertyDomainAxiom op : onto.getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
			for (OWLObjectPropertyAssertionAxiom op : onto.getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
				// VERIFICA SE O INDIVIDUO FAZ PARTE DO DOMAIN DA PROPRIEDADE
				//if() {}
				OWLIndividual individualProv = op.getSubject();
				
				//System.out.println("Domain: " + individualProv.asOWLNamedIndividual().getIRI().getShortForm());
				//System.out.println("Object Property: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
				//System.out.println("Range: " + op.getObject().asOWLNamedIndividual().getIRI().getShortForm() + "\n");
				
				//System.out.println(op.toString());
				//if(op.getDomain().equals(ind)) {
				//	System.out.println(op.toString());
				//	for (OWLObjectProperty oop : op.getObjectPropertiesInSignature()) {
				//		System.out.println("Individual: " + ind.toString());
				//		System.out.println("Object Property: " + oop.getIRI().getShortForm());
						//System.out.println("\t\t +: " + oop.getIRI().getShortForm());
								
				//	}
				//}
			}
			
			
			for (OWLDataPropertyAssertionAxiom op : onto.getOntology().getAxioms(AxiomType.DATA_PROPERTY_ASSERTION)) {
				// VERIFICA SE O INDIVIDUO FAZ PARTE DO DOMAIN DA PROPRIEDADE
				//if() {}
				OWLIndividual individualProv = op.getSubject();
				//System.out.println("\t Data Property");
				//System.out.println("Domain: " + individualProv.asOWLNamedIndividual().getIRI().getShortForm());
				//System.out.println("Data Property: " + op.getProperty().asDataPropertyExpression().asOWLDataProperty().getIRI().getShortForm());
				//System.out.println("Range: " + op.getObject().toString());
				
				//System.out.println(op.toString());
				//if(op.getDomain().equals(ind)) {
				//	System.out.println(op.toString());
				//	for (OWLObjectProperty oop : op.getObjectPropertiesInSignature()) {
				//		System.out.println("Individual: " + ind.toString());
				//		System.out.println("Object Property: " + oop.getIRI().getShortForm());
						//System.out.println("\t\t +: " + oop.getIRI().getShortForm());
								
				//	}
				//}
			}
			
		}
		
	}

}
