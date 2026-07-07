
public class Bank
{
    private readonly BankContext _db;

    public Bank(BankContext db)
    {
        _db = db;
    }

    public Account CreateAccount(string name)
    {
        Account account = new Account(name);

        _db.Accounts.Add(account);
        _db.SaveChanges();

        return account;
    }

    public Account FindAccount(Guid id)
    {
        Account account = _db.Accounts.FirstOrDefault(a => a.Id == id);

        if (account == null)
        {
            throw new InvalidOperationException("Account not found");
        }

        return account;
    }

    public void Deposit(Guid id, decimal amount)
    {
        if (amount <= 0)
        {
            throw new ArgumentException("Amount must be positive");
        }
        
        Account account = FindAccount(id);

        lock(account.LockObject)
        {
             account.Balance += amount;
            _db.SaveChanges();
        }
       
    }

    public void Withdraw(Guid id, decimal amount)
    {
        if (amount <= 0)
        {
            throw new ArgumentException("Amount must be positive");
        }

        Account account = FindAccount(id);

        lock(account.LockObject)
        {
           if (account.Balance < amount)
                {
                    throw new InvalidOperationException("Insufficient funds");
                } 
            account.Balance -= amount;
            _db.SaveChanges();
        }
    }

    public void Transfer(Guid fromId, Guid toId, decimal amount)
    {
        // using means clean up automatically after block has been executed
        using var transaction = _db.Database.BeginTransaction();

        if (fromId == toId)
        {
            throw new ArgumentException("Withdrawal account id and deposit account id are the same");
        }

        try
        {
            Withdraw(fromId, amount);
            Deposit(toId, amount);

            _db.Transactions.Add(new Transaction(fromId, toId, amount));

            _db.SaveChanges();
            transaction.Commit();
        } 
        catch
        {
            transaction.Rollback();
            throw;
        }
    }

    public void ShowAccounts()
    {
        List<Account> accounts = _db.Accounts.ToList();
        
        if (accounts.Count == 0)
        {
            Console.WriteLine("No accounts");
        }

        for (int i = 0; i < accounts.Count; i++)
        {
            string owner = accounts[i].Owner;
            Guid id = accounts[i].Id;
            decimal balance = accounts[i].Balance;
            Console.WriteLine($"Account owner: {owner}. Account id: {id}. Balance: {balance}");
        }
    }

    public void ShowTransactions()
    {
        List<Transaction> transactions = _db.Transactions.ToList();

        if (transactions.Count == 0)
        {
            Console.WriteLine("No transactions");
        }
        
        for (int i = 0; i < transactions.Count; i++)
        {
            Guid transactionId = transactions[i].Id;
            Guid fromAccount = transactions[i].FromAccountId;
            Guid toAccount = transactions[i].ToAccountId;
            decimal amount = transactions[i].Amount;

            Console.WriteLine($"Transaction id: {transactionId}. From account id: {fromAccount}. To account id: {toAccount}. Amount: {amount}");
        }
    }
}