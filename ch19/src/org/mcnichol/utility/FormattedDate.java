package org.mcnichol.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattedDate extends Date{
    public FormattedDate(Date from) {
        super(from.getTime());
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this);
    }
}
