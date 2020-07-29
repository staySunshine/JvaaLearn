package demo2;

public class RealSubject implements Subject {
    @Override
    public void hello(String str) {
        System.out.println("Hello  " + str);
    }
    @Override
    public String bye() {
        System.out.println("Goodbye");
        return "Over";
    }
}
