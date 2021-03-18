package chap16.collect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ToListExample {
    public static void main(String[] args) {
        List<Student> totalList = Student.totalList();

        List<Student> maleList = totalList.stream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(Collectors.toList());

        maleList.stream().forEach(m -> System.out.println(m.getName()));

        Set<Student> femaleSet = totalList.stream()
                .filter(s -> s.getSex() == Student.Sex.FEMALE)
                .collect(Collectors.toCollection(HashSet::new));

        femaleSet.stream().forEach(f -> System.out.println(f.getName()));

    }
}
