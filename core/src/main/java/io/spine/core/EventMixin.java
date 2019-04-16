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

import com.google.errorprone.annotations.Immutable;
import com.google.protobuf.Timestamp;
import io.spine.base.EventMessage;

import static io.spine.core.Events.retrieveActorContext;
import static io.spine.validate.Validate.isDefault;

/**
 * Mixin interface for event objects.
 */
@Immutable
public interface EventMixin extends MessageWithContext<EventId, EventMessage, EventContext> {

    /**
     * Obtains the ID of the tenant of the event.
     */
    @Override
    default TenantId tenant() {
        return retrieveActorContext(context()).getTenantId();
    }

    @Override
    default Timestamp time() {
        return context().getTimestamp();
    }

    /**
     * Obtains the ID of the root command, which lead to this event.
     *
     * <p> In case the {@code Event} is a reaction to another {@code Event},
     * the identifier of the very first command in this chain is returned.
     *
     * @return the root command ID
     */
    default CommandId rootCommandId() {
        return context().getRootCommandId();
    }

    /**
     * Checks if this event is a rejection.
     *
     * @return {@code true} if the given event is a rejection, {@code false} otherwise
     */
    default boolean isRejection() {
        EventContext context = context();
        boolean result = context.hasRejection() || !isDefault(context.getRejection());
        return result;
    }
}
