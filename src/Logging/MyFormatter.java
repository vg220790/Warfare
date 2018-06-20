package Logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringBuffer buf = new StringBuffer(1000);
        buf.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        buf.append(" ");
        buf.append(record.getLevel());
        buf.append("\t-->\t");
        buf.append(formatMessage(record));
        buf.append("\n");
        return buf.toString();
    }
}
