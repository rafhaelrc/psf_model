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
