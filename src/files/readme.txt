Hi! Welcome to our Phase1 Project! To properly view it please click on the gear and uncheck
"Compact Middle Package". After that, please right click the Java folder and select that to be the
source root. I would also like to note that all customers need to have a primary chequing account for our
TransferToOther to work. This is a bug we will fix in Phase2. Sorry about that. :(

Please check the Design.pdf in our files folder for our UML design.

For transaction, Our BankManager is only able to undo the most recent transaction for all the accounts. For example,
if a Customer has a Savings and Chequing account the Bank Manager will not be able to undo the most recent transaction
in each. If a Customer deposited 10 dollars into Chequing then moved 40 dollars into Savings, the most recent
transaction will be the additional $40 dollars in the Savings account and that would be the transaction the
Bank Manager can undo. The Bank Manager can also remove any transaction with the UndoTransaction method
aslong as the trxID is inputed.

To log in as Bank Manager, please run ATM and enter 0 when prompted. The username and password to log in
will be provided in the workers.txt.

The ATM will have 100 of each bill everyday.

Please use the following formats for all our txt files! We are using \t (tab) so please do not use space!
None of the files should have any blank lines! We have also included samples of each txt in the files folder,
except for deposit. The deposit.txt file  needs to be manually entered so we have included a sample below.

"phase1/src/files/customers.txt";
CustomerNumber \t CustomerUserName \t CustomerPassword

"phase1/src/files/workers.txt"; ( We created this file ourselves, and there is only Bank Manager (bm) in it right now)
EmployeeID /t Jobtitle /t Password /t PositionofEmployee

"phase1/src/files/accounts.txt";
AccountID \t CustomerNumber \t AvailableBalance \t Defaulttime (yyyy-MM-ddTHH:mm:ss) \t Account Type \t Is it the Primary account? (true or false) \t RecentTransactionID

"phase1/src/files/transactions.txt";
Transaction ID \t Customer Number\t Transaction Type \t Transaction Amount \t Account Number \t Transaction Date (yyyy-MM-ddTHH:mm:ss) \t Second account/customer
involved in the transaction \t isReversingEntry (True or False)
This column is for if the entry is reversed(undo) then it is True, otherwise it is false.For example,
This means if we add $100 to Deposit the reverse entry would be -100$ in Deposits.

"phase1/src/files/outgoing.txt";
TransactionID \t TransactionType(always "Paybill") \t AccountNumber \t Amount \t TransactionDate (yyyy-MM-dd HH:mm:ss) \t Payee Number


"phase1/src/files/accountRequest.txt";
Customer Number \t Account type \t LocalDateTime

"phase1/src/files/deposits.txt";
AccountNumber \t CustomerNumber \t Amount
Once the deposits.txt file has been created, please log in as bm and press E to process the deposit file
Sample deposits File:
1\t1\t500

"phase1/src/files/alerts.txt";