/*
 * Copyright 2017, TeamDev Ltd. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.base;

import com.google.common.testing.NullPointerTester;
import io.spine.test.Tests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorsShould {

    @Test
    public void have_private_constructor() {
        Tests.assertHasPrivateParameterlessCtor(Errors.class);
    }

    @Test
    public void create_Error_by_Exception() {
        final String msg = "create_error_by_exception";
        final Exception exception = new NullPointerException(msg);
        final Error error = Errors.fromException(exception);

        assertEquals(msg, error.getMessage());
        assertEquals(exception.getClass()
                              .getName(), error.getType());
    }

    @Test
    public void create_Error_by_Throwable() {
        final String msg = "create_Error_by_Throwable";
        final Throwable throwable = new IllegalStateException(msg);

        final Error error = Errors.fromThrowable(throwable);
        assertEquals(msg, error.getMessage());
        assertEquals(throwable.getClass()
                              .getName(), error.getType());
    }

    @Test
    public void pass_the_null_tolerance_check() {
        new NullPointerTester()
                .setDefault(Exception.class, new RuntimeException("null check"))
                .testAllPublicStaticMethods(Errors.class);
    }
}
