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
package com.netbout.hub;

import com.netbout.spi.BoutNotFoundException;
import com.netbout.spi.MessageNotFoundException;
import com.netbout.spi.Urn;

/**
 * Manager of all bouts.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public interface BoutMgr {

    /**
     * Get some statistics, for the stage.
     * @return The text
     */
    String statistics();

    /**
     * Create new bout.
     * @param author Who is creating it
     * @return It's number (unique)
     */
    Long create(Urn author);

    /**
     * Find and return bout from collection.
     * @param number Number of the bout
     * @return The bout found or restored
     * @throws BoutNotFoundException If this bout is not found
     * @checkstyle RedundantThrows (4 lines)
     */
    BoutDt find(Long number) throws BoutNotFoundException;

    /**
     * Find and return bout from collection, by message number.
     * @param msg Number of the message
     * @return The bout found or restored
     * @throws MessageNotFoundException If this bout is not found
     * @checkstyle RedundantThrows (4 lines)
     */
    BoutDt boutOf(Long msg) throws MessageNotFoundException;

    /**
     * Destroy all bouts that are related to this identity.
     * @param author The identity
     */
    void destroy(Urn author);

}
