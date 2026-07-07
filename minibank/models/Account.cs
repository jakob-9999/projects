using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;
public class Account
{
    [Key]
    public Guid Id {get; set; }
    public string Owner {get; set; } = "";
    
    [Precision(18, 2)]
    public decimal Balance {get; set; }

    public object LockObject { get; } = new();

    public Account() {}

    public Account (string owner) 
    {
        Id = Guid.NewGuid();
        Owner = owner;
        Balance = 0;
    }
}