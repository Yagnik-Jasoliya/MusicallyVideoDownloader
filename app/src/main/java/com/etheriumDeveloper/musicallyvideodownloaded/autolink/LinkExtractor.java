package com.etheriumDeveloper.musicallyvideodownloaded.autolink;

import com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal.EmailScanner;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal.Scanner;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal.UrlScanner;
import com.etheriumDeveloper.musicallyvideodownloaded.autolink.internal.WwwScanner;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class LinkExtractor {
    private final Scanner emailScanner;
    private final Scanner urlScanner;
    private final Scanner wwwScanner;

    public static class Builder {
        private boolean emailDomainMustHaveDot;
        private Set<LinkType> linkTypes;

        private Builder() {
            this.linkTypes = EnumSet.allOf(LinkType.class);
            this.emailDomainMustHaveDot = true;
        }

        public Builder linkTypes(Set<LinkType> linkTypes) {
            if (linkTypes == null) {
                throw new NullPointerException("linkTypes must not be null");
            }
            this.linkTypes = new HashSet(linkTypes);
            return this;
        }

        public Builder emailDomainMustHaveDot(boolean emailDomainMustHaveDot) {
            this.emailDomainMustHaveDot = emailDomainMustHaveDot;
            return this;
        }

        public LinkExtractor build() {
            UrlScanner urlScanner;
            WwwScanner wwwScanner;
            EmailScanner emailScanner;
            if (this.linkTypes.contains(LinkType.URL)) {
                urlScanner = new UrlScanner();
            } else {
                urlScanner = null;
            }
            if (this.linkTypes.contains(LinkType.WWW)) {
                wwwScanner = new WwwScanner();
            } else {
                wwwScanner = null;
            }
            if (this.linkTypes.contains(LinkType.EMAIL)) {
                emailScanner = new EmailScanner(this.emailDomainMustHaveDot);
            } else {
                emailScanner = null;
            }
            return new LinkExtractor(urlScanner, wwwScanner, emailScanner);
        }
    }

    private class LinkIterator implements Iterator<LinkSpan> {
        private int index = 0;
        private final CharSequence input;
        private LinkSpan next = null;
        private int rewindIndex = 0;

        public LinkIterator(CharSequence input) {
            this.input = input;
        }

        public boolean hasNext() {
            setNext();
            return this.next != null;
        }

        public LinkSpan next() {
            if (hasNext()) {
                LinkSpan link = this.next;
                this.next = null;
                return link;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        private void setNext() {
            if (this.next == null) {
                int length = this.input.length();
                while (this.index < length) {
                    Scanner scanner = LinkExtractor.this.trigger(this.input.charAt(this.index));
                    if (scanner != null) {
                        LinkSpan link = scanner.scan(this.input, this.index, this.rewindIndex);
                        if (link != null) {
                            this.next = link;
                            this.index = link.getEndIndex();
                            this.rewindIndex = this.index;
                            return;
                        }
                        this.index++;
                    } else {
                        this.index++;
                    }
                }
            }
        }
    }

    private LinkExtractor(UrlScanner urlScanner, WwwScanner wwwScanner, EmailScanner emailScanner) {
        this.urlScanner = urlScanner;
        this.wwwScanner = wwwScanner;
        this.emailScanner = emailScanner;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Iterable<LinkSpan> extractLinks(final CharSequence input) {
        return new Iterable<LinkSpan>() {
            public Iterator<LinkSpan> iterator() {
                return new LinkIterator(input);
            }
        };
    }

    private Scanner trigger(char c) {
        switch (c) {
            case ':':
                return this.urlScanner;
            case '@':
                return this.emailScanner;
            case 'w':
                return this.wwwScanner;
            default:
                return null;
        }
    }
}
