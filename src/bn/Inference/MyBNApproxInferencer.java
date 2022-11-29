package bn.Inference;

import bn.parser.XMLBIFParser;
import bn.base.*;

public class MyBNApproxInferencer {
    public static void main(String[] args) throws Exception{
        RejectionSampling rejectionSampling = new RejectionSampling();
        LikelihoodWeighting likelihoodWeighting = new LikelihoodWeighting();
        XMLBIFParser x = new XMLBIFParser();

        try {
            int sampleSize = Integer.parseInt(args[0]);
            bn.core.BayesianNetwork network = x.readNetworkFromFile(args[1]);
            Assignment assignment = new Assignment();
            for (int i = 3; i < args.length; i += 2) {
                assignment.put(network.getVariableByName(args[i]), new StringValue(args[i+1]));
            }
            bn.core.Distribution distributionRej = rejectionSampling.rejectionSampling(network.getVariableByName(args[2]),
                    assignment, network, sampleSize);
            bn.core.Distribution distributionLike = likelihoodWeighting.LikelihoodWeighting(network.getVariableByName(args[2]),
                    assignment, network, sampleSize);
            System.out.println("Rejection Sampling: " + distributionRej);
            System.out.println("Likelihood Weighting: " + distributionLike);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
