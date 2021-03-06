package org.mcnichol;

import java.util.Locale;

/**
 * Printable locale is used to populate a readable name in ComboBox Dropdown
 */
public class PrintableLocale {
    private final Locale thisLocale;

    public PrintableLocale(Locale thisLocale) {
        this.thisLocale = thisLocale;
    }


    public Locale getThisLocale() {
        return thisLocale;
    }

    @Override
    public String toString() {
        return thisLocale.getDisplayLanguage();
    }
}
