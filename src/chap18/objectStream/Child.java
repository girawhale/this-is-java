package chap18.objectStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Child extends Parent implements Serializable {
    public String childField;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(parentField);
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        parentField = in.readUTF();
        in.defaultReadObject();
    }
}
