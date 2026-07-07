namespace Tests;

public class UnitTest1
{
    [Fact]
    public void Test1()
    {
        // CHANGE TO MOCKUP DB
        BankContext _testDb = new BankContext();
        Bank bank = new Bank(_testDb);
        Account account = bank.CreateAccount("Jakob");
        bank.Deposit(account.Id, 2000);

        bank.Withdraw(account.Id, 2);
        bank.Withdraw(account.Id, 2);
        bank.Withdraw(account.Id, 2);
        bank.Withdraw(account.Id, 2);
        bank.Withdraw(account.Id, 2);

        decimal balance = account.Balance;

        Console.WriteLine($"Balance: {balance}");
    }
}
