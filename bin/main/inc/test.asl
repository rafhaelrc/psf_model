+!testa_cons_rule(Y) : constitutive_rule(X,Y,T,W)
   <- .print("Detectou constitutive rule ", X, Y, T, W).

-!testa_cons_rule(Y) <-
   .print("Não detectou a constitutive rule, tentando novamente..");
   .wait(100);
   !testa_cons_rule(Y).