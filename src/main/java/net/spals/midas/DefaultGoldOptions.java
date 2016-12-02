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

/**
 * The default implementation of {@link GoldOptions}.
 *
 * @author spags
 */
public final class DefaultGoldOptions implements GoldOptions {

    private boolean writable;
    private boolean checkout;

    private DefaultGoldOptions() {
        writable = true;
        checkout = true;
    }

    public static DefaultGoldOptions create() {
        return new DefaultGoldOptions();
    }

    /**
     * @return can we overwrite the current file or do we write a back up?
     */
    @Override
    public boolean checkout() {
        return checkout;
    }

    /**
     * @param checkout if we can overwrite the current file or do we write a back up?
     * @return the options
     */
    public GoldOptions setCheckout(final boolean checkout) {
        this.checkout = checkout;
        return this;
    }

    /**
     * @return should we actually write a file to the system?
     */
    @Override
    public boolean writable() {
        return writable;
    }

    /**
     * @param writable if we should actually write a file to the system?
     * @return the options
     */
    public GoldOptions setWritable(final boolean writable) {
        this.writable = writable;
        return this;
    }
}
