{ include("common_inst.asl") }


~kill_soldier_from_allied_base.

+!conqueringANewTerritory 
 <- 
 	.wait(100);
 	?constitutive_rule(sending_a_brodcast_message,Status,_,_);
 	.print("Status: " , Status);
 	 
 	 getStatusFunctionAndReturnPurpose(Status, NamePurposes);
 	 //!percorreList(NamePurposes);
 	 
 	 isStateOfPurpose(NamePurposes , States);
 	 isPredicateOfState(States, Predicates);
 	 !percorreList(Predicates);
 	 
 	// Nessa linha que o mapeamento entre o objetivo e o estado do mundo é feito.
 	//isPurposeOfState("isOwnerOf(joao,bookA)", NamePurpose); // Predicate has two terms.
 	
 	//isPurposeOfState(isOwnerOf(joao, Product), NamePurpose); // Predicate has two terms.
 	
 	
 	
 	//isPurposeOfSF(NamePurpose, NameStatusFunction); // inverter essa function
 	//!percorreList(NameStatusFunction);
 	
 	//!searchActionCountAsStatus(NameStatusFunction);
 	.
 	
 
 +!percorreList([]).

+!percorreList([H|T])
<-
	.print("Predicate: " , H);
	.term2string(Te,H);
	//!searchActionCountAsStatus(Te);
	!percorreList(T);
	.
 	
 	
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") } 	