package chap16.collect;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingByExample {
    public static void main(String[] args) {
        List<Student> totalList = Student.totalList();

        Map<Student.Sex, List<Student>> mapBySex = totalList.stream()
                .collect(Collectors.groupingBy(Student::getSex));

        System.out.println(mapBySex);

        System.out.println("[남학생]");
        mapBySex.get(Student.Sex.MALE).stream()
                .forEach(s -> System.out.println(s.getName()));

        System.out.println();
        System.out.println("[여학생]");
        mapBySex.get(Student.Sex.FEMALE).stream()
                .forEach(s -> System.out.println(s.getName()));


        System.out.println("===============================");

        Map<Student.City, List<String>> mapByCity = totalList.stream()
                .collect(Collectors.groupingBy(
                        Student::getCity, // city로 매핑하는 function을 key로
                        Collectors.mapping(Student::getName, Collectors.toList()) // 그룹핑된 이름 List가 값인 Map을 생성하는 Collector
                ));

        System.out.println("[서울]");
        mapByCity.get(Student.City.Seoul).stream().forEach(System.out::println);

        System.out.println();
        System.out.println("[부산]");
        mapByCity.get(Student.City.Pusan).stream().forEach(System.out::println);
    }
}
