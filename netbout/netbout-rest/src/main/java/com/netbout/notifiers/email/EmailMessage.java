/**
 * Copyright (c) 2009-2011, netBout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are PROHIBITED without prior written permission from
 * the author. This product may NOT be used anywhere and on any computer
 * except the server platform of netBout Inc. located at www.netbout.com.
 * Federal copyright law prohibits unauthorized reproduction by any means
 * and imposes fines up to $25,000 for violation. If you received
 * this code occasionally and without intent to use it, please report this
 * incident to the author by email.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.netbout.notifiers.email;

import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 * One email message.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
final class EmailMessage {

    /**
     * End of line to use.
     */
    private static final String EOL = "\n";

    /**
     * Stoppers of content.
     * @checkstyle LineLength (7 lines)
     */
    private static final Pattern[] STOPPERS = new Pattern[] {
        Pattern.compile("from:.*"),
        Pattern.compile(".*<.*@.*>"),
        Pattern.compile("-+original\\s+message-+"),
        Pattern.compile("On \\w{3}, \\w{3} \\d{1,2}, \\d{4} at \\d{2}:\\d{2} (AM|PM), .* wrote:"),
    };

    /**
     * The message.
     */
    private final transient Message message;

    /**
     * Public ctor.
     * @param msg The msg
     */
    public EmailMessage(final Message msg) {
        this.message = msg;
    }

    /**
     * Get its visible part.
     * @return The text of it
     * @throws MessageParsingException If some problem inside
     */
    public String text() throws MessageParsingException {
        final StringBuilder text = new StringBuilder();
        for (String line
            : StringUtils.splitPreserveAllTokens(this.raw(), this.EOL)) {
            final String polished = line.trim().replaceAll("[\\s\r\t]+", " ");
            boolean found = false;
            for (Pattern pattern : this.STOPPERS) {
                if (pattern.matcher(polished).matches()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
            text.append(polished).append(this.EOL);
        }
        return text.toString().trim();
    }

    /**
     * Get raw text.
     * @return The text of it
     * @throws MessageParsingException If some problem inside
     */
    private String raw() throws MessageParsingException {
        try {
            final Object body = this.message.getContent();
            if (!(body instanceof Multipart)) {
                throw new MessageParsingException("body is not Multipart");
            }
            final Multipart parts = (Multipart) body;
            for (int pos = 0; pos < parts.getCount(); pos += 1) {
                final BodyPart part = parts.getBodyPart(pos);
                if (part.getContentType().startsWith(MediaType.TEXT_PLAIN)) {
                    return part.getContent().toString();
                }
            }
        } catch (java.io.IOException ex) {
            throw new MessageParsingException(ex);
        } catch (javax.mail.MessagingException ex) {
            throw new MessageParsingException(ex);
        }
        throw new MessageParsingException("no plain/text part found");
    }

}
