package bn.Inference;

import bn.core.*;

public class LikelihoodWeighting {

    public class WeightedAssignment {
        double weight;
        Assignment assignment;

        public WeightedAssignment(Assignment assignment, double weight) {
            this.weight = weight;
            this.assignment = assignment;
        }
    }

    public bn.base.Distribution LikelihoodWeighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {
        /**
         * function LIKELIHOOD-WEIGHTING(X , e, bn,N ) returns an estimate of P(X|e)
         *  inputs: X , the query variable
         *          e, observed values for variables E
         *          bn, a Bayesian network specifying joint distribution P(X1,...,Xn) N , the total number of samples to be generated
         *          local variables: W, a vector of weighted counts for each value of X , initially zero
         * for j = 1 to N do
         *      x,w ← WEIGHTED-SAMPLE(bn, e)
         *      W[x] ←W[x] + w where x is the value of X in x
         * return NORMALIZE(W)
         */
        bn.base.Distribution W = new bn.base.Distribution(X);
        for (Value v : X.getDomain())
            W.set(v, 0);

        for (int j = 1; j <= N; j++) {
            Assignment eCopy = e.copy();
            WeightedAssignment wa = weightedSample(bn, eCopy); // WeightedAssignment is x,w
            Value value = eCopy.get(X);
            W.set(value, W.get(value) + wa.weight); //W[x] ←W[x] + w where x is the value of X in x
        }
        W.normalize();
        return W;
    }

    public WeightedAssignment weightedSample(BayesianNetwork bn, Assignment e) {
        /**
         * function WEIGHTED-SAMPLE(bn, e) returns an event and a weight
         * w ← 1; x ←an event with n elements initialized from e
         * foreach variable Xi in X1,...,Xn do
         *      if Xi is an evidence variable with value xi in e
         *          then w ← w × P(Xi = xi | parents(Xi))
         *      else x[i] ← a random sample from P(Xi | parents(Xi))
         * return x, w
         */
        double weight = 1.0; // w <- 1
        Assignment x = new bn.base.Assignment();
        for (RandomVariable n : bn.getVariablesSortedTopologically()) {
            if (e.containsKey(n)) {
                x.put(n, e.get(n));
                weight = weight * bn.getProbability(n, x);
            } else {
                Value v = getRandomValue(n, x, bn); // a random sample
                e.put(n, v);
                x.put(n, v);
            }
        }
        return new WeightedAssignment(x, weight); // return x,w
    }

    public Value getRandomValue(RandomVariable n, Assignment x, BayesianNetwork bn) { //simplified priorSampling
        Distribution distribution = new bn.base.Distribution(n);
        for (Value v: n.getDomain()) {
            Assignment assignment = x.copy();
            assignment.put(n,v);
            distribution.put(v, bn.getProbability(n, assignment));
        }
        distribution.normalize();
        double random = Math.random();
        for (Value v: n.getDomain()) {
            random -= distribution.get(v);
            if (random < 0) {
                return v;
            }
        }
        return null;
    }
}
