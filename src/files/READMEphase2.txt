Hi! Welcome to the Phase 2 portion of our java project!

To properly view it please click on the gear and uncheck
"Compact Middle Package". After that, please right click the Java folder and select that to be the
source root.

To personalise our project, we decided to create a User Interface! In our files, I have included
a controller UML diagram and a model UML diagram! Please look at the controller.png  and model.png
 for those!

There is a workers.txt file that includes the employee user names and passwords!

To login as Bank Manager, please enter bm as the username, 123 for the password and then press
Bank login.

To login as Bank Advisor, please enter ba1 as the username, 123 for the password and then press
Bank login.

You are able to create new customers as Bank manager and then you can log in as those customers
and request accounts!

We currently have a few existing customers so if you want to log in as those you can as well!
To do this, you can type in c1 for the username, 123 for the password then customer login.

Please use the following formats for all our txt files! We are using \t (tab)
so please do not use space! None of the files should be blank!

"phase2/src/files/customers.txt";
CustomerNumber \t CustomerUserName \t CustomerPassword \t SIN Number

"phase2/src/files/workers.txt";
EmployeeID /t Jobtitle /t Password /t PositionofEmployee /t SIN Number

"phase2/src/files/accounts.txt";
AccountID \t CustomerNumber \t AvailableBalance \t Defaulttime (yyyy-MM-ddTHH:mm:ss) \t Account Type \t Is it the Primary account? (true or false) \t RecentTransactionID \t Is this a JointAccount? (True or false) \t CustomerID for second account holder

"phase2/src/files/transactions.txt";
Transaction ID \t Customer Number\t Transaction Type \t Transaction Amount \t Account Number \t Transaction Date (yyyy-MM-ddTHH:mm:ss) \t Second account/customer
involved in the transaction \t isReversingEntry (True or False)
"phase2/src/files/outgoing.txt";
TransactionID \t TransactionType(always "Paybill") \t AccountNumber \t Amount \t TransactionDate (yyyy-MM-dd HH:mm:ss) \t Payee Number

"phase2/src/files/accountRequest.txt";
Customer Number \t Account type \t LocalDateTime

The deposit file needs to be manually created! After making it with the following format
please log in as Bank Manager or Bank Advisor and use the process deposits button!
"phase2/src/files/deposits.txt";
AccountNumber \t CustomerNumber \t Amount

"phase2/src/files/appointments.txt";
EmployeeID \t CustomerNumber \t Default time (yyyy-MM-ddTHH:mm:ss) \t String ( Why the appointment was created)