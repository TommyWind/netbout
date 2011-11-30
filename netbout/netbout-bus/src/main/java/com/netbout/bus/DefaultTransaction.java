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
package com.netbout.bus;

import com.netbout.bus.attrs.AsDefaultAttr;
import com.netbout.bus.attrs.InBoutAttr;
import com.netbout.spi.Bout;
import com.netbout.spi.Plain;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * One transaction, default implementation.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
final class DefaultTransaction implements Transaction {

    /**
     * Transaction mnemo.
     */
    private final transient String imnemo;

    /**
     * List of arguments.
     */
    private final transient List<Plain<?>> args =
        new CopyOnWriteArrayList<Plain<?>>();

    /**
     * Attributes.
     */
    private final transient TxAttributes attributes;

    /**
     * Public ctor.
     * @param mnemo Mnemo-code of the request
     * @param arguments The arguments
     * @param config List of attributes
     */
    public DefaultTransaction(final String mnemo,
        final List<Plain<?>> arguments, final TxAttributes attrs) {
        this.imnemo = mnemo;
        this.args.addAll(arguments);
        this.attributes = attrs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TxToken makeToken() {
        return new DefaultTxToken(this.imnemo, this.args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plain<?> getDefaultResult() {
        return this.attributes.get(AsDefaultAttr.class).getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInsideBout() {
        return this.attributes.get(InBoutAttr.class).isInsideBout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bout getBout() {
        return this.attributes.get(InBoutAttr.class).getBout();
    }

}
