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

package net.spals.midas.serializer;

import java.util.List;
import java.util.Set;
import java.util.stream.*;

/**
 * For a given {@code Iterable<T>}, this will use the {@link Serializer} provided for T to serialize.
 * <pre>
 * Sets will be marked with {, }
 * Lists will be marked with [, ]
 * Any other iterable will be marked with (, )
 * </pre>
 *
 * @author spags
 */
class IterableSerializer implements Serializer {

    private final SerializerRegistry registry;

    IterableSerializer(final SerializerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String fileExtension() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param iterable the iterable to be serialized
     * @return the bytes of the serialized iterable
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] serialize(final Object iterable) {
        final Collector<CharSequence, ?, String> joiner;
        if (iterable instanceof Set) {
            joiner = Collectors.joining(", ", "{", "}");
        } else if (iterable instanceof List) {
            joiner = Collectors.joining(", ", "[", "]");
        } else {
            joiner = Collectors.joining(", ", "(", ")");
        }
        return StringEncoding.get().encode(
            StreamSupport.stream(((Iterable<?>) iterable).spliterator(), false)
                .map(v -> StringEncoding.get().decode(registry.getUnsafe(v.getClass())
                    .orElseThrow(() -> new RuntimeException("No serializer found for type" + v.getClass()))
                    .serialize(v)))
                .collect(joiner)
        );
    }
}
