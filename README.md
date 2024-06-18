See the [TASK](./TASK.md) file for instructions.

Please document your solution here...
-------
# Documentation

### How to run the service.

Run the following command inside your application’s root directory:

```
mvn clean package
```
The executable JAR file is now available in the target directory and we may start up the application by executing the following command on the command line:

```
 java -jar target/trade-enrichment-service-0.0.1-SNAPSHOT.jar
```
### How to use the API.

Sample HTTP Request command:
```
curl -F 'file=@src/test/resources/trade.csv' http://localhost:8080/api/v1/enrich
```

### Any limitations of the code.


### Any discussion/comment on the design.

Commons-csv might not be the best choice, I suppose. The code is not very clean. It is difficult to separate CSV parsing logic from business logic and validation.

It is possible to parallelize the processing. However, even in this non-optimized form, the processing takes a couple of seconds. It might be redundant.

### Any ideas for improvement if there were more time available.
1. Logger should be asynchronous.
2. Use a different library for working with CSV or avoid it altogether. This would simplify `DataPreparationService`.
3. Code requires a lot of different validations.
4. `DateUtil` uses default date parsing logic, which could be optimized. Maybe add caching for validation results.
5. Date validation and retrieving product names can be done in parallel for optimization.
6. It is possible to optimize parsing by splitting the trade string into three parts and replacing `product_id` with `product_name`.
7. Add SpringDoc for the controller.
8. If-else checks can be replaced with implementations via AOP or validation. However, given the size of the service, this seems redundant.
9. There are not enough tests to cover all edge cases and invalid data in the CSV.
10.  For `DataPreparationService`, JavaDoc should be written.

