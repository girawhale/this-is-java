package part6.어노테이션.싱글톤;

public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton() {
    }

    static Singleton getInstance() {
        return instance;
    }
}
