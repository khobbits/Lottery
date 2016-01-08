package net.erbros.lottery;

import java.util.UUID;

/*
 * Copyright 2015 Luuk Jacobs

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class UUIDNameEntry
{
    private final UUID uuid;
    private final String name;

    public UUIDNameEntry( UUID uuid, String name )
    {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public String getName()
    {
        return name;
    }
}
