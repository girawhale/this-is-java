package chap12.WaitNotifyDataBoxTest;

public class WaitNotifyExample {
    public static void main(String[] args) {
        DataBox dataBox = new DataBox();

        ProducerThread producerThread = new ProducerThread(dataBox);
        ConsumerThread consumerThread = new ConsumerThread(dataBox);

        producerThread.start();
        consumerThread.start();
    }
}
// 출력
//ProducerThread가 생성한 데이터 : Data-1
//Consumer Thread가 읽은 데이터 : Data-1
//ProducerThread가 생성한 데이터 : Data-2
//Consumer Thread가 읽은 데이터 : Data-2
//ProducerThread가 생성한 데이터 : Data-3
//Consumer Thread가 읽은 데이터 : Data-3