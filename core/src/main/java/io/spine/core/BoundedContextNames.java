/*
 * Copyright 2019, TeamDev. All rights reserved.
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

package io.spine.core;

import com.google.common.annotations.VisibleForTesting;
import io.spine.annotation.Internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.validate.Validate.checkNotEmptyOrBlank;

/**
 * Utility class for working with Bounded Context names.
 *
 * @see io.spine.core.BoundedContextName
 */
public final class BoundedContextNames {

    /** The name of a Bounded Context to be used if the name was explicitly set. */
    private static final BoundedContextName ASSUMING_TESTS = newName("AssumingTests");

    private static final BoundedContextName OUTSIDE = newName("Outside");

    /**
     * Prevents the utility class instantiation.
     */
    private BoundedContextNames() {
    }

    /**
     * Creates a new value object for a bounded context name.
     *
     * <p>The {@code name} argument value must not be {@code null} or empty.
     *
     * <p>This method, however, does not check for the uniqueness of the value passed.
     *
     * @param name the unique string name of the {@code BoundedContext}
     * @return a newly created name
     */
    public static BoundedContextName newName(String name) {
        checkNotNull(name);
        checkArgument(!name.isEmpty(), "Empty context name is not allowed.");
        BoundedContextName result = BoundedContextName
                .newBuilder()
                .setValue(name)
                .build();
        checkValid(result);
        return result;
    }

    /**
     * Validates the given {@link BoundedContextName}.
     *
     * <p>The name must not be empty or blank in order to pass the validation.
     *
     * @throws IllegalArgumentException if the name is not valid
     */
    @Internal
    public static void checkValid(BoundedContextName name) throws IllegalArgumentException {
        checkNotNull(name);
        String value = name.getValue();
        checkValid(value);
    }

    /**
     * Validates the given {@link BoundedContextName}.
     *
     * <p>The name must not be {@code null}, empty or blank in order to pass the validation.
     */
    @Internal
    public static void checkValid(String boundedContextName) throws IllegalArgumentException {
        checkNotNull(boundedContextName);
        checkNotEmptyOrBlank(
                boundedContextName,
                "A Bounded Context name cannot be empty or blank."
        );
    }

    /**
     * Obtains the name for a Bounded Context, which will be used when no name was specified.
     */
    @Internal
    @VisibleForTesting
    public static String assumingTestsValue() {
        return ASSUMING_TESTS.getValue();
    }

    /**
     * Obtains the name for a Bounded Context, which will be used when no name was specified.
     */
    @Internal
    @VisibleForTesting
    public static BoundedContextName assumingTests() {
        return ASSUMING_TESTS;
    }

    /**
     * Obtains the name which is used to signify absence of a Bounded Context.
     *
     * <p>When integrating with a third-party, use this name where a Bounded Context would go.
     */
    @Internal
    @VisibleForTesting
    public static BoundedContextName outside() {
        return OUTSIDE;
    }
}
