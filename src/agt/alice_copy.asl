// agent's goal
!terBook("MultiAgentSytem"). 

+!terBook(Product) <-
   // step 1 of the algorithm
  !commonPurpose("terBook", Product). 

+!commonPurpose(Purpose, Product)
  <-
  	isPurposeOfStatusFunction(Purpose, Product, NameStatusFunction); 	// step 2 of the algorithm
  	?constitutive_rule(Action, NameStatusFunction,_,_); 			  	// step 3 of the algorithm
  	Action. 
  	
  	


// arquivo só para tirar print pros paper.