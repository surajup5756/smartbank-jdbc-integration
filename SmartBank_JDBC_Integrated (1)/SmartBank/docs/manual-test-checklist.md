# SmartBank — Manual Test Checklist

Use this checklist during the console demo. Record the actual result and mark Pass/Fail as you test.

| ID | Test | Steps | Expected result | Actual result |
|---|---|---|---|---|
| TC-01 | Create Savings Account | Create one with opening balance equal to minimum | Account is created and receives an account number | |
| TC-02 | Savings opening balance too low | Enter opening balance below minimum | Account is not created | |
| TC-03 | Create Current Account | Create one with a valid overdraft limit | Account is created | |
| TC-04 | Positive deposit | Deposit a positive amount | Balance increases by that amount | |
| TC-05 | Invalid deposit | Enter zero or a negative amount | Request is rejected | |
| TC-06 | Savings minimum balance | Withdraw an amount that would cross the minimum | Request is rejected; balance stays unchanged | |
| TC-07 | Current overdraft limit | Withdraw beyond the permitted overdraft | Request is rejected; balance stays unchanged | |
| TC-08 | Valid transfer | Transfer between two active accounts | Sender is debited and receiver is credited | |
| TC-09 | Missing destination | Enter an account number that does not exist | Transfer is rejected; sender is not debited | |
| TC-10 | Same-account transfer | Use the same number for source and destination | Transfer is rejected | |
| TC-11 | Blocked account | Block an account and attempt a deposit or transfer | Transaction is rejected | |
| TC-12 | Close account with funds | Try to close an account with a non-zero balance | Closing is rejected | |
| TC-13 | Transaction history | Complete a deposit, withdrawal and transfer | Entries show type, date, amount and resulting balance | |
| TC-14 | CSV export | Export an account statement | CSV appears in the `reports` folder | |
| TC-15 | Non-numeric menu input | Enter text instead of a menu number | Program shows an invalid option and continues | |
