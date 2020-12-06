/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.playstation.internal;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.i18n.ChannelTypeI18nLocalizationService;
import org.eclipse.smarthome.core.thing.type.ChannelType;
import org.eclipse.smarthome.core.thing.type.DynamicStateDescriptionProvider;
import org.eclipse.smarthome.core.types.StateDescription;
import org.eclipse.smarthome.core.types.StateDescriptionFragmentBuilder;
import org.eclipse.smarthome.core.types.StateOption;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * The {@link PS4AppDynamicStateDescriptionProvider} provides a base implementation for the
 * {@link DynamicStateDescriptionProvider}.
 * <p>
 * It provides dynamic state options while leaving other state description fields as
 * original. Therefore the inheriting class has to request the reference for the
 * {@link ChannelTypeI18nLocalizationService} on its own.
 *
 * @author Fredrik Ahlström - Initial contribution
 */
@Component(service = { DynamicStateDescriptionProvider.class, PS4AppDynamicStateDescriptionProvider.class })
@NonNullByDefault
public class PS4AppDynamicStateDescriptionProvider implements DynamicStateDescriptionProvider {

    protected final Map<ChannelUID, @Nullable List<StateOption>> channelOptionsMap = new ConcurrentHashMap<>();

    /**
     * For a given channel UID, set a {@link List} of {@link StateOption}s that should be used for the channel, instead
     * of the one defined statically in the {@link ChannelType}.
     *
     * @param channelUID the channel UID of the channel
     * @param options a {@link List} of {@link StateOption}s
     */
    public void setStateOptions(ChannelUID channelUID, List<StateOption> options) {
        channelOptionsMap.put(channelUID, options);
    }

    @Override
    public @Nullable StateDescription getStateDescription(Channel channel, @Nullable StateDescription original,
            @Nullable Locale locale) {
        List<StateOption> options = channelOptionsMap.get(channel.getUID());
        if (options == null) {
            return null;
        }

        StateDescriptionFragmentBuilder builder = (original == null) ? StateDescriptionFragmentBuilder.create()
                : StateDescriptionFragmentBuilder.create(original);

        builder.withOptions(options);

        return builder.build().toStateDescription();
    }

    @Activate
    protected void activate(ComponentContext componentContext) {
    }

    @Deactivate
    public void deactivate() {
        channelOptionsMap.clear();
    }
}
