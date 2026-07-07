using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;
public class Transaction
{
    [Key]  
    public Guid Id { get; set; }
    public Guid FromAccountId {get; set; }
    public Guid ToAccountId {get; set; }

    [Precision(18, 2)]
    public decimal Amount {get; set;}

    public DateTime Timestamp { get; set; }

    public Transaction() {}

    public Transaction(Guid fromAccountId, Guid toAccountId, decimal amount)
    {
        Id = Guid.NewGuid();
        FromAccountId = fromAccountId;
        ToAccountId = toAccountId;
        Amount = amount;
        Timestamp = DateTime.UtcNow;
    }
}