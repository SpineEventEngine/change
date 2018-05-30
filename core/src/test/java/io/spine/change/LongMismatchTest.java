/*
 * Copyright 2018, TeamDev. All rights reserved.
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

package io.spine.change;

import com.google.common.testing.NullPointerTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.change.BooleanMismatch.expectedTrue;
import static io.spine.change.LongMismatch.expectedNonZero;
import static io.spine.change.LongMismatch.expectedZero;
import static io.spine.change.LongMismatch.unexpectedValue;
import static io.spine.change.LongMismatch.unpackActual;
import static io.spine.change.LongMismatch.unpackExpected;
import static io.spine.change.LongMismatch.unpackNewValue;
import static io.spine.test.DisplayNames.HAVE_PARAMETERLESS_CTOR;
import static io.spine.test.DisplayNames.NOT_ACCEPT_NULLS;
import static io.spine.test.Tests.assertHasPrivateParameterlessCtor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"InnerClassMayBeStatic" /* JUnit 5 Nested classes cannot be static */,
                   "DuplicateStringLiteralInspection" /* A lot of similar test display names */})
@DisplayName("LongMismatch should")
class LongMismatchTest {

    private static final long EXPECTED = 1839L;
    private static final long ACTUAL = 1900L;
    private static final long NEW_VALUE = 1452L;
    private static final int VERSION = 7;

    @Test
    @DisplayName(HAVE_PARAMETERLESS_CTOR)
    void haveUtilityConstructor() {
        assertHasPrivateParameterlessCtor(LongMismatch.class);
    }

    @Test
    @DisplayName(NOT_ACCEPT_NULLS)
    void passNullToleranceCheck() {
        new NullPointerTester()
                .testAllPublicStaticMethods(LongMismatch.class);
    }

    @Nested
    @DisplayName("create ValueMismatch instance")
    class Create {

        @Test
        @DisplayName("from given long values")
        void withLongs() {
            final ValueMismatch mismatch = LongMismatch.of(EXPECTED, ACTUAL, NEW_VALUE, VERSION);

            assertEquals(EXPECTED, unpackExpected(mismatch));
            assertEquals(ACTUAL, unpackActual(mismatch));
            assertEquals(NEW_VALUE, unpackNewValue(mismatch));
            assertEquals(VERSION, mismatch.getVersion());
        }

        @Test
        @DisplayName("for expected zero amount")
        void forExpectedZero() {
            final long expected = 0L;
            final ValueMismatch mismatch = expectedZero(ACTUAL, NEW_VALUE, VERSION);

            assertEquals(expected, unpackExpected(mismatch));
            assertEquals(ACTUAL, unpackActual(mismatch));
            assertEquals(NEW_VALUE, unpackNewValue(mismatch));
            assertEquals(VERSION, mismatch.getVersion());
        }

        @Test
        @DisplayName("for expected non zero amount")
        void forExpectedNonZero() {
            final long actual = 0L;
            final ValueMismatch mismatch = expectedNonZero(EXPECTED, NEW_VALUE, VERSION);

            assertEquals(EXPECTED, unpackExpected(mismatch));
            assertEquals(actual, unpackActual(mismatch));
            assertEquals(NEW_VALUE, unpackNewValue(mismatch));
            assertEquals(VERSION, mismatch.getVersion());
        }

        @SuppressWarnings("Duplicates") // Common test case for different Mismatches.
        @Test
        @DisplayName("for unexpected long value")
        void forUnexpectedLong() {
            final ValueMismatch mismatch = unexpectedValue(EXPECTED, ACTUAL, NEW_VALUE, VERSION);

            assertEquals(EXPECTED, unpackExpected(mismatch));
            assertEquals(ACTUAL, unpackActual(mismatch));
            assertEquals(NEW_VALUE, unpackNewValue(mismatch));
            assertEquals(VERSION, mismatch.getVersion());
        }
    }

    @Test
    @DisplayName("not accept same expected and actual values")
    void notAcceptSameExpectedAndActual() {
        final long value = 1919L;
        assertThrows(IllegalArgumentException.class,
                     () -> unexpectedValue(value, value, NEW_VALUE, VERSION));
    }

    @Nested
    @DisplayName("if given non-long ValueMismatch, fail to unpack")
    class FailToUnpack {

        @Test
        @DisplayName("expected")
        void expectedWithWrongType() {
            final ValueMismatch mismatch = expectedTrue(VERSION);
            assertThrows(RuntimeException.class, () -> unpackExpected(mismatch));
        }

        @Test
        @DisplayName("actual")
        void actualWithWrongType() {
            final ValueMismatch mismatch = expectedTrue(VERSION);
            assertThrows(RuntimeException.class, () -> unpackActual(mismatch));
        }

        @Test
        @DisplayName("new value")
        void newValueWithWrongType() {
            final ValueMismatch mismatch = expectedTrue(VERSION);
            assertThrows(RuntimeException.class, () -> unpackNewValue(mismatch));
        }
    }
}
