using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld
{
    abstract class Human
    {
        private String name = "Test";
        private int age;
        public static int count;

        public Human()
        {
            count++;
        }

        public void eat()
        {
            Console.WriteLine($"{name} is eating");
        }
    }

    class Man : Human 
    { 
        public Man()
        {

        }
    }
}
