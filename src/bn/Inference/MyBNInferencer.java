package bn.Inference;

import bn.base.Assignment;
import bn.core.Distribution;
import bn.base.StringValue;
import bn.core.BayesianNetwork;
import bn.parser.XMLBIFParser;

public class MyBNInferencer {
    public static void main(String[] args) throws Exception{
        Enumeration enumeration = new Enumeration();
        //java -cp "./bin" MyBNInferencer aima-alarm.xml B J true M true

        XMLBIFParser x = new XMLBIFParser();

        try {
            BayesianNetwork network = x.readNetworkFromFile(args[0]);
            Assignment assignment = new Assignment();
            for (int i = 2; i < args.length - 1; i += 2) {
                assignment.put(network.getVariableByName(args[i]), new StringValue(args[i+1]));
            }
            Distribution distribution = enumeration.query(network.getVariableByName(args[1]), assignment, network);
            System.out.println(distribution);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
