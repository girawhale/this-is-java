package chap12.WaitNotifyDataBoxTest;

public class DataBox {
    private String data;

    public synchronized String getData() {
        if (this.data == null) { //data가 null이면 소비자를 일시정지
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String returnVal = data;
        System.out.println("Consumer Thread가 읽은 데이터 : " + returnVal);
        data = null; // data필드를 null로 만들고 생산자를 실행 대기
        notify();
        return returnVal;
    }

    public synchronized void setData(String data) {
        if (this.data != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.data = data;
        System.out.println("ProducerThread가 생성한 데이터 : " + data);
        notify();
    }
}
