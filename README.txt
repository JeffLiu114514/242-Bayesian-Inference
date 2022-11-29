Project 3 Bayesian Inference
Team member:	Shaotong Sun(****@u.rocehster.edu)
			Junfei Liu(jliu137@u.rochester.edu)
			Dingcheng Wang(****@u.rochester.edu)

How to compile:
cd to Project3
use the java -cp command.
for example:
	java -cp "./src" bn/Inference/MyBNApproxInferencer 100000 src/bn/examples/aima-wet-grass.xml R S true
	java -cp "./src" bn/Inference/MyBNApproxInferencer 100000 src/bn/examples/aima-alarm.xml B J true M true
	java -cp "./src" bn/Inference/MyBNInferencer src/bn/examples/aima-alarm.xml B J true M true
	java -cp "./src" bn/Inference/MyBNInferencer src/bn/examples/aima-wet-grass.xml R S true

(the first two are for the Approx and the last two are for Exact).

be sure to have your JDK update to version 17.0.1
openjdk 17.0.1 2021-10-19
OpenJDK Runtime Environment (build 17.0.1+12-39)
OpenJDK 64-Bit Server VM (build 17.0.1+12-39, mixed mode, sharing) 

If you ever want to try compilng our .java files, you can do it using javac **/*.java to compile all .java files(though I think it only works on bash)

Btw, we notice that the true and false may appear in different orders(true false OR false true). 
