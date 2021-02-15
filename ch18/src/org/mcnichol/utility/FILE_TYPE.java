package org.mcnichol.utility;

import org.mcnichol.concurrency.counter.*;

/**
 * Utility File for determining file-type and providing increment typing functionality
 */
public enum FILE_TYPE {

    DOCUMENT, IMAGE, MUSIC, OTHER, VIDEO;


    public static FILE_TYPE getType(String fileType) {

        switch (fileType.toUpperCase()) {
            case "PDF":
            case "XML":
            case "TXT":
            case "DOC":
            case "DOCX":
            case "DJVU":
            case "EPUB":
            case "HTML":
            case "HTM":
            case "MOBI":
            case "RTF":
            case "TEX":
            case "XLS":
            case "XLSX":
                return DOCUMENT;
            case "JPEG":
            case "JPG":
            case "TIFF":
            case "BMP":
            case "PNG":
            case "WEBP":
            case "HDR":
            case "SVG":
                return IMAGE;
            case "AAC":
            case "AAX":
            case "AIFF":
            case "APE":
            case "DSS":
            case "FLAC":
            case "M4A":
            case "M4B":
            case "M4P":
            case "MP3":
            case "OGG":
            case "WAV":
            case "WMA":
                return MUSIC;
            case "WEBM":
            case "MKV":
            case "FLV":
            case "VOB":
            case "GIF":
            case "AVI":
            case "M2TS":
            case "TS":
            case "QT":
            case "MOV":
            case "WMV":
            case "MP4":
            case "M4V":
            case "MPG":
            case "MPEG":
            case "MP2":
            case "MPV":
            case "3GP":
            case "F4V":
                return VIDEO;
            default:
                return OTHER;
        }
    }

    public static void incrementType(FILE_TYPE type) {
        switch (type) {
            case DOCUMENT:
                DOCUMENT_COUNTER.count();
                break;
            case IMAGE:
                IMAGE_COUNTER.count();
                break;
            case MUSIC:
                MUSIC_COUNTER.count();
                break;
            case VIDEO:
                VIDEO_COUNTER.count();
                break;
            default:
                OTHER_COUNTER.count();
        }
    }
}
