### Instructions
- The repo source code (src) can be imported to IDE of choice.
- The main code is in the **Main.java** class and the function to return the final allocation is _**allocateFunds()**_. The deposit plans and deposits by customer are hardcoded with values from the assignent problem. The values can be changed by recompiling the project or simply use test cases to test with different values.
- The _**allocateFunds()**_ attempts to redistribute funds (from extra deposits) equally among available funds and if the extra deposits contains fractional value, it attempts to distribute it to the first portfolio.
- There is one test class _**FundAllocationTest**_ in the **test** folder (requires JUnit 5) which contain test cases. Different values can be changed in these test cases to test the functionality of the code.
