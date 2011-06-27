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
 * incident to the author by email: privacy@netbout.com.
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
package com.netbout.engine.impl;

import com.netbout.data.UserEnt;
import com.netbout.data.UserManager;
import com.netbout.engine.User;
import com.netbout.engine.UserFactory;
import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class DefaultUserFactoryTest {

    private static final Long USER_ID = 543L;

    private static final String USER_LOGIN = "john";

    private static final String USER_PWD = "secret54";

    @Test
    public void testSimpleUserFinding() throws Exception {
        final UserEnt entity = mock(UserEnt.class);
        final UserManager manager = mock(UserManager.class);
        doReturn(entity).when(manager).find(this.USER_LOGIN, this.USER_PWD);
        final UserFactory factory = new DefaultUserFactory(manager);
        final User found = factory.find(this.USER_LOGIN, this.USER_PWD);
        verify(manager).find(this.USER_LOGIN, this.USER_PWD);
    }

    @Test
    public void testDefaultClassInstantiating() throws Exception {
        final UserFactory factory = new DefaultUserFactory();
        assertThat(
            factory,
            instanceOf(UserFactory.class)
        );
    }

}
