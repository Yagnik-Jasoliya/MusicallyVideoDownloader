package com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal;


import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkSpan;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkType;

public class EmailScanner implements Scanner {
    private final boolean domainMustHaveDot;

    public EmailScanner(boolean domainMustHaveDot) {
        this.domainMustHaveDot = domainMustHaveDot;
    }

    public LinkSpan scan(CharSequence input, int triggerIndex, int rewindIndex) {
        int first = findFirst(input, triggerIndex - 1, rewindIndex);
        if (first == -1) {
            return null;
        }
        int last = findLast(input, triggerIndex + 1);
        if (last != -1) {
            return new LinkSpanImpl(LinkType.EMAIL, first, last + 1);
        }
        return null;
    }

    private int findFirst(CharSequence input, int beginIndex, int rewindIndex) {
        int first = -1;
        boolean atomBoundary = true;
        for (int i = beginIndex; i >= rewindIndex; i--) {
            char c = input.charAt(i);
            if (!localAtomAllowed(c)) {
                if (c != '.' || atomBoundary) {
                    break;
                }
                atomBoundary = true;
            } else {
                first = i;
                atomBoundary = false;
            }
        }
        return first;
    }

    private int findLast(CharSequence input, int beginIndex) {
        boolean firstInSubDomain = true;
        boolean canEndSubDomain = false;
        int firstDot = -1;
        int last = -1;
        for (int i = beginIndex; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!firstInSubDomain) {
                if (c != '.') {
                    if (c != '-') {
                        if (!subDomainAllowed(c)) {
                            break;
                        }
                        last = i;
                        canEndSubDomain = true;
                    } else {
                        canEndSubDomain = false;
                    }
                } else if (!canEndSubDomain) {
                    break;
                } else {
                    firstInSubDomain = true;
                    if (firstDot == -1) {
                        firstDot = i;
                    }
                }
            } else if (!subDomainAllowed(c)) {
                break;
            } else {
                last = i;
                firstInSubDomain = false;
                canEndSubDomain = true;
            }
        }
        if (!this.domainMustHaveDot) {
            return last;
        }
        if (firstDot == -1 || firstDot > last) {
            return -1;
        }
        return last;
    }

    private boolean localAtomAllowed(char c) {
        if (Scanners.isAlnum(c) || Scanners.isNonAscii(c)) {
            return true;
        }
        switch (c) {
            case '!':
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '*':
            case '+':
            case '-':
            case '/':
            case '=':
            case '?':
            case '^':
            case '_':
            case '`':
            case '{':
            case '|':
            case '}':
            case '~':
                return true;
            default:
                return false;
        }
    }

    private boolean subDomainAllowed(char c) {
        return Scanners.isAlnum(c) || Scanners.isNonAscii(c);
    }
}
