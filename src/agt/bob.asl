
/*
 * First example - different agents and purposes in the same scenario. 
 */

{ include("common.asl") }

 // plan for different purposes and same scenario. 


+!percorreList([]).

+!percorreList([H|T])
<-
	//+H
	//.term2string(Te,H);
	
	//.type(H,string);
	
	//+Te;
	.print("Purpose: ", H);
	!percorreList(T);
	.



+!isOwnerOf(Owner, Product) 
 <- .print("Owner is: " , Owner);
 	.print("Product is: " , Product);
 	// isPurposeofStatusFunction(Purpose, Product, NameStatusFunction);
 	
 	isPurposeOfState("isOwnerOf(joao, bookA)", NamePurpose);
 	
 	if(.list(NamePurpose)){
 		.print("éééé");
 	}
 	!percorreList(NamePurpose);
 	isPurposeOfSF(NamePurpose, NameStatusFunction);
 	.term2string(Te,NameStatusFunction);
 	.print("SF: " , Te);
	!searchActionCountAsStatus(Te);
	.
	
+!searchActionCountAsStatus(Status) : constitutive_rule(Action, Status,_,_)
	<-
	.print("acao associada ao Status: ", Action);
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
