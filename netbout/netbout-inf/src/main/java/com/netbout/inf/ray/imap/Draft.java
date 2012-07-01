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
package com.netbout.inf.ray.imap;

import com.jcabi.log.Logger;
import com.netbout.inf.Attribute;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Sub-directory with draft documents.
 *
 * <p>Class is thread-safe.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 * @checkstyle ClassDataAbstractionCoupling (500 lines)
 */
@SuppressWarnings("PMD.TooManyMethods")
final class Draft implements Closeable {

    /**
     * Lock on the directory.
     */
    private final transient Lock lock;

    /**
     * Public ctor.
     * @param lck The directory where to work
     * @throws IOException If some I/O problem inside
     */
    public Draft(final Lock lck) throws IOException {
        this.lock = lck;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("draft:%s", this.lock.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.lock.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object draft) {
        return this == draft || (draft instanceof Draft
            && Draft.class.cast(draft).lock.equals(this.lock));
    }

    /**
     * Expire it.
     * @throws IOException If some I/O problem inside
     */
    public void expire() throws IOException {
        this.lock.expire();
    }

    /**
     * Create new temp file for numbers.
     * @param attr Attribute
     * @return File name
     * @throws IOException If some I/O problem inside
     */
    public File numbers(final Attribute attr) throws IOException {
        final File folder = new File(this.lock.dir(), attr.toString());
        folder.mkdirs();
        return File.createTempFile("numbers-", ".inf", folder);
    }

    /**
     * Get access to existing file for numbers.
     * @param attr Attribute
     * @param name Name of the file
     * @return File name
     * @throws IOException If some I/O problem inside
     */
    public File numbers(final Attribute attr,
        final String name) throws IOException {
        final File folder = new File(this.lock.dir(), attr.toString());
        folder.mkdirs();
        return new File(folder, name);
    }

    /**
     * Get name of reverse file.
     * @param attr Attribute
     * @return File name
     * @throws IOException If some I/O problem inside
     */
    public File reverse(final Attribute attr) throws IOException {
        final File file = new File(
            this.lock.dir(),
            String.format("/%s/reverse.inf", attr)
        );
        FileUtils.touch(file);
        return file;
    }

    /**
     * Get backlog.
     * @param attr Attribute
     * @return The catalog
     * @throws IOException If some I/O problem inside
     */
    public Backlog backlog(final Attribute attr) throws IOException {
        return new Backlog(
            new File(
                this.lock.dir(),
                String.format("/%s/backlog.inf", attr)
            )
        );
    }

    /**
     * Baseline it to a new place.
     * @param dest Where to save baseline
     * @param src Original baseline
     * @throws IOException If some I/O problem inside
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void baseline(final Baseline dest,
        final Baseline src) throws IOException {
        final long start = System.currentTimeMillis();
        final Collection<Attribute> attrs = new LinkedList<Attribute>();
        for (File file : this.lock.dir().listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }
            final Attribute attr = new Attribute(
                FilenameUtils.getName(file.getPath())
            );
            this.baseline(dest, src, attr);
            attrs.add(attr);
        }
        Logger.debug(
            this,
            "#baseline('%s', '%s'): baselined %[list]s in %[ms]s",
            dest,
            src,
            attrs,
            System.currentTimeMillis() - start
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.lock.close();
        Logger.debug(this, "#close(): closed");
    }

    /**
     * Baseline to the given place, for the given attribute.
     * @param dest Where to save baseline
     * @param src Original baseline
     * @param attr Attribute
     * @throws IOException If some I/O problem inside
     */
    private void baseline(final Baseline dest, final Baseline src,
        final Attribute attr) throws IOException {
        File reverse = this.reverse(attr);
        if (!reverse.exists()) {
            reverse = src.reverse(attr);
        }
        if (reverse.exists()) {
            FileUtils.copyFile(reverse, dest.reverse(attr));
        }
        final Pipeline pipeline = new Pipeline(this, dest, src, attr);
        dest.catalog(attr).create(pipeline);
        pipeline.close();
    }

}