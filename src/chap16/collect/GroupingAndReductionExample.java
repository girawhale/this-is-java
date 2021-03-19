package chap16.collect;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingAndReductionExample {
    public static void main(String[] args) {
        List<Student> totalList = Student.totalList();

        Map<Student.Sex, Double> mapBySex = totalList.stream()
                .collect(Collectors.groupingBy(
                        Student::getSex,
                        Collectors.averagingDouble(Student::getScore)
                ));

        System.out.println(mapBySex);

        Map<Student.Sex, String> mapByName = totalList.stream()
                .collect(
                        Collectors.groupingBy(
                                Student::getSex,
                                Collectors.mapping(
                                        Student::getName,
                                        Collectors.joining(",")
                                )
                        )
                );

        System.out.println(mapByName);
    }
}
