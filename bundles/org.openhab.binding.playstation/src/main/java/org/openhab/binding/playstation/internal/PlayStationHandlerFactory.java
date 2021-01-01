/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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

import static org.openhab.binding.playstation.internal.PlayStationBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.i18n.LocaleProvider;
import org.eclipse.smarthome.core.net.NetworkAddressService;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link PlayStationHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Fredrik Ahlström - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.playstation", service = ThingHandlerFactory.class)
public class PlayStationHandlerFactory extends BaseThingHandlerFactory {

    private final LocaleProvider localeProvider;
    private final NetworkAddressService networkAS;
    private final PS4AppDynamicStateDescriptionProvider stateDescriptionProvider;

    @Activate
    public PlayStationHandlerFactory(@Reference LocaleProvider provider, @Reference NetworkAddressService network,
            @Reference PS4AppDynamicStateDescriptionProvider descriptionProvider) {
        localeProvider = provider;
        networkAS = network;
        stateDescriptionProvider = descriptionProvider;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return PlayStationBindingConstants.SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_PS4.equals(thingTypeUID) || THING_TYPE_PS5.equals(thingTypeUID)) {
            return new PS4Handler(thing, localeProvider, networkAS, stateDescriptionProvider);
        }
        if (THING_TYPE_PS3.equals(thingTypeUID)) {
            return new PS3Handler(thing);
        }

        return null;
    }
}