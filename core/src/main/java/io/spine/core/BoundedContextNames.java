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

package io.spine.core;

import com.google.common.annotations.VisibleForTesting;
import io.spine.annotation.Internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.validate.Validate.checkNotEmptyOrBlank;
import static java.lang.String.format;

/**
 * Utility class for working with Bounded Context names.
 *
 * @see io.spine.core.BoundedContextName
 */
public final class BoundedContextNames {

    /** The name of a Bounded Context to be used if the name was explicitly set. */
    private static final BoundedContextName ASSUMING_TESTS = newName("AssumingTests");
    private static final String SYSTEM_TEMPLATE = "%s_System";

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
        checkArgument(!name.isEmpty());
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
        checkNotEmptyOrBlank(name.getValue(), "name");
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
     * Obtains the name of the system bounded context for the bounded context with the given name.
     *
     * @param name the name of the original bounded context
     * @return the name of the system bounded context
     */
    @Internal
    public static BoundedContextName system(BoundedContextName name) {
        String value = format(SYSTEM_TEMPLATE, name.getValue());
        BoundedContextName result = BoundedContextName
                .newBuilder()
                .setValue(value)
                .build();
        return result;
    }
}
