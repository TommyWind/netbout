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
package com.netbout.inf;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case of {@link PredicateBuilder}.
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class PredicateBuilderTest {

    /**
     * PredicateBuilder can parse different formats.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void parsesDifferentQueriesWithoutProblems() throws Exception {
        final String[] queries = new String[] {
            "",
            "it's my story: \"\n\t\r \u0435\"",
            "(and (equal $bout.number 1) (or (matches 'some text' $text)))",
            "(equal $bout.title 'test')",
            "(talks-with 'urn:abc:')",
            "(and (ns 'urn:test:test-me') (limit 2))",
            "(and (from 5) (limit 2) (unique $bout.number))",
            "just simple text: \u0435",
        };
        final PredicateBuilder builder =
            new PredicateBuilder(new IndexMocker().mock());
        for (String query : queries) {
            builder.parse(query);
        }
    }

    /**
     * PredicateBuilder can parse invalid format and throw exception.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void parsesInvalidQueriesAndThrowsExceptions() throws Exception {
        final String[] queries = new String[] {
            "(--)",
            "(\n\t\r \u0435\")",
            "(unknown-function 1 2 3)",
            "(invalid-name-of-predicate# 5)",
        };
        final PredicateBuilder builder =
            new PredicateBuilder(new IndexMocker().mock());
        for (String query : queries) {
            try {
                builder.parse(query);
                throw new IllegalArgumentException(
                    String.format("should fail with '%s'", query)
                );
            } catch (PredicateException ex) {
                MatcherAssert.assertThat(
                    ex.getMessage(),
                    Matchers.containsString(query)
                );
            }
        }
    }

    /**
     * PredicateBuilder can build a predicate from a string.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void buildsPredicateFromQuery() throws Exception {
        final PredicateBuilder builder =
            new PredicateBuilder(new IndexMocker().mock());
        final String text = "\u043F\u0440\u0438\u0432\u0435";
        final Predicate pred = builder.parse(
            String.format("(and (matches \"%s\" $text) (pos 0))", text)
        );
        MatcherAssert.assertThat(pred, Matchers.notNullValue());
    }

    /**
     * PredicateBuilder can build a predicate from a plain text.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void buildsPredicateFromText() throws Exception {
        final PredicateBuilder builder =
            new PredicateBuilder(new IndexMocker().mock());
        final String text = "\u043F\u0440\u0438\u0432\u0435\u0442";
        final Predicate pred = builder.parse(text);
        MatcherAssert.assertThat(pred, Matchers.notNullValue());
    }

}
