package com.etheriumDeveloper.musicallyvideodownloaded.autolink;

public class Autolink {
    public static String renderLinks(CharSequence input, Iterable<LinkSpan> links, LinkRenderer linkRenderer) {
        StringBuilder sb = new StringBuilder(input.length() + 16);
        int lastIndex = 0;
        for (LinkSpan link : links) {
            sb.append(input, lastIndex, link.getBeginIndex());
            linkRenderer.render(link, input, sb);
            lastIndex = link.getEndIndex();
        }
        if (lastIndex < input.length()) {
            sb.append(input, lastIndex, input.length());
        }
        return sb.toString();
    }
}
