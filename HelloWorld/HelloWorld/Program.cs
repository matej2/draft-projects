
byte number2 = 2;

int count = 10;
float totalPrice = 20.95F;
char character = 'A';
string firstName = "Janez";
bool isWorking = true;

Console.WriteLine("Hello, World!");
Console.WriteLine(totalPrice);
Console.WriteLine(character);
Console.WriteLine(firstName);
Console.WriteLine(isWorking);

Console.WriteLine("{0} {1}", byte.MinValue, byte.MaxValue);


Console.WriteLine("Enter values for a and b:");
int a = Convert.ToInt32(Console.ReadLine());
int b = Convert.ToInt32(Console.ReadLine());

double result = Math.Sqrt(Math.Pow(a, 2) + Math.Pow(b, 2));

Console.WriteLine("Result: {0}", result);

Random random = new Random();


int min = 1;
int max = 100;

int guess;
int number;
int guesses;

guess = 0;
guesses = 0;
number = random.Next(min, max);

while (guess != number)
{
    Console.WriteLine("Guess the number between {0} and {1}", min, max);
    guess = Convert.ToInt32(Console.ReadLine());
    Console.WriteLine("Guess: {0}", guess);

    if (guess > number)
    {
        Console.WriteLine("Guess is too high");
    } else
    {
        Console.WriteLine("Guess is too low");
    }
    guesses++;
}

Console.WriteLine("You win. Number: {0}, num of guesses: {1}", number, guesses);
