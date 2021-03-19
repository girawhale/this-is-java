package chap16.collect;

import java.util.List;

public class MaleStudentExample {
    public static void main(String[] args) {
        List<Student> totalList = Student.totalList();

//        MaleStudent maleStudent = totalList.stream()
//                .filter(s -> s.getSex() == Student.Sex.MALE)
//                .collect(MaleStudent::new, MaleStudent::accumulate, MaleStudent::combine);
//
//        maleStudent.getList().stream().forEach(s -> System.out.println(s.getName()));

        MaleStudent maleStudent = totalList.parallelStream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(MaleStudent::new, MaleStudent::accumulate, MaleStudent::combine);

        maleStudent.getList().stream().forEach(s -> System.out.println(s.getName()));

    }
}

//[main] : MaleStudent()
//[main] : accumulate()
//[main] : accumulate()
//홍길동
//신용권
//[main] : MaleStudent()
//[main] : accumulate()
//[main] : MaleStudent()
//[main] : combine()
//[ForkJoinPool.commonPool-worker-3] : MaleStudent()
//[ForkJoinPool.commonPool-worker-5] : MaleStudent()
//[ForkJoinPool.commonPool-worker-5] : accumulate()
//[ForkJoinPool.commonPool-worker-5] : combine()
//[ForkJoinPool.commonPool-worker-5] : combine()
//홍길동
//신용권