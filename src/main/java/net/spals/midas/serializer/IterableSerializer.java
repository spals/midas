/*
 * Copyright (c) 2015, James T Spagnola & Timothy P Kral
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * For a given {@link Iterable}&lt;T&gt;, this will use the {@link Serializer} provided for T to serialize.
 * <br>Sets will be marked with {, }
 * <br>Lists will be marked with [, ]
 * <br>Any other iterable will be marked with (, )
 *
 * @author spags
 */
class IterableSerializer implements Serializer<Iterable> {

    private final SerializerMap serializers;

    public IterableSerializer(final SerializerMap serializers) {
        this.serializers = serializers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] serialize(final Iterable iterable) {
        final Collector<CharSequence, ?, String> joiner;
        if (iterable instanceof Set) {
            joiner = Collectors.joining(", ", "{", "}");
        } else if (iterable instanceof List) {
            joiner = Collectors.joining(", ", "[", "]");
        } else {
            joiner = Collectors.joining(", ", "(", ")");
        }
        return Strings.encode(
            StreamSupport.stream(((Iterable<?>) iterable).spliterator(), false)
                .map(v -> Strings.decode(serializers.getUnsafe(v.getClass()).serialize(v)))
                .collect(joiner)
        );
    }
}
