## Description
A Restful Web-API that calculates [IRR](https://en.wikipedia.org/wiki/Internal_rate_of_return) and [APR](https://en.wikipedia.org/wiki/Annual_percentage_rate) for a given loan, the loan installments as well as the interest fee does not have to be fixed amounts

## Running locally
```sbtshell
sbt reStart
```

After the server is up, use your favorite client to make a `POST` call to the endpoint
`http://localhost:8080/calculation` with a given loan

#### Caveats 
- You can use the input part of the example provided in `example.json`
- Paste the contents of `api.yaml` on https://editor.swagger.io for a visual representation of the API components

#### Disclaimer
It is important to mention that the method to calculate the `IRR` and `APR` are drawn completely from various online resources and mildly adapted to Scala from Java, which is obvious by the apparent non-idiomatic algorithm to calculate the `IRR`

## Developer guide 
The application uses `Http4s` as an http server, with its dependencies `cats`, `cats-effects`, `circe` and `specs2` for testing

Making use of the `IO` monad to encode the results of the computation as well as stack side effects and failures. 

The application follows an MVC-like paradigm, where it exists  

- An `http` layer handles the queries
- A `services` layer handles the computation
- A `models` layer to handle application models as well as `json` marshalling/unmarshalling
- Additional `utilities`, `Error`, `httpErrorHandler` does error transformation and handling and utlitiary functions/models  


## Testing
Unit tests were written for each of the functional components using `specs2` library 
  
To run the unit tests, type the following command in your terminal
```sbtshell
sbt test
```

## Future Improvements

- Better Logging modules and functional logging
- Scenario and integration tests
- Contribution guidelines and better architectural documentation 
- Performance analysis and JVM profiling
- Better, idiomatic approach to calculating `IRR` and `APR` instead of the existing one


