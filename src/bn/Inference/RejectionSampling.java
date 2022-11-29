package bn.Inference;

import bn.core.*;
import java.util.ArrayList;
import java.util.List;

public class RejectionSampling {
    /**
     * function REJECTION-SAMPLING(X, e, bn,N) returns an estimate of P(X|e)
     * inputs: X, the query variable
     *         e, observed values for variables E
     *         bn, a Bayesian network
     *         N, the total number of samples to be generated
     *         local variables: N, a vector of counts for each value of X, initially zero(this is a different N)
     * for j = 1 to N do
     *      x←PRIOR-SAMPLE(bn)
     *      if x is consistent with e then
     *          N[x]←N[x]+1 where x is the value of X in x
     * return NORMALIZE(N)
     */
    public bn.base.Distribution rejectionSampling (RandomVariable X, Assignment e, BayesianNetwork bn, int N){
        bn.base.Distribution distribution = new bn.base.Distribution(X);
        for (Value val : X.getDomain())//init value to 0
            distribution.set(val, 0);

        for(int j=1; j<=N; j++){
            Assignment x = priorSample(bn);
            if (x.containsAll(e)) {//if x is consistent with e then
                distribution.put(x.get(X), distribution.get(x.get(X)) + 1.0);
            }
        }
        distribution.normalize();
        return distribution;
    }

    public Assignment priorSample (BayesianNetwork bn){
        /**
         * function PRIOR-SAMPLE(bn) returns an event sampled from the prior specified by bn
         * inputs: bn, a Bayesian network specifying joint distribution P(X1, …, Xn)
         *
         * x ← an event with n elements
         * foreach variable Xi in X1, …, Xn do
         *      x[i] ← a random sample from P(Xi | parents(Xi))
         * return x
         */

        Assignment x = new bn.base.Assignment();//x ← an event with n elements
        List<RandomVariable> vars = bn.getVariablesSortedTopologically();
        for (RandomVariable Xi : vars) {
            ArrayList<Double> distribution = new ArrayList<>();
            for (int i = 0; i < Xi.getDomain().size(); i++) {//x[i] ←
                bn.base.Domain domain = (bn.base.Domain) Xi.getDomain();
                x.put(Xi, domain.getElements().get(i));
                distribution.add(bn.getProbability(Xi, x));//P(Xi | parents(Xi))
                x.remove(Xi);
            }
            double random = Math.random();
            int index = 0;
            for (int i = 0; i < distribution.size(); i++) {
                random -= distribution.get(i);
                if (random <= 0) {
                    index = i;
                    break;
                }
            }
            bn.base.Domain domain = (bn.base.Domain) Xi.getDomain();
            x.put(Xi, domain.getElements().get(index));
        }
        return x;
    }



}
