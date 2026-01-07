using System;
using System.Collections.Generic;
using System.Text;

namespace HelloWorld
{
    abstract class Human
    {
        protected String name;
        private int age;
        public static int count;

        public Human()
        {
            count++;
        }

        public virtual void eat()
        {
            Console.WriteLine($"{name} is eating");
        }


    }

    class Man : Human 
    { 
        public Man(string name)
        {
            this.name = name;
        }
        public static Human clone(String name)
        {
            return new Man(name);
        }

        public override void eat()
        {
            base.eat();
            Console.WriteLine("Override eating");
        }

        public override string ToString()
        {
            return $"Human with name {name}";
        }
    }
}
