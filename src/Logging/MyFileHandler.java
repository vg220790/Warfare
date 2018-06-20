package Logging;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

public class MyFileHandler extends FileHandler{
    private transient String pattern;
    private String sourceClassName;
    public MyFileHandler(String pattern, String sourceClassName) throws IOException, SecurityException {
        super(pattern);
        this.pattern = pattern;
        this.sourceClassName = sourceClassName;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        if(sourceClassName.equals(record.getSourceClassName()))
            super.publish(record);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyFileHandler that = (MyFileHandler) o;
        return Objects.equals(pattern, that.pattern);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pattern);
    }
}
