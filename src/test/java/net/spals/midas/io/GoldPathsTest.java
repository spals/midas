/*
 * Copyright (c) 2016, James T Spagnola & Timothy P Kral
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.spals.midas.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import net.spals.midas.util.Tests;

public class GoldPathsTest {

    private static final Path FOO = Paths.get("foo");

    @Test
    public void testFullClass() throws IOException {
        final GoldPath path = GoldPaths.fullClass(GoldPaths.MAVEN, getClass());
        assertThat(path.get(FOO).toString(), is("src/test/resources/net/spals/midas/io/GoldPathsTest/foo"));
    }

    @Test
    public void testSimpleClass() {
        final GoldPath path = GoldPaths.simpleClass(GoldPaths.MAVEN, getClass());
        assertThat(path.get(FOO).toString(), is("src/test/resources/GoldPathsTest/foo"));
    }

    @Test
    public void testParent() {
        final GoldPath path = GoldPaths.parent(Paths.get("foo", "bar"));
        assertThat(path.get(FOO).toString(), is("foo/bar/foo"));
    }

    @Test
    public void testSimple() {
        final GoldPath path = GoldPaths.simple();
        assertThat(path.get(FOO).toString(), is("foo"));
    }

    @Test
    public void testPrivate() throws Exception {
        Tests.testPrivate(GoldPaths.class);
    }
}