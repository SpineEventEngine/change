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
import org.junit.jupiter.api.Test;

import static io.spine.change.BooleanMismatch.expectedTrue;
import static io.spine.change.DoubleMismatch.expectedNonZero;
import static io.spine.change.DoubleMismatch.expectedZero;
import static io.spine.change.DoubleMismatch.unexpectedValue;
import static io.spine.change.DoubleMismatch.unpackActual;
import static io.spine.change.DoubleMismatch.unpackExpected;
import static io.spine.change.DoubleMismatch.unpackNewValue;
import static io.spine.test.Tests.assertHasPrivateParameterlessCtor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("DoubleMismatch should")
class DoubleMismatchTest {

    private static final int VERSION = 100;
    private static final double EXPECTED = 18.39;
    private static final double ACTUAL = 19.01;
    private static final double NEW_VALUE = 14.52;
    private static final double DELTA = 0.01;

    @Test
    @DisplayName("have private parameterless constructor")
    void haveUtilityConstructor() {
        assertHasPrivateParameterlessCtor(DoubleMismatch.class);
    }

    @Test
    @DisplayName("pass the null tolerance check")
    void passNullToleranceCheck() {
        new NullPointerTester()
                .testAllPublicStaticMethods(DoubleMismatch.class);
    }

    @Test
    @DisplayName("return ValueMismatch object with given double values")
    void returnMismatchWithDoubles() {
        final ValueMismatch mismatch = DoubleMismatch.of(EXPECTED, ACTUAL, NEW_VALUE, VERSION);

        assertEquals(EXPECTED, unpackExpected(mismatch), DELTA);
        assertEquals(ACTUAL, unpackActual(mismatch), DELTA);
        assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
        assertEquals(VERSION, mismatch.getVersion());
    }

    @Test
    @DisplayName("create ValueMismatch instance for expected zero amount")
    void createForExpectedZero() {
        final double expected = 0.0;
        final ValueMismatch mismatch = expectedZero(ACTUAL, NEW_VALUE, VERSION);

        assertEquals(expected, unpackExpected(mismatch), DELTA);
        assertEquals(ACTUAL, unpackActual(mismatch), DELTA);
        assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
        assertEquals(VERSION, mismatch.getVersion());
    }

    @Test
    @DisplayName("create ValueMismatch instance for expected non zero amount")
    void createForExpectedNonZero() {
        final double actual = 0.0;
        final ValueMismatch mismatch = expectedNonZero(EXPECTED, NEW_VALUE, VERSION);

        assertEquals(EXPECTED, unpackExpected(mismatch), DELTA);
        assertEquals(actual, unpackActual(mismatch), DELTA);
        assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
        assertEquals(VERSION, mismatch.getVersion());
    }

    @Test
    @DisplayName("create ValueMismatch instance for unexpected double value")
    void createForUnexpectedDouble() {
        final ValueMismatch mismatch = unexpectedValue(EXPECTED, ACTUAL, NEW_VALUE, VERSION);

        assertEquals(EXPECTED, unpackExpected(mismatch), DELTA);
        assertEquals(ACTUAL, unpackActual(mismatch), DELTA);
        assertEquals(NEW_VALUE, unpackNewValue(mismatch), DELTA);
        assertEquals(VERSION, mismatch.getVersion());
    }

    @Test
    @DisplayName("not unpackExpected if passed ValueMismatch is not a DoubleMismatch")
    void notUnpackExpectedForWrongType() {
        final ValueMismatch mismatch = expectedTrue(VERSION);
        assertThrows(RuntimeException.class, () -> unpackExpected(mismatch));
    }

    @Test
    @DisplayName("not unpackActual if passed ValueMismatch is not a DoubleMismatch")
    void notUnpackActualForWrongType() {
        final ValueMismatch mismatch = expectedTrue(VERSION);
        assertThrows(RuntimeException.class, () -> unpackActual(mismatch));
    }

    @Test
    @DisplayName("not unpackNewValue if passed ValueMismatch is not a DoubleMismatch")
    void notUnpackNewValueForWrongType() {
        final ValueMismatch mismatch = expectedTrue(VERSION);
        assertThrows(RuntimeException.class, () -> unpackNewValue(mismatch));
    }

    @Test
    @DisplayName("not accept same expected and actual values")
    void notAcceptSameExpectedAndActual() {
        final double value = 19.19;
        assertThrows(IllegalArgumentException.class,
                     () -> unexpectedValue(value, value, NEW_VALUE, VERSION));
    }
}
