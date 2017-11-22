/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator.collector;


/**
 * the property collector.
 * Created by heaven7 on 2017/11/8.
 * @since 1.4.1
 */
public interface PropertyCollector extends PropertyDispatcher {

    /**
     * open property collector.
     */
    void open();

    /**
     * is the collector opened or not.
     * @return true if the collector is opened.
     */
    boolean isOpened();

    /**
     * close property collector. and fire all event. after dispatch the event pool is cleared.
     * @param receiver the property receiver. if receiver == null, the all event will be discard.
     */
    void close(PropertyEventReceiver receiver);
}
