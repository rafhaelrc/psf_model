
/*
 * First example - different agents and purposes in the same scenario. 
 */

//{ include("common.asl") }

!in_bh_institution.


// plan for join in the institution.
+!in_bh_institution <-
   joinWorkspace("bh",_);
   lookupArtifact("bh_art", InstArt);
   focus(InstArt).

-!in_bh_institution <-
   .wait(100);
   !in_bh_institution.



 // plan for different purposes and same scenario. 


+!percorreList([]).

+!percorreList([H|T])
<-
	//.print("Status: " , H);
	.term2string(Te,H);
	!searchActionCountAsStatus(Te);
	!percorreList(T);
	.

// hasBook(Product); Product = nome do livro.

//haveBook(Product) // goal


+!isOwnerOf(Owner, Product) 
 <- //.print("Owner is: " , Owner);
 	//.print("Product is: " , Product);
 	
 	// trocar string por Literal
 	
 	isPurposeOfState("isOwnerOf(joao,bookA)", NamePurpose); // Predicate has two terms.
 	//isPurposeOfState("isOwnerOf(bookA, joao)", NamePurpose); // Predicate has two terms.
 	
 	//isPurposeOfState("holder(bookB)", NamePurpose); // Predicate with one term.
 	
 	isPurposeOfSF(NamePurpose, NameStatusFunction);
 	!percorreList(NameStatusFunction);
 	
 	//!searchActionCountAsStatus(NameStatusFunction);
 	.
	
	
+!searchActionCountAsStatus(Status) : constitutive_rule(Action, Status,_,_)
	<-
	.print("action associated with the status: ", Action);
	Action;
	.



/* 

+!haveBook(Product)
	<- 
	//P = haveBook(Product);
	//P =.. [F, T, A];
	//.print("FUNCTOR: " , F);
	
	// Ler o nome da TRIGGER
	!commonPurpose("haveBook", Product);
	.

*/
//*/



/*
 * Second example - Agents with the same purpose in different scenarios.
 

!in_bh_institution. 

// plan for join in the institution.
+!in_bh_institution <-
   joinWorkspace("bh",_);
   lookupArtifact("bh_art", InstArt);
   focus(InstArt).

-!in_bh_institution <-
   .wait(100);
   !in_bh_institution.

+!haveBook(Product)
	<-
  	// Verifico qual classe o individuo Book pertence... Return = "book(multiAgentSystems)"
  	getClassOfTheInstanceAndReturnliteral(Product, Literal);
	+Literal;

  	// Descubro qual sf está associada ao propósito. // havebook ispurposeOF --- SF (repace)
  	isPurposeOfStatusFunction("haveBook", Product, NameStatusFunction);
  	+nameStatusFunctionAssociatedToPurpose(NameStatusFunction);

  	.print("Name of the status-function: ", NameStatusFunction, " associated with the purpose ", "havebook(" , Product, ")");
  	!searchActionCountAsStatus(NameStatusFunction)
  	.
	
+!searchActionCountAsStatus(Status) : constitutive_rule(Action, Status,_,_)
	<-
	.print("acao associada ao Status: ", Action);
	Action;
	.

 */



{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
