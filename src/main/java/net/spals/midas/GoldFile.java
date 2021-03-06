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

package net.spals.midas;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.common.annotations.VisibleForTesting;

import net.spals.midas.differ.Differ;
import net.spals.midas.differ.Differs;
import net.spals.midas.io.*;
import net.spals.midas.serializer.*;

/**
 * Creates an algorithm to diff an object against a source gold file reported the results.
 * The file will be stored in {file}.midas.
 * Default serializer is a {@link #toString()}.
 * Default path is {@link GoldPaths#simple()}
 *
 * @author spags
 * @author tkral
 */
public class GoldFile {

    private static final Logger LOGGER = Logger.getLogger(GoldFile.class.getName());

    private final Serializer serializer;
    private final GoldPath goldPath;
    private final Differ differ;
    private final FileUtil files;

    private GoldFile(final Builder builder) {
        goldPath = builder.path;
        serializer = builder.serializer;
        files = builder.files;
        differ = builder.differ;
    }

    /**
     * @return an instance of {@link GoldFile.Builder}
     */
    public static GoldFile.Builder builder() {
        return new GoldFile.Builder();
    }

    /**
     * Runs with the default values of {@link DefaultGoldOptions}
     *
     * @param object the object to be run against the gold file
     * @param path   the location of the gold file
     */
    public void run(final Object object, final Path path) {
        run(object, path, DefaultGoldOptions.create());
    }

    /**
     * Allows you to specify the behavior of this run with {@link DefaultGoldOptions} that are independent of this
     * particular gold file suite.
     *
     * @param object  the object to be run against the gold file
     * @param path    the location of the gold file
     * @param options the run time options of the gold file execution
     */
    public void run(final Object object, final Path path, final GoldOptions options) {
        final Path fullPath = goldPath.get(path);
        final Path file = Extensions.add(fullPath, Extensions.MIDAS_EXT);

        files.makeParents(file);
        files.createFile(file);

        final byte[] newBytes = serializer.serialize(object);
        final byte[] oldBytes = files.readAllBytes(file);

        try {
            if (!Arrays.equals(oldBytes, newBytes)) {
                final String diff = differ.diff(oldBytes, newBytes);
                throw new GoldFileException(String.format("\nDiffs: %s\n%s", file, diff));
            }

            final boolean updatedTimeStamp = files.setLastModified(file, System.currentTimeMillis());
            if (!updatedTimeStamp) {
                LOGGER.warning("Unable to update last modification date: " + fullPath);
            }
        } finally {
            if (options.writable()) {
                final Path resultFile = options.checkout() ? file : Extensions.add(file, Extensions.RESULT_EXT);
                files.write(resultFile, newBytes);
            }
        }
    }

    public final static class Builder {

        private Serializer serializer;
        private GoldPath path;
        private FileUtil files;
        private Differ differ;

        private Builder() {
            path = GoldPaths.simple();
            serializer = Serializers.newToString(SerializerRegistry.newDefault());
            files = new FileUtil();
            differ = Differs.strings();
        }

        @VisibleForTesting
        Builder withFileUtil(final FileUtil files) {
            this.files = Objects.requireNonNull(files, "bad file util");
            return this;
        }

        /**
         * The parent path for this set of gold file results, by default this is empty.
         *
         * @param path the parent path
         * @return the current builder
         */
        public Builder withPath(final GoldPath path) {
            this.path = Objects.requireNonNull(path, "bad path");
            return this;
        }

        /**
         * Assign a specific serialize to use, by default it will use a toString.
         *
         * @param serializer the mechanism for translating an object
         * @return the current builder
         */
        public Builder withSerializer(final Serializer serializer) {
            this.serializer = Objects.requireNonNull(serializer, "bad serializer");
            return this;
        }

        /**
         * Assign a specific differ to use, by default it will use a string differ.
         *
         * @param differ the mechanism for diffing an object
         * @return the current builder
         */
        public Builder withDiffer(final Differ differ) {
            this.differ = Objects.requireNonNull(differ, "bad differ");
            return this;
        }

        /**
         * @return an instance of {@link GoldFile} from the builder
         */
        public GoldFile build() {
            return new GoldFile(this);
        }
    }
}
