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
import static io.spine.change.FloatMismatch.expectedNonZero;
import static io.spine.change.FloatMismatch.expectedZero;
import static io.spine.change.FloatMismatch.unexpectedValue;
import static io.spine.change.FloatMismatch.unpackActual;
import static io.spine.change.FloatMismatch.unpackExpected;
import static io.spine.change.FloatMismatch.unpackNewValue;
import static io.spine.test.DisplayNames.HAVE_PARAMETERLESS_CTOR;
import static io.spine.test.DisplayNames.NOT_ACCEPT_NULLS;
import static io.spine.test.Tests.assertHasPrivateParameterlessCtor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"InnerClassMayBeStatic" /* JUnit 5 Nested classes cannot be static */,
                   "DuplicateStringLiteralInspection" /* A lot of similar test display names */})
@DisplayName("FloatMismatch should")
class FloatMismatchTest {

    private static final int VERSION = 100;
    private static final float EXPECTED = 18.39f;
    private static final float ACTUAL = 19.01f;
    private static final float NEW_VALUE = 14.52f;
    private static final float DELTA = 0.01f;

    @Test
    @DisplayName(HAVE_PARAMETERLESS_CTOR)
    void haveUtilityConstructor() {
        assertHasPrivateParameterlessCtor(FloatMismatch.class);
    }

    @Test
    @DisplayName(NOT_ACCEPT_NULLS)
    void passNullToleranceCheck() {
        new NullPointerTester()
                .testAllPublicStaticMethods(FloatMismatch.class);
    }

    @Nested
    @DisplayName("when creating ValueMismatch")
    class CreateMismatchTest {

        @Test
        @DisplayName("successfully create instance from given float values")
        void createMismatchWithFloats() {
            final ValueMismatch mismatch = FloatMismatch.of(EXPECTED, ACTUAL, NEW_VALUE, VERSION);

            assertEquals(EXPECTED, unpackExpected(mismatch), DELTA);
            assertEquals(ACTUAL, unpackActual(mismatch), DELTA);
            assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
            assertEquals(VERSION, mismatch.getVersion());
        }

        @Test
        @DisplayName("successfully create instance for expected zero amount")
        void createForExpectedZero() {
            final double expected = 0.0f;
            final ValueMismatch mismatch = expectedZero(ACTUAL, NEW_VALUE, VERSION);

            assertEquals(expected, unpackExpected(mismatch), DELTA);
            assertEquals(ACTUAL, unpackActual(mismatch), DELTA);
            assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
            assertEquals(VERSION, mismatch.getVersion());
        }

        @Test
        @DisplayName("successfully create instance for expected non zero amount")
        void createForExpectedNonZero() {
            final float actual = 0.0f;
            final ValueMismatch mismatch = expectedNonZero(EXPECTED, NEW_VALUE, VERSION);

            assertEquals(EXPECTED, unpackExpected(mismatch), DELTA);
            assertEquals(actual, unpackActual(mismatch), DELTA);
            assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
            assertEquals(VERSION, mismatch.getVersion());
        }

        @SuppressWarnings("Duplicates") // Common test case for different Mismatches.
        @Test
        @DisplayName("successfully create instance for unexpected float value")
        void createForUnexpectedFloat() {
            final ValueMismatch mismatch = unexpectedValue(EXPECTED, ACTUAL, NEW_VALUE, VERSION);

            assertEquals(EXPECTED, unpackExpected(mismatch), DELTA);
            assertEquals(ACTUAL, unpackActual(mismatch), DELTA);
            assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
            assertEquals(VERSION, mismatch.getVersion());
        }

        @Test
        @DisplayName("not accept same expected and actual values")
        void notAcceptSameExpectedAndActual() {
            final float value = 19.19f;
            assertThrows(IllegalArgumentException.class,
                         () -> unexpectedValue(value, value, NEW_VALUE, VERSION));
        }
    }

    @Nested
    @DisplayName("when unpacking passed ValueMismatch")
    class UnpackMismatchTest {

        @Test
        @DisplayName("unpackExpected only if passed value is FloatMismatch")
        void notUnpackExpectedForWrongType() {
            final ValueMismatch mismatch = expectedTrue(VERSION);
            assertThrows(RuntimeException.class, () -> unpackExpected(mismatch));
        }

        @Test
        @DisplayName("unpackActual only if passed value is FloatMismatch")
        void notUnpackActualForWrongType() {
            final ValueMismatch mismatch = expectedTrue(VERSION);
            assertThrows(RuntimeException.class, () -> unpackActual(mismatch));
        }

        @Test
        @DisplayName("unpackNewValue only if passed value is FloatMismatch")
        void notUnpackNewValueForWrongType() {
            final ValueMismatch mismatch = expectedTrue(VERSION);
            assertThrows(RuntimeException.class, () -> unpackNewValue(mismatch));
        }
    }
}
