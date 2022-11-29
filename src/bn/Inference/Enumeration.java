package bn.Inference;
import bn.core.*;
import java.util.List;

public class Enumeration implements Inferencer {

    @Override
    public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
        /**
         * function ENUMERATION-ASK(X , e, bn) returns a distribution over X
         *  inputs: X , the query variable
         *          e, observed values for variables E
         *          bn, a Bayes net with variables {X} ∪ E ∪ Y // Y = hidden variables
         *
         *  Q(X)←a distribution over X, initially empty
         *  for each value xi of X do
         *      Q(xi)←ENUMERATE - ALL(bn.VARS, exi)
         *          where exi is e extended with X = xi
         *  return NORMALIZE(Q(X))
         */
        List<RandomVariable> vars = network.getVariablesSortedTopologically();
        Distribution Q = new bn.base.Distribution(X);
        for (Value xi : X.getDomain()){
            Assignment exi = e.copy();
            exi.put(X, xi);
            Q.put(xi, enumeration_all(network, exi, vars));
        }
        Q.normalize();
        return Q;
    }

    public double enumeration_all(BayesianNetwork bn, Assignment e, List<RandomVariable> vars){
        /**
         * function ENUMERATE-ALL(vars, e) returns a real number
         *  if EMPTY?(vars) then return 1.0
         *  Y ← FIRST(vars)
         *  if Y has value y in e
         *      then return P(y | parents(Y )) × ENUMERATE-ALL(REST(vars), e)
         *      else return SIGMAy P(y | parents(Y )) × ENUMERATE-ALL(REST(vars), ey)
         *          where ey is e extended with Y = y
         */
        if(vars.isEmpty())
            return 1.0;
        RandomVariable Y = vars.get(0);
        if(e.containsKey(Y)){
            return bn.getProbability(Y, e) * enumeration_all(bn, e, vars.subList(1, vars.size()));//remove or sublist
        } else {
            double summation=0.0;
            for (Value v: Y.getDomain()) {
                Assignment ey = e.copy();
                ey.put(Y, v);
                summation += bn.getProbability(Y, ey) * enumeration_all(bn, ey, vars.subList(1, vars.size()));
                ey.remove(Y);
            }
//            for (int i = 0; i < Y.getDomain().size(); i++){
//                vars.remove(0);
//                Assignment ey = e.copy();
//                summation += bn.getProbability(Y, e) * enumeration_all(bn, ey, vars);
//            }
            return summation;
        }
    }
}
