/*
 * Copyright (c) 2016, spals
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

package net.spals.midas.serializer;

import static org.hamcrest.MatcherAssert.assertThat;

import static net.spals.midas.serializer.ByteMatcher.bytes;

import java.util.*;

import com.google.common.collect.*;

import org.testng.annotations.Test;

/**
 * @author spags
 */
public class ReflectionSerializerTest {

    @Test
    public void testSerialize() {
        final byte[] actual = new ReflectionSerializer(SerializerRegistry.newDefault())
            .serialize(new Foo());
        final String expected =
            "littleInt = 0\n" +
                "bigInt = 1\n" +
                "littleChar = \u0002\n" +
                "bigChar = \u0003\n" +
                "littleLong = 4\n" +
                "bigLong = 5\n" +
                "littleDouble = 6.0\n" +
                "bigDouble = 6.0\n" +
                "littleFloat = 7.0\n" +
                "bigFloat = 8.0\n" +
                "littleShort = 9\n" +
                "bigShort = 10\n" +
                "littleBoolean = true\n" +
                "bigBoolean = false\n" +
                "string = foo\n" +
                "intArray = [1, 3, 5]\n" +
                "stringSet = [a, b, c]\n" +
                "intSet = {2, 4, 6}\n" +
                "map = (foo -> 1)\n";
        assertThat(actual, bytes(expected));
    }

    @SuppressWarnings("unused")
    private static class Default {

        private final Foo foo = new Foo();
        private final Foo nullFoo = null;
    }

    @SuppressWarnings({ "unused", "MismatchedReadAndWriteOfArray" })
    private static class Foo {

        private final int littleInt = 0;
        private final Integer bigInt = 1;
        private final char littleChar = 2;
        private final Character bigChar = 3;
        private final long littleLong = 4;
        private final Long bigLong = 5L;
        private final double littleDouble = 6;
        private final Double bigDouble = 6.0;
        private final float littleFloat = 7;
        private final Float bigFloat = 8F;
        private final short littleShort = 9;
        private final Short bigShort = 10;
        private final boolean littleBoolean = true;
        private final Boolean bigBoolean = false;
        private final String string = "foo";

        private final int[] intArray = new int[] { 1, 3, 5 };
        private final List<String> stringSet = ImmutableList.of("a", "b", "c");
        // make sure the order of the set is consistent.
        private final Set<Integer> intSet = Sets.newLinkedHashSet(Arrays.asList(2, 4, 6));
        private final Map<String, Integer> map = ImmutableMap.of("foo", 1);

        public String toString() {
            return "Foo";
        }
    }
}