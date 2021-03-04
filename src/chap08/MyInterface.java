package chap08;

public interface MyInterface {
// 필드 선언 불가
//    int field;

    // 상수(초기값 반드시 필요)
    int constant = 10;

// 생성자 선언 불가
//    public MyInterface(){
//    }

    void abstractMethod();

    // 디폴트 메소드
    default void method() {
    }

    // 정적 메소드
    static void staticMethod() {
    }

}
