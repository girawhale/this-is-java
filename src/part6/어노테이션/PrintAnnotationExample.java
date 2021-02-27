package part6.어노테이션;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrintAnnotationExample {
    public static void main(String[] args) {
        Method[] declaredMethods = Service.class.getDeclaredMethods(); // Service 에 선언된 메소드 얻기

        for (Method method : declaredMethods) { // 메소드 객체 하나씩
            if (method.isAnnotationPresent(PrintAnnotation.class)) { // PrintAnnotation 적용 확인
                PrintAnnotation printAnnotation = method.getAnnotation(PrintAnnotation.class); // 에노테이션 객체 얻기

                System.out.printf("[%s]\n", method.getName()); // 메소드 이름 출력
                System.out.println(printAnnotation.value().repeat(printAnnotation.number())); // 구분선 출력

                try {
                    method.invoke(new Service()); // 메소드 호출
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/*
출력

[method2]
***************
실행 내용 2
[method3]
####################
실행 내용 3
[method1]
---------------
실행 내용 1
*/
