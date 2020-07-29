package MyJunit;

public class Test {
    public static void main(String[] args) {
        new B();
    }
}

class A<T>{
    public A(){
        //this是谁？A还是B？
        Class clazz = this.getClass();
        System.out.println(clazz.getName());
    }
}

class B extends A<String>{

}
