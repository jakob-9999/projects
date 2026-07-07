using Microsoft.EntityFrameworkCore;

public class BankContext : DbContext
{
    public DbSet<Account> Accounts { get; set; }
    public DbSet<Transaction> Transactions { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder options)
    {
        var password = Environment.GetEnvironmentVariable("SQL_PASSWORD")
            ?? throw new Exception("Missing SQL_PASSWORD env variable");

        var connectionString =
            $"Server=localhost,1433;Database=MiniBankDB;User Id=sa;Password={password};TrustServerCertificate=True;";

        options.UseSqlServer(connectionString);
    }
}