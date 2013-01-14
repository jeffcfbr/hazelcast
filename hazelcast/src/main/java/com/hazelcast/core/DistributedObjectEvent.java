/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.core;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class DistributedObjectEvent implements DataSerializable, HazelcastInstanceAware {

    public enum EventType {
        CREATED, DESTROYED
    }

    private EventType eventType;
    private String serviceName;
    private Object objectId;
    private transient HazelcastInstance hazelcastInstance;

    public DistributedObjectEvent() {
    }

    public DistributedObjectEvent(EventType eventType, String serviceName, Object objectId) {
        this.eventType = eventType;
        this.serviceName = serviceName;
        this.objectId = objectId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Object getObjectId() {
        return objectId;
    }

    public DistributedObject getDistributedObject() {
        return hazelcastInstance != null ? hazelcastInstance.getServiceProxy(serviceName, objectId) : null;
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeBoolean(eventType == EventType.CREATED);
        out.writeUTF(serviceName);
        out.writeObject(objectId);
    }

    public void readData(ObjectDataInput in) throws IOException {
        eventType = in.readBoolean() ? EventType.CREATED : EventType.DESTROYED;
        serviceName = in.readUTF();
        objectId = in.readObject();
    }
}
