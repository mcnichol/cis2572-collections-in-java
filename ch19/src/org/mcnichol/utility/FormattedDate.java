package org.mcnichol.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>FormattedDate</h1>
 * This class extends the {@link Date} to override toString and display a narrower pattern of yyyy-MM-dd for UI
 * <p>
 *
 * @author Michael McNichol
 * @version 1.0
 * @since 2021/02/21
 */
public class FormattedDate extends Date {
    public FormattedDate(Date from) {
        super(from.getTime());
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this);
    }
}
