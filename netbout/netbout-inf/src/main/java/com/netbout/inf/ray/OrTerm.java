/**
 * Copyright (c) 2009-2012, Netbout.com
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
package com.netbout.inf.ray;

import com.jcabi.log.Logger;
import com.netbout.inf.Cursor;
import com.netbout.inf.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * OR term.
 *
 * <p>The class is immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
final class OrTerm implements Term {

    /**
     * Index map.
     */
    private final transient IndexMap imap;

    /**
     * Terms.
     */
    private final transient Collection<Term> terms;

    /**
     * Public ctor.
     * @param map The index map
     * @param args Arguments (terms)
     */
    public OrTerm(final IndexMap map, final Collection<Term> args) {
        this.imap = map;
        this.terms = new ArrayList<Term>(args);
        if (this.terms.isEmpty()) {
            this.terms.add(new AlwaysTerm(this.imap));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder();
        text.append("(OR");
        for (Term term : this.terms) {
            text.append(' ').append(term);
        }
        text.append(')');
        return text.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor shift(final Cursor cursor) {
        Cursor slider;
        if (cursor.end()) {
            slider = cursor;
        } else {
            final Collection<Long> msgs =
                new ArrayList<Long>(this.terms.size());
            for (Term term : this.terms) {
                final Cursor shifted = term.shift(cursor);
                if (!shifted.end()) {
                    msgs.add(shifted.msg().number());
                }
            }
            if (msgs.isEmpty()) {
                slider = new MemCursor(0L, this.imap);
            } else {
                slider = new MemCursor(Collections.max(msgs), this.imap);
            }
        }
        Logger.debug(this, "#shift(%s): to %s", cursor, slider);
        return slider;
    }

}
