public class HelloWorld {
    public native void sayHello(String name);

    static {
        System.loadLibrary("dawg-java");
    }

    public static void main(String[] args) {
        new HelloWorld().sayHello("Kien");
    }
}