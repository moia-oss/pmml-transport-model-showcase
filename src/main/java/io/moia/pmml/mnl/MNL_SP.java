package io.moia.apollo;

import jakarta.xml.bind.JAXBException;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MNL_SP {

    public static void main(String[] args) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        // Building a model evaluator from a PMML file
        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                .load(new File("Apollo_MNL_SP.pmml"))
                .build();

        // Performing the self-check to validate the model
        evaluator.verify();

        // Printing input fields
        List<InputField> inputFields = evaluator.getInputFields();
        System.out.println("Input fields:");
        inputFields.forEach(field -> System.out.println(field.getName() + " (" + field.getDataType() + ")"));

        // Printing target (predicted) fields
        List<TargetField> targetFields = evaluator.getTargetFields();
        System.out.println("Target field(s):");
        targetFields.forEach(field -> System.out.println(field.getName() + " (" + field.getDataType() + ")"));

        // Printing output fields (additional outputs like probability, decision, etc.)
        List<OutputField> outputFields = evaluator.getOutputFields();
        System.out.println("Output fields:");
        outputFields.forEach(field -> System.out.println(field.getName() + " (" + field.getDataType() + ")"));

        Map<String, ?> decodedResults = null;
        //for (int i = 0; i < 1000000; i++) {

            // Here we simulate reading a record from a data source
            Map<String, Object> arguments = new LinkedHashMap<>();

            // Chocie availabilities -1 (vilable) or 0 (not avilable)
            arguments.put("av_car"          , 0.);
            arguments.put("av_bus"          , 1.);
            arguments.put("av_air"          , 1.);
            arguments.put("av_rail"         , 1.);
            arguments.put("time_car",       10.);
            arguments.put("cost_car",       10.);
            arguments.put("time_bus",       10.);
            arguments.put("access_bus",     10.);
            arguments.put("cost_bus",       10.);
            arguments.put("time_air",       10.);
            arguments.put("access_air",     10.);
            arguments.put("cost_air",       10.);
            arguments.put("service_air",    10.);
            arguments.put("time_rail",      10.);
            arguments.put("access_rail",    10.);
            arguments.put("cost_rail",      1.);
            arguments.put("service_rail",   10.);

            // Evaluating the model with the given input
            Map<String, ?> results = evaluator.evaluate(arguments);
            // Decoupling results from the JPMML-Evaluator runtime environment
            decodedResults = EvaluatorUtil.decodeAll(results);
        //}

        // Writing results to the console (simulating writing to a data sink)
        System.out.println("Model predictions:");
        decodedResults.forEach((key, value) -> System.out.println(key + ": " + value));

        // Making the model evaluator eligible for garbage collection
        evaluator = null;
    }
}
