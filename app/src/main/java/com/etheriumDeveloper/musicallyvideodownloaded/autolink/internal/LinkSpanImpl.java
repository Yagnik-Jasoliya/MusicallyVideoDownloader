package com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal;

import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkSpan;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.LinkType;

public class LinkSpanImpl implements LinkSpan {
    private final int beginIndex;
    private final int endIndex;
    private final LinkType linkType;

    public LinkSpanImpl(LinkType linkType, int beginIndex, int endIndex) {
        this.linkType = linkType;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    public LinkType getType() {
        return this.linkType;
    }

    public int getBeginIndex() {
        return this.beginIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public String toString() {
        return "Link{type=" + getType() + ", beginIndex=" + this.beginIndex + ", endIndex=" + this.endIndex + "}";
    }
}
