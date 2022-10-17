package com.example.minecraftAI;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {

    private int inputDepth;
    private int outputDepth;
    private int[] hiddenLayerDepth;

    // accessed through network[layer][node][info(0)/weights(1)][bias(0)/currentvalue(1)/weightnum(n)]
    private double[][][][] network;

    public NeuralNetwork(int inputDepth, int outputDepth, int hiddenLayers, int[] hiddenLayerDepth) {

        // if the hidden layer depth has no data, assume depth is 10
        if (hiddenLayerDepth.length == 0) {
            hiddenLayerDepth = new int[0];
            hiddenLayerDepth[0] = 10;
        } 
        
        // if the hidden layer depth is not the right size, assume all the same depth layers
        if (hiddenLayerDepth.length != hiddenLayers) {
            int depth = hiddenLayerDepth[0];
            hiddenLayerDepth = new int[hiddenLayers];
            Arrays.fill(hiddenLayerDepth, depth);
        }

        // set the attributes 
        this.inputDepth = inputDepth;
        this.outputDepth = outputDepth;
        this.hiddenLayerDepth = hiddenLayerDepth;
        
        // create the blank neural network
        this.network = initNeuralNetwork();
        randomiseNetworkWeights();

    }

    public double[] runNetwork(double[] inputs) {

        // set the inputs
        for (int i = 0; i < inputs.length; i++) {
            network[0][i][0][1] = inputs[i];
        }

        // compute each network layer
        for (int layer = 0; layer < network.length; layer++) {
            computeNetworkLayer(layer);
        }

        return getOutputs();
    }
    
    public double[] getOutputs(){

        // return the outputs (processed into a 1d array)
        double[] outputs = new double[outputDepth];
        for (int i = 0; i < outputDepth; i++) {
            outputs[i] = network[network.length - 1][i][0][1];
        }

        return outputs;
    }
    
    private double[][][][] initNeuralNetwork() {

        // make a new array called layerDepth holding all of the combined (input, hidden, output) depths to use with iteration
        int[] layerDepth = new int[hiddenLayerDepth.length + 2];
        layerDepth[0] = inputDepth;
        layerDepth[layerDepth.length - 1] = outputDepth;
        for (int i = 0; i < hiddenLayerDepth.length; i++) {
            layerDepth[i + 1] = hiddenLayerDepth[i];
        }


        // accessed through network[layer][node][info(0)/weights(1)][bias(0)/currentvalue(1)/weightnum(n)]
        double[][][][] network = new double[layerDepth.length][][][];

        // iterate through the layer depths, adding in each layer (discluding the final layer)
        for (int layer = 0; layer < layerDepth.length - 1; layer++) {
            
            // each node of the current layer contains weights that go to the following layers 
            double[][][] layerArray = new double[layerDepth[layer]][2][2];
            for (int i = 0; i < layerArray.length; i++) {
                layerArray[i][1] = new double[layerDepth[layer + 1]];
            }

            network[layer] = layerArray;
        }

        // set the output array size 
        network[network.length - 1] = new double[layerDepth[network.length -1]][2][2];

        return network;

    }

    private void computeNetworkLayer(int layer) {
        if (layer == 0) {return;}

        for (int i = 0; i < network[layer].length; i++) {
            double total = 0;

            // for each node in the prevous layer of the network, times the weight for the current node by their holding value
            for (int node = 0; node < network[layer - 1].length; node++) {
                total += network[layer - 1][node][1][i] * network[layer - 1][node][0][1];
            }

            // add the current node's bias and sigmoid the final new value
            total += network[layer][i][0][0];
            total = sigmoid(total);

            // set the holding value for the current node
            network[layer][i][0][1] = total;

        }
    }
    
    // provide default optional values for randomiseNetworkWeights
    private void randomiseNetworkWeights() { randomiseNetworkWeights(-1, 1); }

    private void randomiseNetworkWeights(int min, int max) {
        Random rand = new Random();
        
        // iterate through all of the layers of the network (except for output)
        for (int layer = 0; layer < network.length - 1; layer++) {

            // for each weight within each node, randomise the value within the min and the max
            for (int node = 0; node < network[layer].length; node++) {
                for (int weight = 0; weight < network[layer][node].length; weight++) {
                    network[layer][node][1][weight] = rand.nextDouble(max - min) + min;
                }
            }
            
        }
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

}
