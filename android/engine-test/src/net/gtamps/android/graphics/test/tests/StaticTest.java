package net.gtamps.android.graphics.test.tests;

public class StaticTest {

    static class A {

        public static UniquePerClassType s;

        public A() {
            if (s == null) {
                s = new UniquePerClassType("A");
                System.out.println("new A");
            }
        }

        public String toString() {
            return s.name;
        }

    }

    static class B extends A {

        public static UniquePerClassType s;

        public B() {
            if (s == null) {
                s = new UniquePerClassType("B");
                System.out.println("new B");
            }
        }

        public String toString() {
            return s.name;
        }
    }

    static class UniquePerClassType {

        public String name;

        public UniquePerClassType(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        new A();
        new B();
        new A();
        new B();
        new A();
        new B();
        new A();
        new B();
    }
}