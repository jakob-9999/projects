
class Program
{
    public static void Main(string[] args)
    {
        BankContext _db = new BankContext();
        Bank bank = new Bank(_db);

        while (true)
        {
            Console.WriteLine("Choose from the menu");
            Console.WriteLine("1. Create account");
            Console.WriteLine("2. Deposit");
            Console.WriteLine("3. Withdraw");
            Console.WriteLine("4. Transfer");
            Console.WriteLine("5. Show accounts");
            Console.WriteLine("6. Show transactions");
            Console.WriteLine("7. Exit");

            Console.Write("Choose: ");
            string choice = Console.ReadLine();

            switch (choice)
            {
                case "1":
                Console.Write("Enter name: ");
                string name = Console.ReadLine();
                bank.CreateAccount(name);
                break;

                case "2":
                Console.WriteLine("Enter account id: ");

                Guid id;
                while (!Guid.TryParse(Console.ReadLine(), out id))
                    {
                        Console.WriteLine("Invalid id, try again");
                    }

                Console.WriteLine("Enter deposit amount: ");

                decimal deposit;
                while (!decimal.TryParse(Console.ReadLine(), out deposit))
                    {
                        Console.WriteLine("invalid amount, try again");
                    }

                bank.Deposit(id, deposit);
                break;

                case "3":
                Console.WriteLine("Enter account id: ");

                Guid accountId;
                while (!Guid.TryParse(Console.ReadLine(), out accountId))
                    {
                        Console.WriteLine("Invalid id, try again");
                    }

                Console.WriteLine("Enter withdrawal amount: ");

                decimal withdrawal;
                while (!decimal.TryParse(Console.ReadLine(), out withdrawal))
                    {
                        Console.WriteLine("invalid amount, try again");
                    }
                bank.Withdraw(accountId, withdrawal);
                break;

                case "4":
                Console.WriteLine("Enter withdrawal account id: ");

                Guid withdrawalAccountId;
                while (!Guid.TryParse(Console.ReadLine(), out withdrawalAccountId))
                    {
                        Console.WriteLine("Invalid withdrawal account id, try again");
                    }

                Console.WriteLine("Enter deposit account id: ");

                Guid depositAccountId;
                while (!Guid.TryParse(Console.ReadLine(), out depositAccountId))
                    {
                        Console.WriteLine("Invalid deposit account id, try again");
                    }

                Console.WriteLine("Enter transfer amount: ");

                decimal transferAmount;
                while (!decimal.TryParse(Console.ReadLine(), out transferAmount))
                    {
                        Console.WriteLine("invalid amount, try again");
                    }

                bank.Transfer(withdrawalAccountId, depositAccountId, transferAmount);
                break;

                case "5":
                bank.ShowAccounts();
                break;

                case "6":
                bank.ShowTransactions();
                break;

                case "7":
                return;

                default:
                Console.WriteLine("Not valid");
                break;
            }
        }
        
    }
}
