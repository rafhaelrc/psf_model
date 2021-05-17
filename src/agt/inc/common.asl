// common Goal of the all agents.

!in_bh_institution.


// plan for join in the institution.
+!in_bh_institution <-
   joinWorkspace("bh",_);
   lookupArtifact("bh_art", InstArt);
   focus(InstArt).

-!in_bh_institution <-
   .wait(100);
   !in_bh_institution.


+!addToTheBeliefBase([]).
+!addToTheBeliefBase([H|T])
<-
	+H;
	.print("Ontology information added to belief base: ", H)
	!addToTheBeliefBase(T)
	.


+!commonPurpose(Purpose, Product)
  <-
  .my_name(N);
  .print("The ", N, "'s goal is ", Purpose, "(", Product ,")");
  
  // Verifico qual classe o individuo Book pertence... Return = "book(multiAgentSystems)"
	getClassOfTheInstanceAndReturnliteral(Product, Literal);
	+Literal;

  	// Descubro qual sf está associada ao propósito.
  	isPurposeOfStatusFunction(Purpose, Product, NameStatusFunction);
  	+nameStatusFunctionAssociatedToPurpose(NameStatusFunction);

  	.print("Name of the status-function: ", NameStatusFunction, " associated with the purpose. ");
  	!searchActionCountAsStatus(NameStatusFunction)
  	.



+!searchActionCountAsStatus(NameStatusFunction) : constitutive_rule(Action, NameStatusFunction,_,_)
	<-
	
	.print("Ação finalmente: ", Action);
	/* Preciso fazer essa conversao porque esp. SAI nao deixa colocar Variavel no X
	* A letra inicial de um conceito na ontologia é escrita com letra Maiuscula 
	* Por isso toda essa conversao é necessaria.
	*/
	.term2string(Action, ActionString);
	.nth(0,ActionString,FirstLetterAction);  // cut primeira letra
	.upper_case(FirstLetterAction,FirstUpperCaseLetterAction); // deixa maiuscula
	.delete(0,ActionString, ActionWithCut)  // deleta primeira letra
	.concat(FirstUpperCaseLetterAction, ActionWithCut, UpperCaseAction);
	.print("Correct Action: ", UpperCaseAction);
	
	
	!getInformationsAboutAction(Action, UpperCaseAction)
	.


//-!searchActionCountAsStatus(Status) <- .print("NÃ£o encontrei aÃ§Ã£o que conta como ", Status).


+!getInformationsAboutAction(Action, UpperCaseAction)
	<-
	+nameAction(Action);
	
	/**
	*  Verificar se Existe essa classe (TransferBook) no sistema..
	*  Faz isso para conseguir "entender" as propriedades associadas.
	*/
	isaClass(UpperCaseAction, IsaClass);
	.print("Action is a class: ", IsaClass);
	
	/*
	* Descubro todas as propriedades (de objeto e de dados) que estao associadas
	* com a acao UpperCaseAction.
	*/
	getFullPropertiesByDomain(UpperCaseAction, FullResult);
	!addToTheBeliefBase(FullResult);
	
	?book(B);
	
	getClassOfTheInstanceAndReturnClass(B, ClassResult);
	.print("Teste return class: " , ClassResult);
	
	/**
	* Verifico se a classe livro (ClassResult) e o range hasReason (C) sao iguais
	* Pensei nisso para que o agente possa passar essa informacao na hora de criar
	* um individuo na ontologia que representara a acao concreta que sera executada por ele.
	* Se ele informar o Reason, os demais campos envolvidos na transfer sao inferidos.
	* Ele pode apenas consulta-los para ter todos os dados envolvidos na acao concreta.
	*/
	?hasReason(_,C);
	/* c = "Book" */

	if(.term2string(C, S1) & S1 == ClassResult){
		+equalsRangeOfPropertyAndClassOfTheBook(true);
	};
	.print("ACTION:: ", Action); 
	!Action;
	.


+!transferBook // (Reason) 
	<-
	// Cria a acao na ontologia. A acao concreta vai ser um espelho desta ja persistida na ontologia
	addInstance("teste1", "TransferBook");
	// Passa o motivo
	?book(B);
	.print("Testando nome livro na transferencia: ", B);
	addProperty("teste1", "hasReason", B);
	
	
	getRangeByObjectPropertyandIndividual("teste1", "hasReason", TransferReason);
	getRangeByObjectPropertyandIndividual("teste1", "hasReceiver", TransferReceiver);

	//getRangeByDataPropertyandIndividual("teste1", "hasValue", TransferValue);
	getRangeByDataPropertyandIndividual(TransferReason, "hasPrice", TransferValue);

	.print("Transfer Reason: ", TransferReason);
	.print("Transfer Receiver: ", TransferReceiver);
	.print("Transfer Value: ", TransferValue);
	saveOntotogy("src/resources/initial_ontology_model_three_version.owl");


	/* Próximo passo:
	* Consultar a ontologia para pegar os dados necessÃ¡rios para executar
	* a acao concreta
	*/

	?nameAction(Action);
	Action;
	.











/* 

+!transferBook : equalsRangeOfPropertyAndClassOfTheBook(R) & R == true
	<-
	?hasReason(_, Classe);
	// Cria a aï¿½ï¿½o na ontologia. A aï¿½ï¿½o concreta vai ser um espelho desta jï¿½ persistida na ontologia
	addInstance("teste1", "TransferBook");
	// Passa o motivo
	?book(B);
	.print("Testando nome livro na transferÃªncia: ", B);
	addProperty("teste1", "hasReason", B);
	getRangeByObjectPropertyandIndividual("teste1", "hasReason", TransferReason);
	getRangeByObjectPropertyandIndividual("teste1", "hasReceiver", TransferReceiver);

	//getRangeByDataPropertyandIndividual("teste1", "hasValue", TransferValue);
	getRangeByDataPropertyandIndividual(TransferReason, "hasPrice", TransferValue);

	.print("Transfer Reason: ", TransferReason);
	.print("Transfer Receiver: ", TransferReceiver);
	.print("Transfer Value: ", TransferValue);
	saveOntotogy("src/resources/initial_ontology_model_three_version.owl");


	/* PrÃ³ximo passo:
	* Consultar a ontologia para pegar os dados necessÃ¡rios para executar
	* a aÃ§Ã£o concreta
	*/

 /* tem q apagar esse comentario pra funcionar tbm
  
	?nameAction(Action);
	Action;
	.

*/	
	