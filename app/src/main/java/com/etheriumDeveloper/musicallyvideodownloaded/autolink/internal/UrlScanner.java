package com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal;


import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkSpan;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkType;

public class UrlScanner implements Scanner {
    public LinkSpan scan(CharSequence input, int triggerIndex, int rewindIndex) {
        int afterSlashSlash = triggerIndex + 3;
        if (afterSlashSlash >= input.length() || input.charAt(triggerIndex + 1) != '/' || input.charAt(triggerIndex + 2) != '/') {
            return null;
        }
        int first = findFirst(input, triggerIndex - 1, rewindIndex);
        if (first == -1) {
            return null;
        }
        int last = Scanners.findUrlEnd(input, afterSlashSlash);
        if (last != -1) {
            return new LinkSpanImpl(LinkType.URL, first, last + 1);
        }
        return null;
    }

    private int findFirst(CharSequence input, int beginIndex, int rewindIndex) {
        int first = -1;
        int digit = -1;
        for (int i = beginIndex; i >= rewindIndex; i--) {
            char c = input.charAt(i);
            if (Scanners.isAlpha(c)) {
                first = i;
            } else if (Scanners.isDigit(c)) {
                digit = i;
            } else if (!schemeSpecial(c)) {
                break;
            }
        }
        if (first <= 0 || first - 1 != digit) {
            return first;
        }
        return -1;
    }

    private static boolean schemeSpecial(char c) {
        switch (c) {
            case '+':
            case '-':
            case '.':
                return true;
            default:
                return false;
        }
    }
}
