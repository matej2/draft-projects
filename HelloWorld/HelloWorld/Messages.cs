using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld
{
    class Human
    {
        private String name = "Test";
        private int age;

        public void eat()
        {
            Console.WriteLine($"{name} is eating");
        }
    }
}
