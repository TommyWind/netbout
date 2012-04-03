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
package com.netbout.inf.atoms;

import com.netbout.inf.Atom;
import org.apache.commons.lang.SerializationUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case of {@link VariableAtom}.
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class VariableAtomTest {

    /**
     * VariableAtom can encapsulate text.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void encapsulatesText() throws Exception {
        final Atom<String> atom = new VariableAtom("bout.number");
        MatcherAssert.assertThat(
            atom.toString(),
            Matchers.equalTo("$bout.number")
        );
    }

    /**
     * VariableAtom can compare to another object.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void comparesToSimilarObject() throws Exception {
        final String text = "text";
        MatcherAssert.assertThat(
            new VariableAtom(text),
            Matchers.equalTo(new VariableAtom(text))
        );
    }

    /**
     * VariableAtom can be serialized.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void serializesToBytes() throws Exception {
        final VariableAtom var = new VariableAtom("bout.title");
        final byte[] bytes = SerializationUtils.serialize(var);
        MatcherAssert.assertThat(
            ((VariableAtom) SerializationUtils.deserialize(bytes)).toString(),
            Matchers.equalTo(var.toString())
        );
    }

}
