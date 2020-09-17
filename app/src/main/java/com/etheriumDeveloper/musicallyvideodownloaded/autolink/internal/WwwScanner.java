package com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal;


import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkSpan;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkType;

public class WwwScanner implements Scanner {
    public LinkSpan scan(CharSequence input, int triggerIndex, int rewindIndex) {
        int afterDot = triggerIndex + 4;
        if (afterDot >= input.length() || !isWww(input, triggerIndex)) {
            return null;
        }
        int first = findFirst(input, triggerIndex, rewindIndex);
        if (first == -1) {
            return null;
        }
        int last = findLast(input, afterDot);
        if (last != -1) {
            return new LinkSpanImpl(LinkType.WWW, first, last + 1);
        }
        return null;
    }

    private static int findFirst(CharSequence input, int beginIndex, int rewindIndex) {
        return (beginIndex == rewindIndex || isAllowed(input.charAt(beginIndex - 1))) ? beginIndex : -1;
    }

    private static int findLast(CharSequence input, int beginIndex) {
        int last = Scanners.findUrlEnd(input, beginIndex);
        if (last == -1) {
            return -1;
        }
        int pointer = last;
        while (true) {
            pointer--;
            if (pointer <= beginIndex) {
                return -1;
            }
            if (input.charAt(pointer) == '.' && pointer > beginIndex) {
                return last;
            }
        }
    }

    private static boolean isAllowed(char c) {
        return (c == '.' || Scanners.isAlnum(c)) ? false : true;
    }

    private static boolean isWww(CharSequence input, int triggerIndex) {
        return input.charAt(triggerIndex + 1) == 'w' && input.charAt(triggerIndex + 2) == 'w' && input.charAt(triggerIndex + 3) == '.';
    }
}
