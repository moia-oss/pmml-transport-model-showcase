# Predictive Model Markup Language (PMML) for Transport Modeling

The predictive model markup language is an xml-based data format to document and exchange statistical models.
As such it separates model training from prediction and allows to train models with one software and employ them in another.
In addition, models can be persistently saved and documented, allowing them to be shared and re-used and may be published along with academic papers.

Many studies estimate elaborate models, such as for mode choice. 
Often, the model results are only presented in tables within the article itself, which makes it hard to 
reproduce and re-use trained models.

We see quite some benefits for standardizing these model outputs:
- Consistent output that can be consumed by subsequent models (i.e., use discrete choice model in subsequent traffic assignment/simulation models)
- Human-readable
- Make estimation and prediction independent / interchangeable
- Version control and documentation
- Publication of model pmmls along with papers

## PMML
Is a well-maintained and documented standard maintained by the data mining group (see https://dmg.org/ and https://dmg.org/pmml/v4-1/GeneralStructure.html) 
with members such as IBM and SAS, among others. While the standard exists since the 90s and supports a wide range of statistical models (regression, neural networks, etc..), the application within the transport modeling domain has been very limited.

Support for the pmml format exists in multiple software and programming languages:
- scala (https://github.com/autodeployai/pmml4s)
- java (https://github.com/jpmml)
- R (https://cran.r-project.org/web/packages/pmml/index.html, https://github.com/jpmml/r2pmml)
- python (https://github.com/maxkferg/python-pmml, https://pypi.org/project/sklearn-pmml-model/)



## Examples

### Multinomial Logit with apollo
This example shows how to use a multinomial logit model trained by the apollo R package
(https://www.apollochoicemodelling.com/index.html) by converting it into the pmml 
format and applying it for prediction in Java.

#### Training
The example is based on the MNL_SP example from the apollo project 
(https://www.apollochoicemodelling.com/examples.html).

As a prerequisite we need to install the r2pmml and apollo packages:
> install.packages("r2pmml")  
> install.packages("apollo")

In essence, we use the script provided on the website and use the _r2pmml_ package 
(https://github.com/jpmml/r2pmml) to translate the trained model into the .pmml format
>model = apollo_estimate(apollo_beta, apollo_fixed, apollo_probabilities, apollo_inputs)  
>r2pmml(model, "output/MNL_SP.pmml")

The script including the input and a run.sh can befound in the R/apollo/MNL_SP directory.

### Prediction
As an example of employing the resulting pmml file we use a Java program based on the jpmml 
library (https://github.com/jpmml/jpmml-evaluator). By doing that, we simply need to read in the pmml file
> Evaluator evaluator = new LoadingModelEvaluatorBuilder()   
>    .load(new File("./R/apollo/MNL_SP/.output/MNL_SP.pmml")).  
>    .build();

and provide arguments with the independent variables...

> Map<String, Object> arguments = new LinkedHashMap<>();   
>        // Choice availabilities 1 (available) or 0 (not available)  
>        arguments.put("av_car", 0.);  
>        arguments.put("av_bus", 1.);   
>        arguments.put("av_air", 1.);   
>        arguments.put("av_rail", 1.);   
>        arguments.put("time_car", 10.);       
>        ...   

.... to make a prediction:

> Map<String, ?> results = evaluator.evaluate(arguments);    
> decodedResults = EvaluatorUtil.decodeAll(results);    
> System.out.println("Model predictions:");    
> decodedResults.forEach((key, value) -> System.out.println(key + ": " + value));    


