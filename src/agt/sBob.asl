{ include("common_inst.asl") }

anti_goal(kill_soldier_from_allied_base).

+!conqueringANewTerritory 
 <- 
 	.wait(100);
 	?constitutive_rule(brodcasting_a_message,Status,_,_);
 	getStatusFunctionAndReturnPurpose(Status, NamePurposes);
 	isStateOfPurpose(NamePurposes , States);
 	isPredicateOfState(States, Properties);
 	if(anti_goal(AG) & .member(AG, Properties)){
 		.fail;
	}
 	else{
 		 brodcasting_a_message;
 	}
 	.
 	
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") } 	


/* 
 +!percorreList([]).

+!percorreList([H|T])
<-
	.print("Predicate: " , H);
	.term2string(Te,H);
	//!searchActionCountAsStatus(Te);
	!percorreList(T);
	.
*/ 	