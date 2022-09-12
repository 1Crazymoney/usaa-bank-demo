# Credit Card Account Demo in Java

This demo application simulates a simplified Credit Card Management Service.

It contains three primary features:

- Add customers by `name`
- Create credit card accounts for those customers with whole-number credit limits
- Process transactions that reduce the available credit

Additionally, it performs basic error handling such as:

- Customers require a name
- Credit Card Accounts must be attached to an existing customer account
- Credit Card Accounts must have credit limits between $500-$50,000
- Transactions will not proceed if they exceed available remaining credit

## To Install and Run Locally
```bash
git clone git@github.com:joshfreemanIO/usaa-bank-demo.git
cd ./usaa-bank-demo

# Requires Java 18
./mvnw spring-boot:run
```
